package ru.elite.store.imp.entities;

import org.jetbrains.annotations.Nullable;
import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;
import ru.elite.core.STATE_TYPE;

public interface MinorFactionData {

    @Nullable
    Long getId();

    String getName();

    GOVERNMENT getGovernment();

    FACTION getFaction();

    @Nullable
    STATE_TYPE getState();

    @Nullable
    Float getInfluence();

    @Nullable
    String getHomeSystemName();

    @Nullable
    Boolean isPlayers();

}
