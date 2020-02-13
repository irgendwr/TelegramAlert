# TelegramAlert

[![Build Status](https://travis-ci.org/irgendwr/TelegramAlert.svg?branch=master)](https://travis-ci.org/irgendwr/TelegramAlert)
[![GitHub Release](https://img.shields.io/github/release/irgendwr/TelegramAlert.svg)](https://github.com/irgendwr/TelegramAlert/releases)
[![Graylog Marketplace](https://img.shields.io/badge/Graylog-Marketplace-blue.svg)](https://marketplace.graylog.org/addons/8a67cfdf-8c1c-4d17-87bf-db38e79015f1)
[![Donate](https://img.shields.io/badge/PayPal-Donate-blue.svg)](https://paypal.me/jonasboegle)

TelegramAlert allows you to send [Graylog](https://www.graylog.org) alert messages to a specified [Telegram](https://telegram.org) chat.

## Requirements

Requires Graylog 2.4 or later. It's recommended to use the [latest Graylog release](https://www.graylog.org/products/latestversion).

## Installation

1. Download the [latest TelegramAlert release](https://github.com/irgendwr/TelegramAlert/releases/latest)
and place the `telegram-alert-x.x.x.jar` file in your `plugin_dir` that is configured in your `graylog.conf`
as described in the [Graylog documentation](http://docs.graylog.org/en/latest/pages/plugins.html#installing-and-loading-plugins).

2. Restart your graylog-server, i.e. `service graylog-server restart`.

3. Configure an alert notification as described in the next section.

## Usage

### Step 1 - Create Bot

Create a new Telegram bot with the [BotFather](https://t.me/BotFather) and copy the bot token.

### Step 2 - Get Channel ID

Use the [Message Tool](https://irgendwr.github.io/TelegramAlert/message-tool) to find the **Chat ID** of the desired chat.

You can use a private chat, add the bot to a group-chat or to a channel. Just make sure to copy the correct Channel ID.

### Step 3 - Add Alert Notification

Add a new alert notification in your Graylog-interface and select `Telegram Alert` as the notification type.

The message is a template that can be configured as described in the [Graylog Documentation](http://docs.graylog.org/en/latest/pages/streams/alerts.html#email-alert-notification)

![Add alert notification](https://raw.githubusercontent.com/irgendwr/TelegramAlert/master/screenshots/add_alert_notification.png)
![Create new Telegram Alert](https://raw.githubusercontent.com/irgendwr/TelegramAlert/master/screenshots/new_telegram_alert.png)

## Update

To update the plugin you need to remove the old `telegram-alert-x.x.x.jar` file from your plugins folder and follow the [installation](#Installation) instructions again.

You may need to remove and re-create your Telegram Alert Notifications.

## Build

This project is using [Maven](https://maven.apache.org) and requires Java 8 or higher.

You can build the plugin using the following command:

```bash
mvn package
```

The plugin file `telegram-alert-x.x.x.jar` will be saved in the `target` directory

## Plugin Release

Versions are released using the [maven release plugin](https://maven.apache.org/maven-release/maven-release-plugin/):

```bash
mvn release:prepare
mvn release:perform
```

TravisCI builds and uploads the release artifacts automatically.

## Credits

Thanks to:

- Alexey Medov (@kakogoxepa) - *for his valuable ideas and generous donation!*
- Everyone that [starred this repository](https://github.com/irgendwr/TelegramAlert/stargazers) ⭐️ - *you keep me motivated* 🙂
- [Contributors](https://github.com/irgendwr/TelegramAlert/graphs/contributors) that submitted useful [pull-requests](https://github.com/irgendwr/TelegramAlert/pulls?utf8=%E2%9C%93&q=is%3Apr+is%3Aclosed+is%3Amerged) or opened good issues with suggestions or a detailed bug report.