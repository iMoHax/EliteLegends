package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;
import ru.elite.entity.MinorFaction;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface StarSystemData {
    Optional<Long> getId();
    String getName();

    double getX();
    double getY();
    double getZ();

    Optional<Long> getPopulation();

    @Nullable
    MinorFactionData getFaction();

    Optional<SECURITY_LEVEL> getSecurity();

    Optional<POWER> getPower();
    Optional<POWER_STATE> getPowerState();

    Optional<Long> getIncome();

    @Nullable
    Collection<StationData> getStations();

    @Nullable
    Collection<MinorFactionData> getFactions();

    @Nullable
    Collection<BodyData> getBodies();

    Optional<LocalDateTime> getModifiedTime();

}
