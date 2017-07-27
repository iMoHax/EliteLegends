package ru.elite.entity;

import org.jetbrains.annotations.NotNull;
import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;
import ru.elite.core.POWER;

import java.util.Collection;
import java.util.Objects;

public interface Item extends Comparable<Item> {
    Long getEID();
    void setEID(Long eid);

    String getName();

    Group getGroup();
    void setGroup(Group group);

    default boolean isIllegal(Station station){
        MinorFaction faction = station.getFaction();
        return isIllegal(station.getStarSystem(), faction.getFaction(), faction.getGovernment());
    }

    default boolean isLegal(FACTION faction){
        return getLegalFactions().contains(faction);
    }

    default boolean isIllegal(FACTION faction){
        return getIllegalFactions().contains(faction);
    }

    default boolean isLegal(GOVERNMENT government){
        return getLegalGovernments().contains(government);
    }

    default boolean isIllegal(GOVERNMENT government){
        return getIllegalGovernments().contains(government);
    }

    default boolean isIllegal(StarSystem starSystem, FACTION faction, GOVERNMENT government){
        if (starSystem != null){
            POWER power = starSystem.getPower();
            if (power != null){
                if (power.isLegal(faction, this, starSystem.getPowerState())) return false;
                if (power.isIllegal(faction, this, starSystem.getPowerState())) return true;
            }
        }
        if (faction != null && isIllegal(faction)) return false;
        if (government != null && isLegal(government)) return false;
        return faction != null && isIllegal(faction) ||
               government != null && isIllegal(government);
    }

    Collection<FACTION> getIllegalFactions();
    void setIllegal(FACTION faction, boolean illegal);
    Collection<GOVERNMENT> getIllegalGovernments();
    void setIllegal(GOVERNMENT government, boolean illegal);

    Collection<FACTION> getLegalFactions();
    void setLegal(FACTION faction, boolean legal);
    Collection<GOVERNMENT> getLegalGovernments();
    void setLegal(GOVERNMENT government, boolean legal);

    @Override
    default int compareTo(@NotNull Item other){
        Objects.requireNonNull(other, "Not compare with null");
        if (this == other) return 0;
        String name = getName();
        String otherName = other.getName();
        return name != null ? otherName != null ? name.compareTo(otherName) : -1 : 0;
    }

}
