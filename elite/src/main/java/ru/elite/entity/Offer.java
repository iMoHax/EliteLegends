package ru.elite.entity;

import ru.elite.core.OFFER_TYPE;

import java.time.LocalDateTime;

public interface Offer {
    long getId();

    Item getItem();
    OFFER_TYPE getType();
    Station getStation();

    double getPrice();
    void setPrice(double price);

    long getCount();
    void setCount(long count);

    default boolean isIllegal(){
        Station station = getStation();
        Item item = getItem();
        return item.isIllegal(station);
    }

    LocalDateTime getModifiedTime();
    void setModifiedTime(LocalDateTime time);
}
