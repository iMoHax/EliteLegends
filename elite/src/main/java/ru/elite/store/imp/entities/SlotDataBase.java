package ru.elite.store.imp.entities;

import java.util.Optional;

public abstract class SlotDataBase implements SlotData {
    @Override
    public Optional<Boolean> isActive() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getPriority() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getModuleName() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getBlueprint() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getBlueprintLevel() {
        return Optional.empty();
    }

    @Override
    public Optional<Double> getHealth() {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getAmmoClip() {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getAmmoHopper() {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getCost() {
        return Optional.empty();
    }
}
