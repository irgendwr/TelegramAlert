package de.sandstorm_projects;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class TelegramAlertMetaData implements PluginMetaData {
    @Override
    public String getUniqueId() {
        return "de.sandstorm_projects.TelegramAlert";
    }

    @Override
    public String getName() {
        return "TelegramAlert";
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
        return new Version(1, 0, 0);
    }

    @Override
    public String getDescription() {
        return "Sends graylog alerts to telegram.";
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
