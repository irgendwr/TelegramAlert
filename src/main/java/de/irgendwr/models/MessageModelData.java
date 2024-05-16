package de.irgendwr.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import org.graylog.events.event.EventDto;
import org.graylog.events.processor.EventDefinitionDto;
import org.graylog2.plugin.MessageSummary;

import java.util.List;
import java.util.Optional;

@AutoValue
public abstract class MessageModelData {
    @JsonProperty("event_definition")
    public abstract Optional<EventDefinitionDto> eventDefinition();

    @JsonProperty("event_definition_id")
    public abstract String eventDefinitionId();

    @JsonProperty("event_definition_type")
    public abstract String eventDefinitionType();

    @JsonProperty("event_definition_title")
    public abstract String eventDefinitionTitle();

    @JsonProperty("event_definition_description")
    public abstract String eventDefinitionDescription();

    @JsonProperty("job_definition_id")
    public abstract String jobDefinitionId();

    @JsonProperty("job_trigger_id")
    public abstract String jobTriggerId();

    @JsonProperty("event")
    public abstract EventDto event();

    @JsonProperty("backlog")
    public abstract ImmutableList<MessageSummary> backlog();

    @JsonProperty("backlog_size")
    public abstract int backlogSize();

    @JsonProperty("message_too_long")
    public abstract boolean messageTooLong();

    @JsonProperty("graylog_url")
    public abstract String graylogUrl();

    @JsonProperty("streams")
    public abstract ImmutableList<StreamModelData> streams();

    public static Builder builder() {
        return new AutoValue_MessageModelData.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder eventDefinition(Optional<EventDefinitionDto> eventDefinitionDto);

        public abstract Builder eventDefinitionId(String id);

        public abstract Builder eventDefinitionType(String type);

        public abstract Builder eventDefinitionTitle(String title);

        public abstract Builder eventDefinitionDescription(String description);

        public abstract Builder jobDefinitionId(String jobDefinitionId);

        public abstract Builder jobTriggerId(String jobTriggerId);

        public abstract Builder event(EventDto event);

        public abstract Builder backlog(List<MessageSummary> backlog);

        public abstract Builder backlogSize(int backlogSize);

        public abstract Builder messageTooLong(boolean messageTooLong);

        public abstract Builder graylogUrl(String graylogUrl);

        public abstract Builder streams(List<StreamModelData> streams);

        public abstract MessageModelData build();
    }
}
