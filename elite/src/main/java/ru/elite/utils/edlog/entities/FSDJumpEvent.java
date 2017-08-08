package ru.elite.utils.edlog.entities;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.store.imp.entities.MinorFactionData;
import ru.elite.store.imp.entities.MinorFactionDataBase;
import ru.elite.utils.edlog.EDConverter;
import ru.elite.store.imp.entities.StarSystemData;
import ru.elite.store.imp.entities.StarSystemDataBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FSDJumpEvent {
    private final JsonNode node;

    public FSDJumpEvent(JsonNode node) {
        this.node = node;
    }

    public String getStarSystem(){
        JsonNode n = node.get("StarSystem");
        if (n == null){
            throw new IllegalArgumentException("Event FSDJump don't have StarSystem attribute");
        }
        return n.asText();
    }

    public double getX(){
        JsonNode n = node.get("StarPos");
        if (n == null || !n.isArray() || n.size() < 3){
            throw new IllegalArgumentException("Event FSDJump don't have correct StarPos attribute");
        }
        return n.get(0).asDouble();
    }

    public double getY(){
        JsonNode n = node.get("StarPos");
        if (n == null || !n.isArray() || n.size() < 3){
            throw new IllegalArgumentException("Event FSDJump don't have correct StarPos attribute");
        }
        return n.get(1).asDouble();
    }

    public double getZ(){
        JsonNode n = node.get("StarPos");
        if (n == null || !n.isArray() || n.size() < 3){
            throw new IllegalArgumentException("Event FSDJump don't have correct StarPos attribute");
        }
        return n.get(2).asDouble();
    }


    @Nullable
    public GOVERNMENT getGovernment(){
        JsonNode n = node.get("SystemGovernment");
        return n != null ? EDConverter.asGovernment(n.asText()) : null;
    }

    @Nullable
    public FACTION getAllegiance(){
        JsonNode n = node.get("SystemAllegiance");
        return n != null ? EDConverter.asAllegiance(n.asText()) : null;
    }

    @Nullable
    public ECONOMIC_TYPE getEconomic(){
        JsonNode n = node.get("SystemEconomy");
        return n != null ? EDConverter.asEconomic(n.asText()) : null;
    }

    @Nullable
    public SECURITY_LEVEL getSecurityLevel(){
        JsonNode n = node.get("SystemSecurity");
        return n != null ? EDConverter.asSecurityLevel(n.asText()) : null;
    }

    @Nullable
    public POWER getPower(){
        JsonNode n = node.get("Powers");
        if (n == null) return null;
        if (n.isArray()){
            n = n.get(0);
        }
        return n != null ? EDConverter.asPower(n.asText()) : null;
    }

    @Nullable
    public POWER_STATE getPowerState(){
        JsonNode n = node.get("PowerplayState");
        return n != null ? EDConverter.asPowerState(n.asText()) : null;
    }

    @Nullable
    public String getControllingFaction(){
        JsonNode n = node.get("SystemFaction");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public STATE_TYPE getFactionState(){
        JsonNode n = node.get("FactionState");
        return n != null ? EDConverter.asFactionState(n.asText()) : null;
    }

    @Nullable
    public String getBodyName(){
        JsonNode n = node.get("Body");
        return n != null ? n.asText() : null;
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

    @Nullable
    public Collection<FactionStateNode> getFactions(){
        JsonNode n = node.get("Factions");
        if (n == null || !n.isArray()) return null;
        Collection<FactionStateNode> nodes = new ArrayList<>();
        for (JsonNode jsonNode : n) {
            nodes.add(new FactionStateNode(jsonNode));
        }
        return nodes;
    }

    public StarSystemData asImportData(){
        Collection<FactionStateNode> nodes = FSDJumpEvent.this.getFactions();
        final List<MinorFactionData> factions = nodes != null ? nodes.stream().map(FactionStateNode::asImportData).collect(Collectors.toList()) : null;
        final MinorFactionData controllingFaction = asMinorFactionData();
        return new StarSystemDataBase() {
            @Override
            public String getName() {
                return FSDJumpEvent.this.getStarSystem();
            }

            @Override
            public double getX() {
                return FSDJumpEvent.this.getX();
            }

            @Override
            public double getY() {
                return FSDJumpEvent.this.getY();
            }

            @Override
            public double getZ() {
                return FSDJumpEvent.this.getZ();
            }

            @Nullable
            @Override
            public MinorFactionData getFaction() {
                return controllingFaction;
            }

            @Override
            public Optional<SECURITY_LEVEL> getSecurity() {
                return Optional.of(FSDJumpEvent.this.getSecurityLevel());
            }

            @Override
            public Optional<POWER> getPower() {
                return Optional.of(FSDJumpEvent.this.getPower());
            }

            @Override
            public Optional<POWER_STATE> getPowerState() {
                return Optional.of(FSDJumpEvent.this.getPowerState());
            }

            @Nullable
            @Override
            public Collection<MinorFactionData> getFactions() {
                return factions;
            }
        };

    }

    private MinorFactionData asMinorFactionData(){
        return new MinorFactionDataBase() {
            @Override
            public String getName() {
                return FSDJumpEvent.this.getControllingFaction();
            }

            @Override
            public GOVERNMENT getGovernment() {
                return FSDJumpEvent.this.getGovernment();
            }

            @Override
            public FACTION getFaction() {
                return FSDJumpEvent.this.getAllegiance();
            }

            @Override
            public Optional<STATE_TYPE> getState() {
                return Optional.of(FSDJumpEvent.this.getFactionState());
            }
        };
    }

}
