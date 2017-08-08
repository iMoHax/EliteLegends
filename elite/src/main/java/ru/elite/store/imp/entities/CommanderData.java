package ru.elite.store.imp.entities;


import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface CommanderData {
    Optional<Long> getId();
    String getName();
    @Nullable
    ShipData getShip();
    Optional<Double> getCredits();
    @Nullable
    StarSystemData getStarSystem();
    @Nullable
    StationData getStation();
    @Nullable
    BodyData getBody();
    Optional<Boolean> isLanded();
    Optional<Double> getLatitude();
    Optional<Double> getLongitude();
    @Nullable
    Map<String, Integer> getRanks();
    Optional<Boolean> isDead();
    @Nullable
    Collection<InventoryEntryData> getInventory();
    @Nullable
    Collection<ShipData> getShips();

}
