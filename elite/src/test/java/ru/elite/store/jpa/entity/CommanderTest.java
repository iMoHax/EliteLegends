package ru.elite.store.jpa.entity;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.*;
import ru.elite.store.jpa.JPATest;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;

public class CommanderTest extends JPATest {
    private final static Logger LOG = LoggerFactory.getLogger(CommanderTest.class);

    private final Long EID = 432L;
    private final String NAME = "MoHax";
    private final double CREDITS = 456446.44;
    private final StarSystem SYSTEM = DEFAULT_GALAXY.EURYALE;
    private final Station STATION = DEFAULT_GALAXY.EG_MAIN_HQ;
    private final boolean LANDED = true;
    private final double LAT = -152.4;
    private final double LONG = 75.445;
    private final boolean DEAD = false;

    public Commander createCommander(){
        Commander commander = new CommanderImpl(NAME);
        commander.setEID(EID);
        commander.setCredits(CREDITS);
        commander.setStarSystem(SYSTEM);
        commander.setStation(STATION);
        commander.setLanded(LANDED);
        commander.setLatitude(LAT);
        commander.setLongitude(LONG);
        commander.setDead(DEAD);
        return commander;
    }

    public void assertCmdr(Commander cmdr){
        assertEquals(NAME, cmdr.getName());
        assertEquals(EID, cmdr.getEID());
        assertEquals(CREDITS, cmdr.getCredits(), 0.0);
        assertEquals(SYSTEM, cmdr.getStarSystem());
        assertEquals(STATION, cmdr.getStation());
        assertEquals(LANDED, cmdr.isLanded());
        assertEquals(LAT, cmdr.getLatitude(), 0.0);
        assertEquals(LONG, cmdr.getLongitude(), 0.0);
        assertEquals(DEAD, cmdr.isDead());
    }

    @Test
    public void createTest(){
        LOG.info("Create cmdr test");
        Commander cmdr = createCommander();
        assertCmdr(cmdr);
    }

    @Test
    public void shipsTest(){
        LOG.info("Cmdr ships test");
        final int SHIP1_SID = 1;
        final String SHIP1_TYPE = "Empire_Courier";
        final int SHIP2_SID = 2;
        final String SHIP2_TYPE = "Type9";
        final int SHIP3_SID = 3;
        final String SHIP3_TYPE = "Anaconda";

        Commander cmdr = createCommander();
        Collection<Ship> ships = cmdr.getShips();
        assertEquals(0, ships.size());
        assertNull(cmdr.getShip());

        final Ship ship1 = cmdr.addShip(SHIP1_SID, SHIP1_TYPE);
        final Ship ship2 = cmdr.addShip(SHIP2_SID, SHIP2_TYPE);
        final Ship ship3 = cmdr.addShip(SHIP3_SID, SHIP3_TYPE);

        assertEquals(cmdr, ship1.getCmdr());
        assertEquals(cmdr, ship2.getCmdr());
        assertEquals(cmdr, ship3.getCmdr());

        ships = cmdr.getShips();
        assertThat(ships, hasItems(ship1, ship2, ship3));
        assertEquals(3, ships.size());

        Ship ship = cmdr.getShip(SHIP1_SID).get();
        assertEquals(ship, ship1);
        assertEquals(SHIP1_SID, ship.getSid());
        assertEquals(SHIP1_TYPE, ship.getType());

        ship = cmdr.getShip(SHIP2_SID).get();
        assertEquals(ship, ship2);
        assertEquals(SHIP2_SID, ship.getSid());
        assertEquals(SHIP2_TYPE, ship.getType());

        ship = cmdr.getShip(SHIP3_SID).get();
        assertEquals(ship, ship3);
        assertEquals(SHIP3_SID, ship.getSid());
        assertEquals(SHIP3_TYPE, ship.getType());

        assertNull(cmdr.getShip());
        cmdr.setShip(ship1);
        assertEquals(ship1, cmdr.getShip());

        cmdr.removeShip(ship2);
        assertNull(ship2.getCmdr());
        assertEquals(ship1, cmdr.getShip());
        ships = cmdr.getShips();
        assertThat(ships, hasItems(ship1, ship3));
        assertEquals(2, ships.size());
        assertTrue(cmdr.getShip(SHIP1_SID).isPresent());
        assertFalse(cmdr.getShip(SHIP2_SID).isPresent());
        assertTrue(cmdr.getShip(SHIP3_SID).isPresent());

        cmdr.clearShips();
        assertNull(ship1.getCmdr());
        assertNull(ship2.getCmdr());
        assertNull(ship3.getCmdr());
        assertNull(cmdr.getShip());
        ships = cmdr.getShips();
        assertEquals(0, ships.size());
        assertFalse(cmdr.getShip(SHIP1_SID).isPresent());
        assertFalse(cmdr.getShip(SHIP2_SID).isPresent());
        assertFalse(cmdr.getShip(SHIP3_SID).isPresent());
    }

