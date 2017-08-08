package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public interface JournalEvent {

    String getType();
    LocalDateTime getTimestamp();
    JsonNode getRaw();

}
