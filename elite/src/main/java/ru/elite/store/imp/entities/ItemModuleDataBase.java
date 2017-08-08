package ru.elite.store.imp.entities;

import java.util.Optional;

public abstract class ItemModuleDataBase implements ItemModuleData {
    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getGroupName() {
        return Optional.empty();
    }


}
