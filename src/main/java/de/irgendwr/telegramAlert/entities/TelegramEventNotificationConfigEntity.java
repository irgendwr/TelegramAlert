package de.irgendwr.telegramAlert.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import de.irgendwr.telegramAlert.TelegramEventNotificationConfig;
import org.graylog.events.contentpack.entities.EventNotificationConfigEntity;
import org.graylog.events.notifications.EventNotificationConfig;
import org.graylog2.contentpacks.model.entities.EntityDescriptor;
import org.graylog2.contentpacks.model.entities.references.ValueReference;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

@AutoValue
@JsonTypeName(TelegramEventNotificationConfigEntity.TYPE_NAME)
@JsonDeserialize(builder = TelegramEventNotificationConfigEntity.Builder.class)
public abstract class TelegramEventNotificationConfigEntity implements EventNotificationConfigEntity {

    public static final String TYPE_NAME = "telegram-notification-v2";
    private static final String FIELD_BOT_TOKEN = "bot_token";
    //private static final String FIELD_GRAYLOG_URL = "graylog_url"; // TODO: remove?
    private static final String FIELD_CHATS = "chats";
    private static final String FIELD_MESSAGE_TEMPLATE = "message_template";
    //private static final String FIELD_PARSE_MODE = "parse_mode"; // TODO: remove?
    private static final String FIELD_PROXY_ADDRESS = "proxy_address";
    private static final String FIELD_PROXY_USER = "proxy_user";
    private static final String FIELD_PROXY_PASSWORD = "proxy_password";

    @JsonProperty(FIELD_BOT_TOKEN)
    @NotBlank
    public abstract ValueReference botToken();

    /*@JsonProperty(FIELD_GRAYLOG_URL) // TODO: remove?
    @NotBlank
    public abstract ValueReference graylogURL();*/

    @JsonProperty(FIELD_CHATS)
    @NotBlank
    public abstract Set<String> chats();

    @JsonProperty(FIELD_MESSAGE_TEMPLATE)
    @NotBlank
    public abstract ValueReference messageTemplate();

    /*@JsonProperty(FIELD_PARSE_MODE) // TODO: remove?
    @NotBlank
    public abstract ValueReference parseMode();*/

    @JsonProperty(FIELD_PROXY_ADDRESS)
    public abstract ValueReference proxyAddress();

    @JsonProperty(FIELD_PROXY_USER)
    public abstract ValueReference proxyUser();

    @JsonProperty(FIELD_PROXY_PASSWORD)
    public abstract ValueReference proxyPassword();

    public static Builder builder() {
        return Builder.create();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public static abstract class Builder implements EventNotificationConfigEntity.Builder<Builder> {

        @JsonCreator
        public static Builder create() {
            return new AutoValue_TelegramEventNotificationConfigEntity.Builder()
                    .type(TYPE_NAME);
        }

        @JsonProperty(FIELD_BOT_TOKEN)
        public abstract Builder botToken(ValueReference botToken);

        /*@JsonProperty(FIELD_GRAYLOG_URL) // TODO: remove?
        public abstract Builder graylogURL(ValueReference graylogURL);*/

        @JsonProperty(FIELD_CHATS)
        public abstract Builder chats(Set<String> chats);

        @JsonProperty(FIELD_MESSAGE_TEMPLATE)
        public abstract Builder messageTemplate(ValueReference messageTemplate);

        /*@JsonProperty(FIELD_PARSE_MODE) // TODO: remove?
        public abstract Builder parseMode(ValueReference parseMode);*/

        @JsonProperty(FIELD_PROXY_ADDRESS)
        public abstract Builder proxyAddress(ValueReference proxyAddress);

        @JsonProperty(FIELD_PROXY_USER)
        public abstract Builder proxyUser(ValueReference proxyUser);

        @JsonProperty(FIELD_PROXY_PASSWORD)
        public abstract Builder proxyPassword(ValueReference proxyPassword);

        public abstract TelegramEventNotificationConfigEntity build();
    }

    @Override
    public EventNotificationConfig toNativeEntity(Map<String, ValueReference> parameters, Map<EntityDescriptor, Object> nativeEntities) {
        return TelegramEventNotificationConfig.builder()
                .botToken(botToken().asString(parameters))
                //.graylogURL(graylogURL().asString(parameters)) // TODO: remove?
                .chats(chats())
                .messageTemplate(messageTemplate().asString()) // TODO: is there a reason for leaving out "parameters" here?
                //.parseMode(parseMode().asString(parameters)) // TODO: remove?
                .proxyAddress(proxyAddress().asString(parameters))
                .proxyUser(proxyUser().asString(parameters))
                .proxyPassword(proxyPassword().asString(parameters))
                .build();
    }
}
