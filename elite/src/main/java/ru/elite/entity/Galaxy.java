package ru.elite.entity;

import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;
import ru.elite.core.GROUP_TYPE;

import java.util.Collection;

public interface Galaxy {

    Collection<StarSystem> getStarSystems();
    StarSystem addStarSystem(String name, double x, double y, double z);
    boolean remove(StarSystem starSystem);
    void clearStarSystems();

    Collection<Group> getGroups();
    Group addGroup(String name, GROUP_TYPE type);
    boolean remove(Group group);
    void clearGroups();

    Collection<Item> getItems();
    Item addItem(String name, Group group);
    boolean remove(Item item);
    void clearItems();

    Collection<MinorFaction> getFactions();
    MinorFaction addFaction(String name, FACTION faction, GOVERNMENT government);
    boolean remove(MinorFaction faction);
    void clearFactions();

    Collection<Commander> getCmdrs();
    Commander addCmdr(String name);
    boolean remove(Commander cmdr);
    void clearCmdrs();

}
