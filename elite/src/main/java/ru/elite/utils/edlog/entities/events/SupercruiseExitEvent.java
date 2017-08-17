package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import ru.elite.utils.edlog.entities.nodes.BodyNode;
import ru.elite.utils.edlog.entities.nodes.StarSystemNode;
import ru.elite.utils.edlog.entities.nodes.StationNode;

public class SupercruiseExitEvent extends JournalEventImpl {
    private final BodyNode body;

    public SupercruiseExitEvent(JsonNode node) {
        super(node);
        this.body = new BodyNode(node);
    }

    public String getStarSystem(){
        JsonNode n = node.get("StarSystem");
        if (n == null){
            throw new IllegalArgumentException("Event SupercruiseExit don't have StarSystem attribute");
        }
        return n.asText();
    }

    public BodyNode getBody() {
        return body;
    }
}
