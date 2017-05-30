package ru.elite.entity;

import ru.elite.core.*;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StarSystem {
    long getId();

    String getName();
    void setName(String name);

    double getX();
    double getY();
    double getZ();
    void setPosition(double x, double y, double z);

    long getPopulation();
    void setPopulation(long population);

    Collection<MinorFactionState> getFactions();
    MinorFactionState add(MinorFactionState factionState);
    boolean remove(MinorFactionState factionState);

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
    Station add(Station station);
    boolean remove(Station station);

    default boolean isEmpty(){
        return get().isEmpty();
    }

    LocalDateTime getModifiedTime();
    void setModifiedTime(LocalDateTime time);

}
