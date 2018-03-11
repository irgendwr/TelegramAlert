package de.sandstorm_projects.telegramAlert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.TextField;
import org.graylog2.plugin.configuration.fields.TextField.Attribute;
import org.graylog2.plugin.streams.Stream;

import de.sandstorm_projects.telegramAlert.config.Config;
import de.sandstorm_projects.telegramAlert.config.TelegramAlarmCallbackConfig;

public class TelegramAlarmCallback implements AlarmCallback {
    private Configuration config;
    private Logger logger;
    private TelegramBot bot;

    @Override
    public void initialize(Configuration config) throws AlarmCallbackConfigurationException {
    	this.config = config;
    	
    	try {
    		checkConfiguration();
        } catch (ConfigurationException e) {
            throw new AlarmCallbackConfigurationException("Configuration error: " + e.getMessage());
        }
        
        logger = Logger.getLogger("TelegramAlert");
        bot = new TelegramBot(config.getString(Config.TOKEN), config.getString(Config.CHAT), logger);
        
        logger.info("Initialized successfully");
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult checkResult) throws AlarmCallbackException {
    	bot.sendMessage(createRequestMsg(stream, checkResult));
    }


    private String createRequestMsg(Stream stream, AlertCondition.CheckResult checkResult) {
		String msg = config.getString(Config.MESSAGE);

		msg = msg.replaceAll("(?i)%streamTitle%", stream.getTitle());
		msg = msg.replaceAll("(?i)%streamDescription%", stream.getDescription());
		msg = msg.replaceAll("(?i)%streamUrl%", buildStreamLink(stream));
		msg = msg.replaceAll("(?i)%alertDescription%", checkResult.getTriggeredCondition().getDescription());
		
		StringBuilder backlog = new StringBuilder("```\n");
		getAlarmBacklog(checkResult).forEach(item -> {
			backlog.append(item);
			backlog.append("\n");
		});
		backlog.append("```");
		msg = msg.replaceAll("(?i)%backlog%", backlog.toString());
		
		return msg;
    }

    protected List<String> getAlarmBacklog(AlertCondition.CheckResult checkResult) {
        AlertCondition alertCondition = checkResult.getTriggeredCondition();
        List<MessageSummary> matchingMessages = checkResult.getMatchingMessages();

        int effectiveBacklogSize = Math.min(alertCondition.getBacklog(), matchingMessages.size());

        if (effectiveBacklogSize == 0) {
            return Collections.emptyList();
        }

        List<MessageSummary> backlogSummaries = matchingMessages.subList(0, effectiveBacklogSize);
        List<String> backlog = new ArrayList<>(effectiveBacklogSize);

        for (MessageSummary messageSummary : backlogSummaries) {
            backlog.add(messageSummary.getMessage());
        }

        return backlog;
    }

    protected String buildStreamLink(Stream stream) {
        return getGraylogURL() + "streams/" + stream.getId() + "/messages?q=%2A&rangetype=relative&relative=3600";
    }

    protected String buildMessageLink(String index, String id) {
        return getGraylogURL() + "messages/" + index + "/" + id;
    }
    
    private String getGraylogURL() {
    	String url = config.getString(Config.GRAYLOG_URL);
        
        if (!url.endsWith("/")) {
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
        return "Telegram Alert Notification";
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
