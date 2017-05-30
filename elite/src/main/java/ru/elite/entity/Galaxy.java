package ru.elite.entity;

import java.util.Collection;

public interface Galaxy {

    Collection<StarSystem> get();
    StarSystem add(StarSystem starSystem);
    boolean remove(StarSystem starSystem);
    void addAll(Collection<StarSystem> systems);
    void clearStarSystems();

    Collection<Group> getGroups();
    Group add(Group group);
    boolean remove(Group group);
    void addGroups(Collection<Group> groups);
    void clearGroups();

    Collection<Item> getItems();
    Item add(Item item);
    boolean remove(Item item);
    void addItems(Collection<Item> items);
    void clearItems();

    Collection<MinorFaction> getFactions();
    MinorFaction add(MinorFaction faction);
    boolean remove(MinorFaction faction);
    void addFactions(MinorFaction factions);
    void clearFactions();

}
