package ru.elite.utils.edlog;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.core.*;

public class EDConverter {
    private final static Logger LOG = LoggerFactory.getLogger(EDConverter.class);


    @Nullable
    public static STATION_TYPE asStationType(String type){
        if (type == null) return null;
        switch (type){
//            case "": return STATION_TYPE.STARPORT;
            case "Coriolis": return STATION_TYPE.CORIOLIS_STARPORT;
            case "Bernal": return STATION_TYPE.OCELLUS_STARPORT;
            case "Orbis": return STATION_TYPE.ORBIS_STARPORT;
            case "Outpost": return STATION_TYPE.OUTPOST;
/*            case "": return STATION_TYPE.CIVILIAN_OUTPOST;
            case "": return STATION_TYPE.COMMERCIAL_OUTPOST;
            case "": return STATION_TYPE.INDUSTRIAL_OUTPOST;
            case "": return STATION_TYPE.MILITARY_OUTPOST;
            case "": return STATION_TYPE.MINING_OUTPOST;
            case "": return STATION_TYPE.SCIENTIFIC_OUTPOST;
            case "": return STATION_TYPE.UNSANCTIONED_OUTPOST;
            case "": return STATION_TYPE.PLANETARY_PORT;
            case "": return STATION_TYPE.PLANETARY_OUTPOST;*/

        }
        LOG.warn("Unknown station type: {}", type);
        return null;
    }

    @Nullable
    public static GOVERNMENT asGovernment(String government){
        if (government == null) return null;
        switch (government){
            case "$government_Anarchy;": return GOVERNMENT.ANARCHY;
            case "$government_Colony;": return GOVERNMENT.COLONY;
            case "$government_Communism;": return GOVERNMENT.COMMUNISM;
            case "$government_Confederacy;": return GOVERNMENT.CONFEDERACY;
            case "$government_Cooperative;": return GOVERNMENT.COOPERATIVE;
            case "$government_Corporate;": return GOVERNMENT.CORPORATE;
            case "$government_Democracy;": return GOVERNMENT.DEMOCRACY;
            case "$government_Dictatorship;": return GOVERNMENT.DICTATORSHIP;
            case "$government_Feudal;": return GOVERNMENT.FEUDAL;
            case "$government_Imperial;": return GOVERNMENT.IMPERIAL;
            case "$government_Patronage;": return GOVERNMENT.PATRONAGE;
            case "$government_PrisonColony;": return GOVERNMENT.PRISON_COLONY;
            case "$government_Theocracy;": return GOVERNMENT.THEOCRACY;
            case "$government_engineer;": return GOVERNMENT.ENGINEER;
            case "$government_Workshop;": return GOVERNMENT.WORKSHOP;
            case "$government_None;": return GOVERNMENT.NONE;
        }
        LOG.warn("Unknown government type: {}", government);
        return null;
    }

    @Nullable
    public static FACTION asAllegiance(String allegiance){
        if (allegiance == null) return null;
        switch (allegiance){
            case "$faction_Federation;": return FACTION.FEDERATION;
            case "$faction_Empire;": return FACTION.EMPIRE;
            case "$faction_Alliance;": return FACTION.ALLIANCE;
            case "$faction_Independent;": return FACTION.INDEPENDENT;
            case "$faction_Pirate;": return FACTION.PIRATE;
            case "$faction_none;": return FACTION.NONE;
            case "Federation": return FACTION.FEDERATION;
            case "Empire": return FACTION.EMPIRE;
            case "Alliance": return FACTION.ALLIANCE;
            case "Independent": return FACTION.INDEPENDENT;
            case "Pirate": return FACTION.PIRATE;
            case "": return FACTION.NONE;
        }
        
        LOG.warn("Unknown allegiance type: {}", allegiance);
        return null;
    }

    @Nullable
    public static ECONOMIC_TYPE asEconomic(String economic){
        if (economic == null) return null;
        switch (economic){
            case "$economy_Agri;": return ECONOMIC_TYPE.AGRICULTURE;
            case "$economy_Extraction;": return ECONOMIC_TYPE.EXTRACTION;
            case "$economy_HighTech;": return ECONOMIC_TYPE.HIGH_TECH;
            case "$economy_Industrial;": return ECONOMIC_TYPE.INDUSTRIAL;
            case "$economy_Military;": return ECONOMIC_TYPE.MILITARY;
            case "$economy_Refinery;": return ECONOMIC_TYPE.REFINERY;
            case "$economy_Service;": return ECONOMIC_TYPE.SERVICE;
            case "$economy_Terraforming;": return ECONOMIC_TYPE.TERRAFORMING;
            case "$economy_Tourism;": return ECONOMIC_TYPE.TOURISM;
            case "$economy_Colony;": return ECONOMIC_TYPE.COLONY;
            case "$economy_None;": return ECONOMIC_TYPE.NONE;
        }
        
        LOG.warn("Unknown economic type: {}", economic);
        return null;
    }


