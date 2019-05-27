package de.sandstorm_projects.telegramAlert.config;

import org.apache.commons.lang.StringUtils;
import org.graylog2.plugin.configuration.Configuration;

public class Config {
    public static final String MESSAGE = "message_template";
    public static final String CHAT = "telegram_chat";
    public static final String PARSE_MODE = "parse_mode";
    public static final String TOKEN = "telegram_token";
    public static final String TELEGRAM_API_URL = "telegram_api_url";
    public static final String GRAYLOG_URL = "graylog_url";
    public static final String PROXY = "proxy";

    public static String getApiUrl(Configuration config) {
        String rawApiUrl = config.getString(Config.TELEGRAM_API_URL);
        return StringUtils.replaceEach(rawApiUrl,
                new String[]{"${bot_token}"},
                new String[]{config.getString(Config.TOKEN)});
    }
}
