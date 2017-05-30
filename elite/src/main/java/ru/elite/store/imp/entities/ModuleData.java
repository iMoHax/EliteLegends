package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;

public interface ModuleData {

    @Nullable
    Long getId();
    String getName();
    @Nullable
    Long getGroupId();
    @Nullable
    String getGroupName();
    long getPrice();

}
