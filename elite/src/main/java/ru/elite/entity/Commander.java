package ru.elite.entity;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface Commander {
    Long getEID();
    void setEID(Long eid);

    String getName();
    void setName(String name);

    Ship getShip();
    void setShip(Ship ship);

    double getCredits();
    void setCredits(double credits);

    StarSystem getStarSystem();
    void setStarSystem(StarSystem starSystem);

    Station getStation();
    void setStation(Station station);

    boolean isLanded();
    void setLanded(boolean landed);

    Double getLatitude();
    void setLatitude(double latitude);

    Double getLongitude();
    void setLongitude(double longitude);

    int getRank(String type);
    void setRank(String type, int value);
    void resetRanks();

    boolean isDead();
    void setDead(boolean dead);

    Collection<InventoryEntry> getInventory();
    InventoryEntry addItem(Item item, long count);
    boolean removeItem(Item item);
    void clearItems();
    long getCount(Item item);

    Set<Ship> getShips();
    Ship addShip(long sid, String type);
    boolean removeShip(Ship ship);
    void clearShips();

    default Optional<Ship> getShip(long sid){
        return getShips().stream().filter(s -> s.getSid() == sid).findAny();
    }
}
