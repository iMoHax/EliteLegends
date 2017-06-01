package ru.elite.utils.edlog.entities;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.store.imp.entities.*;
import ru.elite.utils.edlog.EDConverter;

import java.util.Collection;
import java.util.Collections;

public class FactionStateNode {
    private final JsonNode node;

    public FactionStateNode(JsonNode node) {
        this.node = node;
    }

    public String getName(){
        JsonNode n = node.get("Name");
        if (n == null){
            throw new IllegalArgumentException("Faction node don't have Name attribute");
        }
        return n.asText();
    }

    @Nullable
    public GOVERNMENT getGovernment(){
        JsonNode n = node.get("Government");
        return n != null ? EDConverter.asGovernment(n.asText()) : null;
    }

    @Nullable
    public FACTION getAllegiance(){
        JsonNode n = node.get("Allegiance");
        return n != null ? EDConverter.asAllegiance(n.asText()) : null;
    }

    @Nullable
    public STATE_TYPE getFactionState(){
        JsonNode n = node.get("FactionState");
        return n != null ? EDConverter.asFactionState(n.asText()) : null;
    }

    @Nullable
    public Float getInfluence(){
        JsonNode n = node.get("Influence");
        return n != null && n.isNumber() ? n.floatValue() : null;
    }

    public MinorFactionData asImportData(){
        return new MinorFactionDataBase() {
            @Override
            public String getName() {
                return FactionStateNode.this.getName();
            }

            @Override
            public GOVERNMENT getGovernment() {
                return FactionStateNode.this.getGovernment();
            }

            @Override
            public FACTION getFaction() {
                return FactionStateNode.this.getAllegiance();
            }

            @Nullable
            @Override
            public STATE_TYPE getState() {
                return FactionStateNode.this.getFactionState();
            }

            @Override
            public float getInfluence() {
                Float influence = FactionStateNode.this.getInfluence();
                return influence != null ? influence : super.getInfluence();
            }
        };
    }
}
