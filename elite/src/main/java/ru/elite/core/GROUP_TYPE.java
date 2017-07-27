package ru.elite.core;

public enum GROUP_TYPE {
    MARKET, SHIP, OUTFIT, MATERIAL, DATA;

    public boolean isMarket(){
        return this == MARKET;
    }

    public boolean isShip(){
        return this == SHIP;
    }

    public boolean isOutfit(){
        return this == OUTFIT;
    }

    public boolean isMaterial(){
        return this == MATERIAL || this == DATA;
    }

    public boolean isData(){
        return this == DATA;
    }

}

