package ru.elite.entity;

import ru.elite.core.*;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StarSystem {
    Long getEID();
    void setEID(Long eid);

    String getName();
    void setName(String name);

    double getX();
    double getY();
    double getZ();
    void setPosition(double x, double y, double z);

    long getPopulation();
    void setPopulation(long population);

    Collection<MinorFactionState> getFactions();
    MinorFactionState addFaction(MinorFaction faction, STATE_TYPE state, float influence);
    boolean removeFaction(MinorFactionState factionState);
    void clearFactions();

    MinorFaction getControllingFaction();
    void setControllingFaction(MinorFaction faction);

    SECURITY_LEVEL getSecurity();
    void setSecurity(SECURITY_LEVEL security);

    POWER_STATE getPowerState();
    void setPower(POWER power, POWER_STATE state);
    POWER getPower();

    long getIncome();
    void setIncome(long income);

    Collection<Station> get();
    Station addStation(String name, STATION_TYPE type, double distance);
    boolean removeStation(Station station);
    void clearStations();

    default boolean isEmpty(){
        return get().isEmpty();
    }

    LocalDateTime getModifiedTime();
    void setModifiedTime(LocalDateTime time);

}
