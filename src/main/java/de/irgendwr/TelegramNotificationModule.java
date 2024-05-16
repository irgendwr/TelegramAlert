package de.irgendwr;

import java.util.Collections;
import java.util.Set;

import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import de.irgendwr.entities.TelegramEventNotificationConfigEntity;

/**
 * Extend the PluginModule abstract class here to add you plugin to the system.
 */
public class TelegramNotificationModule extends PluginModule {
    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }
    
    @Override
    protected void configure() {
        addNotificationType(TelegramEventNotificationConfig.TYPE_NAME,
                TelegramEventNotificationConfig.class,
                TelegramEventNotification.class,
                TelegramEventNotification.Factory.class,
                TelegramEventNotificationConfigEntity.TYPE_NAME,
                TelegramEventNotificationConfigEntity.class);
    }
}
