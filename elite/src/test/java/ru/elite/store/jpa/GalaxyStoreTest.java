package ru.elite.store.jpa;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.core.*;
import ru.elite.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public class GalaxyStoreTest extends JPATest {
    private final static Logger LOG = LoggerFactory.getLogger(GalaxyStoreTest.class);

    private final String SYSTEM_NAME = "Euryale";
    private final double X = 35.3;
    private final double Y = -68.96875;
    private final double Z = 24;
    private final MinorFaction CONTROLLING_FACTION = DEFAULT_GALAXY.EGU;
    private final Long SYSTEM_EID = 222314L;
    private final long POPULATION = 3423235;
    private final SECURITY_LEVEL SECURITY_LVL = SECURITY_LEVEL.HIGH;
    private final POWER POWER_VALUE = POWER.GROM;
    private final POWER_STATE POWER_STATE_VALUE = POWER_STATE.CONTROL;
    private final long INCOME = 190;
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.of(2017,7,3,8,45);

    private final MinorFaction FACTION1 = DEFAULT_GALAXY.EGU;
    private final float FLUENCY1 = 80.3f;
    private final STATE_TYPE STATE1 = STATE_TYPE.CIVIL_WAR;
    private final MinorFaction FACTION2 = DEFAULT_GALAXY.DIAMOND_FROGS;
    private final float FLUENCY2 = 10.3f;
    private final STATE_TYPE STATE2 = STATE_TYPE.CIVIL_WAR;
    private final MinorFaction FACTION3 = DEFAULT_GALAXY.SIRIUS;
    private final float FLUENCY3 = 0.3f;
    private final STATE_TYPE STATE3 = STATE_TYPE.NONE;


    @Test
    public void galaxyTest(){
        LOG.info("Start galaxy store test");
        EntityManager manager = EMF.createEntityManager();
        try {
            GalaxyStore galaxy = new GalaxyStore(manager);
            EntityTransaction transaction = galaxy.startTransaction();
            StarSystem starSystem = addSystem(galaxy);
            transaction.commit();
            Optional<StarSystem> s = galaxy.findStarSystemByEID(SYSTEM_EID);
            assertTrue(s.isPresent());
            StarSystem actual = s.get();
            assertEquals(starSystem, actual);
            assertStarSystem(actual);
        } finally {
            manager.close();
        }
    }

    private StarSystem addSystem(GalaxyStore galaxy){
        StarSystem starSystem = galaxy.addStarSystem(SYSTEM_NAME, X, Y, Z);
        starSystem.setEID(SYSTEM_EID);
        starSystem.setPopulation(POPULATION);
        starSystem.setControllingFaction(CONTROLLING_FACTION);
        starSystem.setSecurity(SECURITY_LVL);
        starSystem.setPower(POWER_VALUE, POWER_STATE_VALUE);
        starSystem.setIncome(INCOME);
        starSystem.setModifiedTime(MODIFIED_TIME);
        starSystem.addFaction(FACTION1, STATE1, FLUENCY1);
        starSystem.addFaction(FACTION2, STATE2, FLUENCY2);
        starSystem.addFaction(FACTION3, STATE3, FLUENCY3);
        return starSystem;
    }

    public void assertStarSystem(StarSystem system){
        assertEquals(SYSTEM_NAME, system.getName());
        assertEquals(X, system.getX(),0.0);
        assertEquals(Y, system.getY(),0.0);
        assertEquals(Z, system.getZ(),0.0);
        assertEquals(SYSTEM_EID, system.getEID());
        assertEquals(POPULATION, system.getPopulation());
        assertEquals(CONTROLLING_FACTION, system.getControllingFaction());
        assertEquals(SECURITY_LVL, system.getSecurity());
        assertEquals(POWER_VALUE, system.getPower());
        assertEquals(POWER_STATE_VALUE, system.getPowerState());
        assertEquals(INCOME, system.getIncome());
        assertEquals(MODIFIED_TIME, system.getModifiedTime());
        Collection<MinorFactionState> factions = system.getFactions();
        assertEquals(3, factions.size());
    }

}
