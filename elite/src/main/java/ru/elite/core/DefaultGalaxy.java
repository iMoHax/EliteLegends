package ru.elite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.Galaxy;
import ru.elite.store.GalaxyService;

import javax.persistence.EntityTransaction;

public class DefaultGalaxy {
    private final static Logger LOG = LoggerFactory.getLogger(DefaultGalaxy.class);

    public static final String GROUP_CHEMICALS = "chemicals";
    public static final String GROUP_CONSUMER_ITEMS = "consumer_items";
    public static final String GROUP_FOODS = "foods";
    public static final String GROUP_INDUSTRIAL_MATERIALS = "engineered_ceramics";
    public static final String GROUP_DRUGS = "drugs";
    public static final String GROUP_MACHINERY = "machinery";
    public static final String GROUP_MEDICINES = "medicines";
    public static final String GROUP_METALS = "metals";
    public static final String GROUP_MINERALS = "minerals";
    public static final String GROUP_SALVAGE = "salvage";
    public static final String GROUP_SLAVERY = "slaves";
    public static final String GROUP_TECHNOLOGY = "technology";
    public static final String GROUP_TEXTILES = "textiles";
    public static final String GROUP_WASTE = "waste";
    public static final String GROUP_WEAPONS = "weapons";
    public static final String GROUP_SHIPS = "ships";
    public static final String GROUP_MATERIALS = "materials";
    public static final String GROUP_MANUFACTURED = "manufactured";
    public static final String GROUP_DATAS = "datas";
    public static final String DEFAULT_MARKET_GROUP = GROUP_SALVAGE;

    public static void init(GalaxyService galaxyService){
        EntityTransaction transaction = galaxyService.startTransaction();
        try {
            initGroups(galaxyService.getGalaxy());
            transaction.commit();
        } catch (Exception e){
            LOG.error("Error on init default galaxy: {}", e);
            transaction.rollback();
        }
    }

    public static void initGroups(Galaxy galaxy){
        galaxy.addGroup(GROUP_CHEMICALS, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_CONSUMER_ITEMS, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_FOODS, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_INDUSTRIAL_MATERIALS, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_DRUGS, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_MACHINERY, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_MEDICINES, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_METALS, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_MINERALS, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_SALVAGE, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_SLAVERY, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_TECHNOLOGY, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_TEXTILES, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_WASTE, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_WEAPONS, GROUP_TYPE.MARKET);
        galaxy.addGroup(GROUP_SHIPS, GROUP_TYPE.SHIP);
        galaxy.addGroup(GROUP_MATERIALS, GROUP_TYPE.MATERIAL);
        galaxy.addGroup(GROUP_MANUFACTURED, GROUP_TYPE.MATERIAL);
        galaxy.addGroup(GROUP_DATAS, GROUP_TYPE.DATA);

    }

}
