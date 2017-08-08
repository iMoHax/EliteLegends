package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.store.imp.entities.CommanderData;
import ru.elite.store.imp.entities.CommanderDataBase;
import ru.elite.store.imp.entities.ShipData;
import ru.elite.utils.edlog.entities.nodes.ShipNode;

import java.util.Optional;

public class LoadGameEvent extends JournalEventImpl {
    private final ShipNode ship;

    public LoadGameEvent(JsonNode node) {
        super(node);
        this.ship = new ShipNode(node);
    }

    public String getName(){
        JsonNode n = node.get("Commander");
        if (n == null){
            throw new IllegalArgumentException("Event LoadGameEvent don't have Commander attribute");
        }
        return n.asText();
    }

    public ShipNode getShip(){
        return ship;
    }

    public boolean isLanded(){
        JsonNode n = node.get("StartLanded");
        return n != null && n.asBoolean();
    }

    public boolean isDead(){
        JsonNode n = node.get("StartDead");
        return n != null && n.asBoolean();
    }

    @Nullable
    public String getGameMode(){
        JsonNode n = node.get("GameMode");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public String getGroup(){
        JsonNode n = node.get("Group");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public Double getCredits(){
        JsonNode n = node.get("Credits");
        return n != null && n.isNumber() ? n.asDouble() : null;
    }

    @Nullable
    public Double getLoan(){
        JsonNode n = node.get("Loan");
        return n != null && n.isNumber() ? n.asDouble() : null;
    }

    public CommanderData asImportData(){
        final ShipData shipData = ship.asImportData();
        return new CommanderDataBase() {
            @Override
            public String getName() {
                return LoadGameEvent.this.getName();
            }

            @Nullable
            @Override
            public ShipData getShip() {
                return shipData;
            }

            @Override
            public Optional<Double> getCredits() {
                return Optional.of(LoadGameEvent.this.getCredits());
            }

            @Override
            public Optional<Boolean> isLanded() {
                return Optional.of(LoadGameEvent.this.isLanded());
            }

            @Override
            public Optional<Boolean> isDead() {
                return Optional.of(LoadGameEvent.this.isDead());
            }
        };
    }
}