    @Test
    public void rankTest(){
        LOG.info("Cmdr ranks test");

        final String RANK1_TYPE = "Federation";
        final int RANK1_VALUE = 4;
        final String RANK2_TYPE = "Alliance";
        final int RANK2_VALUE = 7;

        Commander cmdr = createCommander();
        assertEquals(0, cmdr.getRank(RANK1_TYPE));
        assertEquals(0, cmdr.getRank(RANK2_TYPE));

        cmdr.setRank(RANK1_TYPE, RANK1_VALUE);
        assertEquals(RANK1_VALUE, cmdr.getRank(RANK1_TYPE));
        assertEquals(0, cmdr.getRank(RANK2_TYPE));

        cmdr.setRank(RANK2_TYPE, RANK2_VALUE);
        assertEquals(RANK1_VALUE, cmdr.getRank(RANK1_TYPE));
        assertEquals(RANK2_VALUE, cmdr.getRank(RANK2_TYPE));


        cmdr.setRank(RANK1_TYPE, 0);
        assertEquals(0, cmdr.getRank(RANK1_TYPE));
        assertEquals(RANK2_VALUE, cmdr.getRank(RANK2_TYPE));

        cmdr.resetRanks();
        assertEquals(0, cmdr.getRank(RANK1_TYPE));
        assertEquals(0, cmdr.getRank(RANK2_TYPE));
    }

