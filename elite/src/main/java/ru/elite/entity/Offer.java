package ru.elite.entity;

import org.jetbrains.annotations.NotNull;
import ru.elite.core.OFFER_TYPE;

import java.time.LocalDateTime;
import java.util.Objects;

public interface Offer extends Comparable<Offer> {

    Station getStation();
    void removeStation();

    Item getItem();
    OFFER_TYPE getType();

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



    @Override
    default int compareTo(@NotNull Offer other) {
        Objects.requireNonNull(other, "Not compare with null");
        if (this == other) return 0;
        int cmp = getType().compareTo(other.getType());
        if (cmp != 0) return cmp;
        cmp = Double.compare(getPrice(), other.getPrice());
        if (cmp != 0) return cmp;
        cmp = getStation().compareTo(other.getStation());
        if (cmp != 0) return cmp;
        return getItem().compareTo(other.getItem());
    }
}
