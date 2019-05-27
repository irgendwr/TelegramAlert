package de.sandstorm_projects.telegramAlert;

import de.sandstorm_projects.telegramAlert.config.Config;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;
import org.graylog2.plugin.configuration.Configuration;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

class TelegramBot {
    private String apiUrl;
    private String chat;
    private Logger logger;
    private String parseMode;
    private String proxy;

    TelegramBot(Configuration config) {
        this.chat = config.getString(Config.CHAT);
        this.parseMode = config.getString(Config.PARSE_MODE);
        this.proxy = config.getString(Config.PROXY);
        this.apiUrl = Config.getApiUrl(config);

        logger = Logger.getLogger("TelegramAlert");
    }

    void sendMessage(String msg) throws AlarmCallbackException {
        final CloseableHttpClient client;

        if (!proxy.isEmpty()) {
            String[] proxyArr = proxy.split(":");
            HttpHost proxy = new HttpHost(proxyArr[0], Integer.parseInt(proxyArr[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            client = HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .build();
        } else {
            client = HttpClients.createDefault();
        }

        HttpPost request = new HttpPost(apiUrl);

        try {
            request.setEntity(createJsonStringEntity(msg));

            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                String body = new BasicResponseHandler().handleResponse(response);
                String error = String.format("API request was unsuccessful (%d): %s", status, body);
                logger.warning(error);
                throw new AlarmCallbackException(error);
            }
        } catch (IOException e) {
            String error = "API request failed: " + e.getMessage();
            logger.warning(error);
            e.printStackTrace();
            throw new AlarmCallbackException(error);
        }
    }

    private HttpEntity createJsonStringEntity(String msg) {
        JSONObject params = new JSONObject();
        params.put("chat_id", chat);
        params.put("text", msg);
        params.put("disable_web_page_preview", "true");
        if (!parseMode.equals("text")) {
            params.put("parse_mode", parseMode);
        }
        return new StringEntity(params.toString(), ContentType.APPLICATION_JSON);
    }
}