    @Test
    public void inventoryTest(){
        LOG.info("Cmdr inventory test");
        final Item ITEM1 = DEFAULT_GALAXY.ALGAE;
        final long ITEM1_COUNT = 10;
        final Item ITEM2 = DEFAULT_GALAXY.SHIELD_B4;
        final long ITEM2_COUNT = 2;
        final Item ITEM3 = DEFAULT_GALAXY.ANACONDA;
        final long ITEM3_COUNT = 1;
        final Item ITEM4 = DEFAULT_GALAXY.CHEMICAL_PROCESSORS;
        final long ITEM4_COUNT = 4;
        final Item ITEM5 = DEFAULT_GALAXY.NICKEL;
        final long ITEM5_COUNT = 55;
        final Item ITEM6 = DEFAULT_GALAXY.FSD_TELEMETRY;
        final long ITEM6_COUNT = 20;

        Commander cmdr = createCommander();
        Collection<InventoryEntry> entries = cmdr.getInventory();

        assertEquals(0, entries.size());

        final InventoryEntry entry1 = cmdr.addItem(ITEM1, ITEM1_COUNT);
        final InventoryEntry entry2 = cmdr.addItem(ITEM2, ITEM2_COUNT);
        final InventoryEntry entry3 = cmdr.addItem(ITEM3, ITEM3_COUNT);
        final InventoryEntry entry4 = cmdr.addItem(ITEM4, ITEM4_COUNT);
        final InventoryEntry entry5 = cmdr.addItem(ITEM5, ITEM5_COUNT);
        final InventoryEntry entry6 = cmdr.addItem(ITEM6, ITEM6_COUNT);

        assertEquals(cmdr, entry1.getCmdr());
        assertEquals(cmdr, entry2.getCmdr());
        assertEquals(cmdr, entry3.getCmdr());
        assertEquals(cmdr, entry4.getCmdr());
        assertEquals(cmdr, entry5.getCmdr());
        assertEquals(cmdr, entry6.getCmdr());

        entries = cmdr.getInventory();
        assertThat(entries, hasItems(entry1, entry2, entry3, entry4, entry5, entry6));
        assertEquals(6, entries.size());

        assertEquals(ITEM1_COUNT, cmdr.getCount(ITEM1));
        assertEquals(ITEM2_COUNT, cmdr.getCount(ITEM2));
        assertEquals(ITEM3_COUNT, cmdr.getCount(ITEM3));
        assertEquals(ITEM4_COUNT, cmdr.getCount(ITEM4));
        assertEquals(ITEM5_COUNT, cmdr.getCount(ITEM5));
        assertEquals(ITEM6_COUNT, cmdr.getCount(ITEM6));

        cmdr.removeItem(ITEM2);
        assertNull(entry2.getCmdr());
        entries = cmdr.getInventory();
        assertThat(entries, hasItems(entry1, entry3, entry4, entry5, entry6));
        assertEquals(5, entries.size());
        assertEquals(ITEM1_COUNT, cmdr.getCount(ITEM1));
        assertEquals(0, cmdr.getCount(ITEM2));
        assertEquals(ITEM3_COUNT, cmdr.getCount(ITEM3));
        assertEquals(ITEM4_COUNT, cmdr.getCount(ITEM4));
        assertEquals(ITEM5_COUNT, cmdr.getCount(ITEM5));
        assertEquals(ITEM6_COUNT, cmdr.getCount(ITEM6));

        cmdr.addItem(ITEM1, ITEM2_COUNT);
        assertEquals(cmdr, entry1.getCmdr());
        entries = cmdr.getInventory();
        assertThat(entries, hasItems(entry1, entry3, entry4, entry5, entry6));
        assertEquals(5, entries.size());
        assertEquals(ITEM1_COUNT+ITEM2_COUNT, cmdr.getCount(ITEM1));
        assertEquals(0, cmdr.getCount(ITEM2));
        assertEquals(ITEM3_COUNT, cmdr.getCount(ITEM3));
        assertEquals(ITEM4_COUNT, cmdr.getCount(ITEM4));
        assertEquals(ITEM5_COUNT, cmdr.getCount(ITEM5));
        assertEquals(ITEM6_COUNT, cmdr.getCount(ITEM6));

        cmdr.addItem(ITEM1, -ITEM2_COUNT);
        assertEquals(cmdr, entry1.getCmdr());
        entries = cmdr.getInventory();
        assertThat(entries, hasItems(entry1, entry3, entry4, entry5, entry6));
        assertEquals(5, entries.size());
        assertEquals(ITEM1_COUNT, cmdr.getCount(ITEM1));
        assertEquals(0, cmdr.getCount(ITEM2));
        assertEquals(ITEM3_COUNT, cmdr.getCount(ITEM3));
        assertEquals(ITEM4_COUNT, cmdr.getCount(ITEM4));
        assertEquals(ITEM5_COUNT, cmdr.getCount(ITEM5));
        assertEquals(ITEM6_COUNT, cmdr.getCount(ITEM6));

        cmdr.addItem(ITEM1, -ITEM1_COUNT);
        assertNull(entry1.getCmdr());
        entries = cmdr.getInventory();
        assertThat(entries, hasItems(entry3, entry4, entry5, entry6));
        assertEquals(4, entries.size());
        assertEquals(0, cmdr.getCount(ITEM1));
        assertEquals(0, cmdr.getCount(ITEM2));
        assertEquals(ITEM3_COUNT, cmdr.getCount(ITEM3));
        assertEquals(ITEM4_COUNT, cmdr.getCount(ITEM4));
        assertEquals(ITEM5_COUNT, cmdr.getCount(ITEM5));
        assertEquals(ITEM6_COUNT, cmdr.getCount(ITEM6));

        cmdr.addItem(ITEM5, -ITEM5_COUNT-10);
        assertNull(entry5.getCmdr());
        entries = cmdr.getInventory();
        assertThat(entries, hasItems(entry3, entry4, entry6));
        assertEquals(3, entries.size());
        assertEquals(0, cmdr.getCount(ITEM1));
        assertEquals(0, cmdr.getCount(ITEM2));
        assertEquals(ITEM3_COUNT, cmdr.getCount(ITEM3));
        assertEquals(ITEM4_COUNT, cmdr.getCount(ITEM4));
        assertEquals(0, cmdr.getCount(ITEM5));
        assertEquals(ITEM6_COUNT, cmdr.getCount(ITEM6));


        cmdr.clearItems();
        assertNull(entry1.getCmdr());
        assertNull(entry2.getCmdr());
        assertNull(entry3.getCmdr());
        assertNull(entry4.getCmdr());
        assertNull(entry5.getCmdr());
        assertNull(entry6.getCmdr());
        entries = cmdr.getInventory();
        assertEquals(0, entries.size());
        assertEquals(0, cmdr.getCount(ITEM1));
        assertEquals(0, cmdr.getCount(ITEM2));
        assertEquals(0, cmdr.getCount(ITEM3));
        assertEquals(0, cmdr.getCount(ITEM4));
        assertEquals(0, cmdr.getCount(ITEM5));
        assertEquals(0, cmdr.getCount(ITEM6));
    }

}
