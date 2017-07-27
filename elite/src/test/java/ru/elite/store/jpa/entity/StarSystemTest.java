package ru.elite.store.jpa.entity;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.core.*;
import ru.elite.entity.MinorFaction;
import ru.elite.entity.MinorFactionState;
import ru.elite.entity.StarSystem;
import ru.elite.entity.Station;
import ru.elite.store.jpa.JPATest;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;

public class StarSystemTest extends JPATest {
    private final static Logger LOG = LoggerFactory.getLogger(StarSystemTest.class);

    private final MinorFaction CONTROLLING_FACTION = DEFAULT_GALAXY.EGU;
    private final String SYSTEM_NAME = "Euryale";
    private final double X = 35.3;
    private final double Y = -68.96875;
    private final double Z = 24;
    private final Long EID = 222314L;
    private final long POPULATION = 3423235;
    private final SECURITY_LEVEL SECURITY_LVL = SECURITY_LEVEL.HIGH;
    private final POWER POWER_VALUE = POWER.GROM;
    private final POWER_STATE POWER_STATE_VALUE = POWER_STATE.CONTROL;
    private final long INCOME = 190;
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.of(2017,7,3,8,45);

    public StarSystem createSystem(){
        StarSystem system = new StarSystemImpl(SYSTEM_NAME, X, Y, Z);
        system.setEID(EID);
        system.setPopulation(POPULATION);
        system.setControllingFaction(CONTROLLING_FACTION);
        system.setSecurity(SECURITY_LVL);
        system.setPower(POWER_VALUE, POWER_STATE_VALUE);
        system.setIncome(INCOME);
        system.setModifiedTime(MODIFIED_TIME);
        return system;
    }

    public void assertSystem(StarSystem system){
        assertEquals(SYSTEM_NAME, system.getName());
        assertEquals(X, system.getX(),0.0);
        assertEquals(Y, system.getY(),0.0);
        assertEquals(Z, system.getZ(),0.0);
        assertEquals(EID, system.getEID());
        assertEquals(POPULATION, system.getPopulation());
        assertEquals(CONTROLLING_FACTION, system.getControllingFaction());
        assertEquals(SECURITY_LVL, system.getSecurity());
        assertEquals(POWER_VALUE, system.getPower());
        assertEquals(POWER_STATE_VALUE, system.getPowerState());
        assertEquals(INCOME, system.getIncome());
        assertEquals(MODIFIED_TIME, system.getModifiedTime());
    }

    @Test
    public void createTest(){
        LOG.info("Create system test");
        StarSystem system = createSystem();
        assertSystem(system);
    }


    @Test
    public void factionsTest(){
        LOG.info("StarSystem factions test");
        final MinorFaction FACTION1 = DEFAULT_GALAXY.EGU;
        final float FLUENCY1 = 80.3f;
        final STATE_TYPE STATE1 = STATE_TYPE.CIVIL_WAR;

        final MinorFaction FACTION2 = DEFAULT_GALAXY.DIAMOND_FROGS;
        final float FLUENCY2 = 10.3f;
        final STATE_TYPE STATE2 = STATE_TYPE.CIVIL_WAR;

        final MinorFaction FACTION3 = DEFAULT_GALAXY.SIRIUS;
        final float FLUENCY3 = 0.3f;
        final STATE_TYPE STATE3 = STATE_TYPE.NONE;

        StarSystem system = createSystem();
        Collection<MinorFactionState> factions = system.getFactions();
        assertEquals(0, factions.size());

        final MinorFactionState faction1 = system.addFaction(FACTION1, STATE1, FLUENCY1);
        final MinorFactionState faction2 = system.addFaction(FACTION2, STATE2, FLUENCY2);
        final MinorFactionState faction3 = system.addFaction(FACTION3, STATE3, FLUENCY3);

        assertEquals(system, faction1.getStarSystem());
        assertEquals(system, faction2.getStarSystem());
        assertEquals(system, faction3.getStarSystem());

        factions = system.getFactions();
        assertThat(factions, hasItems(faction1, faction2, faction3));
        assertEquals(3, factions.size());
        for (MinorFactionState faction : factions) {
            if (faction.equals(faction1)){
                assertEquals(FACTION1, faction.getFaction());
                assertEquals(STATE1, faction.getState());
                assertEquals(FLUENCY1, faction.getInfluence(), 0.0);
            } else
            if (faction.equals(faction2)){
                assertEquals(FACTION2, faction.getFaction());
                assertEquals(STATE2, faction.getState());
                assertEquals(FLUENCY2, faction.getInfluence(), 0.0);
            } else
            if (faction.equals(faction3)){
                assertEquals(FACTION3, faction.getFaction());
                assertEquals(STATE3, faction.getState());
                assertEquals(FLUENCY3, faction.getInfluence(), 0.0);
            }
        }

        system.removeFaction(faction2);
        assertNull(faction2.getStarSystem());
        factions = system.getFactions();
        assertThat(factions, hasItems(faction1, faction3));
        assertEquals(2, factions.size());

        system.clearFactions();
        assertNull(faction1.getStarSystem());
        assertNull(faction2.getStarSystem());
        assertNull(faction3.getStarSystem());
        factions = system.getFactions();
        assertEquals(0, factions.size());

    }

