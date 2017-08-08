package ru.elite.store.imp;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum IMPORT_FLAG {
    STARSYSTEMS, STATIONS, ITEMS,
    ADD_STARSYSTEMS,
    REMOVE_STATIONS, ADD_STATIONS,
    REMOVE_COMMODITY, ADD_COMMODITY,
    REMOVE_ITEM_MODULE, ADD_ITEM_MODULE,
    REMOVE_ITEM_SHIP, ADD_ITEM_SHIP,
    REMOVE_SLOTS, ADD_SLOTS,
    REMOVE_SHIPS, ADD_SHIPS,
    REMOVE_BODIES, ADD_BODIES,
    REMOVE_INVENTORY_ITEMS, ADD_INVENTORY_ITEMS,
    SEARCH_BY_ID;

    public static final Set<IMPORT_FLAG> UPDATE_ONLY = Collections.unmodifiableSet(EnumSet.of(STARSYSTEMS, STATIONS, ITEMS));
    public static final Set<IMPORT_FLAG> ADD_AND_UPDATE = Collections.unmodifiableSet(EnumSet.of(STARSYSTEMS, STATIONS, ITEMS, ADD_STARSYSTEMS, ADD_STATIONS, ADD_COMMODITY, ADD_ITEM_SHIP, ADD_ITEM_MODULE, ADD_SLOTS, ADD_SHIPS, ADD_BODIES, ADD_INVENTORY_ITEMS));
    public static final Set<IMPORT_FLAG> ADD_AND_REMOVE = Collections.unmodifiableSet(EnumSet.of(ADD_STARSYSTEMS, ADD_STATIONS, ADD_COMMODITY, ADD_ITEM_SHIP, ADD_ITEM_MODULE, ADD_SLOTS, ADD_SHIPS, ADD_BODIES, ADD_INVENTORY_ITEMS,
                                                                                                 REMOVE_STATIONS, REMOVE_COMMODITY, REMOVE_ITEM_MODULE, REMOVE_ITEM_SHIP, REMOVE_SLOTS, REMOVE_SHIPS, REMOVE_BODIES, REMOVE_INVENTORY_ITEMS));
    public static final Set<IMPORT_FLAG> UPDATE_MARKET = Collections.unmodifiableSet(EnumSet.of(ITEMS, ADD_STARSYSTEMS, ADD_STATIONS, ADD_COMMODITY, ADD_ITEM_SHIP, ADD_ITEM_MODULE));


}
