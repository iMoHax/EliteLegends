package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.store.imp.entities.MinorFactionData;
import ru.elite.store.imp.entities.MinorFactionDataBase;
import ru.elite.utils.edlog.EDConverter;
import ru.elite.store.imp.entities.StarSystemData;
import ru.elite.store.imp.entities.StarSystemDataBase;
import ru.elite.utils.edlog.entities.nodes.FactionStateNode;
import ru.elite.utils.edlog.entities.nodes.StarSystemNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FSDJumpEvent extends JournalEventImpl {
    private final StarSystemNode starSystem;

    public FSDJumpEvent(JsonNode node) {
        super(node);
        this.starSystem = new StarSystemNode(node);
    }

    public StarSystemNode getStarSystem(){
        return starSystem;
    }

    public double getJumpDistance(){
        JsonNode n = node.get("JumpDist");
        if (n == null || !n.isNumber()){
            throw new IllegalArgumentException("Event FSDJump don't have correct JumpDist attribute");
        }
        return n.asDouble();
    }

    public double getFuelUsed(){
        JsonNode n = node.get("FuelUsed");
        if (n == null || !n.isNumber()){
            throw new IllegalArgumentException("Event FSDJump don't have correct FuelUsed attribute");
        }
        return n.asDouble();
    }

    public double getFuelLevel(){
        JsonNode n = node.get("FuelLevel");
        if (n == null || !n.isNumber()){
            throw new IllegalArgumentException("Event FSDJump don't have correct FuelLevel attribute");
        }
        return n.asDouble();
    }

    public boolean isBoostUsed(){
        JsonNode n = node.get("BoostUsed");
        return n != null && n.asBoolean();
    }

}
