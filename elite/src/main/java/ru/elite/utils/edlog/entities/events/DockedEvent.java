package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.store.imp.entities.*;
import ru.elite.utils.edlog.EDConverter;
import ru.elite.utils.edlog.entities.nodes.StationNode;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class DockedEvent extends JournalEventImpl {
    private final StationNode station;

    public DockedEvent(JsonNode node) {
        super(node);
        this.station = new StationNode(node);
    }

    public String getStarSystem(){
        JsonNode n = node.get("StarSystem");
        if (n == null){
            throw new IllegalArgumentException("Event Docked don't have StarSystem attribute");
        }
        return n.asText();
    }

    public StationNode getStation(){
        return station;
    }

    public boolean isCockpitBreach(){
        JsonNode n = node.get("CockpitBreach");
        return n != null && n.asBoolean();
    }

}
