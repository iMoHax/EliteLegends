package ru.elite.utils.edlog.entities.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.store.imp.entities.MinorFactionData;
import ru.elite.store.imp.entities.MinorFactionDataBase;
import ru.elite.store.imp.entities.StationData;
import ru.elite.store.imp.entities.StationDataBase;
import ru.elite.utils.edlog.EDConverter;

import java.util.Optional;

public class StationNode {
    private final JsonNode node;

    public StationNode(JsonNode node) {
        this.node = node;
    }

    public String getName(){
        JsonNode n = node.get("StationName");
        if (n == null){
            throw new IllegalArgumentException("Event don't have StationName attribute");
        }
        return n.asText();
    }

    @Nullable
    public STATION_TYPE getStationType(){
        JsonNode n = node.get("StationType");
        return n != null ? EDConverter.asStationType(n.asText()) : null;
    }

    @Nullable
    public GOVERNMENT getGovernment(){
        JsonNode n = node.get("StationGovernment");
        return n != null ? EDConverter.asGovernment(n.asText()) : null;
    }

    @Nullable
    public FACTION getAllegiance(){
        JsonNode n = node.get("StationAllegiance");
        return n != null ? EDConverter.asAllegiance(n.asText()) : null;
    }

    @Nullable
    public ECONOMIC_TYPE getEconomic(){
        JsonNode n = node.get("StationEconomy");
        return n != null ? EDConverter.asEconomic(n.asText()) : null;
    }

    @Nullable
    public String getControllingFaction(){
        JsonNode n = node.get("StationFaction");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public Double getDistance(){
        JsonNode n = node.get("DistFromStarLS");
        return n != null && n.isNumber() ? n.asDouble() : null;
    }

    public StationData asImportData(){
        final MinorFactionData controllingFaction = asMinorFactionData();
        return new StationDataBase(){

            @Override
            public String getName() {
                return StationNode.this.getName();
            }

            @Override
            public Optional<STATION_TYPE> getType() {
                return Optional.ofNullable(StationNode.this.getStationType());
            }

            @Nullable
            @Override
            public MinorFactionData getFaction() {
                return controllingFaction;
            }

            @Override
            public Optional<ECONOMIC_TYPE> getEconomic() {
                return Optional.ofNullable(StationNode.this.getEconomic());
            }

            @Override
            public Optional<Double> getDistance() {
                return Optional.ofNullable(StationNode.this.getDistance());
            }
        };
    }

    private MinorFactionData asMinorFactionData(){
        return new MinorFactionDataBase() {
            @Nullable
            @Override
            public String getName() {
                return StationNode.this.getControllingFaction();
            }

            @Override
            public Optional<GOVERNMENT> getGovernment() {
                return  Optional.ofNullable(StationNode.this.getGovernment());
            }

            @Override
            public Optional<FACTION> getFaction() {
                return  Optional.ofNullable(StationNode.this.getAllegiance());
            }

        };
    }

}
