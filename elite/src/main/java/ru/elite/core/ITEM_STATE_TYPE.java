package ru.elite.core;

public enum ITEM_STATE_TYPE {
    LEGAL, ILLEGAL;

    public boolean isLegal(){
        return this == LEGAL;
    }

    public boolean isIllegal(){
        return this == ILLEGAL;
    }
}
