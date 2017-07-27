package ru.elite.entity;

import java.util.Collection;
import java.util.Optional;

public interface Ship {
    Long getEID();
    void setEID(Long eid);

    Commander getCmdr();
    void removeCmdr();

    long getSid();
    String getType();

    String getName();
    void setName(String name);

    String getIdent();
    void setIdent(String ident);

    double getFuel();
    void setFuel(double fuel);

    double getTank();
    void setTank(double tank);

    Collection<Slot> getSlots();
    Slot addSlot(String name);
    boolean removeSlot(Slot slot);
    void clearSlots();

    default Optional<Slot> getSlot(String name){
        return getSlots().stream().filter(s -> s.getName().equals(name)).findAny();
    }

    default boolean hasModule(String name){
        return getSlots().stream().filter(s -> s.isModule(name)).findAny().isPresent();
    }
}
