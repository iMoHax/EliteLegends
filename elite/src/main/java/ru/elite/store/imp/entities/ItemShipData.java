package ru.elite.store.imp.entities;

import java.util.Optional;

public interface ItemShipData {

    Optional<Long> getId();
    String getName();
    Optional<Long> getPrice();

}
