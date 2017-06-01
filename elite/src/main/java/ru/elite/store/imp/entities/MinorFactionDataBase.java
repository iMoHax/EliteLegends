package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.STATE_TYPE;

public abstract class MinorFactionDataBase implements MinorFactionData {
    @Nullable
    @Override
    public Long getId() {
        return null;
    }

    @Nullable
    @Override
    public STATE_TYPE getState() {
        return null;
    }

    @Override
    public float getInfluence() {
        return Float.NaN;
    }

    @Nullable
    @Override
    public String getHomeSystemName() {
        return null;
    }

    @Nullable
    @Override
    public Boolean isPlayers() {
        return null;
    }
}
