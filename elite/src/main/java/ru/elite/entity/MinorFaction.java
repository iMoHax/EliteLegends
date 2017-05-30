package ru.elite.entity;

import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;

public interface MinorFaction {
    long getId();

    String getName();
    void setName(String name);

    StarSystem getHomeSystem();
    void setHomeSystem(StarSystem starSystem);

    FACTION getFaction();
    void setFaction(FACTION faction);

    GOVERNMENT getGovernment();
    void setGovernment(GOVERNMENT government);

    boolean isPlayers();
    void setPlayers(boolean isPlayers);
}
