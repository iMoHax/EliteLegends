package ru.elite.store.imp;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.core.OFFER_TYPE;
import ru.elite.core.SERVICE_TYPE;
import ru.elite.entity.*;
import ru.elite.store.GalaxyService;
import ru.elite.store.imp.entities.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractImporter implements Importer {
    private final static Logger LOG = LoggerFactory.getLogger(AbstractImporter.class);
    private final EnumSet<IMPORT_FLAG> flags;
    private boolean canceled;

    protected AbstractImporter() {
        this.flags = EnumSet.copyOf(IMPORT_FLAG.ADD_AND_UPDATE);
    }

    protected abstract void before() throws IOException;
    protected abstract void after() throws IOException;

    @Override
    public void addFlag(IMPORT_FLAG flag) {
        flags.add(flag);
    }

    @Override
    public void removeFlag(IMPORT_FLAG flag) {
        flags.remove(flag);
    }

    @Override
    public void setFlags(EnumSet<IMPORT_FLAG> flags) {
        this.flags.clear();
        this.flags.addAll(flags);
    }

    @Override
    public void cancel(){
        this.canceled = true;
    }

    @Override
    public void imp(GalaxyService galaxyService) throws IOException {
        canceled = false;
        before();
        galaxyService.startTransaction();
        try {
            while (next()) {
                if (canceled) break;
                StarSystemData systemData = getSystem();
                StarSystem system = impSystem(galaxyService, systemData);
                if (system != null) {
                    impStations(galaxyService, system, systemData.getStations());
                    impFactions(galaxyService, system, systemData.getFactions());
                } else {
                    LOG.warn("System {} not found", systemData.getName());
                }
            }
        } catch (Exception ex){
            LOG.error("Error on import:", ex);
        } finally {
            galaxyService.commit();
            after();
        }
    }

    @Nullable
    protected StarSystem impSystem(GalaxyService galaxyService, StarSystemData data){
        StarSystem system = findStarSystem(galaxyService, data.getId(), data.getName());
        if (system == null){
            if (flags.contains(IMPORT_FLAG.ADD_STARSYSTEMS)){
                LOG.debug("{} - is new system, adding", data.getName());
                system = galaxyService.createStarSystem(data.getName(), data.getX(), data.getY(), data.getZ());
            } else {
                return null;
            }
        }
        if (flags.contains(IMPORT_FLAG.STARSYSTEMS)){
            updateSystem(galaxyService, system, data);
        }
        return system;
    }

    protected void updateSystem(GalaxyService galaxyService, StarSystem system, StarSystemData data){
        if (data.getName() != null){
            system.setName(data.getName());
        }
        if (!Double.isNaN(data.getX()) && !Double.isNaN(data.getY()) && !Double.isNaN(data.getZ()) &&
                (data.getX() != system.getX() || data.getY() != system.getY() || data.getZ() != system.getZ())){
            system.setPosition(data.getX(), data.getY(), data.getZ());
        }
        if (data.getPopulation() != null){
            system.setPopulation(data.getPopulation());
        }
        if (data.getFaction() != null){
            MinorFaction faction = impFaction(galaxyService, data.getFaction());
            system.setControllingFaction(faction);
        }
        if (data.getSecurity() != null){
            system.setSecurity(data.getSecurity());
        }
        if (data.getPower() != null && data.getPowerState() != null) {
            system.setPower(data.getPower(), data.getPowerState());
        }
        if (data.getIncome() != null && data.getIncome() > 0) {
            system.setIncome(data.getIncome());
        }
        if (data.getModifiedTime() != null){
            system.setModifiedTime(data.getModifiedTime());
        }
    }

    protected void impFactions(GalaxyService galaxyService, StarSystem system, Collection<MinorFactionData> factions){
        if (factions == null) return;
        Set<Long> factionsList = new HashSet<>();
        factionsList.addAll(galaxyService.getAllMinorFactionsIds(system));
        for (MinorFactionData f : factions) {
            MinorFaction faction = impFaction(galaxyService, f);
            if (faction != null){
                factionsList.remove(faction.getId());
                impFactionState(galaxyService, system, faction, f);
            } else {
                LOG.warn("Faction {} not found", f.getName());
            }
        }
        for (Long f : factionsList) {
            LOG.debug("{} - is old faction in system {}, remove", f, system.getName());
            galaxyService.removeFactionFromSystem(system.getId(), f);
        }
    }

    protected MinorFactionState impFactionState(GalaxyService galaxyService, StarSystem system, MinorFaction faction, MinorFactionData data){
        Optional<MinorFactionState> factionState = galaxyService.findFactionState(system.getId(), faction.getId());
        if (!factionState.isPresent()){
            LOG.debug("{} - is new faction in system {}, adding", faction.getName(), system.getName());
            return galaxyService.addFactionToSystem(system, faction, data.getState(), data.getInfluence());
        } else {
            MinorFactionState fs = factionState.get();
            updateFactionState(fs, data);
            return fs;
        }
    }

    protected void updateFactionState(MinorFactionState state, MinorFactionData data){
        if (data.getState() != null){
            state.setState(data.getState());
        }
        if (!Float.isNaN(data.getInfluence())){
            state.setInfluence(data.getInfluence());
        }
    }

    protected MinorFaction impFaction(GalaxyService galaxyService, MinorFactionData data){
        MinorFaction faction = findFaction(galaxyService, data.getId(), data.getName());
        if (faction == null){
            LOG.debug("{} - is new faction, adding", data.getName());
            faction = galaxyService.addFaction(data.getName(), data.getFaction(), data.getGovernment());
        }
        updateFaction(galaxyService, faction, data);
        return faction;
    }

    protected void updateFaction(GalaxyService galaxyService, MinorFaction faction, MinorFactionData data){
        if (data.getName() != null){
            faction.setName(data.getName());
        }
        if (data.getGovernment() != null){
            faction.setGovernment(data.getGovernment());
        }
        if (data.getFaction() != null){
            faction.setFaction(data.getFaction());
        }
        if (data.getHomeSystemName() != null){
            Optional<StarSystem> system = galaxyService.findStarSystemByName(data.getHomeSystemName());
            if (system.isPresent()){
                faction.setHomeSystem(system.get());
            }
        }
        Boolean isPlayers = data.isPlayers();
        if (isPlayers != null){
            faction.setPlayers(isPlayers);
        }
    }

    protected void impStations(GalaxyService galaxyService, StarSystem system, Collection<StationData> stations){
        if (stations == null) return;
        Set<Long> stationsList = new HashSet<>();
        if (flags.contains(IMPORT_FLAG.REMOVE_STATIONS)){
            stationsList.addAll(galaxyService.getAllStationIds(system));
        }
        for (StationData s : stations) {
            Station station = impStation(galaxyService, system, s);
            if (station != null){
                stationsList.remove(station.getId());
                impItems(galaxyService, station, s.getCommodities(), s.getModules(), s.getShips());
            } else {
                LOG.warn("Station {} not found", s.getName());
            }
        }
        if (flags.contains(IMPORT_FLAG.REMOVE_STATIONS)){
            for (Long s : stationsList) {
                LOG.debug("{} - is old station, remove", s);
                galaxyService.removeStation(s);
            }
        }
    }

    @Nullable
    protected Station impStation(GalaxyService galaxyService, StarSystem system, StationData data){
        Station station = findStation(galaxyService, system, data.getId(), data.getName());
        if (station == null){
            if (flags.contains(IMPORT_FLAG.ADD_STATIONS)){
                LOG.debug("{} - is new station, adding", data.getName());
                station = galaxyService.addStationToSystem(system, data.getName(), data.getType(), data.getDistance());
            } else {
                return null;
            }
        }
        if (flags.contains(IMPORT_FLAG.STATIONS)){
            updateStation(galaxyService, station, data);
        }
        return station;
    }

    protected void updateStation(GalaxyService galaxyService, Station station, StationData data) {
        if (data.getName() != null){
            station.setName(data.getName());
        }
        if (!Double.isNaN(data.getDistance())){
            station.setDistance(data.getDistance());
        }
        if (data.getType() != null){
            station.setType(data.getType());
        }
        if (data.getFaction() != null){
            MinorFaction faction = impFaction(galaxyService, data.getFaction());
            station.setFaction(faction);
        }
        if (data.getEconomic() != null){
            station.setEconomic(data.getEconomic());
        }
        if (data.getSubEconomic() != null){
            station.setSubEconomic(data.getSubEconomic());
        }
        Collection<SERVICE_TYPE> importedServices = data.getServices();
        if (importedServices != null){
            Collection<SERVICE_TYPE> services = new ArrayList<>(station.getServices());
            services.removeAll(importedServices);
            services.forEach(station::remove);
            importedServices.forEach(station::add);
        }
        if (data.getModifiedTime() != null){
            station.setModifiedTime(data.getModifiedTime());
        }
    }


    protected void impItems(GalaxyService galaxyService, Station station, Collection<ItemData> commodities, Collection<ModuleData> modules, Collection<ShipData> ships){
        Set<Item> itemsList = new HashSet<>();
        if (flags.contains(IMPORT_FLAG.REMOVE_COMMODITY) || flags.contains(IMPORT_FLAG.REMOVE_MODULE) || flags.contains(IMPORT_FLAG.REMOVE_SHIP)){
            Predicate<Offer> isCanRemove = o ->
                           ships != null && o.getItem().getGroup().isShip() && flags.contains(IMPORT_FLAG.REMOVE_SHIP) ||
                           modules != null && o.getItem().getGroup().isOutfit() && flags.contains(IMPORT_FLAG.REMOVE_MODULE) ||
                           commodities != null && o.getItem().getGroup().isMarket() && flags.contains(IMPORT_FLAG.REMOVE_COMMODITY);
            station.get(OFFER_TYPE.SELL).filter(isCanRemove).map(Offer::getItem).forEach(itemsList::add);
            station.get(OFFER_TYPE.BUY).filter(isCanRemove).map(Offer::getItem).forEach(itemsList::add);
        }
        if (commodities != null){
            for (ItemData c : commodities) {
                Item item = impItem(galaxyService, station, c);
                if (item != null){
                    itemsList.remove(item);
                } else {
                    LOG.warn("Item {}({}) not found", c.getId(), c.getName());
                }
            }
        }
        if (modules != null){
            for (ModuleData m : modules) {
                Item item = impModule(galaxyService, station, m);
                if (item != null){
                    itemsList.remove(item);
                } else {
                    LOG.warn("Item {}({}) not found", m.getId(), m.getName());
                }
            }
        }
        if (ships != null){
            for (ShipData s : ships) {
                Item item = impShip(galaxyService, station, s);
                if (item != null){
                    itemsList.remove(item);
                } else {
                    LOG.warn("Item {}({}) not found", s.getId(), s.getName());
                }
            }
        }

        if (flags.contains(IMPORT_FLAG.REMOVE_COMMODITY) || flags.contains(IMPORT_FLAG.REMOVE_MODULE) || flags.contains(IMPORT_FLAG.REMOVE_SHIP)){
            for (Item i : itemsList) {
                LOG.debug("Remove old offers of {}", i);
                galaxyService.removeOfferFromStation(station.getId(), OFFER_TYPE.SELL, i.getId());
                galaxyService.removeOfferFromStation(station.getId(), OFFER_TYPE.BUY, i.getId());
            }
        }
    }

    @Nullable
    protected Item impItem(GalaxyService galaxyService, Station station, ItemData data) {
        Item item = findItem(galaxyService, data.getId(), data.getName());
        if (item == null){
            if (flags.contains(IMPORT_FLAG.ADD_COMMODITY)){
                LOG.debug("{} - is new commodity, adding", data.getName());
                Group group = findGroup(galaxyService, data.getGroupId(), data.getGroupName());
                if (group != null) {
                    item = galaxyService.addItem(data.getName(), group);
                } else {
                    LOG.warn("Not found group, id = {}, name = {} skip item", data.getGroupId(), data.getGroupName());
                }
            }
        }
        if (item != null && flags.contains(IMPORT_FLAG.ITEMS)){
            updateOffers(galaxyService, station, item, data);
        }
        return item;
    }

    protected void updateOffers(GalaxyService galaxyService, Station station, Item item, ItemData data){
        Optional<Offer> sellOffer = station.getSell(item);
        if (data.getBuyOfferPrice() > 0){
            if (sellOffer.isPresent()){
                Offer o = sellOffer.get();
                o.setPrice(data.getSellOfferPrice());
                o.setCount(data.getSupply());
            } else {
                galaxyService.addOfferToStation(station, OFFER_TYPE.SELL, item, data.getBuyOfferPrice(), data.getSupply());
            }
        } else {
            if (sellOffer.isPresent()) station.remove(sellOffer.get());
        }
        Optional<Offer> buyOffer = station.getBuy(item);
        if (data.getSellOfferPrice() > 0){
            if (buyOffer.isPresent()){
                Offer o = buyOffer.get();
                o.setPrice(data.getBuyOfferPrice());
                o.setCount(data.getDemand());
            } else {
                galaxyService.addOfferToStation(station, OFFER_TYPE.BUY, item, data.getSellOfferPrice(), data.getDemand());
            }
        } else {
            if (buyOffer.isPresent()) station.remove(buyOffer.get());
        }
    }

    protected Item impShip(GalaxyService galaxyService, Station station, ShipData data) {
        Item item = findItem(galaxyService, data.getId(), data.getName());
        if (item == null){
            if (flags.contains(IMPORT_FLAG.ADD_SHIP)){
                LOG.debug("{} - is new ship, adding", data.getName());
                Optional<Group> group = galaxyService.getGalaxy().getGroups().stream().filter(Group::isShip).findAny();
                if (group.isPresent()) {
                    item = galaxyService.addItem(data.getName(), group.get());
                } else {
                    LOG.warn("Not found ship group, skip");
                }
            }
        }
        if (item != null && flags.contains(IMPORT_FLAG.ITEMS)){
            updateOffers(galaxyService, station, item, data);
        }
        return item;
    }

    protected void updateOffers(GalaxyService galaxyService, Station station, Item item, ShipData data){
        Long price = data.getPrice();
        if (price == null) return;
        Optional<Offer> sellOffer = station.getSell(item);
        if (price > 0){
            if (sellOffer.isPresent()){
                Offer o = sellOffer.get();
                o.setPrice(price);
                o.setCount(1);
            } else {
                galaxyService.addOfferToStation(station, OFFER_TYPE.SELL, item, price, 1);
            }
        } else {
            if (sellOffer.isPresent()) station.remove(sellOffer.get());
        }
    }

    protected Item impModule(GalaxyService galaxyService, Station station, ModuleData data) {
        Item item = findItem(galaxyService, data.getId(), data.getName());
        if (item == null){
            if (flags.contains(IMPORT_FLAG.ADD_MODULE)){
                LOG.debug("{} - is new module, adding", data.getName());
                Group group = findGroup(galaxyService, data.getGroupId(), data.getGroupName());
                if (group != null) {
                    item = galaxyService.addItem(data.getName(), group);
                } else {
                    LOG.warn("Not found outfit group, id = {}, name = {} skip module", data.getGroupId(), data.getGroupName());
                }
            }
        }
        if (item != null && flags.contains(IMPORT_FLAG.ITEMS)){
            updateOffers(galaxyService, station, item, data);
        }
        return item;
    }

    protected void updateOffers(GalaxyService galaxyService, Station station, Item item, ModuleData data){
        Optional<Offer> sellOffer = station.getSell(item);
        if (data.getPrice() > 0){
            if (sellOffer.isPresent()){
                Offer o = sellOffer.get();
                o.setPrice(data.getPrice());
                o.setCount(1);
            } else {
                galaxyService.addOfferToStation(station, OFFER_TYPE.SELL, item, data.getPrice(), 1);
            }
        } else {
            if (sellOffer.isPresent()) station.remove(sellOffer.get());
        }
    }

    @Nullable
    private StarSystem findStarSystem(GalaxyService galaxyService, Long id, String name){
        Optional<StarSystem> system;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id != null){
            system = galaxyService.findStarSystemById(id);
        } else {
            system = galaxyService.findStarSystemByName(name);
        }
        return system.orElse(null);
    }

    @Nullable
    private MinorFaction findFaction(GalaxyService galaxyService, Long id, String name){
        Optional<MinorFaction> faction;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id != null){
            faction = galaxyService.findFactionById(id);
        } else {
            faction = galaxyService.findFactionByName(name);
        }
        return faction.orElse(null);
    }

    @Nullable
    private Station findStation(GalaxyService galaxyService, StarSystem system, Long id, String name){
        Optional<Station> station;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id != null){
            station = galaxyService.findStationById(id);
        } else {
            station = galaxyService.findStationByName(system, name);
        }
        return station.orElse(null);
    }

    @Nullable
    private Item findItem(GalaxyService galaxyService, Long id, String name){
        Optional<Item> item;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id != null){
            item = galaxyService.findItemById(id);
        } else {
            item = galaxyService.findItemByName(name);
        }
        return item.orElse(null);
    }

    @Nullable
    private Group findGroup(GalaxyService galaxyService, Long id, String name){
        Optional<Group> group;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id != null){
            group = galaxyService.findGroupById(id);
        } else {
            group = galaxyService.findGroupByName(name);
        }
        return group.orElse(null);
    }





}
