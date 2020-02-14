package de.irgendwr.telegramAlert;

import org.graylog2.plugin.PluginModule;

public class TelegramModule extends PluginModule {
    @Override
    protected void configure() {
        addNotificationType(TelegramEventNotificationConfig.TYPE_NAME,
                TelegramEventNotificationConfig.class,
                TelegramEventNotification.class,
                TelegramEventNotification.Factory.class);
    }
}
