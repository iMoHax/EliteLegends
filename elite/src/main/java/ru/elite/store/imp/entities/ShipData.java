package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;

public interface ShipData {

    @Nullable
    Long getId();
    String getName();
    @Nullable
    Long getPrice();

}
