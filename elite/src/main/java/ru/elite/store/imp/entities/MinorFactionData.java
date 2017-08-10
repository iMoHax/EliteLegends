package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;
import ru.elite.core.STATE_TYPE;

import java.util.Collection;
import java.util.Optional;

public interface MinorFactionData {

    Optional<Long> getId();

    String getName();

    Optional<GOVERNMENT> getGovernment();

    Optional<FACTION> getFaction();

    Optional<STATE_TYPE> getState();

    Optional<Float> getInfluence();

    Optional<String> getHomeSystemName();

    Optional<Boolean> isPlayers();

    @Nullable
    Collection<STATE_TYPE> getPendingStates();

    @Nullable
    Collection<STATE_TYPE> getRecoveringStates();

}
