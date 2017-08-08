package ru.elite.store.imp.entities;

import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;
import ru.elite.core.STATE_TYPE;

import java.util.Optional;

public interface MinorFactionData {

    Optional<Long> getId();

    String getName();

    GOVERNMENT getGovernment();

    FACTION getFaction();

    Optional<STATE_TYPE> getState();

    Optional<Float> getInfluence();

    Optional<String> getHomeSystemName();

    Optional<Boolean> isPlayers();

}
