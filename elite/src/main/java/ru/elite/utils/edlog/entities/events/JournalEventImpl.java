package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class JournalEventImpl implements JournalEvent {
    protected final JsonNode node;

    public JournalEventImpl(JsonNode node) {
        this.node = node;
    }

    @Override
    public String getType() {
        JsonNode n = node.get("event");
        if (n == null){
            throw new IllegalArgumentException("Event don't have event attribute");
        }
        return n.asText();
    }

    @Override
    public LocalDateTime getTimestamp() {
        JsonNode n = node.get("timestamp");
        if (n == null){
            throw new IllegalArgumentException("Event don't have timestamp attribute");
        }
        return LocalDateTime.parse(n.asText());
    }

    @Override
    public JsonNode getRaw() {
        return node;
    }

    @Override
    public String toString() {
        return "JournalEventImpl{" +
                "node=" + node +
                '}';
    }
}
