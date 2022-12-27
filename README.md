# TelegramNotification Plugin for Graylog

[![Build status](https://github.com/irgendwr/TelegramAlert/workflows/build/badge.svg)](https://github.com/irgendwr/TelegramAlert/actions?query=workflow%3Abuild)
[![GitHub Release](https://img.shields.io/github/release/irgendwr/TelegramAlert.svg)](https://github.com/irgendwr/TelegramAlert/releases)
[![Graylog Marketplace](https://img.shields.io/badge/Graylog-Marketplace-blue.svg)](https://marketplace.graylog.org/addons/8a67cfdf-8c1c-4d17-87bf-db38e79015f1)
[![Donate](https://img.shields.io/badge/PayPal-Donate-blue.svg)](https://paypal.me/jonasboegle)

TelegramNotification allows you to send [Graylog](https://www.graylog.org) alert messages to a specified [Telegram](https://telegram.org) chat.

## Requirements

Requires Graylog 3.1 or later. It's recommended to use the [latest Graylog release](https://www.graylog.org/products/latestversion).

## Installation

1. Download the [latest TelegramNotification release](https://github.com/irgendwr/TelegramAlert/releases/latest)
and place the `graylog-plugin-telegram-notification-x.x.x.jar` file in your `plugin_dir` that is configured in your `graylog.conf`
as described in the [Graylog documentation](https://go2docs.graylog.org/5-0/what_more_can_graylog_do_for_me/plugins.html?tocpath=What%20More%20Can%20Graylog%20Do%20for%20Me%253F%7CPlugins%7C_____0#InstallingandLoadingPlugins).

2. Restart your graylog-server, i.e. `systemctl restart graylog-server`.

3. Configure an alert notification as described in the next section.

## Usage

### Step 1 - Create Bot

Create a new Telegram bot with the [BotFather](https://t.me/BotFather) and copy the bot token.

### Step 2 - Get Channel ID

Use the [Message Tool](https://irgendwr.github.io/TelegramAlert/message-tool) to find the **Chat ID** of the desired chat.

You can use a private chat, add the bot to a group-chat or to a channel. Just make sure to copy the correct Channel ID.

### Step 3 - Add Notification

Navigate to **Alerts > Notifications** in your Graylog-interface and select `Telegram Notification` as the notification type.

The message is a template that can be configured as described in the [Graylog Documentation](https://go2docs.graylog.org/5-0/interacting_with_your_log_data/notifications.html?tocpath=Interacting%20with%20Your%20Log%20Data%7CAlerts%20and%20Notifications%7CNotifications%7C_____0#EmailAlertNotification)

![Settings](https://raw.githubusercontent.com/irgendwr/TelegramAlert/master/screenshots/settings.png)

## Update

To update the plugin you need to remove the old `graylog-plugin-telegram-notification-x.x.x.jar` (or `telegram-alert-x.x.x.jar`) file from your plugins folder and follow the [installation](#Installation) instructions again.

You may need to remove and re-create your Telegram Notifications.

## Development

See [development/README.md](development/README.md) for some notes on how to get started with development.

Contributions are welcome.

## Credits

Thanks to:

- Alexey Medov (@kakogoxepa) - *for his valuable ideas and generous donation!*
- Everyone that [starred this repository](https://github.com/irgendwr/TelegramAlert/stargazers) ⭐️ - *you keep me motivated* 🙂
- [Contributors](https://github.com/irgendwr/TelegramAlert/graphs/contributors) that submitted useful [pull-requests](https://github.com/irgendwr/TelegramAlert/pulls?utf8=%E2%9C%93&q=is%3Apr+is%3Aclosed+is%3Amerged) or opened good issues with suggestions or a detailed bug report.
