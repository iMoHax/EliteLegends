package ru.elite.entity;

import ru.elite.core.STATE_STATUS;
import ru.elite.core.STATE_TYPE;

import java.util.stream.Stream;

public interface MinorFactionState {

    StarSystem getStarSystem();
    void removeStarSystem();

    MinorFaction getFaction();

    float getInfluence();
    void setInfluence(float influence);

    STATE_TYPE getState();
    void setState(STATE_TYPE state);

    Stream<STATE_TYPE> getStates(STATE_STATUS status);
    void addState(STATE_TYPE state, STATE_STATUS status);
    void setStateStatus(STATE_TYPE state, STATE_STATUS status);
    boolean removeState(STATE_TYPE state);


}
