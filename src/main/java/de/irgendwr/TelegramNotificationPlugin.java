package de.irgendwr;

import org.graylog2.plugin.*;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the Plugin interface here.
 */
public class TelegramNotificationPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new MetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singletonList(new TelegramNotificationModule());
    }

    public static class MetaData implements PluginMetaData {
        private static final String PLUGIN_PROPERTIES = "de.irgendwr.graylog-plugin-telegram-notification/graylog-plugin.properties";
    
        @Override
        public String getUniqueId() {
            return "de.irgendwr.TelegramNotificationPlugin";
        }
    
        @Override
        public String getName() {
            return "TelegramNotification";
        }
    
        @Override
        public String getAuthor() {
            return "Jonas Bögle <graylog@jonas.boegle.me>";
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
            return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(0, 0, 0, "unknown"));
        }

        @Override
        public Set<ServerStatus.Capability> getRequiredCapabilities() {
            return Collections.emptySet();
        }
    }
}
