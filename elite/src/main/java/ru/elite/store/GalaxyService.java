package ru.elite.store;

import ru.elite.core.*;
import ru.elite.entity.*;

import java.util.Collection;
import java.util.Optional;

public interface GalaxyService {

    void startTransaction();
    void commit();
    void rollback();

    Galaxy getGalaxy();

    Collection<Long> getAllStationIds(StarSystem system);

    Group createGroup(String name, GROUP_TYPE type);
    Optional<Group> findGroupById(long id);
    Optional<Group> findGroupByName(String name);

    Item createItem(String name, Group group);
    Optional<Item> findItemById(long id);
    Optional<Item> findItemByName(String name);

    MinorFaction createFaction(String name, FACTION faction, GOVERNMENT government);
    Optional<MinorFaction> findFactionById(long id);
    Optional<MinorFaction> findFactionByName(String name);

    MinorFactionState createFactionState(MinorFaction faction, STATE_TYPE state, float influence);
    Optional<MinorFactionState> findFactionStateById(long id);
    Optional<MinorFactionState> findFactionState(long systemId, long factionId);

    StarSystem createStarSystem(String name, double x, double y, double z);
    Optional<StarSystem> findStarSystemById(long id);
    Optional<StarSystem> findStarSystemByName(String name);

    Station createStation(String name, STATION_TYPE stationType, double distance);
    Optional<Station> findStationById(long id);
    Optional<Station> findStationByName(StarSystem system, String name);

    Offer createOffer(OFFER_TYPE type, Item item, double price, long count);
    Optional<Offer> findOfferById(long id);
    Optional<Offer> findOffer(long stationId, long itemId, OFFER_TYPE type);

    Group addGroup(String name, GROUP_TYPE type);
    boolean removeGroup(long groupId);

    Optional<Item> addItem(String name, long groupId);
    Item addItem(String name, Group group);
    boolean removeItem(long itemId);

    MinorFaction addFaction(String name, FACTION faction, GOVERNMENT government);
    boolean removeFaction(long factionId);

    Optional<MinorFactionState> addFactionToSystem(long systemId, long factionId, STATE_TYPE state, float influence);
    MinorFactionState addFactionToSystem(StarSystem system, MinorFaction faction, STATE_TYPE state, float influence);
    boolean removeFactionFromSystem(long systemId, long factionId);
    boolean removeFactionState(long factionStateId);

    Optional<Station> addStationToSystem(long systemId, String name, STATION_TYPE stationType, double distance);
    Station addStationToSystem(StarSystem system, String name, STATION_TYPE stationType, double distance);
    boolean removeStation(long stationId);

    Optional<Offer> addOfferToStation(long stationId, OFFER_TYPE type, long itemId, double price, long count);
    Offer addOfferToStation(Station station, OFFER_TYPE type, Item item, double price, long count);
    boolean removeOfferFromStation(long stationId, OFFER_TYPE type, long itemId);
    boolean removeOffer(long offerId);

}
