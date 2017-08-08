package ru.elite.core;

public enum BODY_TYPE {
    BARYCENTRE, STAR, PLANET, PLANETARY_RING, STELLAR_RING, STATION, ASTEROID_CLUSTER;

    public boolean isStation(){
        return this == STATION;
    }
}
