package de.sandstorm_projects.telegramAlert.config;

import de.sandstorm_projects.telegramAlert.bot.ParseMode;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;

public class TelegramAlarmCallbackConfigValues {
    private final Configuration config;

    public TelegramAlarmCallbackConfigValues(Configuration config) {
        this.config = config;
    }

    public Configuration getConfig() {
        return config;
    }

    public void check() throws ConfigurationException {
        TelegramAlarmCallbackConfig.check(config);
    }

    public String getMessage() {
        return config.getString(Config.MESSAGE);
    }

    public String[] getChatIDs() {
        String chats = config.getString(Config.CHAT);

        if (chats == null || chats.isEmpty()) {
            return new String[0];
        } else {
            // remove spaces and split at commas
            return chats.replace(" ", "").split(",");
        }
    }

    public ParseMode getParseMode() {
        return ParseMode.fromString(config.getString(Config.PARSE_MODE));
    }

    public String getToken() {
        return config.getString(Config.TOKEN);
    }

    public String getGraylogURL() {
        String url = config.getString(Config.GRAYLOG_URL);

        if (url != null && !url.endsWith("/")) {
            url += "/";
        }

        return url;
    }

    public String getProxy() {
        return config.getString(Config.PROXY);
    }

    public String getProxyUser() { return config.getString(Config.PROXY_USER); }

    public String getProxyPassword() { return config.getString(Config.PROXY_PASSWORD); }
}
