package ru.elite.store.jpa.entity;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.Commander;
import ru.elite.entity.Ship;
import ru.elite.entity.Slot;
import ru.elite.store.jpa.JPATest;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;

public class ShipTest extends JPATest {
    private final static Logger LOG = LoggerFactory.getLogger(ShipTest.class);

    private final Commander cmdr = DEFAULT_GALAXY.CMDR;
    private final Long EID = 444L;
    private final int SHIP_SID = 1;
    private final String SHIP_TYPE = "Empire_Courier";
    private final String SHIP_NAME = "Fenix";
    private final String SHIP_IDENT = "FNX5";
    private final double FUEL = 15.7;
    private final double TANK = 18;


    public Ship createShip(){
        Ship ship = new ShipImpl(cmdr, SHIP_SID, SHIP_TYPE);
        ship.setEID(EID);
        ship.setName(SHIP_NAME);
        ship.setIdent(SHIP_IDENT);
        ship.setFuel(FUEL);
        ship.setTank(TANK);
        return ship;
    }

    public void assertShip(Ship ship){
        assertEquals(cmdr, ship.getCmdr());
        assertEquals(SHIP_SID, ship.getSid());
        assertEquals(SHIP_TYPE, ship.getType());
        assertEquals(EID, ship.getEID());
        assertEquals(SHIP_NAME, ship.getName());
        assertEquals(SHIP_IDENT, ship.getIdent());
        assertEquals(FUEL, ship.getFuel(), 0.0);
        assertEquals(TANK, ship.getTank(), 0.0);
    }

    @Test
    public void createTest(){
        LOG.info("Create ship test");
        Ship ship = createShip();
        assertShip(ship);
    }

    @Test
    public void slotsTest(){
        LOG.info("Ship slots test");
        final String SLOT1_NAME = "MainEngines";
        final String SLOT2_NAME = "MediumHardpoint1";
        final String SLOT3_NAME = "CargoHatch";
        final String MODULE1_NAME = "Int_Engine_Size3_Class5_Fast";
        final String MODULE2_NAME = "Int_Engine_Size3_Class1_Fast";

        Ship ship = createShip();
        Collection<Slot> slots = ship.getSlots();
        assertEquals(0, slots.size());

        final Slot slot1 = ship.addSlot(SLOT1_NAME);
        final Slot slot2 = ship.addSlot(SLOT2_NAME);
        final Slot slot3 = ship.addSlot(SLOT3_NAME);
        slot1.setModule(MODULE1_NAME);
        slot2.setModule(MODULE2_NAME);

        assertEquals(ship, slot1.getShip());
        assertEquals(ship, slot2.getShip());
        assertEquals(ship, slot3.getShip());

        slots = ship.getSlots();
        assertThat(slots, hasItems(slot1, slot2, slot3));
        assertEquals(3, slots.size());
        assertTrue(ship.getSlot(SLOT1_NAME).isPresent());
        assertTrue(ship.getSlot(SLOT2_NAME).isPresent());
        assertTrue(ship.hasModule(MODULE1_NAME));
        assertTrue(ship.hasModule(MODULE2_NAME));

        ship.removeSlot(slot2);
        assertNull(slot2.getShip());
        slots = ship.getSlots();
        assertThat(slots, hasItems(slot1, slot3));
        assertEquals(2, slots.size());
        assertTrue(ship.getSlot(SLOT1_NAME).isPresent());
        assertFalse(ship.getSlot(SLOT2_NAME).isPresent());
        assertTrue(ship.hasModule(MODULE1_NAME));
        assertFalse(ship.hasModule(MODULE2_NAME));

        ship.clearSlots();
        assertNull(slot1.getShip());
        assertNull(slot2.getShip());
        assertNull(slot3.getShip());
        slots = ship.getSlots();
        assertEquals(0, slots.size());
        assertFalse(ship.getSlot(SLOT1_NAME).isPresent());
        assertFalse(ship.getSlot(SLOT2_NAME).isPresent());
        assertFalse(ship.hasModule(MODULE1_NAME));
        assertFalse(ship.hasModule(MODULE2_NAME));
    }
}
