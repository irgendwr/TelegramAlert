# TelegramAlert Plugin for Graylog
> TelegramAlert enables you to send Graylog alerts via Telegram to a specified chat or channel.

[![Build Status](https://travis-ci.org/irgendwr/TelegramAlert.svg?branch=master)](https://travis-ci.org/irgendwr/TelegramAlert)

## Requirements
- Graylog 2.0 or later

## Installation
1. [Download the plugin](https://github.com/irgendwr/TelegramAlert/releases)
and place the `.jar` file in your plugins folder that is configured in your `graylog.conf`.
The default is just `plugins/` relative from your `graylog-server` directory.

2. Restart graylog-server: `service graylog-server restart`

## Usage
3. Create a new bot with the [BotFather](https://t.me/BotFather).

4. Add a new notification in the web-interface and select `TelegramAlert` as the notification type.

## Build
With [maven](https://maven.apache.org/):

```
mvn clean package
```
The plugin file `telegram-alert-x.x.x.jar` will be saved in the `target` directory
