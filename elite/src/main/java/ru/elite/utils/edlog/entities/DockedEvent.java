package ru.elite.utils.edlog.entities;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.store.imp.entities.*;
import ru.elite.utils.edlog.EDConverter;

import java.util.Collection;
import java.util.Collections;

public class DockedEvent {
    private final JsonNode node;

    public DockedEvent(JsonNode node) {
        this.node = node;
    }

    public String getStation(){
        JsonNode n = node.get("StationName");
        if (n == null){
            throw new IllegalArgumentException("Event Docked don't have StationName attribute");
        }
        return n.asText();
    }

    public String getStarSystem(){
        JsonNode n = node.get("StarSystem");
        if (n == null){
            throw new IllegalArgumentException("Event Docked don't have StarSystem attribute");
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
    public STATE_TYPE getFactionState(){
        JsonNode n = node.get("FactionState");
        return n != null ? EDConverter.asFactionState(n.asText()) : null;
    }

    public double getDistance(){
        JsonNode n = node.get("DistFromStarLS");
        if (n == null || !n.isNumber()){
            throw new IllegalArgumentException("Event Docked don't have correct DistFromStarLS attribute");
        }
        return n.asDouble();
    }

    public boolean isCockpitBreach(){
        JsonNode n = node.get("CockpitBreach");
        return n != null && n.asBoolean();
    }


    public StarSystemData asImportData(){
        final Collection<StationData> stationData = Collections.singleton(asStationData());
        return new StarSystemDataBase() {

            @Override
            public String getName() {
                return DockedEvent.this.getStarSystem();
            }

            @Nullable
            @Override
            public Collection<StationData> getStations() {
                return stationData;
            }
        };
    }

    private StationData asStationData(){
        final MinorFactionData controllingFaction = asMinorFactionData();
        return new StationDataBase(){

            @Override
            public String getName() {
                return DockedEvent.this.getStation();
            }

            @Nullable
            @Override
            public STATION_TYPE getType() {
                return DockedEvent.this.getStationType();
            }

            @Nullable
            @Override
            public MinorFactionData getFaction() {
                return controllingFaction;
            }

            @Nullable
            @Override
            public ECONOMIC_TYPE getEconomic() {
                return DockedEvent.this.getEconomic();
            }

            @Override
            public double getDistance() {
                return DockedEvent.this.getDistance();
            }
        };
    }

    private MinorFactionData asMinorFactionData(){
        return new MinorFactionDataBase() {
            @Nullable
            @Override
            public String getName() {
                return DockedEvent.this.getControllingFaction();
            }

            @Override
            public GOVERNMENT getGovernment() {
                return DockedEvent.this.getGovernment();
            }

            @Override
            public FACTION getFaction() {
                return DockedEvent.this.getAllegiance();
            }

            @Override
            public STATE_TYPE getState() {
                return DockedEvent.this.getFactionState();
            }
        };
    }

}
