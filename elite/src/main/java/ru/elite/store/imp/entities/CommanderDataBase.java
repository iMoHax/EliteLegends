package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class CommanderDataBase implements CommanderData {
    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public ShipData getShip() {
        return null;
    }

    @Override
    public Optional<Double> getCredits() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public StarSystemData getStarSystem() {
        return null;
    }

    @Nullable
    @Override
    public StationData getStation() {
        return null;
    }

    @Nullable
    @Override
    public BodyData getBody() {
        return null;
    }

    @Override
    public Optional<Boolean> isLanded() {
        return Optional.empty();
    }

    @Override
    public Optional<Double> getLatitude() {
        return Optional.empty();
    }

    @Override
    public Optional<Double> getLongitude() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public Map<String, Integer> getRanks() {
        return null;
    }

    @Override
    public Optional<Boolean> isDead() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public Collection<ShipData> getShips() {
        return null;
    }

    @Nullable
    @Override
    public Collection<InventoryEntryData> getInventory() {
        return null;
    }

}
