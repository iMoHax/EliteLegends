package ru.elite.store.imp.entities;

import java.util.Optional;

public interface InventoryEntryData {

    Optional<Long> getId();
    String getName();

    Optional<String> getGroupName();
    long getCount();


}
