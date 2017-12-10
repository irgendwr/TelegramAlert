package de.sandstorm_projects;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;;

public class TelegramAlertBot extends TelegramLongPollingBot {
	private String username;
	private String token;
	
	public TelegramAlertBot(String username, String token) {
		this.username = username;
		this.token = token;
	}
	
	@Override
    public void onUpdateReceived(Update update) {
		
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
