package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import ru.elite.core.DefaultGalaxy;
import ru.elite.utils.edlog.entities.nodes.ItemNode;
import ru.elite.utils.edlog.entities.nodes.StationNode;

import java.util.ArrayList;
import java.util.Collection;

public class UndockedEvent extends JournalEventImpl {

    public UndockedEvent(JsonNode node) {
        super(node);
    }

    public String getStation(){
        JsonNode n = node.get("StationName");
        if (n == null){
            throw new IllegalArgumentException("Event UndockedEvent don't have StationName attribute");
        }
        return n.asText();
    }



}
