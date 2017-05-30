package ru.elite.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.core.*;
import ru.elite.entity.*;

import java.util.Optional;

public abstract class AbstractGalaxyService implements GalaxyService {
    private final static Logger LOG = LoggerFactory.getLogger(AbstractGalaxyService.class);

    @Override
    public Group addGroup(String name, GROUP_TYPE type) {
        Group group = createGroup(name, type);
        return getGalaxy().add(group);
    }

    @Override
    public boolean removeGroup(long groupId) {
        Optional<Group> g = findGroupById(groupId);
        if (g.isPresent()){
            return getGalaxy().remove(g.get());
        } else {
            LOG.warn("Group not found, id = {}", groupId);
        }
        return false;
    }

    @Override
    public Optional<Item> addItem(String name, long groupId) {
        Optional<Group> g = findGroupById(groupId);
        if (g.isPresent()){
            Item item = addItem(name, g.get());
            return Optional.of(item);
        } else {
            LOG.warn("Group not found, id = {}", groupId);
        }
        return Optional.empty();
    }

    @Override
    public Item addItem(String name, Group group) {
        Item item = createItem(name, group);
        return getGalaxy().add(item);
    }

    @Override
    public boolean removeItem(long itemId) {
        Optional<Item> i = findItemById(itemId);
        if (i.isPresent()){
            return getGalaxy().remove(i.get());
        } else {
            LOG.warn("Item not found, id = {}", itemId);
        }
        return false;
    }

    @Override
    public MinorFaction addFaction(String name, FACTION faction, GOVERNMENT government) {
        MinorFaction minorFaction = createFaction(name, faction, government);
        return getGalaxy().add(minorFaction);
    }

    @Override
    public boolean removeFaction(long factionId) {
        Optional<MinorFaction> f = findFactionById(factionId);
        if (f.isPresent()){
            return getGalaxy().remove(f.get());
        } else {
            LOG.warn("Minor faction not found, id = {}", factionId);
        }
        return false;
    }

    @Override
    public Optional<MinorFactionState> addFactionToSystem(long systemId, long factionId, STATE_TYPE state, float influence){
        Optional<StarSystem> s = findStarSystemById(systemId);
        Optional<MinorFaction> f = findFactionById(factionId);
        if (s.isPresent() && f.isPresent()){
            MinorFactionState fs = addFactionToSystem(s.get(), f.get(), state, influence);
            return Optional.of(fs);
        } else {
            if (!s.isPresent()){
                LOG.warn("System not found, id = {}", systemId);
            } else {
                LOG.warn("Faction not found, id = {}", factionId);
            }
        }
        return Optional.empty();
    }

    @Override
    public MinorFactionState addFactionToSystem(StarSystem system, MinorFaction faction, STATE_TYPE state, float influence) {
        MinorFactionState fs = createFactionState(faction, state, influence);
        return system.add(fs);
    }

    @Override
    public boolean removeFactionFromSystem(long systemId, long factionId) {
        Optional<MinorFactionState> fs = findFactionState(systemId, factionId);
        if (fs.isPresent()){
            return fs.get().getStarSystem().remove(fs.get());
        } else {
            LOG.warn("Faction state not found, starSystemId = {}, factionId = {}", systemId, factionId);
        }
        return false;
    }

    @Override
    public boolean removeFactionState(long factionStateId) {
        Optional<MinorFactionState> fs = findFactionStateById(factionStateId);
        if (fs.isPresent()){
            return fs.get().getStarSystem().remove(fs.get());
        } else {
            LOG.warn("Faction state not found, id = {}", factionStateId);
        }
        return false;
    }

    @Override
    public Optional<Station> addStationToSystem(long systemId, String name, STATION_TYPE stationType, double distance) {
        Optional<StarSystem> s = findStarSystemById(systemId);
        if (s.isPresent()){
            Station station = addStationToSystem(s.get(), name, stationType, distance);
            return Optional.of(station);
        } else {
            LOG.warn("System not found, id = {}", systemId);
        }
        return Optional.empty();
    }

    @Override
    public Station addStationToSystem(StarSystem system, String name, STATION_TYPE stationType, double distance) {
        Station station = createStation(name, stationType, distance);
        return system.add(station);
    }

    @Override
    public boolean removeStation(long stationId) {
        Optional<Station> s = findStationById(stationId);
        if (s.isPresent()){
            return s.get().getStarSystem().remove(s.get());
        } else {
            LOG.warn("Station not found, id = {}", stationId);
        }
        return false;
    }

    @Override
    public Optional<Offer> addOfferToStation(long stationId, OFFER_TYPE type, long itemId, double price, long count) {
        Optional<Station> s = findStationById(stationId);
        Optional<Item> i = findItemById(itemId);
        if (s.isPresent() && i.isPresent()){
            Offer offer = addOfferToStation(s.get(), type, i.get(), price, count);
            return Optional.of(offer);
        } else {
            if (!s.isPresent()){
                LOG.warn("Station not found, id = {}", stationId);
            } else {
                LOG.warn("Item not found, id = {}", itemId);
            }
        }
        return Optional.empty();
    }

    @Override
    public Offer addOfferToStation(Station station, OFFER_TYPE type, Item item, double price, long count) {
        Offer offer = createOffer(type, item, price, count);
        return station.add(offer);
    }

    @Override
    public boolean removeOfferFromStation(long stationId, OFFER_TYPE type, long itemId) {
        Optional<Offer> o = findOffer(stationId, itemId, type);
        if (o.isPresent()){
            return o.get().getStation().remove(o.get());
        } else {
            LOG.warn("Offer not found, stationId = {}, type = {}, itemId = {}", stationId, type, itemId);
        }
        return false;
    }

    @Override
    public boolean removeOffer(long offerId) {
        Optional<Offer> o = findOfferById(offerId);
        if (o.isPresent()){
            return o.get().getStation().remove(o.get());
        } else {
            LOG.warn("Offer not found, id = {}", offerId);
        }
        return false;
    }
}
