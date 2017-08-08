package ru.elite.utils.edlog.entities.events;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.BODY_TYPE;
import ru.elite.store.imp.entities.*;
import ru.elite.utils.edlog.entities.nodes.StationNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class StartupEvents {
    private CargoEvent cargoEvent;
    private LoadoutEvent loadoutEvent;
    private MaterialsEvent materialsEvent;
    private LoadGameEvent loadGameEvent;
    private RankEvent rankEvent;
    private LocationEvent locationEvent;

    public boolean needInit(){
        return cargoEvent == null || loadoutEvent == null || materialsEvent == null ||
               loadGameEvent == null || rankEvent == null || locationEvent == null;
    }

    public boolean init(JournalEvent event){
        boolean isStartupEvent = false;

        switch (event.getType()){
            case "Cargo": cargoEvent = (CargoEvent) event;
                isStartupEvent = true;
                break;
            case "Loadout": loadoutEvent = (LoadoutEvent) event;
                isStartupEvent = true;
                break;
            case "Materials": materialsEvent = (MaterialsEvent) event;
                isStartupEvent = true;
                break;
            case "LoadGame": loadGameEvent = (LoadGameEvent) event;
                isStartupEvent = true;
                break;
            case "Rank": rankEvent = (RankEvent) event;
                isStartupEvent = true;
                break;
            case "Location": locationEvent = (LocationEvent) event;
                isStartupEvent = true;
                break;
            case "Progress":
                isStartupEvent = true;
                break;
        }
        return isStartupEvent;
    }

    public CommanderData asImportData(){
        final Collection<InventoryEntryData> inventory = asInventory();
        final BodyData body = asBody();
        final ShipData ship = loadoutEvent.getShip().asImportData();
        final StarSystemData starSystem = locationEvent.getStarSystem().asImportData();
        StationNode stationNode = locationEvent.getStation();
        final StationData station = stationNode != null ? stationNode.asImportData() : null;
        final Map<String, Integer> ranks = rankEvent.getRanks();
        return new CommanderDataBase() {
            @Override
            public String getName() {
                return loadGameEvent.getName();
            }

            @Nullable
            @Override
            public ShipData getShip() {
                return ship;
            }

            @Override
            public Optional<Double> getCredits() {
                return Optional.of(loadGameEvent.getCredits());
            }

            @Nullable
            @Override
            public StarSystemData getStarSystem() {
                return starSystem;
            }

            @Nullable
            @Override
            public StationData getStation() {
                return station;
            }

            @Nullable
            @Override
            public BodyData getBody() {
                return body;
            }

            @Override
            public Optional<Boolean> isLanded() {
                return Optional.of(loadGameEvent.isLanded());
            }

            @Override
            public Optional<Double> getLatitude() {
                return Optional.of(locationEvent.getLatitude());
            }

            @Override
            public Optional<Double> getLongitude() {
                return Optional.of(locationEvent.getLongitude());
            }

            @Nullable
            @Override
            public Map<String, Integer> getRanks() {
                return ranks;
            }

            @Override
            public Optional<Boolean> isDead() {
                return Optional.of(loadGameEvent.isDead());
            }

            @Nullable
            @Override
            public Collection<InventoryEntryData> getInventory() {
                return inventory;
            }
        };
    }

    private Collection<InventoryEntryData> asInventory(){
        Collection<InventoryEntryData> items = new ArrayList<>();
        cargoEvent.getInventory().forEach(i -> items.add(i.asImportData()));
        materialsEvent.getMaterials().forEach(i -> items.add(i.asImportData()));
        materialsEvent.getManufactured().forEach(i -> items.add(i.asImportData()));
        materialsEvent.getDatas().forEach(i -> items.add(i.asImportData()));
        return items;
    }

    private BodyData asBody(){
        return new BodyDataBase() {
            @Override
            public String getName() {
                return locationEvent.getBodyName();
            }

            @Override
            public BODY_TYPE getType() {
                return locationEvent.getBodyType();
            }
        };
    }

}
