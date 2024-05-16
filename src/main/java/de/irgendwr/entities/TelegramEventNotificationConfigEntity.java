package de.irgendwr.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

import de.irgendwr.TelegramEventNotificationConfig;
import org.graylog.events.contentpack.entities.EventNotificationConfigEntity;
import org.graylog.events.notifications.EventNotificationConfig;
import org.graylog2.contentpacks.model.entities.EntityDescriptor;
import org.graylog2.contentpacks.model.entities.references.ValueReference;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

@AutoValue
@JsonTypeName(TelegramEventNotificationConfigEntity.TYPE_NAME)
@JsonDeserialize(builder = TelegramEventNotificationConfigEntity.Builder.class)
public abstract class TelegramEventNotificationConfigEntity implements EventNotificationConfigEntity {

    public static final String TYPE_NAME = "telegram-notification-v2";
    private static final String FIELD_BOT_TOKEN = "bot_token";
    private static final String FIELD_GRAYLOG_URL = "graylog_url";
    private static final String FIELD_CHATS = "chats";
    private static final String FIELD_MESSAGE_TEMPLATE = "message_template";
    private static final String FIELD_PROXY_ADDRESS = "proxy_address";
    private static final String FIELD_PROXY_USER = "proxy_user";
    private static final String FIELD_PROXY_PASSWORD = "proxy_password";

    @JsonProperty(FIELD_BOT_TOKEN)
    @NotBlank
    public abstract ValueReference botToken();

    @JsonProperty(FIELD_GRAYLOG_URL)
    @NotBlank
    public abstract ValueReference graylogURL();

    @JsonProperty(FIELD_CHATS)
    @NotBlank
    public abstract ImmutableSet<String> chats();

    @JsonProperty(FIELD_MESSAGE_TEMPLATE)
    @NotBlank
    public abstract ValueReference messageTemplate();

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

        @JsonProperty(FIELD_GRAYLOG_URL)
        public abstract Builder graylogURL(ValueReference graylogURL);

        @JsonProperty(FIELD_CHATS)
        public abstract Builder chats(Set<String> chats);

        @JsonProperty(FIELD_MESSAGE_TEMPLATE)
        public abstract Builder messageTemplate(ValueReference messageTemplate);

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
                .graylogURL(graylogURL().asString(parameters))
                .chats(chats())
                .messageTemplate(messageTemplate().asString(parameters))
                .proxyAddress(proxyAddress().asString(parameters))
                .proxyUser(proxyUser().asString(parameters))
                .proxyPassword(proxyPassword().asString(parameters))
                .build();
    }
}
