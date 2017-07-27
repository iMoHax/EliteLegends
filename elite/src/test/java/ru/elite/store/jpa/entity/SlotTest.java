package ru.elite.store.jpa.entity;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.Module;
import ru.elite.entity.Ship;
import ru.elite.entity.Slot;
import ru.elite.store.jpa.JPATest;

public class SlotTest extends JPATest {
    private final static Logger LOG = LoggerFactory.getLogger(SlotTest.class);

    private final Ship ship = DEFAULT_GALAXY.COURIER;
    private final String SLOT_NAME = "MainEngines";
    private final boolean SLOT_ON = true;
    private final int SLOT_PRIORITY = 3;


    public Slot createSlot(){
        Slot slot = new SlotImpl(ship, SLOT_NAME);
        slot.setActive(SLOT_ON);
        slot.setPriority(SLOT_PRIORITY);
        return slot;
    }

    public void assertSlot(Slot slot){
        assertEquals(ship, slot.getShip());
        assertEquals(SLOT_NAME, slot.getName());
        assertEquals(SLOT_ON, slot.isActive());
        assertEquals(SLOT_PRIORITY, slot.getPriority());
    }

    @Test
    public void createTest(){
        LOG.info("Create slot test");
        Slot slot = createSlot();
        assertSlot(slot);
    }

    @Test
    public void moduleTest(){
        LOG.info("Slot module test");
        final String MODULE1_NAME = "Int_Engine_Size3_Class5_Fast";
        final String MODULE2_NAME = "Int_Engine_Size3_Class1_Fast";

        Slot slot = createSlot();
        Module module = slot.getModule();
        assertNull(module);
        assertFalse(slot.isModule(MODULE1_NAME));
        assertFalse(slot.isModule(MODULE2_NAME));

        Module module1 = slot.setModule(MODULE1_NAME);
        assertEquals(slot, module1.getSlot());
        assertEquals(module1, slot.getModule());
        assertTrue(slot.isModule(MODULE1_NAME));
        assertFalse(slot.isModule(MODULE2_NAME));

        Module module2 = slot.setModule(MODULE2_NAME);
        assertEquals(slot, module2.getSlot());
        assertEquals(module2, slot.getModule());
        assertNull(module1.getSlot());
        assertFalse(slot.isModule(MODULE1_NAME));
        assertTrue(slot.isModule(MODULE2_NAME));

        module = slot.swapModule(module1);
        assertEquals(module2, module);
        assertEquals(slot, module1.getSlot());
        assertEquals(module1, slot.getModule());
        assertTrue(slot.isModule(MODULE1_NAME));
        assertFalse(slot.isModule(MODULE2_NAME));
        assertNull(module2.getSlot());

        slot.removeModule();
        assertNull(module1.getSlot());
        assertNull(slot.getModule());
    }
}
