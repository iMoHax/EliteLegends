package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface ShipData {
    Optional<Long> getId();

    long getSid();
    String getType();

    Optional<String> getName();
    Optional<String> getIdent();
    Optional<Double> getFuel();
    Optional<Double> getTank();

    @Nullable
    Collection<SlotData> getSlots();

}
