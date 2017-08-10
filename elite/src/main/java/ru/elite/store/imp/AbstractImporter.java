package ru.elite.store.imp;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.core.OFFER_TYPE;
import ru.elite.core.SERVICE_TYPE;
import ru.elite.core.STATE_STATUS;
import ru.elite.core.STATE_TYPE;
import ru.elite.entity.*;
import ru.elite.store.GalaxyService;
import ru.elite.store.imp.entities.*;

import javax.persistence.EntityTransaction;
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

    protected abstract boolean next() throws IOException;
    protected abstract StarSystemData getSystem();
    protected abstract CommanderData getCmdr();


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
        EntityTransaction transaction = galaxyService.startTransaction();
        try {
            while (next()) {
                if (canceled) break;
                StarSystemData systemData = getSystem();
                StarSystem system = impSystem(galaxyService, systemData);
                if (system == null) {
                    LOG.warn("System {} not found", systemData.getName());
                }
            }
            CommanderData cmdr = getCmdr();
            impCommander(galaxyService, cmdr);
            transaction.commit();
        } catch (Exception ex){
            LOG.error("Error on import:", ex);
            transaction.rollback();
        } finally {
            after();
        }
    }

    @Nullable
    protected StarSystem impSystem(GalaxyService galaxyService, StarSystemData data){
        return impSystem(galaxyService, data, true);
    }

    @Nullable
    protected StarSystem impSystem(GalaxyService galaxyService, StarSystemData data, boolean includeAll){
        StarSystem system = findStarSystem(galaxyService, data.getId(), data.getName());
        if (system == null){
            if (flags.contains(IMPORT_FLAG.ADD_STARSYSTEMS)){
                LOG.debug("{} - is new system, adding", data.getName());
                system = galaxyService.getGalaxy().addStarSystem(data.getName(), data.getX(), data.getY(), data.getZ());
            } else {
                return null;
            }
        }
        if (flags.contains(IMPORT_FLAG.STARSYSTEMS)){
            updateSystem(galaxyService, system, data);
        }
        if (includeAll){
            impStations(galaxyService, system, data.getStations());
            impFactions(galaxyService, system, data.getFactions());
            impBodies(galaxyService, system, data.getBodies());
        }
        return system;
    }

    protected void updateSystem(GalaxyService galaxyService, StarSystem system, StarSystemData data){
        data.getId().ifPresent(system::setEID);
        if (data.getName() != null){
            system.setName(data.getName());
        }
        if (!Double.isNaN(data.getX()) && !Double.isNaN(data.getY()) && !Double.isNaN(data.getZ()) &&
                (data.getX() != system.getX() || data.getY() != system.getY() || data.getZ() != system.getZ())){
            system.setPosition(data.getX(), data.getY(), data.getZ());
        }
        data.getPopulation().ifPresent(system::setPopulation);
        if (data.getFaction() != null){
            MinorFaction faction = impFaction(galaxyService, data.getFaction());
            if (faction != null){
                system.setControllingFaction(faction);
            }
        }
        data.getSecurity().ifPresent(system::setSecurity);
        if (data.getPower().isPresent() && data.getPowerState().isPresent()) {
            system.setPower(data.getPower().get(), data.getPowerState().get());
        }
        data.getIncome().ifPresent(i -> { if (i > 0) system.setIncome(i);});
        data.getModifiedTime().ifPresent(system::setModifiedTime);
    }

    protected void impFactions(GalaxyService galaxyService, StarSystem system, Collection<MinorFactionData> factions){
        if (factions == null) return;
        Set<String> factionsList = new HashSet<>();
        factionsList.addAll(galaxyService.getAllMinorFactionsNames(system));
        for (MinorFactionData f : factions) {
            MinorFaction faction = impFaction(galaxyService, f);
            if (faction != null){
                factionsList.remove(faction.getName());
                impFactionState(galaxyService, system, faction, f);
            } else {
                LOG.warn("Faction {} not found", f.getName());
            }
        }
        for (String f : factionsList) {
            LOG.debug("{} - is old faction in system {}, remove", f, system.getName());
            galaxyService.removeFactionFromSystemByName(system, f);
        }
    }

    @Nullable
    protected MinorFactionState impFactionState(GalaxyService galaxyService, StarSystem system, MinorFaction faction, MinorFactionData data){
        Optional<MinorFactionState> factionState = galaxyService.findFactionState(system, faction);
        MinorFactionState fs;
        if (!factionState.isPresent()){
            if (data.getInfluence().isPresent() && data.getState().isPresent()){
                LOG.debug("{} - is new faction in system {}, adding", faction.getName(), system.getName());
                fs = system.addFaction(faction, data.getState().get(), data.getInfluence().get());
            } else {
                return null;
            }
        } else {
            fs = factionState.get();
        }
        updateFactionState(fs, data);
        return fs;
    }

    protected void updateFactionState(MinorFactionState state, MinorFactionData data){
        data.getState().ifPresent(state::setState);
        data.getInfluence().ifPresent(state::setInfluence);
        Collection<STATE_TYPE> states = data.getPendingStates();
        if (states != null){
            states.forEach(s -> state.setStateStatus(s, STATE_STATUS.PENDING));
        }
        states = data.getRecoveringStates();
        if (states != null){
            states.forEach(s -> state.setStateStatus(s, STATE_STATUS.RECOVERY));
        }

    }

    @Nullable
    protected MinorFaction impFaction(GalaxyService galaxyService, MinorFactionData data){
        MinorFaction faction = findFaction(galaxyService, data.getId(), data.getName());
        if (faction == null){
            if (data.getFaction().isPresent() && data.getGovernment().isPresent()){
                LOG.debug("{} - is new faction, adding", data.getName());
                faction = galaxyService.getGalaxy().addFaction(data.getName(), data.getFaction().get(), data.getGovernment().get());
            } else {
                return null;
            }
        }
        updateFaction(galaxyService, faction, data);
        return faction;
    }

    protected void updateFaction(GalaxyService galaxyService, MinorFaction faction, MinorFactionData data){
        data.getId().ifPresent(faction::setEID);
        if (data.getName() != null){
            faction.setName(data.getName());
        }
        data.getGovernment().ifPresent(faction::setGovernment);
        data.getFaction().ifPresent(faction::setFaction);
        data.getHomeSystemName().ifPresent(v -> {
            Optional<StarSystem> system = galaxyService.findStarSystemByName(v);
            if (system.isPresent()){
                faction.setHomeSystem(system.get());
            }
        });
        data.isPlayers().ifPresent(faction::setPlayers);
    }

    protected void impStations(GalaxyService galaxyService, StarSystem system, Collection<StationData> stations){
        if (stations == null) return;
        Set<String> stationsList = new HashSet<>();
        if (flags.contains(IMPORT_FLAG.REMOVE_STATIONS)){
            stationsList.addAll(galaxyService.getAllStationNames(system));
        }
        for (StationData s : stations) {
            Station station = impStation(galaxyService, system, s);
            if (station != null){
                stationsList.remove(station.getName());
            } else {
                LOG.warn("Station {} not found", s.getName());
            }
        }
        if (flags.contains(IMPORT_FLAG.REMOVE_STATIONS)){
            for (String s : stationsList) {
                LOG.debug("{} - is old station, remove", s);
                galaxyService.removeStationByName(system, s);
            }
        }
    }

    @Nullable
    protected Station impStation(GalaxyService galaxyService, StarSystem system, StationData data){
        Station station = findStation(galaxyService, system, data.getId(), data.getName());
        if (station == null){
            if (flags.contains(IMPORT_FLAG.ADD_STATIONS) && data.getType().isPresent() && data.getDistance().isPresent()){
                LOG.debug("{} - is new station, adding", data.getName());
                station = system.addStation(data.getName(), data.getType().get(), data.getDistance().get());
            } else {
                return null;
            }
        }
        if (flags.contains(IMPORT_FLAG.STATIONS)){
            updateStation(galaxyService, station, data);
        }
        impItems(galaxyService, station, data.getCommodities(), data.getModules(), data.getShips());
        return station;
    }

    protected void updateStation(GalaxyService galaxyService, Station station, StationData data) {
        data.getId().ifPresent(station::setEID);
        if (data.getName() != null){
            station.setName(data.getName());
        }
        data.getDistance().ifPresent(station::setDistance);
        data.getType().ifPresent(station::setType);
        if (data.getFaction() != null){
            MinorFaction faction = impFaction(galaxyService, data.getFaction());
            if (faction != null){
                station.setFaction(faction);
            }
        }
        data.getEconomic().ifPresent(station::setEconomic);
        data.getSubEconomic().ifPresent(station::setSubEconomic);
        Collection<SERVICE_TYPE> importedServices = data.getServices();
        if (importedServices != null){
            Collection<SERVICE_TYPE> services = new ArrayList<>(station.getServices());
            services.removeAll(importedServices);
            services.forEach(station::removeService);
            importedServices.forEach(station::addService);
        }
        data.getModifiedTime().ifPresent(station::setModifiedTime);
    }


    protected void impItems(GalaxyService galaxyService, Station station, Collection<ItemData> commodities, Collection<ItemModuleData> modules, Collection<ItemShipData> ships){
        Set<Item> itemsList = new HashSet<>();
        if (flags.contains(IMPORT_FLAG.REMOVE_COMMODITY) || flags.contains(IMPORT_FLAG.REMOVE_ITEM_MODULE) || flags.contains(IMPORT_FLAG.REMOVE_ITEM_SHIP)){
            Predicate<Offer> isCanRemove = o ->
                           ships != null && o.getItem().getGroup().getType().isShip() && flags.contains(IMPORT_FLAG.REMOVE_ITEM_SHIP) ||
                           modules != null && o.getItem().getGroup().getType().isOutfit() && flags.contains(IMPORT_FLAG.REMOVE_ITEM_MODULE) ||
                           commodities != null && o.getItem().getGroup().getType().isMarket() && flags.contains(IMPORT_FLAG.REMOVE_COMMODITY);
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
            for (ItemModuleData m : modules) {
                Item item = impModuleItem(galaxyService, station, m);
                if (item != null){
                    itemsList.remove(item);
                } else {
                    LOG.warn("Item {}({}) not found", m.getId(), m.getName());
                }
            }
        }
        if (ships != null){
            for (ItemShipData s : ships) {
                Item item = impShipItem(galaxyService, station, s);
                if (item != null){
                    itemsList.remove(item);
                } else {
                    LOG.warn("Item {}({}) not found", s.getId(), s.getName());
                }
            }
        }

        if (flags.contains(IMPORT_FLAG.REMOVE_COMMODITY) || flags.contains(IMPORT_FLAG.REMOVE_ITEM_MODULE) || flags.contains(IMPORT_FLAG.REMOVE_ITEM_SHIP)){
            for (Item i : itemsList) {
                LOG.debug("Remove old offers of {}", i);
                galaxyService.removeOffersFromStation(station, i);
            }
        }
    }

    @Nullable
    protected Item impItem(GalaxyService galaxyService, Station station, ItemData data) {
        Item item = findItem(galaxyService, data.getId(), data.getName());
        if (item == null){
            if (flags.contains(IMPORT_FLAG.ADD_COMMODITY) && data.getGroupName().isPresent()){
                LOG.debug("{} - is new commodity, adding", data.getName());
                Group group = findGroup(galaxyService, data.getGroupName().get());
                if (group != null) {
                    item = galaxyService.getGalaxy().addItem(data.getName(), group);
                } else {
                    LOG.warn("Not found group {}, skip item", data.getGroupName());
                }
            }
        }
        if (item != null && flags.contains(IMPORT_FLAG.ITEMS)){
            updateOffers(station, item, data);
        }
        return item;
    }

    protected void updateOffers(Station station, Item item, ItemData data){
        data.getId().ifPresent(item::setEID);
        Optional<Offer> sellOffer = station.getSell(item);
        if (data.getBuyOfferPrice() > 0){
            if (sellOffer.isPresent()){
                Offer o = sellOffer.get();
                o.setPrice(data.getSellOfferPrice());
                o.setCount(data.getSupply());
            } else {
                station.addOffer(item, OFFER_TYPE.SELL, data.getBuyOfferPrice(), data.getSupply());
            }
        } else {
            if (sellOffer.isPresent()) station.removeOffer(sellOffer.get());
        }
        Optional<Offer> buyOffer = station.getBuy(item);
        if (data.getSellOfferPrice() > 0){
            if (buyOffer.isPresent()){
                Offer o = buyOffer.get();
                o.setPrice(data.getBuyOfferPrice());
                o.setCount(data.getDemand());
            } else {
                station.addOffer(item, OFFER_TYPE.BUY, data.getSellOfferPrice(), data.getDemand());
            }
        } else {
            if (buyOffer.isPresent()) station.removeOffer(buyOffer.get());
        }
    }

    protected Item impShipItem(GalaxyService galaxyService, Station station, ItemShipData data) {
        Item item = findItem(galaxyService, data.getId(), data.getName());
        if (item == null){
            if (flags.contains(IMPORT_FLAG.ADD_ITEM_SHIP)){
                LOG.debug("{} - is new ship item, adding", data.getName());
                Optional<Group> group = galaxyService.getGalaxy().getGroups().stream().filter(g -> g.getType().isShip()).findAny();
                if (group.isPresent()) {
                    item = galaxyService.getGalaxy().addItem(data.getName(), group.get());
                } else {
                    LOG.warn("Not found ship group, skip");
                }
            }
        }
        if (item != null && flags.contains(IMPORT_FLAG.ITEMS)){
            updateOffers(station, item, data);
        }
        return item;
    }

    protected void updateOffers(Station station, Item item, ItemShipData data){
        data.getId().ifPresent(item::setEID);
        data.getPrice().ifPresent(price -> {
            Optional<Offer> sellOffer = station.getSell(item);
            if (price > 0){
                if (sellOffer.isPresent()){
                    Offer o = sellOffer.get();
                    o.setPrice(price);
                    o.setCount(1);
                } else {
                    station.addOffer(item, OFFER_TYPE.SELL, price, 1);
                }
            } else {
                if (sellOffer.isPresent()) station.removeOffer(sellOffer.get());
            }
        });
    }

    protected Item impModuleItem(GalaxyService galaxyService, Station station, ItemModuleData data) {
        Item item = findItem(galaxyService, data.getId(), data.getName());
        if (item == null){
            if (flags.contains(IMPORT_FLAG.ADD_ITEM_MODULE) && data.getGroupName().isPresent()){
                LOG.debug("{} - is new module item, adding", data.getName());
                Group group = findGroup(galaxyService, data.getGroupName().get());
                if (group != null) {
                    item = galaxyService.getGalaxy().addItem(data.getName(), group);
                } else {
                    LOG.warn("Not found outfit group {}, skip module item", data.getGroupName());
                }
            }
        }
        if (item != null && flags.contains(IMPORT_FLAG.ITEMS)){
            updateOffers(station, item, data);
        }
        return item;
    }

    protected void updateOffers(Station station, Item item, ItemModuleData data){
        data.getId().ifPresent(item::setEID);
        Optional<Offer> sellOffer = station.getSell(item);
        if (data.getPrice() > 0){
            if (sellOffer.isPresent()){
                Offer o = sellOffer.get();
                o.setPrice(data.getPrice());
                o.setCount(1);
            } else {
                station.addOffer(item, OFFER_TYPE.SELL, data.getPrice(), 1);
            }
        } else {
            if (sellOffer.isPresent()) station.removeOffer(sellOffer.get());
        }
    }

    protected Commander impCommander(GalaxyService galaxyService, CommanderData data){
        Commander cmdr = findCmdr(galaxyService, data.getId(), data.getName());
        if (cmdr == null){
            LOG.debug("{} - is new cmdr, adding", data.getName());
            cmdr = galaxyService.getGalaxy().addCmdr(data.getName());
        }
        updateCmdr(galaxyService, cmdr, data);
        impInventory(galaxyService, cmdr, data.getInventory());
        impShips(galaxyService, cmdr, data.getShips());
        return cmdr;
    }

    protected void updateCmdr(GalaxyService galaxyService, Commander cmdr, CommanderData data){
        data.getId().ifPresent(cmdr::setEID);
        if (data.getShip() != null){
            Ship ship = impShip(galaxyService, cmdr, data.getShip());
            cmdr.setShip(ship);
        }
        cmdr.setName(data.getName());
        data.getCredits().ifPresent(cmdr::setCredits);
        StarSystemData starSystemData = data.getStarSystem();
        if (starSystemData != null){
            StarSystem starSystem = impSystem(galaxyService, starSystemData, true);
            if (starSystem != null){
                cmdr.setStarSystem(starSystem);
                StationData stationData = data.getStation();
                if (stationData != null){
                    Station station = impStation(galaxyService,  starSystem, stationData);
                    if (station != null){
                        cmdr.setStation(station);
                    } else {
                        LOG.warn("Station {} not found", stationData.getName());
                    }
                }
                BodyData bodyData = data.getBody();
                if (bodyData != null){
                    Body body = impBody(galaxyService, starSystem, bodyData);
                    if (body != null){
                        cmdr.setBody(body);
                    } else {
                        LOG.warn("Body {} not found", bodyData.getName());
                    }
                }
            } else {
                LOG.warn("StarSystem {} not found", starSystemData.getName());
            }
        }
        data.isLanded().ifPresent(cmdr::setLanded);
        data.getLatitude().ifPresent(cmdr::setLatitude);
        data.getLongitude().ifPresent(cmdr::setLongitude);
        Map<String, Integer> ranks = data.getRanks();
        if (ranks != null){
            for (Map.Entry<String, Integer> entry : ranks.entrySet()) {
                cmdr.setRank(entry.getKey(), entry.getValue());
            }
        }
        data.isDead().ifPresent(cmdr::setDead);
    }

    protected void impBodies(GalaxyService galaxyService, StarSystem system, Collection<BodyData> bodies){
        if (bodies == null) return;
        Set<String> bodiesList = new HashSet<>();
        if (flags.contains(IMPORT_FLAG.REMOVE_BODIES)){
            bodiesList.addAll(galaxyService.getAllBodyNames(system));
        }
        for (BodyData b : bodies) {
            if (b.getType().isStation()) continue;
            Body body = impBody(galaxyService, system, b);
            if (body != null){
                bodiesList.remove(body.getName());
            } else {
                LOG.warn("Body {} not found", b.getName());
            }
        }
        if (flags.contains(IMPORT_FLAG.REMOVE_BODIES)){
            for (String b : bodiesList) {
                LOG.debug("{} - is old body, remove", b);
                galaxyService.removeBodyByName(system, b);
            }
        }
    }

    @Nullable
    protected Body impBody(GalaxyService galaxyService, StarSystem system, BodyData data){
        Body body = findBody(galaxyService, system, data.getId(), data.getName());
        if (body == null){
            if (flags.contains(IMPORT_FLAG.ADD_BODIES)){
                LOG.debug("{} - is new body, adding", data.getName());
                body = system.addBody(data.getName(), data.getType());
            } else {
                return null;
            }
        }
        updateBody(body, data);
        return body;
    }

    protected void updateBody(Body body, BodyData data) {
        data.getId().ifPresent(body::setEID);
        body.setName(data.getName());
    }


    protected void impShips(GalaxyService galaxyService, Commander cmdr, Collection<ShipData> ships){
        if (ships == null) return;
        Set<Long> shipList = new HashSet<>();
        if (flags.contains(IMPORT_FLAG.REMOVE_SHIPS)){
            shipList.addAll(galaxyService.getAllShipSids(cmdr));
        }
        for (ShipData s : ships) {
            Ship ship = impShip(galaxyService, cmdr, s);
            if (ship != null){
                shipList.remove(ship.getSid());
            } else {
                LOG.warn("Ship {} not found", s.getSid());
            }
        }
        if (flags.contains(IMPORT_FLAG.REMOVE_SHIPS)){
            for (Long s : shipList) {
                LOG.debug("{} - is old ship, remove", s);
                galaxyService.removeShipBySid(cmdr, s);
            }
        }
    }

    protected Ship impShip(GalaxyService galaxyService, Commander cmdr, ShipData data){
        Ship ship = findShip(galaxyService, cmdr, data.getId(), data.getSid());
        if (ship == null){
            LOG.debug("{} - is new ship, adding", data.getSid());
            ship = cmdr.addShip(data.getSid(), data.getType());
        }
        updateShip(ship, data);
        impSlots(galaxyService, ship, data.getSlots());
        return ship;
    }

    protected void updateShip(Ship ship, ShipData data){
        data.getId().ifPresent(ship::setEID);
        data.getName().ifPresent(ship::setName);
        data.getIdent().ifPresent(ship::setIdent);
        data.getFuel().ifPresent(ship::setFuel);
        data.getTank().ifPresent(ship::setTank);
    }

    protected void impSlots(GalaxyService galaxyService, Ship ship, Collection<SlotData> slots){
        if (slots == null) return;
        Set<String> slotsList = new HashSet<>();
        if (flags.contains(IMPORT_FLAG.REMOVE_SLOTS)){
            slotsList.addAll(galaxyService.getAllSlotNames(ship));
        }
        for (SlotData s : slots) {
            Slot slot = impSlot(ship, s);
            if (slot != null){
                slotsList.remove(slot.getName());
            } else {
                LOG.warn("Slot {} not found", s.getName());
            }
        }
        if (flags.contains(IMPORT_FLAG.REMOVE_SLOTS)){
            for (String s : slotsList) {
                LOG.debug("{} - is old slot, remove", s);
                galaxyService.removeSlotByName(ship, s);
            }
        }
    }

    @Nullable
    protected Slot impSlot(Ship ship, SlotData data){
        Slot slot = ship.getSlot(data.getName()).orElse(null);
        if (slot == null){
            if (flags.contains(IMPORT_FLAG.ADD_SLOTS)){
                LOG.debug("{} - is new slot, adding", data.getName());
                slot = ship.addSlot(data.getName());
            } else {
                return null;
            }
        }
        updateSlot(slot, data);
        impModule(slot, data);
        return slot;
    }

    protected void updateSlot(Slot slot, SlotData data){
        data.isActive().ifPresent(slot::setActive);
        data.getPriority().ifPresent(slot::setPriority);
    }

    protected Module impModule(Slot slot, SlotData data){
        if (!data.getModuleName().isPresent()) return null;
        Module module = slot.getModule();
        String name = data.getModuleName().get();
        if (module == null || !module.getName().equals(name)){
            LOG.debug("{} - is new module, adding", name);
            module = slot.setModule(name);
        }
        updateModule(module, data);
        return module;
    }

    protected void updateModule(Module module, SlotData data){
        if (data.getBlueprint().isPresent() && data.getBlueprintLevel().isPresent()){
            module.setBlueprint(data.getBlueprint().get(), data.getBlueprintLevel().get());
        }
        data.getHealth().ifPresent(module::setHealth);
        data.getAmmoClip().ifPresent(module::setAmmoClip);
        data.getAmmoHopper().ifPresent(module::setAmmoHopper);
        data.getCost().ifPresent(module::setCost);
    }

    protected void impInventory(GalaxyService galaxyService, Commander cmdr, Collection<InventoryEntryData> items){
        if (items == null) return;
        if (flags.contains(IMPORT_FLAG.REMOVE_INVENTORY_ITEMS)){
            LOG.debug("Clear inventory");
            cmdr.clearItems();
        }
        for (InventoryEntryData data : items) {
            InventoryEntry entry = impInventoryEntry(galaxyService, cmdr, data);
            if (entry == null){
                LOG.warn("Not found inventory item {}, skip", data.getName());
            }
        }
    }

    protected InventoryEntry impInventoryEntry(GalaxyService galaxyService, Commander cmdr, InventoryEntryData data){
        InventoryEntry entry = null;
        Item item = findItem(galaxyService, data.getId(), data.getName());
        if (item == null){
            if (flags.contains(IMPORT_FLAG.ADD_INVENTORY_ITEMS) && data.getGroupName().isPresent()){
                LOG.debug("{} - is new inventory item, adding", data.getName());
                Group group = findGroup(galaxyService, data.getGroupName().get());
                if (group != null) {
                    item = galaxyService.getGalaxy().addItem(data.getName(), group);
                } else {
                    LOG.warn("Not found group {}, skip inventory item", data.getGroupName());
                }
            }
        }
        if (item != null){
            entry = cmdr.addItem(item, data.getCount());
        }
        return entry;
    }

    @Nullable
    private StarSystem findStarSystem(GalaxyService galaxyService, Optional<Long> id, String name){
        Optional<StarSystem> system;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id.isPresent()){
            system = galaxyService.findStarSystemByEID(id.get());
        } else {
            system = galaxyService.findStarSystemByName(name);
        }
        return system.orElse(null);
    }

    @Nullable
    private MinorFaction findFaction(GalaxyService galaxyService, Optional<Long> id, String name){
        Optional<MinorFaction> faction;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id.isPresent()){
            faction = galaxyService.findFactionByEID(id.get());
        } else {
            faction = galaxyService.findFactionByName(name);
        }
        return faction.orElse(null);
    }

    @Nullable
    private Station findStation(GalaxyService galaxyService, StarSystem system, Optional<Long> id, String name){
        Optional<Station> station;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id.isPresent()){
            station = galaxyService.findStationByEID(id.get());
        } else {
            station = galaxyService.findStationByName(system, name);
        }
        return station.orElse(null);
    }

    @Nullable
    private Item findItem(GalaxyService galaxyService, Optional<Long> id, String name){
        Optional<Item> item;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id.isPresent()){
            item = galaxyService.findItemByEID(id.get());
        } else {
            item = galaxyService.findItemByName(name);
        }
        return item.orElse(null);
    }

    @Nullable
    private Group findGroup(GalaxyService galaxyService, String name){
        Optional<Group> group;
        group = galaxyService.findGroupByName(name);
        return group.orElse(null);
    }

    @Nullable
    private Commander findCmdr(GalaxyService galaxyService, Optional<Long> id, String name){
        Optional<Commander> cmdr;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id.isPresent()){
            cmdr = galaxyService.findCmdrByEID(id.get());
        } else {
            cmdr = galaxyService.findCmdrByName(name);
        }
        return cmdr.orElse(null);
    }

    @Nullable
    private Body findBody(GalaxyService galaxyService, StarSystem system, Optional<Long> id, String name){
        Optional<Body> body;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id.isPresent()){
            body = galaxyService.findBodyByEID(id.get());
        } else {
            body = galaxyService.findBodyByName(system, name);
        }
        return body.orElse(null);
    }

    @Nullable
    private Ship findShip(GalaxyService galaxyService, Commander cmdr, Optional<Long> id, long sid){
        Optional<Ship> ship;
        if (flags.contains(IMPORT_FLAG.SEARCH_BY_ID) && id.isPresent()){
            ship = galaxyService.findShipByEID(id.get());
        } else {
            ship = galaxyService.findShipBySid(cmdr, sid);
        }
        return ship.orElse(null);
    }


}
