package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public abstract class ShipDataBase implements ShipData {
    @Override
    public Optional<String> getName() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getIdent() {
        return Optional.empty();
    }

    @Override
    public Optional<Double> getFuel() {
        return Optional.empty();
    }

    @Override
    public Optional<Double> getTank() {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public Collection<SlotData> getSlots() {
        return null;
    }
}
