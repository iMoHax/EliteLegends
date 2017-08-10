package ru.elite.utils.edlog.entities.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.store.imp.entities.*;
import ru.elite.utils.edlog.EDConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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

    @Nullable
    private STATE_TYPE getState(JsonNode node){
        JsonNode n = node.get("State");
        return n != null ? EDConverter.asFactionState(n.asText()) : null;
    }

    public Collection<STATE_TYPE> getPendingStates(){
        JsonNode n = node.get("PendingStates");
        Collection<STATE_TYPE> states = new ArrayList<>();
        if (n != null && n.isArray()){
            for (JsonNode j : n) {
                STATE_TYPE state = getState(j);
                if (state != null){
                    states.add(state);
                }
            }
        }
        return states;
    }

    public Collection<STATE_TYPE> getRecoveringStates(){
        JsonNode n = node.get("RecoveringStates");
        Collection<STATE_TYPE> states = new ArrayList<>();
        if (n != null && n.isArray()){
            for (JsonNode j : n) {
                STATE_TYPE state = getState(j);
                if (state != null){
                    states.add(state);
                }
            }
        }
        return states;
    }


    public MinorFactionData asImportData(){
        return new MinorFactionDataBase() {
            @Override
            public String getName() {
                return FactionStateNode.this.getName();
            }

            @Override
            public Optional<GOVERNMENT> getGovernment() {
                return Optional.ofNullable(FactionStateNode.this.getGovernment());
            }

            @Override
            public Optional<FACTION> getFaction() {
                return Optional.ofNullable(FactionStateNode.this.getAllegiance());
            }

            @Override
            public Optional<STATE_TYPE> getState() {
                return Optional.ofNullable(FactionStateNode.this.getFactionState());
            }

            @Override
            public Optional<Float> getInfluence() {
                return Optional.ofNullable(FactionStateNode.this.getInfluence());
            }

            @Nullable
            @Override
            public Collection<STATE_TYPE> getPendingStates() {
                return FactionStateNode.this.getPendingStates();
            }

            @Nullable
            @Override
            public Collection<STATE_TYPE> getRecoveringStates() {
                return FactionStateNode.this.getRecoveringStates();
            }
        };
    }
}
