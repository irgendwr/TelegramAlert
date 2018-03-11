# TelegramAlert

> TelegramAlert allows you to send [Graylog](https://www.graylog.org) alert messages to a specified [Telegram](https://telegram.org) chat.

[![GitHub Release](https://img.shields.io/github/release/irgendwr/TelegramAlert.svg)](https://github.com/irgendwr/TelegramAlert/releases)
[![Build Status](https://travis-ci.org/irgendwr/TelegramAlert.svg?branch=master)](https://travis-ci.org/irgendwr/TelegramAlert)

## Requirements

- Graylog 2.0 or later

## Installation

1. [Download the plugin](https://github.com/irgendwr/TelegramAlert/releases)
and place the `.jar` file in your plugins folder that is configured in your `graylog.conf` as described in the [Graylog documentation](http://docs.graylog.org/en/latest/pages/plugins.html#installing-and-loading-plugins).

2. Restart graylog-server: `service graylog-server restart`

## Usage

1. Create a new bot with the [BotFather](https://t.me/BotFather).

2. Add a new alert notification in your Graylog-interface and select `TelegramAlert` as the notification type.

## Build

This project is using [Maven](https://maven.apache.org) and requires Java 8 or higher.

You can build the plugin using the following command:

```bash
mvn package
```

The plugin file `telegram-alert-x.x.x.jar` will be saved in the `target` directory
