# TelegramAlert Plugin for Graylog
> TelegramAlert enables you to send Graylog alerts via Telegram to a specified chat or channel.

[![Build Status](https://travis-ci.org/irgendwr/TelegramAlert.svg?branch=master)](https://travis-ci.org/irgendwr/TelegramAlert)

**Requires:** Graylog 2.0 or later

Installation
------------

[Download the plugin](https://github.com/irgendwr/TelegramAlert/releases)
and place the `.jar` file in your plugins folder that is configured in your `graylog.conf`.
The default is just `plugins/` relative from your `graylog-server` directory.

Restart graylog-server and you are done.

Usage
-----
Create a new bot with the [BotFather](https://t.me/BotFather).

Add a new notification in the web-interface and select `TelegramAlert` as the notification type.


Build
-----

With maven:

```
mvn clean package
```
The plugin file `telegram-alert-x.x.x.jar` will be saved in the `target` directory
