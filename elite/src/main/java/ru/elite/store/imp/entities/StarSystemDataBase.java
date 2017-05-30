package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.POWER;
import ru.elite.core.POWER_STATE;
import ru.elite.core.SECURITY_LEVEL;

import java.util.Collection;

public abstract class StarSystemDataBase implements StarSystemData {
    @Override
    public Long getId() {
        return null;
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

    @Nullable
    @Override
    public Long getPopulation() {
        return null;
    }

    @Nullable
    @Override
    public MinorFactionData getFaction() {
        return null;
    }

    @Nullable
    @Override
    public SECURITY_LEVEL getSecurity() {
        return null;
    }

    @Nullable
    @Override
    public POWER getPower() {
        return null;
    }

    @Nullable
    @Override
    public POWER_STATE getPowerState() {
        return null;
    }

    @Nullable
    @Override
    public Long getIncome() {
        return null;
    }

    @Nullable
    @Override
    public Collection<StationData> getStations() {
        return null;
    }
}
