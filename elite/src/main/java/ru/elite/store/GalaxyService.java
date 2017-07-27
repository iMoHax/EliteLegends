package ru.elite.store;

import ru.elite.core.*;
import ru.elite.entity.*;

import javax.persistence.EntityTransaction;
import java.util.Collection;
import java.util.Optional;

public interface GalaxyService {

    EntityTransaction startTransaction();
    void close();

    Galaxy getGalaxy();

    Optional<Commander> findCmdrByName(String name);
    Optional<Commander> findCmdrByEID(long eid);

    Collection<String> getAllStationNames(StarSystem system);
    Collection<String> getAllMinorFactionsNames(StarSystem system);

    Optional<Group> findGroupByName(String name);

    Optional<Item> findItemByName(String name);
    Optional<Item> findItemByEID(long eid);

    Optional<MinorFaction> findFactionByName(String name);
    Optional<MinorFaction> findFactionByEID(long eid);

    Optional<MinorFactionState> findFactionState(StarSystem system, MinorFaction faction);

    Optional<StarSystem> findStarSystemByName(String name);
    Optional<StarSystem> findStarSystemByEID(long eid);

    Optional<Station> findStationByName(StarSystem system, String name);
    Optional<Station> findStationByEID(long eid);

    Optional<Offer> findOffer(Station station, Item item, OFFER_TYPE type);

    boolean removeFactionFromSystemByName(StarSystem system, String factionName);

    boolean removeStationByName(StarSystem system, String stationName);

    boolean removeOffersFromStation(Station station, Item item);

}