    @Test
    public void stationsTest(){
        LOG.info("StarSystem stations test");
        final String STATION1_NAME = "EG Main";
        final STATION_TYPE STATION1_TYPE = STATION_TYPE.CORIOLIS_STARPORT;
        final double STATION1_DISTANCE = 700.5;

        final String STATION2_NAME = "Popovich terminal";
        final STATION_TYPE STATION2_TYPE = STATION_TYPE.SCIENTIFIC_OUTPOST;
        final double STATION2_DISTANCE = 1350000.5;

        final String STATION3_NAME = "Gagarin starport";
        final STATION_TYPE STATION3_TYPE = STATION_TYPE.PLANETARY_PORT;
        final double STATION3_DISTANCE = 445;

        StarSystem system = createSystem();
        Collection<Station> stations = system.get();
        assertEquals(0, stations.size());
        assertTrue(system.isEmpty());

        final Station station1 = system.addStation(STATION1_NAME, STATION1_TYPE, STATION1_DISTANCE);
        final Station station2 = system.addStation(STATION2_NAME, STATION2_TYPE, STATION2_DISTANCE);
        final Station station3 = system.addStation(STATION3_NAME, STATION3_TYPE, STATION3_DISTANCE);

        assertEquals(system, station1.getStarSystem());
        assertEquals(system, station2.getStarSystem());
        assertEquals(system, station3.getStarSystem());

        stations = system.get();
        assertThat(stations, hasItems(station1, station2, station3));
        assertEquals(3, stations.size());
        assertFalse(system.isEmpty());
        for (Station station : stations) {
            if (station.equals(station1)){
                assertEquals(STATION1_NAME, station.getName());
                assertEquals(STATION1_TYPE, station.getType());
                assertEquals(STATION1_DISTANCE, station.getDistance(), 0.0);
            } else
            if (station.equals(station2)){
                assertEquals(STATION2_NAME, station.getName());
                assertEquals(STATION2_TYPE, station.getType());
                assertEquals(STATION2_DISTANCE, station.getDistance(), 0.0);
            } else
            if (station.equals(station3)){
                assertEquals(STATION3_NAME, station.getName());
                assertEquals(STATION3_TYPE, station.getType());
                assertEquals(STATION3_DISTANCE, station.getDistance(), 0.0);
            }
        }

        system.removeStation(station2);
        assertNull(station2.getStarSystem());
        stations = system.get();
        assertThat(stations, hasItems(station1, station3));
        assertEquals(2, stations.size());
        assertFalse(system.isEmpty());

        system.clearStations();
        assertNull(station1.getStarSystem());
        assertNull(station2.getStarSystem());
        assertNull(station3.getStarSystem());
        stations = system.get();
        assertEquals(0, stations.size());
        assertTrue(system.isEmpty());

    }
}