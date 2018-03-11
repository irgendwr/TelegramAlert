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

public class TelegramBot {
	private static final String API = "https://api.telegram.org/bot%s/%s";
	
	private String token;
	private String chat;
	private Logger logger;
	private String parseMode;
	
	public TelegramBot(String token, String chat, Logger logger) {
		this.token = token;
		this.chat = chat;
		this.logger = logger;
		
		parseMode = "Markdown";
	}
	
	public void setParseMode(String parseMode) {
		this.parseMode = parseMode;
	}
	
	public void sendMessage(String msg) {
		try {
			HttpClient client = HttpClients.createDefault();
			HttpPost request = new HttpPost(String.format(API, token, "sendMessage"));

			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("chat_id", chat));
			params.add(new BasicNameValuePair("text", msg));
			params.add(new BasicNameValuePair("parse_mode", parseMode));
			params.add(new BasicNameValuePair("disable_web_page_preview", "true"));
			request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse response = client.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status != 200) {
				logger.warning(String.format("API request was unsuccessfull, status: %d", status));
			}
		} catch (IOException e) {
			logger.warning("API request failed.");
			e.printStackTrace();
		}
	}
}
