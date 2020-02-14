// eslint-disable-next-line no-unused-vars
import webpackEntry from 'webpack-entry';

import { PluginManifest, PluginStore } from 'graylog-web-plugin/plugin';

import TelegramNotificationForm from 'form/TelegramNotificationForm';
import TelegramNotificationSummary from 'form/TelegramNotificationSummary';


PluginStore.register(new PluginManifest({}, {
  eventNotificationTypes: [
    {
      type: 'telegram-notification-v2',
      displayName: 'Telegram Notification',
      formComponent: TelegramNotificationForm,
      summaryComponent: TelegramNotificationSummary,
      defaultConfig: TelegramNotificationForm.defaultConfig
    }
  ]
}));