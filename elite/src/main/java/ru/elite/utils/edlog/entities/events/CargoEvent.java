package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import ru.elite.core.DefaultGalaxy;
import ru.elite.utils.edlog.entities.nodes.ItemNode;

import java.util.ArrayList;
import java.util.Collection;

public class CargoEvent extends JournalEventImpl {

    public CargoEvent(JsonNode node) {
        super(node);
    }

    public Collection<ItemNode> getInventory(){
        JsonNode n = node.get("Inventory");
        if (n == null || !n.isArray()) {
            throw new IllegalArgumentException("Event Cargo don't have Inventory attribute");
        }
        Collection<ItemNode> nodes = new ArrayList<>();
        for (JsonNode jsonNode : n) {
            nodes.add(new ItemNode(jsonNode, DefaultGalaxy.DEFAULT_MARKET_GROUP));
        }
        return nodes;
    }


}
