package ru.elite.store.imp.entities;


import java.util.Optional;

public abstract class InventoryEntryDataBase implements InventoryEntryData {
    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getGroupName() {
        return Optional.empty();
    }
}
