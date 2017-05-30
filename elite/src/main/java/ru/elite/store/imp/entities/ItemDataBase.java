package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;

public abstract class ItemDataBase implements ItemData {
    @Override
    public Long getId() {
        return null;
    }

    @Nullable
    @Override
    public Long getGroupId() {
        return null;
    }

    @Nullable
    @Override
    public String getGroupName() {
        return null;
    }

}
