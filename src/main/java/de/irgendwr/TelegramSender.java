package de.irgendwr;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TelegramSender {
    private static final String API = "https://api.telegram.org/bot%s/%s";

    private String token;
    private String parseMode;
    private String proxyAddress;
    private String proxyUser;
    private String proxyPassword;

    public TelegramSender(String botToken) {
        token = botToken;
    }

    public TelegramSender(String botToken, String parseMode) {
        token = botToken;
        this.parseMode = parseMode;
    }

    public void parseMode(String mode) {
        parseMode = mode;
    }

    public void proxyAddress(String address) {
        proxyAddress = address;
    }

    public void proxyUser(String user) {
        proxyUser = user;
    }

    public void proxyPassword(String password) {
        proxyPassword = password;
    }

    public void sendMessage(String chatID, String message) throws TelegramSenderException {
        final CloseableHttpClient client;

        if (StringUtils.isBlank(proxyAddress)) {
            client = HttpClients.createDefault();
        } else {
            String[] proxyArr = proxyAddress.split(":");
            if (proxyArr.length != 2) {
                throw new TelegramSenderException("Invalid proxy address format", true);
            }
            HttpHost proxy = new HttpHost(proxyArr[0], Integer.parseInt(proxyArr[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            HttpClientBuilder clientBuilder = HttpClients.custom().setRoutePlanner(routePlanner);

            if (!StringUtils.isBlank(proxyUser) || !StringUtils.isBlank(proxyPassword)) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(AuthScope.ANY),
                        new UsernamePasswordCredentials(proxyUser, proxyPassword));
                clientBuilder.setDefaultCredentialsProvider(credsProvider);
            }

            client = clientBuilder.build();
        }

        HttpPost request = new HttpPost(String.format(API, token, "sendMessage"));

        request.setEntity(createJSONEntity(chatID, message));

        try {
            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String body;
            switch (status) {
                case 200:
                    break;
                case 401:
                    throw new TelegramSenderException("API request failed (401 Unauthorized). Either the bot token or proxy configuration is invalid.", true);
                case 400:
                    body = EntityUtils.toString(entity, "UTF-8");
                    throw new TelegramSenderException(String.format("API request failed (400 Bad Request). This can caused by a syntax error in the message template, too long message or an invalid chat ID. \nResponse: %s", body), true);
                default:
                    body = EntityUtils.toString(entity, "UTF-8");
                    throw new TelegramSenderException(String.format("API request failed (%d): %s", status, body), false);
            }
        } catch (IOException e) {
            throw new TelegramSenderException("API request failed: " + e.getMessage(), e, false);
        }
    }

    private HttpEntity createJSONEntity(String chatID, String msg) {
        ObjectNode params = JsonNodeFactory.instance.objectNode();
        params.put("chat_id", chatID);
        params.put("text", msg);
        params.put("disable_web_page_preview", true);
        if (StringUtils.isNotEmpty(parseMode)) {
            params.put("parse_mode", parseMode);
        }

        return new StringEntity(params.toString(), ContentType.APPLICATION_JSON);
    }

    public static class TelegramSenderException extends Exception {
        private final Boolean permanent;

        public TelegramSenderException(String msg, Boolean permanent) {
            super(msg);
            this.permanent = permanent;
        }

        public TelegramSenderException(String msg, Throwable cause, Boolean permanent) {
            super(msg, cause);
            this.permanent = permanent;
        }

        public Boolean isPermanent() {
            return permanent;
        }
    }
}
