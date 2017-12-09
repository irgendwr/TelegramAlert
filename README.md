# TelegramAlert Plugin for Graylog

Sends graylog alerts via telegram.

**Required Graylog version:** 2.0 and later

Installation
------------

[Download the plugin](https://github.com/irgendwr/TelegramAlert/releases)
and place the `.jar` file in your plugins folder that is configured in your `graylog.conf`.
The default is just `plugins/` relative from your `graylog-server` directory.

Restart `graylog-server` and you are done.

Usage
-----
Add a new notification in the web-interface and select `TelegramAlert` as the notification type.


Build
-----

With docker:

```
docker run -it --rm --name my-maven-project -v "$PWD":/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-8 mvn clean install
```

