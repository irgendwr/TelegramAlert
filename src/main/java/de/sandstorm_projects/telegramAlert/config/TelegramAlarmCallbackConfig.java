package de.sandstorm_projects.telegramAlert.config;

import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.TextField;
import org.graylog2.plugin.configuration.fields.TextField.Attribute;

public class TelegramAlarmCallbackConfig {
	private static final String ERROR_NOT_SET = "%s is mandatory and must not be empty.";
	
	public static ConfigurationRequest createRequest() {
        final ConfigurationRequest configurationRequest = new ConfigurationRequest();
        // TODO: update to template engine
        configurationRequest.addField(new TextField(
        		Config.MESSAGE, "Message",
        		"[%streamTitle%](%streamUrl%):\n%backlog%",
        		"Message that will be sent, Placeholders: %streamTitle%, %streamDescription%, %streamUrl%, %alertDescription%, %backlog%",
        		ConfigurationField.Optional.NOT_OPTIONAL,
        		Attribute.TEXTAREA
        ));
        configurationRequest.addField(new TextField(
        		Config.CHAT, "Chat ID", "",
        		"ID of the chat that messages should be sent to",
        		ConfigurationField.Optional.NOT_OPTIONAL
        ));
        configurationRequest.addField(new TextField(
        		Config.TOKEN, "Bot Token", "",
        		"HTTP API Token you get from @BotFather",
        		ConfigurationField.Optional.NOT_OPTIONAL,
        		Attribute.IS_PASSWORD
        ));

        configurationRequest.addField(new TextField(
        		Config.GRAYLOG_URL, "Graylog URL", "",
                "URL to your Graylog web interface. Used to build links in alarm notification.",
                ConfigurationField.Optional.NOT_OPTIONAL)
        );
        return configurationRequest;
	}
	
	public static void check(Configuration config) throws ConfigurationException {
		String[] mandetoryFields = {
			Config.MESSAGE,
			Config.CHAT,
			Config.TOKEN,
			Config.GRAYLOG_URL
		};
		
		for (String field : mandetoryFields) {
			if (!config.stringIsSet(field)) {
	            throw new ConfigurationException(String.format(ERROR_NOT_SET, field));
			}
		}
    }
}
