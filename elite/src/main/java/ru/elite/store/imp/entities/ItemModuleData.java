package ru.elite.store.imp.entities;

import java.util.Optional;

public interface ItemModuleData {

    Optional<Long> getId();
    String getName();
    Optional<String> getGroupName();
    long getPrice();

}
