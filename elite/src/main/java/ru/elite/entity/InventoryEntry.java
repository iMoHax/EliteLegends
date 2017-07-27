package ru.elite.entity;

public interface InventoryEntry {

    Commander getCmdr();
    void removeCmdr();
    Item getItem();

    long getCount();
    void setCount(long count);

}
