package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;

public interface ItemData {

    @Nullable
    Long getId();
    String getName();
    @Nullable
    Long getGroupId();
    @Nullable
    String getGroupName();
    long getBuyOfferPrice();
    long getSellOfferPrice();
    long getSupply();
    long getDemand();

}
