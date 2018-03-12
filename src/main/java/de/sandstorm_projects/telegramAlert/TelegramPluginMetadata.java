package de.sandstorm_projects.telegramAlert;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class TelegramPluginMetadata implements PluginMetaData {
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
        return "Jonas Bögle";
    }

    @Override
    public URI getURL() {
        return URI.create("https://sandstorm-projects.de");
    }

    @Override
    public Version getVersion() {
        return new Version(2, 0, 0);
    }

    @Override
    public String getDescription() {
        return "Allows you to send alert messages to a specified Telegram chat.";
    }

    @Override
    public Version getRequiredVersion() {
        return new Version(2, 0, 0);
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
