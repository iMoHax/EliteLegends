package ru.elite.store.imp.entities;

import java.util.Optional;

public interface ItemData {

    Optional<Long> getId();
    String getName();
    Optional<String> getGroupName();
    long getBuyOfferPrice();
    long getSellOfferPrice();
    long getSupply();
    long getDemand();

}
