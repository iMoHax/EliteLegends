package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public abstract class StationDataBase implements StationData {
    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Override
    public Optional<Double> getDistance() {
        return Optional.empty();
    }

    @Override
    public Optional<STATION_TYPE> getType() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public MinorFactionData getFaction() {
        return null;
    }

    @Override
    public Optional<ECONOMIC_TYPE> getEconomic() {
        return Optional.empty();
    }

    @Override
    public Optional<ECONOMIC_TYPE> getSubEconomic() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public Collection<SERVICE_TYPE> getServices() {
        return null;
    }

    @Nullable
    @Override
    public Collection<ItemData> getCommodities() {
        return null;
    }

    @Nullable
    @Override
    public Collection<ItemModuleData> getModules() {
        return null;
    }

    @Nullable
    @Override
    public Collection<ItemShipData> getShips() {
        return null;
    }

    @Override
    public Optional<LocalDateTime> getModifiedTime() {
        return Optional.empty();
    }
}
