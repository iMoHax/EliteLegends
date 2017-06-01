package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.entity.MinorFaction;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StarSystemData {
    @Nullable
    Long getId();
    String getName();

    double getX();
    double getY();
    double getZ();

    Long getPopulation();

    @Nullable
    MinorFactionData getFaction();

    @Nullable
    SECURITY_LEVEL getSecurity();

    @Nullable
    POWER getPower();
    @Nullable
    POWER_STATE getPowerState();

    Long getIncome();

    @Nullable
    Collection<StationData> getStations();

    @Nullable
    Collection<MinorFactionData> getFactions();

    @Nullable
    LocalDateTime getModifiedTime();

}
