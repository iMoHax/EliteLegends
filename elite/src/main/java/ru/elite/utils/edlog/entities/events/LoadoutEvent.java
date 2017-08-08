package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import ru.elite.utils.edlog.entities.nodes.ShipNode;

public class LoadoutEvent extends JournalEventImpl {
    private final ShipNode ship;

    public LoadoutEvent(JsonNode node) {
        super(node);
        this.ship = new ShipNode(node);
    }

    public ShipNode getShip(){
        return ship;
    }

}
