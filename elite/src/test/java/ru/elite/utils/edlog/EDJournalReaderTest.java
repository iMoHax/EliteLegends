package ru.elite.utils.edlog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.elite.entity.Commander;
import ru.elite.store.jpa.GalaxyStore;
import ru.elite.store.jpa.JPATest;
import ru.elite.utils.edlog.entities.JournalHandler;
import ru.elite.utils.edlog.entities.JournalImportHandler;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EDJournalReaderTest extends JPATest {

    private GalaxyStore galaxy;
    private EventImporter importer;

    @Before
    public void setUp() throws Exception {
        EntityManager manager = EMF.createEntityManager();
        galaxy = new GalaxyStore(manager);
        importer = new EventImporter(galaxy);
    }

    @Test
    public void testStartup() throws Exception {
        JournalHandler handler = new JournalImportHandler(importer);
        EDJournalReader reader = new EDJournalReader();
        reader.setHandler(handler);
        BufferedReader input = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/journal.log")));
        reader.read(input);
        assertTrue(importer.getImportedCmdr().isPresent());
        Commander cmdr = importer.getImportedCmdr().get();
        assertEquals("MoHax", cmdr.getName());
        assertEquals(false, cmdr.isDead());
        assertEquals(false, cmdr.isLanded());
        assertNull(cmdr.getLatitude());
        assertNull(cmdr.getLongitude());
        assertEquals("Van Lang", cmdr.getBody().getName());
        assertEquals("Pethes", cmdr.getStarSystem().getName());
        assertNull(cmdr.getStation());
    }

    @Test
    public void testReader() throws Exception {
        JournalHandler handler = new JournalImportHandler(importer);
        EDJournalReader reader = new EDJournalReader();
        reader.setHandler(handler);
        BufferedReader input = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Journal.170810140112.01.log")));
        reader.read(input);
        assertTrue(importer.getImportedCmdr().isPresent());
        Commander cmdr = importer.getImportedCmdr().get();
        assertEquals("MoHax", cmdr.getName());
        assertEquals(false, cmdr.isDead());
        assertEquals(false, cmdr.isLanded());
        assertNull(cmdr.getLatitude());
        assertNull(cmdr.getLongitude());
        assertEquals("Gooheimar", cmdr.getBody().getName());
        assertEquals("Gooheimar", cmdr.getStarSystem().getName());
        assertNull(cmdr.getStation());
    }

    @Test
    public void testLanding() throws Exception {
        JournalHandler handler = new JournalImportHandler(importer);
        EDJournalReader reader = new EDJournalReader();
        reader.setHandler(handler);
        BufferedReader input = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Journal.170810133040.01.log")));
        reader.read(input);
        assertTrue(importer.getImportedCmdr().isPresent());
        Commander cmdr = importer.getImportedCmdr().get();
        assertEquals("MoHax", cmdr.getName());
        assertEquals(false, cmdr.isDead());
        assertEquals(true, cmdr.isLanded());
        assertEquals(-8.669971, cmdr.getLatitude(), 0.000001);
        assertEquals(6.145895, cmdr.getLongitude(), 0.000001);
        assertEquals("Pethes 2 b", cmdr.getBody().getName());
        assertEquals("Pethes", cmdr.getStarSystem().getName());
        assertNull(cmdr.getStation());
    }

    @After
    public void tearDown() throws Exception {
        galaxy.close();
    }

}
