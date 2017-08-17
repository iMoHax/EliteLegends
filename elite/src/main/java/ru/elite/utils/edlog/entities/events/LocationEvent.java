package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.BODY_TYPE;
import ru.elite.utils.edlog.EDConverter;
import ru.elite.utils.edlog.entities.nodes.BodyNode;
import ru.elite.utils.edlog.entities.nodes.StarSystemNode;
import ru.elite.utils.edlog.entities.nodes.StationNode;

public class LocationEvent extends JournalEventImpl {
    private final StarSystemNode starSystem;
    private final StationNode station;
    private final BodyNode body;

    public LocationEvent(JsonNode node) {
        super(node);
        starSystem = new StarSystemNode(node);
        station = new StationNode(node);
        body = new BodyNode(node);
    }

    public StarSystemNode getStarSystem(){
        return starSystem;
    }

    public boolean isDocked(){
        JsonNode n = node.get("Docked");
        return n != null && n.asBoolean();
    }

    @Nullable
    public StationNode getStation(){
        JsonNode n = node.get("StationName");
        return n != null ? station : null;
    }

    @Nullable
    public BodyNode getBody(){
        return body.getBodyName() != null ? body : null;
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

}