    @Nullable
    public static POWER_STATE asPowerState(String state) {
        if (state == null) return null;
        switch (state) {
            case "Controlled": return POWER_STATE.CONTROL;
            case "Exploited": return POWER_STATE.EXPLOITED;
            case "Prepared": return POWER_STATE.EXPANSION;
            case "Contested": return POWER_STATE.CONTESTED;
            case "HomeSystem": return POWER_STATE.HEADQUARTERS;
            case "InPrepareRadius": return POWER_STATE.BLOCKED;
            case "Turmoil": return POWER_STATE.TURMOIL;
            case "": return POWER_STATE.NONE;
        }

        LOG.warn("Unknown power state: {}", state);
        return null;
    }

    @Nullable
    public static POWER asPower(String power) {
        if (power == null) return null;
        switch (power) {
            case "Aisling Duval": return POWER.DUVAL;
            case "Archon Delaine": return POWER.DELAINE;
            case "Arissa Lavigny-Duval": return POWER.LAVIGNY_DUVAL;
            case "Denton Patreus": return POWER.PATREUS;
            case "Edmund Mahon": return POWER.MAHON;
            case "Felicia Winters": return POWER.WINTERS;
            case "Li Yong-Rui": return POWER.YONG_RUI;
            case "Pranav Antal": return POWER.ANTAL;
            case "Zachary Hudson": return POWER.HUDSON;
            case "Zemina Torval": return POWER.TORVAL;
            case "Yuri Grom": return POWER.GROM;
            case "": return POWER.NONE;
        }

        LOG.warn("Unknown power: {}", power);
        return null;
    }

    @Nullable
    public static STATE_TYPE asFactionState(String state) {
        if (state == null) return null;
        switch (state) {
            case "Expansion": return STATE_TYPE.EXPANSION;
            case "War": return STATE_TYPE.WAR;
            case "Civil War": return STATE_TYPE.CIVIL_WAR;
            case "Election": return STATE_TYPE.ELECTION;
            case "Boom": return STATE_TYPE.BOOM;
            case "Bust": return STATE_TYPE.BUST;
            case "Civil Unrest": return STATE_TYPE.CIVIL_UNREST;
            case "Famine": return STATE_TYPE.FAMINE;
            case "Outbreak": return STATE_TYPE.OUTBREAK;
            case "Lockdown": return STATE_TYPE.LOCKDOWN;
            case "Investment": return STATE_TYPE.INVESTMENT;
            case "Retreat": return STATE_TYPE.RETREAT;
            case "None": return STATE_TYPE.NONE;
        }

        LOG.warn("Unknown faction state: {}", state);
        return null;
    }

    @Nullable
    public static SECURITY_LEVEL asSecurityLevel(String security) {
        if (security == null) return null;
        switch (security) {
            case "$GAlAXY_MAP_INFO_state_anarchy;": return SECURITY_LEVEL.ANARCHY;
            case "$GALAXY_MAP_INFO_state_lawless;": return SECURITY_LEVEL.LAWLESS;
            case "$SYSTEM_SECURITY_none;": return SECURITY_LEVEL.NONE;
            case "$SYSTEM_SECURITY_low;": return SECURITY_LEVEL.LOW;
            case "$SYSTEM_SECURITY_medium;": return SECURITY_LEVEL.MEDIUM;
            case "$SYSTEM_SECURITY_high;": return SECURITY_LEVEL.HIGH;
            case "$SYSTEM_SECURITY_high_anarchy;": return SECURITY_LEVEL.HIGH_ANARCHY;
        }

        LOG.warn("Unknown security level: {}", security);
        return null;
    }

    @Nullable
    public static BODY_TYPE asBodyType(String type) {
        if (type == null) return null;
        switch (type) {
            case "Null": return BODY_TYPE.BARYCENTRE;
            case "Star": return BODY_TYPE.STAR;
            case "Planet": return BODY_TYPE.PLANET;
            case "PlanetaryRing": return BODY_TYPE.PLANETARY_RING;
            case "StellarRing": return BODY_TYPE.STELLAR_RING;
            case "Station": return BODY_TYPE.STATION;
            case "AsteroidCluster": return BODY_TYPE.ASTEROID_CLUSTER;
        }

        LOG.warn("Unknown body type: {}", type);
        return null;
    }

}
