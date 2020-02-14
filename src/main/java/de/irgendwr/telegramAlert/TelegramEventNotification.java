package de.irgendwr.telegramAlert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floreysoft.jmte.Engine;
import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.util.Strings;
import org.graylog.events.notifications.EventNotification;
import org.graylog.events.notifications.EventNotificationContext;
import org.graylog.events.notifications.EventNotificationService;
import org.graylog.events.notifications.PermanentEventNotificationException;
import org.graylog.events.notifications.TemporaryEventNotificationException;
import org.graylog2.notifications.Notification;
import org.graylog2.notifications.NotificationService;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.system.NodeId;
import org.graylog2.streams.StreamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class TelegramEventNotification implements EventNotification {
    public interface Factory extends EventNotification.Factory {
        @Override
        TelegramEventNotification create();
    }

    private static final Logger LOG = LoggerFactory.getLogger(TelegramEventNotification.class);

    private static final String UNKNOWN = "<unknown>";

    private final EventNotificationService notificationCallbackService;
    private final StreamService streamService;
    private final Engine templateEngine;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    private final NodeId nodeId;

    @Inject
    public TelegramEventNotification(EventNotificationService notificationCallbackService,
                                     StreamService streamService,
                                     Engine templateEngine,
                                     NotificationService notificationService,
                                     ObjectMapper objectMapper,
                                     NodeId nodeId) {
        this.notificationCallbackService = notificationCallbackService;
        this.streamService = streamService;
        this.templateEngine = templateEngine;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
        this.nodeId = nodeId;
    }

    @Override
    public void execute(EventNotificationContext ctx) throws TemporaryEventNotificationException, PermanentEventNotificationException {
        final TelegramEventNotificationConfig config = (TelegramEventNotificationConfig) ctx.notificationConfig();
        ImmutableList<MessageSummary> backlog = notificationCallbackService.getBacklogForEvent(ctx);

        try {
            //TODO: implement
            final Notification systemNotification = notificationService.buildNow()
                    .addNode(nodeId.toString())
                    .addType(Notification.Type.GENERIC)
                    .addSeverity(Notification.Severity.NORMAL)
                    .addDetail("exception", "TEST");
            notificationService.publishIfFirst(systemNotification);
        /*} catch (ConfigurationError e) {
            throw new TemporaryEventNotificationException(e.getMessage());*/
        } catch (Exception e) {
            String exceptionDetail = e.toString();
            if (e.getCause() != null) {
                exceptionDetail += " (" + e.getCause() + ")";
            }

            final Notification systemNotification = notificationService.buildNow()
                    .addNode(nodeId.toString())
                    .addType(Notification.Type.GENERIC)
                    .addSeverity(Notification.Severity.NORMAL)
                    .addDetail("exception", exceptionDetail);
            notificationService.publishIfFirst(systemNotification);

            throw new PermanentEventNotificationException("Failed to send Telegram messages. " + e.getMessage());
        }

        LOG.debug("Sending Telegram Messages to chats <{}> using notification <{}>",
                Strings.join(config.chats(), ','),
                ctx.notificationId());
    }
}