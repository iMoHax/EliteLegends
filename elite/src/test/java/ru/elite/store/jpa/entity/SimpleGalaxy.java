package ru.elite.store.jpa.entity;


import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;
import ru.elite.core.GROUP_TYPE;
import ru.elite.core.STATION_TYPE;
import ru.elite.entity.*;
import ru.elite.store.jpa.GalaxyStore;

import javax.persistence.*;

public class SimpleGalaxy {
    public final Group FOODS;
    public final Group MINERALS;
    public final Group METALS;
    public final Group SHIPS;
    public final Group FSD;
    public final Group SHIELD;
    public final Group MATERIAL;
    public final Group MANUFACTURED;
    public final Group DATA;

    public final Item ALGAE;
    public final Item COFFEE;
    public final Item INDITE;
    public final Item PAINITE;
    public final Item GOLD;
    public final Item SILVER;
    public final Item ANACONDA;
    public final Item TYPE9;
    public final Item FSD_E1;
    public final Item FSD_A5;
    public final Item BIWEAVE_SHIELD;
    public final Item SHIELD_B4;
    public final Item NICKEL;
    public final Item CHEMICAL_PROCESSORS;
    public final Item FSD_TELEMETRY;

    public final MinorFaction EGU;
    public final MinorFaction SIRIUS;
    public final MinorFaction EAST_INDIA;
    public final MinorFaction ALIOTH_IND;
    public final MinorFaction DIAMOND_FROGS;

    public final StarSystem EURYALE;
    public final StarSystem SOL;
    public final StarSystem ERANIN;

    public final Station EG_MAIN_HQ;

    public final Commander CMDR;
    public final Ship COURIER;

    public SimpleGalaxy(EntityManagerFactory emf) {
        EntityManager manager = emf.createEntityManager();
        try {
            GalaxyStore galaxy = new GalaxyStore(manager);
            EntityTransaction transaction = galaxy.startTransaction();

            FOODS = galaxy.addGroup("foods", GROUP_TYPE.MARKET);
            MINERALS = galaxy.addGroup("minerals", GROUP_TYPE.MARKET);
            METALS = galaxy.addGroup("metals", GROUP_TYPE.MARKET);
            SHIPS = galaxy.addGroup("ships", GROUP_TYPE.SHIP);
            FSD = galaxy.addGroup("fsd", GROUP_TYPE.OUTFIT);
            SHIELD = galaxy.addGroup("shield", GROUP_TYPE.OUTFIT);
            MATERIAL = galaxy.addGroup("material", GROUP_TYPE.MATERIAL);
            MANUFACTURED = galaxy.addGroup("manufactured", GROUP_TYPE.MATERIAL);
            DATA = galaxy.addGroup("data", GROUP_TYPE.DATA);

            ALGAE = galaxy.addItem("algae", FOODS);
            COFFEE = galaxy.addItem("coffee", FOODS);
            INDITE = galaxy.addItem("indite", MINERALS);
            PAINITE = galaxy.addItem("painite", MINERALS);
            GOLD = galaxy.addItem("gold", METALS);
            SILVER = galaxy.addItem("silver", METALS);
            ANACONDA = galaxy.addItem("anaconda", SHIPS);
            TYPE9 = galaxy.addItem("type9", SHIPS);
            FSD_E1 = galaxy.addItem("fsd E1", FSD);
            FSD_A5 = galaxy.addItem("fsd A5", FSD);
            BIWEAVE_SHIELD = galaxy.addItem("bi-weave shield", SHIELD);
            SHIELD_B4 = galaxy.addItem("shield B4", SHIELD);
            NICKEL = galaxy.addItem("nickel", MATERIAL);
            CHEMICAL_PROCESSORS = galaxy.addItem("chemicalprocessors", MANUFACTURED);
            FSD_TELEMETRY = galaxy.addItem("fsdtelemetry", DATA);

            EGU = galaxy.addFaction("EG Union", FACTION.INDEPENDENT, GOVERNMENT.DICTATORSHIP);
            SIRIUS = galaxy.addFaction("Sirius inc", FACTION.INDEPENDENT, GOVERNMENT.CORPORATE);
            EAST_INDIA = galaxy.addFaction("East India Company", FACTION.EMPIRE, GOVERNMENT.CORPORATE);
            ALIOTH_IND = galaxy.addFaction("Alioth Independents", FACTION.ALLIANCE, GOVERNMENT.DEMOCRACY);
            DIAMOND_FROGS = galaxy.addFaction("Diamond Frogs", FACTION.INDEPENDENT, GOVERNMENT.ANARCHY);

            EURYALE = galaxy.addStarSystem("Euryale", 35.375, -68.96875, 24.8125);
            SOL = galaxy.addStarSystem("Sol", 0, 0, 0);
            ERANIN = galaxy.addStarSystem("Eranin", -22.84375, 36.53125, -1.1875);

            EG_MAIN_HQ = EURYALE.addStation("EG Main HQ", STATION_TYPE.CORIOLIS_STARPORT, 795.7);

            CMDR = galaxy.addCmdr("MoHax");
            COURIER = CMDR.addShip(1, "Empire_Courier");


            transaction.commit();

        } finally {
            manager.close();
        }
    }


}
