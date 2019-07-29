package de.sandstorm_projects.telegramAlert.bot;

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
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class TelegramBot {
    private static final String API = "https://api.telegram.org/bot%s/%s";

    private String token;
    private String chat;
    private Logger logger;
    private ParseMode parseMode;
    private String proxy;

    public TelegramBot(String token) {
        this.token = token;
        logger = Logger.getLogger("TelegramAlert");
    }

    public void setChat(String id) {
        chat = id;
    }

    public void setParseMode(ParseMode mode) {
        parseMode = mode;
    }

    public void setProxy(String route) {
        proxy = route;
    }

    public void sendMessage(String msg) throws AlarmCallbackException {
        final CloseableHttpClient client;

        if (proxy == null || proxy.isEmpty()) {
            client = HttpClients.createDefault();
        } else {
            String[] proxyArr = proxy.split(":");
            HttpHost proxy = new HttpHost(proxyArr[0], Integer.parseInt(proxyArr[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            client = HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .build();
        }

        HttpPost request = new HttpPost(String.format(API, token, "sendMessage"));

        try {
            request.setEntity(createJSONEntity(msg));

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

    private HttpEntity createJSONEntity(String msg) {
        JSONObject params = new JSONObject();
        params.put("chat_id", chat);
        params.put("text", msg);
        params.put("disable_web_page_preview", "true");
        if (!parseMode.equals(ParseMode.text())) {
            params.put("parse_mode", parseMode.value());
        }
        return new StringEntity(params.toString(), ContentType.APPLICATION_JSON);
    }
}
