package de.sandstorm_projects;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramAlertBot extends TelegramLongPollingBot {
	private String username;
	private String token;
	
	public TelegramAlertBot(String token) {
		try {
			this.username = getMe().getUserName();
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
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
