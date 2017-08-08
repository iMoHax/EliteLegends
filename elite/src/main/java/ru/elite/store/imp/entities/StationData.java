package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface StationData {
    Optional<Long> getId();
    String getName();

    Optional<Double> getDistance();

    Optional<STATION_TYPE> getType();
    MinorFactionData getFaction();

    Optional<ECONOMIC_TYPE> getEconomic();
    Optional<ECONOMIC_TYPE> getSubEconomic();
    @Nullable
    Collection<SERVICE_TYPE> getServices();

    @Nullable
    Collection<ItemData> getCommodities();
    @Nullable
    Collection<ItemModuleData> getModules();
    @Nullable
    Collection<ItemShipData> getShips();

    Optional<LocalDateTime> getModifiedTime();

}
