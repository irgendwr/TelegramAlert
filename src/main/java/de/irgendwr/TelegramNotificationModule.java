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
    /**
     * Returns all configuration beans required by this plugin.
     *
     * Implementing this method is optional. The default method returns an empty {@link Set}.
     */
    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }

    @Override
    protected void configure() {
        /*
         * Register your plugin types here.
         *
         * Examples:
         *
         * addMessageInput(Class<? extends MessageInput>);
         * addMessageFilter(Class<? extends MessageFilter>);
         * addMessageOutput(Class<? extends MessageOutput>);
         * addPeriodical(Class<? extends Periodical>);
         * addAlarmCallback(Class<? extends AlarmCallback>);
         * addInitializer(Class<? extends Service>);
         * addRestResource(Class<? extends PluginRestResource>);
         *
         *
         * Add all configuration beans returned by getConfigBeans():
         *
         * addConfigBeans();
         */
        addNotificationType(TelegramEventNotificationConfig.TYPE_NAME,
                TelegramEventNotificationConfig.class,
                TelegramEventNotification.class,
                TelegramEventNotification.Factory.class,
                TelegramEventNotificationConfigEntity.TYPE_NAME,
                TelegramEventNotificationConfigEntity.class);
    }
}
