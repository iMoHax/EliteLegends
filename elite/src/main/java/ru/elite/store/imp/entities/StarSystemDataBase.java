package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.POWER;
import ru.elite.core.POWER_STATE;
import ru.elite.core.SECURITY_LEVEL;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public abstract class StarSystemDataBase implements StarSystemData {
    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Override
    public double getX() {
        return Double.NaN;
    }

    @Override
    public double getY() {
        return Double.NaN;
    }

    @Override
    public double getZ() {
        return Double.NaN;
    }

    @Override
    public Optional<Long> getPopulation() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public MinorFactionData getFaction() {
        return null;
    }

    @Override
    public Optional<SECURITY_LEVEL> getSecurity() {
        return Optional.empty();
    }

    @Override
    public Optional<POWER> getPower() {
        return Optional.empty();
    }

    @Override
    public Optional<POWER_STATE> getPowerState() {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getIncome() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public Collection<StationData> getStations() {
        return null;
    }

    @Nullable
    @Override
    public Collection<MinorFactionData> getFactions() {
        return null;
    }

    @Nullable
    @Override
    public Collection<BodyData> getBodies() {
        return null;
    }

    @Override
    public Optional<LocalDateTime> getModifiedTime() {
        return Optional.empty();
    }
}
