package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.STATE_TYPE;

import java.util.Collection;
import java.util.Optional;

public abstract class MinorFactionDataBase implements MinorFactionData {
    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Override
    public Optional<STATE_TYPE> getState() {
        return Optional.empty();
    }

    @Override
    public Optional<Float> getInfluence() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getHomeSystemName() {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> isPlayers() {
        return Optional.empty();
    }

    @Nullable
    @Override
    public Collection<STATE_TYPE> getPendingStates() {
        return null;
    }

    @Nullable
    @Override
    public Collection<STATE_TYPE> getRecoveringStates() {
        return null;
    }
}
