package de.sandstorm_projects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.NumberField;
import org.graylog2.plugin.configuration.fields.TextField;
import org.graylog2.plugin.streams.Stream;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramAlertNotification implements AlarmCallback {
	private static final String TOKEN = "token";
	private static final String USERNAME = "username";
	private static final String CHAT = "chat";
	private static final String LOG_FILE = "log_file";
	
    private Configuration cfg;
    private Logger logger;
    private TelegramLongPollingBot bot;
    private long chatId;

    @Override
    public void initialize(Configuration cfg) throws AlarmCallbackConfigurationException {
        this.cfg = cfg;
        logger = Logger.getLogger("TelegramAlert");
        String logPath = cfg.getString(LOG_FILE);
        
        if (!logPath.isEmpty()) {
	        try {
	        	FileHandler handler = new FileHandler(logPath, true);
	            logger.addHandler(handler);
	            handler.setFormatter(new SimpleFormatter());
	        } catch (IOException | SecurityException ex) {
	            ex.printStackTrace();
	        }
        }
        
        ApiContextInitializer.init();
        
        TelegramBotsApi botApi = new TelegramBotsApi();
        chatId = cfg.getInt(CHAT);
    	bot = new TelegramAlertBot(cfg.getString(USERNAME), cfg.getString(TOKEN));
        
        try {
        	botApi.registerBot(bot);
  
        	bot.execute(new SendMessage()
        			.setChatId(chatId)
                    .setText("Bot loaded successfully! " + Emoji.SMILING_FACE_WITH_SMILING_EYES
            ));
        } catch (TelegramApiException e) {
            logger.warning(e.getMessage());
        }
        
        logger.info("Alert was initialized successfully");
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult checkResult) throws AlarmCallbackException {
    	try {
        	bot.execute(new SendMessage()
        			.setChatId(chatId)
                    .setText(createRequestMsg(stream, checkResult)
            ));
        } catch (TelegramApiException e) {
            logger.warning(e.getMessage());
        }
    }


    private String createRequestMsg(Stream stream, AlertCondition.CheckResult checkResult) {
        String title = stream.getTitle();
		List<String> backlog = getAlarmBacklog(checkResult);
		StringBuilder sb = new StringBuilder("");
		sb.append(title);
		sb.append(":\n```");
		
		for (String msg : backlog) {
            sb.append(msg);
            sb.append("\n");
        }
		sb.append("```");
		return sb.toString();
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

    @Override
    public ConfigurationRequest getRequestedConfiguration() {
        final ConfigurationRequest cfgRequest = new ConfigurationRequest();
        cfgRequest.addField(new TextField(TOKEN, "Bot Token", "",  "HTTP API Token you get from @BotFather",
        		ConfigurationField.Optional.NOT_OPTIONAL));
        cfgRequest.addField(new TextField(USERNAME, "Bot Username", "",  "Username of your bot",
        		ConfigurationField.Optional.NOT_OPTIONAL));
        cfgRequest.addField(new NumberField(CHAT, "Chat ID", 0, "ID of the chat that messages should be sent to",
        		ConfigurationField.Optional.NOT_OPTIONAL));
        cfgRequest.addField(new TextField(LOG_FILE, "File log", "/tmp/telegramAlert.log", "File path for debug logging"));
        return cfgRequest;
    }

    @Override
    public String getName() {
        return "Telegram Alert Notification";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return cfg.getSource();
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {
        if (!cfg.stringIsSet(TOKEN)) {
            throw new ConfigurationException("Token is not set.");
        }
        if (!cfg.stringIsSet(USERNAME)) {
            throw new ConfigurationException("Token is not set.");
        }

        if (!cfg.intIsSet(CHAT)) {
            throw new ConfigurationException("Chat ID is not set.");
        }
    }
}
