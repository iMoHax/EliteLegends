package ru.elite.store.imp.entities;

import java.util.Optional;

public abstract class ItemDataBase implements ItemData {
    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getGroupName() {
        return Optional.empty();
    }

}
