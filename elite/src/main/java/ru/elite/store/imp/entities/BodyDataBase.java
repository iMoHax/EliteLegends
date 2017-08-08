package ru.elite.store.imp.entities;

import java.util.Optional;

public abstract class BodyDataBase implements BodyData {
    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }
}
