package de.sandstorm_projects.telegramAlert;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class TelegramPluginMetadata implements PluginMetaData {
    private static final String PLUGIN_PROPERTIES = "de.sandstorm_projects.telegramAlert/graylog-plugin.properties";

    @Override
    public String getUniqueId() {
        return TelegramPlugin.class.getPackage().getName();
    }

    @Override
    public String getName() {
        return "Telegram Alert";
    }

    @Override
    public String getAuthor() {
        return "Jonas Bögle @irgendwr";
    }

    @Override
    public URI getURL() {
        return URI.create("https://irgendwr.github.io/TelegramAlert/");
    }

    @Override
    public Version getVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public String getDescription() {
        return "Allows you to send alert messages to a specified Telegram chat.";
    }

    @Override
    public Version getRequiredVersion() {
        return Version.from(2, 4, 0);
        //return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(2, 4, 0, "unknown"));
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
