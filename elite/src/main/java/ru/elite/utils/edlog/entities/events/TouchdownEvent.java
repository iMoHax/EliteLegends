package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

public class TouchdownEvent extends JournalEventImpl {
    public TouchdownEvent(JsonNode node) {
        super(node);
    }

    @Nullable
    public Double getLatitude(){
        JsonNode n = node.get("Latitude");
        return n != null && n.isNumber() ? n.asDouble() : null;
    }

    @Nullable
    public Double getLongitude(){
        JsonNode n = node.get("Longitude");
        return n != null && n.isNumber() ? n.asDouble() : null;
    }

    public boolean isPlayerControlled(){
        JsonNode n = node.get("PlayerControlled");
        return n != null && n.asBoolean();
    }


}
