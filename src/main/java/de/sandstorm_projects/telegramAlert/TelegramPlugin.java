package de.sandstorm_projects.telegramAlert;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

public class TelegramPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new TelegramPluginMetadata();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singletonList(new TelegramModule());
    }
}
