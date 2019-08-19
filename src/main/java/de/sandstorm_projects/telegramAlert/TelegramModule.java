package de.sandstorm_projects.telegramAlert;

import org.graylog2.plugin.PluginModule;

public class TelegramModule extends PluginModule {
    protected void configure() {
        addAlarmCallback(TelegramAlarmCallback.class);
    }
}
