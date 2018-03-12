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
        configurationRequest.addField(new TextField(
        		Config.MESSAGE, "Message",
        		"[${stream.title}](${stream_url}): ${alert_condition.title}\n" +
        		"```\n" +
                "${foreach backlog message}\n" +
        		"${message}\n" +
                "${end}\n" +
                "```",
        		"See http://docs.graylog.org/en/latest/pages/streams/alerts.html#email-alert-notification for more details.",
        		ConfigurationField.Optional.NOT_OPTIONAL,
        		Attribute.TEXTAREA
        ));
        configurationRequest.addField(new TextField(
        		Config.CHAT, "Chat ID", "", "",
        		ConfigurationField.Optional.NOT_OPTIONAL
        ));
        configurationRequest.addField(new TextField(
        		Config.TOKEN, "Bot Token", "",
        		"HTTP API Token from @BotFather",
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
