package de.sandstorm_projects.telegramAlert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.floreysoft.jmte.Engine;
import com.google.inject.Inject;

import org.graylog2.plugin.Message;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.*;
import org.graylog2.plugin.configuration.*;
import org.graylog2.plugin.streams.Stream;

import de.sandstorm_projects.telegramAlert.config.Config;
import de.sandstorm_projects.telegramAlert.config.TelegramAlarmCallbackConfig;

public class TelegramAlarmCallback implements AlarmCallback {
    private Configuration config;
    private TelegramBot bot;
    private Engine templateEngine;
    
    @Inject
    public TelegramAlarmCallback(Engine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void initialize(Configuration config) throws AlarmCallbackConfigurationException {
    	this.config = config;
    	
    	try {
    		checkConfiguration();
        } catch (ConfigurationException e) {
            throw new AlarmCallbackConfigurationException("Configuration error: " + e.getMessage());
        }
        
        bot = new TelegramBot(config);
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult result) throws AlarmCallbackException {
    	bot.sendMessage(buildMessage(stream, result));
    }
    
    private String buildMessage(Stream stream, AlertCondition.CheckResult result) {
        List<Message> backlog = getAlarmBacklog(result);
        Map<String, Object> model = getModel(stream, result, backlog);
        try {
            return templateEngine.transform(config.getString(Config.MESSAGE), model);
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
        model.put("stream_url", buildStreamLink(stream));

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

    private String buildStreamLink(Stream stream) {
        return getGraylogURL() + "streams/" + stream.getId() + "/messages?q=%2A&rangetype=relative&relative=3600";
    }

    /*private String buildMessageLink(String index, String id) {
        return getGraylogURL() + "messages/" + index + "/" + id;
    }*/
    
    private String getGraylogURL() {
    	String url = config.getString(Config.GRAYLOG_URL);
        
        if (url != null && !url.endsWith("/")) {
        	url += "/";
        }
        
        return url;
    }

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
        return config.getSource();
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {
    	TelegramAlarmCallbackConfig.check(config);
    }
}
