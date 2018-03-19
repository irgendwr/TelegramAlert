package de.sandstorm_projects.telegramAlert;

import org.graylog2.plugin.PluginModule;

public class TelegramModule extends PluginModule {
    @SuppressWarnings("unused")
    protected void configure() {
        addAlarmCallback(TelegramAlarmCallback.class);
    }
}
