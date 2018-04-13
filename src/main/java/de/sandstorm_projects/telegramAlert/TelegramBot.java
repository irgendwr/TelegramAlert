package de.sandstorm_projects.telegramAlert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;
import org.graylog2.plugin.configuration.Configuration;

import de.sandstorm_projects.telegramAlert.config.Config;

class TelegramBot {
    private static final String API = "https://api.telegram.org/bot%s/%s";

    private String token;
    private String chat;
    private Logger logger;
    private String parseMode;

    TelegramBot(Configuration config) {
        this.token = config.getString(Config.TOKEN);
        this.chat = config.getString(Config.CHAT);
        this.parseMode = config.getString(Config.PARSE_MODE);

        logger = Logger.getLogger("TelegramAlert");
    }

    void sendMessage(String msg) throws AlarmCallbackException {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost request = new HttpPost(String.format(API, token, "sendMessage"));

            List<NameValuePair> params = new ArrayList<>(4);
            params.add(new BasicNameValuePair("chat_id", chat));
            params.add(new BasicNameValuePair("text", msg));
            params.add(new BasicNameValuePair("parse_mode", parseMode));
            params.add(new BasicNameValuePair("disable_web_page_preview", "true"));
            request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                String error = String.format("API request was unsuccessful, status: %d", status);
                logger.warning(error);
                throw new AlarmCallbackException(error);
            }
        } catch (IOException e) {
            String error = "API request failed.";
            logger.warning(error);
            e.printStackTrace();
            throw new AlarmCallbackException(error);
        }
    }
}
