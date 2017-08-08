package ru.elite.store.imp.entities;

import java.util.Optional;

public abstract class ItemShipDataBase implements ItemShipData {

    @Override
    public Optional<Long> getId() {
        return Optional.empty();

    }

    @Override
    public Optional<Long> getPrice() {
        return Optional.empty();
    }
}
