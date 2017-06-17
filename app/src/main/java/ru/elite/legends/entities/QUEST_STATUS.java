package ru.elite.legends.entities;

public enum QUEST_STATUS {
    NONE, ACTIVE, FAILED, COMPLETE;

    public boolean isActive(){
        return this == ACTIVE;
    }

    public boolean isFailed(){
        return this == FAILED;
    }

    public boolean isComplete(){
        return this == COMPLETE;
    }
}
