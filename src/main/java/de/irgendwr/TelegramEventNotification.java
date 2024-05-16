package de.irgendwr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floreysoft.jmte.Engine;
import com.google.common.collect.ImmutableList;
import de.irgendwr.models.MessageModelData;
import de.irgendwr.models.StreamModelData;
import de.irgendwr.template.RawNoopRenderer;
import de.irgendwr.template.TelegramHTMLEncoder;
import org.apache.commons.lang3.StringUtils;
import org.graylog.events.notifications.*;
import org.graylog.events.processor.EventDefinitionDto;
import org.graylog.events.processor.aggregation.AggregationEventProcessorConfig;
import org.graylog.scheduler.JobTriggerDto;
import org.graylog2.jackson.TypeReferences;
import org.graylog2.notifications.Notification;
import org.graylog2.notifications.NotificationService;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.streams.Stream;
import org.graylog2.plugin.system.NodeId;
import org.graylog2.streams.StreamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class TelegramEventNotification implements EventNotification {
    public interface Factory extends EventNotification.Factory<TelegramEventNotification> {
        @Override
        TelegramEventNotification create();
    }

    private static final Logger LOG = LoggerFactory.getLogger(TelegramEventNotification.class);

    // Telegram messages are limited to 4096 characters. This is the default notification message if the message is too long.
    // You can customize it by checking if `message_too_long` is `true` in your message template.
    private static final String DEFAULT_MESSAGE_TOO_LONG_TEXT = "An alert was triggered, but the message was too long. Please check the Graylog interface for details.";
    private static final String UNKNOWN = "<unknown>";

    private final EventNotificationService notificationCallbackService;
    private final StreamService streamService;
    private final NotificationService notificationService;
    private final NodeId nodeId;
    private final ObjectMapper objectMapper;
    private final Engine templateEngine;

    @Inject
    public TelegramEventNotification(EventNotificationService notificationCallbackService,
                                     StreamService streamService,
                                     NotificationService notificationService,
                                     NodeId nodeId,
                                     ObjectMapper objectMapper) {
        this.notificationCallbackService = notificationCallbackService;
        this.streamService = streamService;
        this.notificationService = requireNonNull(notificationService, "notificationService");
        this.nodeId = requireNonNull(nodeId, "nodeId");
        this.objectMapper = requireNonNull(objectMapper, "objectMapper");
        this.templateEngine = new Engine();
        templateEngine.registerNamedRenderer(new RawNoopRenderer());
        templateEngine.setEncoder(new TelegramHTMLEncoder());
    }

    @Override
    public void execute(EventNotificationContext ctx) throws TemporaryEventNotificationException, PermanentEventNotificationException {
        final TelegramEventNotificationConfig config = (TelegramEventNotificationConfig) ctx.notificationConfig();

        Set<String> chats = config.chats();
        if (chats.isEmpty()) {
            final Notification notification = notificationService.buildNow()
                    .addNode(nodeId.toString())
                    .addType(Notification.Type.GENERIC)
                    .addSeverity(Notification.Severity.NORMAL)
                    .addDetail("title", "No recipients (chat IDs) have been defined!")
                    .addDetail("description", "To fix this, go to the notification configuration and add at least one recipient (chat ID).");
            notificationService.publishIfFirst(notification);
            return;
        }

        ImmutableList<MessageSummary> backlog = notificationCallbackService.getBacklogForEvent(ctx);
        Map<String, Object> model = getModel(ctx, backlog, config, false);
        String message = buildMessage(config.messageTemplate(), model);

        // Check if message is too long.
        // This may not be accurate as this doesn't take Telegrams entity parsing into account.
        // https://github.com/irgendwr/TelegramAlert/issues/37#issuecomment-811818760
        if (message.length() > 4096) {
            // first, try the template again with `message_too_long = true`
            // this allows you to customize the message if you check that variable
            model = getModel(ctx, backlog, config, true);
            message = buildMessage(config.messageTemplate(), model);
        }
        if (message.length() > 4096) {
            // as a fallback, send the default text
            message = DEFAULT_MESSAGE_TOO_LONG_TEXT;
        }

        TelegramSender bot = new TelegramSender(config.botToken(), "HTML");
        bot.proxyAddress(config.proxyAddress());
        bot.proxyUser(config.proxyUser());
        bot.proxyPassword(config.proxyPassword());

        try {
            for (String chatID : chats) {
                bot.sendMessage(chatID, message);
            }
        } catch (TelegramSender.TelegramSenderException e) {
            String exceptionDetail = e.getMessage();
            if (e.getCause() != null) {
                exceptionDetail += " (" + e.getCause() + ")";
            }
            errorNotification(exceptionDetail);

            if (e.isPermanent()) {
                throw new PermanentEventNotificationException("Failed to send Telegram messages. " + e.getMessage());
            } else {
                throw new TemporaryEventNotificationException("Failed to send Telegram messages. " + e.getMessage());
            }

        } catch (Exception e) {
            String exceptionDetail = e.toString();
            if (e.getCause() != null) {
                exceptionDetail += " (" + e.getCause() + ")";
            }
            errorNotification(exceptionDetail);

            throw new PermanentEventNotificationException("Failed to send Telegram messages. " + e.getMessage());
        }

        LOG.debug("Sending Telegram Messages to chats <{}> using notification <{}>",
                String.join(",", config.chats()),
                ctx.notificationId());
    }

    private String buildMessage(String template, Map<String, Object> model) {
        return this.templateEngine.transform(template, model);
    }

    private Map<String, Object> getModel(EventNotificationContext ctx, ImmutableList<MessageSummary> backlog, TelegramEventNotificationConfig config, boolean messageTooLong) {
        final Optional<EventDefinitionDto> definitionDto = ctx.eventDefinition();
        final Optional<JobTriggerDto> jobTriggerDto = ctx.jobTrigger();

        List<StreamModelData> streams = streamService.loadByIds(ctx.event().sourceStreams())
                .stream()
                .map(stream -> buildStreamWithUrl(stream, ctx, config.graylogURL()))
                .collect(Collectors.toList());

        final MessageModelData modelData = MessageModelData.builder()
                .eventDefinition(definitionDto)
                .eventDefinitionId(definitionDto.map(EventDefinitionDto::id).orElse(UNKNOWN))
                .eventDefinitionType(definitionDto.map(d -> d.config().type()).orElse(UNKNOWN))
                .eventDefinitionTitle(definitionDto.map(EventDefinitionDto::title).orElse(UNKNOWN))
                .eventDefinitionDescription(definitionDto.map(EventDefinitionDto::description).orElse(UNKNOWN))
                .jobDefinitionId(jobTriggerDto.map(JobTriggerDto::jobDefinitionId).orElse(UNKNOWN))
                .jobTriggerId(jobTriggerDto.map(JobTriggerDto::id).orElse(UNKNOWN))
                .event(ctx.event())
                .backlog(backlog)
                .backlogSize(backlog.size())
                .messageTooLong(messageTooLong)
                .graylogUrl(config.graylogURL())
                .streams(streams)
                .build();

        return objectMapper.convertValue(modelData, TypeReferences.MAP_STRING_OBJECT);
    }

    private StreamModelData buildStreamWithUrl(Stream stream, EventNotificationContext ctx, String graylogURL) {
        String streamUrl = null;
        if(StringUtils.isNotBlank(graylogURL)) {
            streamUrl = StringUtils.appendIfMissing(graylogURL, "/") + "streams/" + stream.getId() + "/search";

            if(ctx.eventDefinition().isPresent()) {
                EventDefinitionDto eventDefinitionDto = ctx.eventDefinition().get();
                if(eventDefinitionDto.config() instanceof AggregationEventProcessorConfig) {
                    String query = ((AggregationEventProcessorConfig) eventDefinitionDto.config()).query();
                    try {
                        streamUrl += "?q=" + URLEncoder.encode(query, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // url without query as fallback
                    }
                }
            }
        }

        return StreamModelData.builder()
                .id(stream.getId())
                .title(stream.getTitle())
                .description(stream.getDescription())
                .url(Optional.ofNullable(streamUrl).orElse(UNKNOWN))
                .build();
    }

    private void errorNotification(String error) {
        LOG.warn(error);

        final Notification systemNotification = notificationService.buildNow()
                .addNode(nodeId.toString())
                .addType(Notification.Type.GENERIC)
                .addSeverity(Notification.Severity.NORMAL)
                .addDetail("title", "Failed to send Telegram messages.")
                .addDetail("description", error)
                .addDetail("exception", error);
        notificationService.publishIfFirst(systemNotification);
    }
}
