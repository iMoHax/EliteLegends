package ru.elite.store.jpa;

import ru.elite.core.*;
import ru.elite.entity.*;
import ru.elite.store.GalaxyService;
import ru.elite.store.jpa.entity.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Optional;

public class GalaxyStore implements GalaxyService, Galaxy {
    private final EntityManager manager;

    public GalaxyStore(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public EntityTransaction startTransaction() {
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        return transaction;
    }

    public void flush() {
        manager.flush();
    }

    @Override
    public void close() {
        manager.close();
    }

    @Override
    public Galaxy getGalaxy() {
        return this;
    }

    @Override
    public Collection<StarSystem> getStarSystems() {
        TypedQuery<StarSystem> query = manager.createNamedQuery("StarSystem.findAll", StarSystem.class);
        return query.getResultList();
    }

    @Override
    public StarSystem addStarSystem(String name, double x, double y, double z) {
        StarSystem starSystem = new StarSystemImpl(name, x, y, z);
        manager.persist(starSystem);
        return starSystem;
    }

    @Override
    public boolean remove(StarSystem starSystem) {
        manager.remove(starSystem);
        return true;
    }

    @Override
    public void clearStarSystems() {
        Query query = manager.createNamedQuery("StarSystem.deleteAll");
        query.executeUpdate();
    }

    @Override
    public Collection<Group> getGroups() {
        TypedQuery<Group> query = manager.createNamedQuery("Group.findAll", Group.class);
        return query.getResultList();
    }

    @Override
    public Group addGroup(String name, GROUP_TYPE type) {
        Group group = new GroupImpl(name, type);
        manager.persist(group);
        return group;
    }

    @Override
    public boolean remove(Group group) {
        manager.remove(group);
        return true;
    }

    @Override
    public void clearGroups() {
        Query query = manager.createNamedQuery("Group.deleteAll");
        query.executeUpdate();
    }

    @Override
    public Collection<Item> getItems() {
        TypedQuery<Item> query = manager.createNamedQuery("Item.findAll", Item.class);
        return query.getResultList();
    }

    @Override
    public Item addItem(String name, Group group) {
        Item item = new ItemImpl(name, group);
        manager.persist(item);
        return item;
    }

    @Override
    public boolean remove(Item item) {
        manager.remove(item);
        return true;
    }

    @Override
    public void clearItems() {
        Query query = manager.createNamedQuery("Item.deleteAll");
        query.executeUpdate();
    }

    @Override
    public Collection<MinorFaction> getFactions() {
        TypedQuery<MinorFaction> query = manager.createNamedQuery("MinorFaction.findAll", MinorFaction.class);
        return query.getResultList();
    }

    @Override
    public MinorFaction addFaction(String name, FACTION faction, GOVERNMENT government) {
        MinorFaction minorFaction = new MinorFactionImpl(name, faction, government);
        manager.persist(minorFaction);
        return minorFaction;
    }

    @Override
    public boolean remove(MinorFaction faction) {
        manager.remove(faction);
        return true;
    }

    @Override
    public void clearFactions() {
        Query query = manager.createNamedQuery("MinorFaction.deleteAll");
        query.executeUpdate();
    }

    @Override
    public Collection<Commander> getCmdrs() {
        TypedQuery<Commander> query = manager.createNamedQuery("Commander.findAll", Commander.class);
        return query.getResultList();
    }

    @Override
    public Commander addCmdr(String name) {
        Commander cmdr = new CommanderImpl(name);
        manager.persist(cmdr);
        return cmdr;
    }

    @Override
    public boolean remove(Commander cmdr) {
        manager.remove(cmdr);
        return true;
    }

    @Override
    public void clearCmdrs() {
        Query query = manager.createNamedQuery("Commander.deleteAll");
        query.executeUpdate();
    }

    @Override
    public Collection<String> getAllStationNames(StarSystem system) {
        TypedQuery<String> query = manager.createNamedQuery("Station.getNamesInSystem", String.class);
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        return query.getResultList();
    }

    @Override
    public Collection<String> getAllBodyNames(StarSystem system) {
        TypedQuery<String> query = manager.createNamedQuery("Body.getNamesInSystem", String.class);
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        return query.getResultList();
    }

    @Override
    public Collection<String> getAllMinorFactionsNames(StarSystem system) {
        TypedQuery<String> query = manager.createNamedQuery("MinorFactionState.getNamesInSystem", String.class);
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        return query.getResultList();
    }

    @Override
    public Collection<Long> getAllShipSids(Commander cmdr) {
        TypedQuery<Long> query = manager.createNamedQuery("Ship.getSIDsInCmdr", Long.class);
        query.setParameter("cmdrId", ((CommanderImpl)cmdr).getId());
        return query.getResultList();
    }

    @Override
    public Collection<String> getAllSlotNames(Ship ship) {
        TypedQuery<String> query = manager.createNamedQuery("Slot.getNamesInShip", String.class);
        query.setParameter("shipId", ((ShipImpl)ship).getId());
        return query.getResultList();
    }

    @Override
    public Optional<Commander> findCmdrByName(String name) {
        TypedQuery<Commander> query = manager.createNamedQuery("Commander.findByName", Commander.class);
        query.setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Commander> findCmdrByEID(long eid) {
        TypedQuery<Commander> query = manager.createNamedQuery("Commander.findByEID", Commander.class);
        query.setParameter("eid", eid);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Group> findGroupByName(String name) {
        TypedQuery<Group> query = manager.createNamedQuery("Group.findByName", Group.class);
        query.setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Item> findItemByName(String name) {
        TypedQuery<Item> query = manager.createNamedQuery("Item.findByName", Item.class);
        query.setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Item> findItemByEID(long eid) {
        TypedQuery<Item> query = manager.createNamedQuery("Item.findByEID", Item.class);
        query.setParameter("eid", eid);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<MinorFaction> findFactionByName(String name) {
        TypedQuery<MinorFaction> query = manager.createNamedQuery("MinorFaction.findByName", MinorFaction.class);
        query.setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<MinorFaction> findFactionByEID(long eid) {
        TypedQuery<MinorFaction> query = manager.createNamedQuery("MinorFaction.findByEID", MinorFaction.class);
        query.setParameter("eid", eid);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<MinorFactionState> findFactionState(StarSystem system, MinorFaction faction) {
        TypedQuery<MinorFactionState> query = manager.createNamedQuery("MinorFactionState.findInSystemByFaction", MinorFactionState.class);
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        query.setParameter("factionId", ((MinorFactionImpl)faction).getId());
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<StarSystem> findStarSystemByName(String name) {
        TypedQuery<StarSystem> query = manager.createNamedQuery("StarSystem.findByName", StarSystem.class);
        query.setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<StarSystem> findStarSystemByEID(long eid) {
        TypedQuery<StarSystem> query = manager.createNamedQuery("StarSystem.findByEID", StarSystem.class);
        query.setParameter("eid", eid);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Station> findStationByName(StarSystem system, String name) {
        TypedQuery<Station> query = manager.createNamedQuery("Station.findInSystemByName", Station.class);
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        query.setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Station> findStationByEID(long eid) {
        TypedQuery<Station> query = manager.createNamedQuery("Station.findByEID", Station.class);
        query.setParameter("eid", eid);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Body> findBodyByName(StarSystem system, String name) {
        TypedQuery<Body> query = manager.createNamedQuery("Body.findInSystemByName", Body.class);
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        query.setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Body> findBodyByEID(long eid) {
        TypedQuery<Body> query = manager.createNamedQuery("Body.findByEID", Body.class);
        query.setParameter("eid", eid);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Offer> findOffer(Station station, Item item, OFFER_TYPE type) {
        TypedQuery<Offer> query = manager.createNamedQuery("Offer.findInStationByItem", Offer.class);
        query.setParameter("stationId", ((StationImpl)station).getId());
        query.setParameter("itemId", ((ItemImpl)item).getId());
        query.setParameter("offerType", type);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Ship> findShipBySid(Commander cmdr, long sid) {
        TypedQuery<Ship> query = manager.createNamedQuery("Ship.findInCmdrBySID", Ship.class);
        query.setParameter("cmdrId", ((CommanderImpl)cmdr).getId());
        query.setParameter("sid", sid);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Ship> findShipByEID(long eid) {
        TypedQuery<Ship> query = manager.createNamedQuery("Ship.findByEID", Ship.class);
        query.setParameter("eid", eid);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex){
            return Optional.empty();
        }
    }

    @Override
    public boolean removeFactionFromSystemByName(StarSystem system, String factionName) {
        Query query = manager.createNamedQuery("MinorFactionState.deleteFromSystemByName");
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        query.setParameter("name", factionName);
        query.executeUpdate();
        return true;
    }

    @Override
    public boolean removeStationByName(StarSystem system, String stationName) {
        Query query = manager.createNamedQuery("Station.deleteFromSystemByName");
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        query.setParameter("name", stationName);
        query.executeUpdate();
        return true;
    }

    @Override
    public boolean removeBodyByName(StarSystem system, String bodyName) {
        Query query = manager.createNamedQuery("Body.deleteFromSystemByName");
        query.setParameter("starSystemId", ((StarSystemImpl)system).getId());
        query.setParameter("name", bodyName);
        query.executeUpdate();
        return true;
    }

    @Override
    public boolean removeOffersFromStation(Station station, Item item) {
        Query query = manager.createNamedQuery("Offer.deleteAllFromStationByItem");
        query.setParameter("stationId", ((StationImpl)station).getId());
        query.setParameter("itemId", ((ItemImpl)item).getId());
        query.executeUpdate();
        return true;
    }

    @Override
    public boolean removeShipBySid(Commander cmdr, long sid) {
        Query query = manager.createNamedQuery("Ship.deleteFromCmdrBySID");
        query.setParameter("cmdrId", ((CommanderImpl)cmdr).getId());
        query.setParameter("sid", sid);
        query.executeUpdate();
        return true;
    }

    @Override
    public boolean removeSlotByName(Ship ship, String slotName) {
        Query query = manager.createNamedQuery("Slot.deleteFromShipByName");
        query.setParameter("shipId", ((ShipImpl)ship).getId());
        query.setParameter("name", slotName);
        query.executeUpdate();
        return true;
    }
}
