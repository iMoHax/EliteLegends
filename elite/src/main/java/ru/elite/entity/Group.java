package ru.elite.entity;

import ru.elite.core.GROUP_TYPE;

public interface Group {
    long getId();

    String getName();
    void setName(String name);

    GROUP_TYPE getType();
    void setType(GROUP_TYPE type);

    default boolean isMarket(){
        return GROUP_TYPE.MARKET.equals(getType());
    }

    default boolean isShip(){
        return GROUP_TYPE.SHIP.equals(getType());
    }

    default boolean isOutfit(){
        return GROUP_TYPE.OUTFIT.equals(getType());
    }

}
