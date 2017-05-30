package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.entity.MinorFaction;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StationData {
    @Nullable
    Long getId();
    String getName();

    double getDistance();

    STATION_TYPE getType();
    MinorFactionData getFaction();

    @Nullable
    ECONOMIC_TYPE getEconomic();
    @Nullable
    ECONOMIC_TYPE getSubEconomic();
    @Nullable
    Collection<SERVICE_TYPE> getServices();

    @Nullable
    Collection<ItemData> getCommodities();
    @Nullable
    Collection<ModuleData> getModules();
    @Nullable
    Collection<ShipData> getShips();

    @Nullable
    LocalDateTime getModifiedTime();

}
