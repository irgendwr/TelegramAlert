package de.sandstorm_projects.telegramAlert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.floreysoft.jmte.Engine;
import com.google.inject.Inject;

import de.sandstorm_projects.telegramAlert.bot.ParseMode;
import de.sandstorm_projects.telegramAlert.bot.TelegramBot;
import de.sandstorm_projects.telegramAlert.config.TelegramAlarmCallbackConfigValues;
import de.sandstorm_projects.telegramAlert.template.RawBracketsRenderer;
import de.sandstorm_projects.telegramAlert.template.RawNoopRenderer;
import de.sandstorm_projects.telegramAlert.template.TelegramHTMLEncoder;
import de.sandstorm_projects.telegramAlert.template.TelegramMarkdownEncoder;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.Tools;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.*;
import org.graylog2.plugin.configuration.*;
import org.graylog2.plugin.streams.Stream;

import de.sandstorm_projects.telegramAlert.config.TelegramAlarmCallbackConfig;
import org.joda.time.DateTime;

public class TelegramAlarmCallback implements AlarmCallback {
    private TelegramAlarmCallbackConfigValues config;
    private TelegramBot bot;
    private Engine templateEngine;
    
    @Inject
    public TelegramAlarmCallback(Engine engine) {
        templateEngine = engine;
        templateEngine.registerNamedRenderer(new RawNoopRenderer());
        templateEngine.registerNamedRenderer(new RawBracketsRenderer());
    }

    @Override
    public void initialize(Configuration c) throws AlarmCallbackConfigurationException {
        config = new TelegramAlarmCallbackConfigValues(c);

        try {
            checkConfiguration();
        } catch (ConfigurationException e) {
            throw new AlarmCallbackConfigurationException("Configuration error: " + e.getMessage());
        }

        ParseMode parseMode = config.getParseMode();
        switch (parseMode.value()) {
            case ParseMode.MARKDOWN:
                templateEngine.setEncoder(new TelegramMarkdownEncoder());
                break;
            case ParseMode.HTML:
                templateEngine.setEncoder(new TelegramHTMLEncoder());
                break;
            default:
                templateEngine.setEncoder(null);
        }
        
        bot = new TelegramBot(config.getToken());
        bot.setProxy(config.getProxy());
        bot.setParseMode(parseMode);
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult result) throws AlarmCallbackException {
        String message = buildMessage(stream, result);

        for (String chatID : config.getChatIDs()) {
            bot.sendMessage(chatID, message);
        }
    }
    
    private String buildMessage(Stream stream, AlertCondition.CheckResult result) {
        List<Message> backlog = getAlarmBacklog(result);
        Map<String, Object> model = getModel(stream, result, backlog);
        try {
            return templateEngine.transform(config.getMessage(), model);
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    
    private Map<String, Object> getModel(Stream stream, AlertCondition.CheckResult result, List<Message> backlog) {
        Map<String, Object> model = new HashMap<>();
        model.put("stream", stream);
        model.put("check_result", result);
        model.put("alert_condition", result.getTriggeredCondition());
        model.put("backlog", backlog);
        model.put("backlog_size", backlog.size());
        model.put("stream_url", buildStreamURL(result, stream));
        model.put("graylog_url", config.getGraylogURL());

        return model;
    }

    private List<Message> getAlarmBacklog(AlertCondition.CheckResult result) {
        final AlertCondition alertCondition = result.getTriggeredCondition();
        final List<MessageSummary> matchingMessages = result.getMatchingMessages();
        final int effectiveBacklogSize = Math.min(alertCondition.getBacklog(), matchingMessages.size());

        if (effectiveBacklogSize == 0) return Collections.emptyList();
        final List<MessageSummary> backlogSummaries = matchingMessages.subList(0, effectiveBacklogSize);
        final List<Message> backlog = new ArrayList<>(effectiveBacklogSize);
        for (MessageSummary messageSummary : backlogSummaries) {
            backlog.add(messageSummary.getRawMessage());
        }

        return backlog;
    }

    private String buildStreamURL(AlertCondition.CheckResult result, Stream stream) {
        int time = 5;
        if (result.getTriggeredCondition().getParameters().get("time") != null) {
            time = (int) result.getTriggeredCondition().getParameters().get("time");
        }

        DateTime dateAlertEnd = result.getTriggeredAt();
        DateTime dateAlertStart = dateAlertEnd.minusMinutes(time);
        String alertStart = Tools.getISO8601String(dateAlertStart);
        String alertEnd = Tools.getISO8601String(dateAlertEnd);

        return config.getGraylogURL() + "streams/" + stream.getId() + "/messages?rangetype=absolute&from=" + alertStart + "&to=" + alertEnd;
    }

    /*private String buildMessageURL(String index, String id) {
        return config.getGraylogURL() + "messages/" + index + "/" + id;
    }*/

    @Override
    public ConfigurationRequest getRequestedConfiguration() {
        return TelegramAlarmCallbackConfig.createRequest();
    }

    @Override
    public String getName() {
        return "Telegram Alert";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return config.getConfig().getSource();
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {
        config.check();
    }
}
