package ru.elite.utils.edlog.entities.events;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.BODY_TYPE;
import ru.elite.store.imp.entities.*;
import ru.elite.utils.edlog.entities.nodes.BodyNode;
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
        BodyNode bodyNode = locationEvent.getBody();
        final BodyData body = bodyNode != null ? bodyNode.asImportData() : null;
        final ShipData ship = asShip();
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
                return Optional.ofNullable(loadGameEvent.getCredits());
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
                return Optional.ofNullable(loadGameEvent.isLanded());
            }

            @Override
            public Optional<Double> getLatitude() {
                return Optional.ofNullable(locationEvent.getLatitude());
            }

            @Override
            public Optional<Double> getLongitude() {
                return Optional.ofNullable(locationEvent.getLongitude());
            }

            @Nullable
            @Override
            public Map<String, Integer> getRanks() {
                return ranks;
            }

            @Override
            public Optional<Boolean> isDead() {
                return Optional.ofNullable(loadGameEvent.isDead());
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

    private ShipData asShip(){
        final ShipData loadoutShip = loadoutEvent.getShip().asImportData();
        final ShipData ship = loadGameEvent.getShip().asImportData();

        return new ShipData() {
            @Override
            public Optional<Long> getId() {
                return ship.getId();
            }

            @Override
            public long getSid() {
                return ship.getSid();
            }

            @Override
            public String getType() {
                return ship.getType();
            }

            @Override
            public Optional<String> getName() {
                return ship.getName();
            }

            @Override
            public Optional<String> getIdent() {
                return ship.getIdent();
            }

            @Override
            public Optional<Double> getFuel() {
                return ship.getFuel();
            }

            @Override
            public Optional<Double> getTank() {
                return ship.getTank();
            }

            @Nullable
            @Override
            public Collection<SlotData> getSlots() {
                return loadoutShip.getSlots();
            }
        };
    }

    @Override
    public String toString() {
        return "StartupEvents{" +
                "cargoEvent=" + cargoEvent +
                ", loadoutEvent=" + loadoutEvent +
                ", materialsEvent=" + materialsEvent +
                ", loadGameEvent=" + loadGameEvent +
                ", rankEvent=" + rankEvent +
                ", locationEvent=" + locationEvent +
                '}';
    }
}
