package ru.elite.utils.edlog;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.core.*;
import ru.elite.entity.*;
import ru.elite.store.jpa.GalaxyStore;
import ru.elite.store.jpa.JPATest;
import ru.elite.utils.edlog.entities.events.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;

public class EventImporterTest extends JPATest {
    private final static Logger LOG = LoggerFactory.getLogger(EventImporterTest.class);

    private final String CARGO_EVENT="{ \"timestamp\":\"2017-08-10T11:01:46Z\", \"event\":\"Cargo\", \"Inventory\":[ { \"Name\":\"basicmedicines\", \"Count\":3 }, { \"Name\":\"clothing\", \"Count\":1 }, { \"Name\":\"domesticappliances\", \"Count\":1 }, { \"Name\":\"thermalcoolingunits\", \"Count\":3 } ] }";
    private final String LOADOUT_EVENT="{ \"timestamp\":\"2017-08-10T11:01:46Z\", \"event\":\"Loadout\", \"Ship\":\"Empire_Courier\", \"ShipID\":31, \"ShipName\":\"Spacer\", \"ShipIdent\":\"A42MO\", \"Modules\":[ { \"Slot\":\"MediumHardpoint1\", \"Item\":\"Hpt_BasicMissileRack_Fixed_Medium\", \"On\":true, \"Priority\":2, \"AmmoInClip\":6, \"AmmoInHopper\":18, \"Health\":0.987233, \"Value\":512400 }, { \"Slot\":\"MediumHardpoint2\", \"Item\":\"Hpt_BasicMissileRack_Fixed_Medium\", \"On\":true, \"Priority\":2, \"AmmoInClip\":6, \"AmmoInHopper\":18, \"Health\":0.998152, \"Value\":512400 }, { \"Slot\":\"MediumHardpoint3\", \"Item\":\"Hpt_Slugshot_Gimbal_Medium\", \"On\":true, \"Priority\":2, \"AmmoInClip\":3, \"AmmoInHopper\":144, \"Health\":0.993243, \"Value\":437760, \"EngineerBlueprint\":\"Weapon_LightWeight\", \"EngineerLevel\":3 }, { \"Slot\":\"TinyHardpoint1\", \"Item\":\"Hpt_ChaffLauncher_Tiny\", \"On\":true, \"Priority\":2, \"AmmoInClip\":1, \"AmmoInHopper\":10, \"Health\":0.994127, \"Value\":8500 }, { \"Slot\":\"TinyHardpoint2\", \"Item\":\"Hpt_ChaffLauncher_Tiny\", \"On\":true, \"Priority\":2, \"AmmoInClip\":1, \"AmmoInHopper\":9, \"Health\":0.994705, \"Value\":8500 }, { \"Slot\":\"TinyHardpoint3\", \"Item\":\"Hpt_ShieldBooster_Size0_Class2\", \"On\":true, \"Priority\":3, \"Health\":0.999349, \"Value\":23000, \"EngineerBlueprint\":\"ShieldBooster_Resistive\", \"EngineerLevel\":1 }, { \"Slot\":\"TinyHardpoint4\", \"Item\":\"Hpt_PlasmaPointDefence_Turret_Tiny\", \"On\":true, \"Priority\":2, \"AmmoInClip\":12, \"AmmoInHopper\":10000, \"Health\":0.990338, \"Value\":18546 }, { \"Slot\":\"Decal1\", \"Item\":\"Decal_Combat_Dangerous\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":0 }, { \"Slot\":\"Armour\", \"Item\":\"Empire_Courier_Armour_Grade3\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":2288637, \"EngineerBlueprint\":\"Armour_Advanced\", \"EngineerLevel\":3 }, { \"Slot\":\"PowerPlant\", \"Item\":\"Int_Powerplant_Size4_Class5\", \"On\":true, \"Priority\":1, \"Health\":0.997545, \"Value\":1610080 }, { \"Slot\":\"MainEngines\", \"Item\":\"Int_Engine_Size3_Class5_Fast\", \"On\":false, \"Priority\":0, \"Health\":0.995649, \"Value\":5103952, \"EngineerBlueprint\":\"Engine_Dirty\", \"EngineerLevel\":3 }, { \"Slot\":\"FrameShiftDrive\", \"Item\":\"Int_Hyperdrive_Size3_Class5\", \"On\":true, \"Priority\":0, \"Health\":0.993468, \"Value\":507912 }, { \"Slot\":\"LifeSupport\", \"Item\":\"Int_LifeSupport_Size1_Class5\", \"On\":true, \"Priority\":1, \"Health\":0.989855, \"Value\":20195 }, { \"Slot\":\"PowerDistributor\", \"Item\":\"Int_PowerDistributor_Size3_Class5\", \"On\":true, \"Priority\":0, \"Health\":0.990836, \"Value\":158331 }, { \"Slot\":\"Radar\", \"Item\":\"Int_Sensors_Size2_Class3\", \"On\":true, \"Priority\":1, \"Health\":0.990445, \"Value\":9048 }, { \"Slot\":\"FuelTank\", \"Item\":\"Int_FuelTank_Size3_Class3\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":7063 }, { \"Slot\":\"Slot01_Size3\", \"Item\":\"Int_ShieldGenerator_Size3_Class5\", \"On\":true, \"Priority\":2, \"Health\":0.997802, \"Value\":507912, \"EngineerBlueprint\":\"ShieldGenerator_Reinforced\", \"EngineerLevel\":5 }, { \"Slot\":\"Slot02_Size3\", \"Item\":\"Int_CargoRack_Size3_Class1\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":10563 }, { \"Slot\":\"Slot03_Size2\", \"Item\":\"Int_HullReinforcement_Size2_Class2\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":36000, \"EngineerBlueprint\":\"HullReinforcement_HeavyDuty\", \"EngineerLevel\":4 }, { \"Slot\":\"Slot04_Size2\", \"Item\":\"Int_HullReinforcement_Size1_Class2\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":15000, \"EngineerBlueprint\":\"HullReinforcement_HeavyDuty\", \"EngineerLevel\":4 }, { \"Slot\":\"Slot05_Size2\", \"Item\":\"Int_HullReinforcement_Size2_Class2\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":36000, \"EngineerBlueprint\":\"HullReinforcement_HeavyDuty\", \"EngineerLevel\":4 }, { \"Slot\":\"Slot06_Size1\", \"Item\":\"Int_HullReinforcement_Size1_Class2\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":15000, \"EngineerBlueprint\":\"HullReinforcement_HeavyDuty\", \"EngineerLevel\":4 }, { \"Slot\":\"PlanetaryApproachSuite\", \"Item\":\"Int_PlanetApproachSuite\", \"On\":true, \"Priority\":1, \"Health\":1.000000, \"Value\":500 }, { \"Slot\":\"ShipCockpit\", \"Item\":\"Empire_Courier_Cockpit\", \"On\":true, \"Priority\":1, \"Health\":0.984969, \"Value\":0 }, { \"Slot\":\"CargoHatch\", \"Item\":\"ModularCargoBayDoor\", \"On\":true, \"Priority\":1, \"Health\":0.999056, \"Value\":0 } ] }";
    private final String MATERIALS_EVENT="{ \"timestamp\": \"2017-08-10T10:59:11Z\", \"event\": \"Materials\", \"Raw\": [{\"Name\": \"mercury\",\"Count\": 6},{\"Name\": \"chromium\",\"Count\": 29},{\"Name\": \"nickel\",\"Count\": 15}], \"Manufactured\": [{\"Name\": \"shieldemitters\",\"Count\": 27}, { \"Name\":\"chemicalprocessors\", \"Count\":32 }], \"Encoded\": [{\"Name\": \"bulkscandata\",\"Count\": 50}] }";
    private final String LOADGAME_EVENT="{ \"timestamp\":\"2017-08-10T11:01:46Z\", \"event\":\"LoadGame\", \"Commander\":\"MoHax\", \"Ship\":\"Empire_Courier\", \"ShipID\":31, \"ShipName\":\"Spacer\", \"ShipIdent\":\"A42MO\", \"FuelLevel\":7.000000, \"FuelCapacity\":8.000000, \"StartLanded\":true, \"GameMode\":\"Open\", \"Credits\":348755553, \"Loan\":0 }";
    private final String RANK_EVENT="{ \"timestamp\":\"2017-07-20T15:42:36Z\", \"event\":\"Rank\", \"Combat\":7, \"Trade\":7, \"Explore\":5, \"Empire\":5, \"Federation\":9, \"CQC\":0 }\n";
    private final String LOCATION_EVENT="{ \"timestamp\":\"2017-08-10T11:02:03Z\", \"event\":\"Location\", \"Latitude\":-8.669971, \"Longitude\":6.145895, \"Docked\":false, \"StarSystem\":\"Pethes\", \"StarPos\":[-20.656,-32.469,-11.906], \"SystemAllegiance\":\"Federation\", \"SystemEconomy\":\"$economy_Industrial;\", \"SystemEconomy_Localised\":\"Промышленность\", \"SystemGovernment\":\"$government_Corporate;\", \"SystemGovernment_Localised\":\"Корпоративная\", \"SystemSecurity\":\"$SYSTEM_SECURITY_medium;\", \"SystemSecurity_Localised\":\"Средн. ур. безопасности\", \"Body\":\"Pethes 2 b\", \"BodyType\":\"Planet\", \"Powers\":[ \"Yuri Grom\" ], \"PowerplayState\":\"Controlled\", \"Factions\":[ { \"Name\":\"Crew of Pethes\", \"FactionState\":\"Boom\", \"Government\":\"Anarchy\", \"Influence\":0.112000, \"Allegiance\":\"Federation\", \"PendingStates\":[ { \"State\":\"Famine\", \"Trend\":0 } ] }, { \"Name\":\"LHS 1050 Holdings\", \"FactionState\":\"Boom\", \"Government\":\"Corporate\", \"Influence\":0.071000, \"Allegiance\":\"Federation\" }, { \"Name\":\"LP 525-39 & Co\", \"FactionState\":\"Boom\", \"Government\":\"Corporate\", \"Influence\":0.089000, \"Allegiance\":\"Federation\" }, { \"Name\":\"Noblemen of Pethes\", \"FactionState\":\"Boom\", \"Government\":\"Feudal\", \"Influence\":0.062000, \"Allegiance\":\"Independent\" }, { \"Name\":\"FT Piscium Vision Network\", \"FactionState\":\"Boom\", \"Government\":\"Corporate\", \"Influence\":0.174000, \"Allegiance\":\"Federation\" }, { \"Name\":\"Pethes Purple Fortune Corp.\", \"FactionState\":\"None\", \"Government\":\"Corporate\", \"Influence\":0.371000, \"Allegiance\":\"Federation\", \"PendingStates\":[ { \"State\":\"War\", \"Trend\":0 } ], \"RecoveringStates\":[ { \"State\":\"Boom\", \"Trend\":0 } ] }, { \"Name\":\"LHS 134 Central & Co\", \"FactionState\":\"Boom\", \"Government\":\"Corporate\", \"Influence\":0.121000, \"Allegiance\":\"Federation\" } ], \"SystemFaction\":\"Pethes Purple Fortune Corp.\", \"FactionState\":\"Boom\" }";
    private final String LOADGAME_DOCKED_EVENT="{ \"timestamp\":\"2017-08-10T14:17:26Z\", \"event\":\"LoadGame\", \"Commander\":\"MoHax\", \"Ship\":\"Empire_Courier\", \"ShipID\":31, \"ShipName\":\"Spacer\", \"ShipIdent\":\"A42MO\", \"FuelLevel\":7.000000, \"FuelCapacity\":8.000000, \"GameMode\":\"Solo\", \"Credits\":348755553, \"Loan\":0 }";
    private final String LOCATION_DOCKED_EVENT="{ \"timestamp\":\"2017-08-10T14:17:29Z\", \"event\":\"Location\", \"Docked\":true, \"StationName\":\"Willis Port\", \"StationType\":\"Bernal\", \"StarSystem\":\"Wolf 124\", \"StarPos\":[-7.250,-27.156,-19.094], \"SystemAllegiance\":\"Federation\", \"SystemEconomy\":\"$economy_Industrial;\", \"SystemEconomy_Localised\":\"Промышленность\", \"SystemGovernment\":\"$government_Democracy;\", \"SystemGovernment_Localised\":\"Демократия\", \"SystemSecurity\":\"$SYSTEM_SECURITY_high;\", \"SystemSecurity_Localised\":\"Высок. ур. безопасности\", \"Body\":\"Willis Port\", \"BodyType\":\"Station\", \"Powers\":[ \"Zachary Hudson\" ], \"PowerplayState\":\"Exploited\", \"Factions\":[ { \"Name\":\"Green Party of Wolf 124\", \"FactionState\":\"Boom\", \"Government\":\"Democracy\", \"Influence\":0.481000, \"Allegiance\":\"Federation\" }, { \"Name\":\"LFT 142 Liberals\", \"FactionState\":\"War\", \"Government\":\"Democracy\", \"Influence\":0.023000, \"Allegiance\":\"Federation\", \"PendingStates\":[ { \"State\":\"Boom\", \"Trend\":1 } ] }, { \"Name\":\"League of Wolf 124 Liberty Party\", \"FactionState\":\"War\", \"Government\":\"Dictatorship\", \"Influence\":0.030000, \"Allegiance\":\"Independent\", \"PendingStates\":[ { \"State\":\"Boom\", \"Trend\":0 } ] }, { \"Name\":\"Wolf 124 Jet Life Industries\", \"FactionState\":\"Boom\", \"Government\":\"Corporate\", \"Influence\":0.149000, \"Allegiance\":\"Federation\" }, { \"Name\":\"Wolf 124 Crimson Drug Empire\", \"FactionState\":\"Boom\", \"Government\":\"Anarchy\", \"Influence\":0.076000, \"Allegiance\":\"Independent\" }, { \"Name\":\"Conservatives of Wolf 124\", \"FactionState\":\"Boom\", \"Government\":\"Dictatorship\", \"Influence\":0.093000, \"Allegiance\":\"Independent\" }, { \"Name\":\"Hutton Orbital Truckers Co-Operative\", \"FactionState\":\"None\", \"Government\":\"Cooperative\", \"Influence\":0.148000, \"Allegiance\":\"Independent\", \"PendingStates\":[ { \"State\":\"Outbreak\", \"Trend\":0 } ], \"RecoveringStates\":[ { \"State\":\"Boom\", \"Trend\":0 } ] } ], \"SystemFaction\":\"Green Party of Wolf 124\", \"FactionState\":\"Boom\" }";
    private final String DOCKED_EVENT="{ \"timestamp\":\"2017-08-10T14:17:29Z\", \"event\":\"Docked\", \"StationName\":\"Willis Port\", \"StationType\":\"Bernal\", \"StarSystem\":\"Wolf 124\", \"StationFaction\":\"Green Party of Wolf 124\", \"FactionState\":\"Boom\", \"StationGovernment\":\"$government_Democracy;\", \"StationGovernment_Localised\":\"Демократия\", \"StationAllegiance\":\"Federation\", \"StationEconomy\":\"$economy_Industrial;\", \"StationEconomy_Localised\":\"Промышленность\", \"DistFromStarLS\":137.572388 }\n";
    private final String JUMP_EVENT="{ \"timestamp\":\"2017-08-10T14:10:04Z\", \"event\":\"FSDJump\", \"StarSystem\":\"Wolf 124\", \"StarPos\":[-7.250,-27.156,-19.094], \"SystemAllegiance\":\"Federation\", \"SystemEconomy\":\"$economy_Industrial;\", \"SystemEconomy_Localised\":\"Промышленность\", \"SystemGovernment\":\"$government_Democracy;\", \"SystemGovernment_Localised\":\"Демократия\", \"SystemSecurity\":\"$SYSTEM_SECURITY_high;\", \"SystemSecurity_Localised\":\"Высок. ур. безопасности\", \"Powers\":[ \"Zachary Hudson\" ], \"PowerplayState\":\"Exploited\", \"JumpDist\":5.573, \"FuelUsed\":0.232189, \"FuelLevel\":6.765994, \"Factions\":[ { \"Name\":\"Green Party of Wolf 124\", \"FactionState\":\"Boom\", \"Government\":\"Democracy\", \"Influence\":0.481000, \"Allegiance\":\"Federation\" }, { \"Name\":\"LFT 142 Liberals\", \"FactionState\":\"War\", \"Government\":\"Democracy\", \"Influence\":0.023000, \"Allegiance\":\"Federation\", \"PendingStates\":[ { \"State\":\"Boom\", \"Trend\":1 } ] }, { \"Name\":\"League of Wolf 124 Liberty Party\", \"FactionState\":\"War\", \"Government\":\"Dictatorship\", \"Influence\":0.030000, \"Allegiance\":\"Independent\", \"PendingStates\":[ { \"State\":\"Boom\", \"Trend\":0 } ] }, { \"Name\":\"Wolf 124 Jet Life Industries\", \"FactionState\":\"Boom\", \"Government\":\"Corporate\", \"Influence\":0.149000, \"Allegiance\":\"Federation\" }, { \"Name\":\"Wolf 124 Crimson Drug Empire\", \"FactionState\":\"Boom\", \"Government\":\"Anarchy\", \"Influence\":0.076000, \"Allegiance\":\"Independent\" }, { \"Name\":\"Conservatives of Wolf 124\", \"FactionState\":\"Boom\", \"Government\":\"Dictatorship\", \"Influence\":0.093000, \"Allegiance\":\"Independent\" }, { \"Name\":\"Hutton Orbital Truckers Co-Operative\", \"FactionState\":\"None\", \"Government\":\"Cooperative\", \"Influence\":0.148000, \"Allegiance\":\"Independent\", \"PendingStates\":[ { \"State\":\"Outbreak\", \"Trend\":0 } ], \"RecoveringStates\":[ { \"State\":\"Boom\", \"Trend\":0 } ] } ], \"SystemFaction\":\"Green Party of Wolf 124\", \"FactionState\":\"Boom\" }";

    private GalaxyStore galaxy;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        EntityManager manager = EMF.createEntityManager();
        galaxy = new GalaxyStore(manager);
        mapper = new ObjectMapper();
    }

    private JsonNode parse(String json){
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            return null;
        }
    }

    private StartupEvents createStartupEvents(){
        CargoEvent cargoEvent = new CargoEvent(parse(CARGO_EVENT));
        LoadoutEvent loadoutEvent = new LoadoutEvent(parse(LOADOUT_EVENT));
        MaterialsEvent materialsEvent = new MaterialsEvent(parse(MATERIALS_EVENT));
        LoadGameEvent loadGameEvent = new LoadGameEvent(parse(LOADGAME_EVENT));
        RankEvent rankEvent = new RankEvent(parse(RANK_EVENT));
        LocationEvent locationEvent = new LocationEvent(parse(LOCATION_EVENT));
        StartupEvents startupEvents = new StartupEvents();
        startupEvents.init(cargoEvent);
        startupEvents.init(loadoutEvent);
        startupEvents.init(materialsEvent);
        startupEvents.init(loadGameEvent);
        startupEvents.init(rankEvent);
        startupEvents.init(locationEvent);
        return startupEvents;
    }

    private void clear(){
        EntityTransaction transaction = galaxy.startTransaction();
        galaxy.clearCmdrs();
        transaction.commit();
    }

    @Test
    public void testImports() throws Exception {
        testImportStartupEvent();
        clear();
        testDockedEvent();
        clear();
        testFSDJumpEvent();
    }


    public void testImportStartupEvent() throws Exception {
        LOG.info("Testing import startup events");
        EventImporter importer = new EventImporter(galaxy);
        StartupEvents events = createStartupEvents();
        importer.importEvent(events);
        Optional<Commander> cmdr = importer.getImportedCmdr();
        assertTrue(cmdr.isPresent());
        assertCmdr(cmdr.get(), false);
    }

    public void assertCmdr(Commander cmdr, boolean docked){
        assertEquals("MoHax", cmdr.getName());
        assertEquals(348755553, cmdr.getCredits(), 0.0);
        assertEquals(false, cmdr.isDead());

        if (docked){
            assertEquals(false, cmdr.isLanded());
            assertNull(cmdr.getLatitude());
            assertNull(cmdr.getLongitude());
            Body body = cmdr.getBody();
            assertEquals("Willis Port", body.getName());
            assertEquals(BODY_TYPE.STATION, body.getType());
            assertStarSystem2(cmdr.getStarSystem());
            assertNull(cmdr.getStation());
        } else {
            assertEquals(348755553, cmdr.getCredits(), 0.0);
            assertEquals(true, cmdr.isLanded());
            assertEquals(-8.669971, cmdr.getLatitude(), 0.0);
            assertEquals(6.145895, cmdr.getLongitude(), 0.0);
            Body body = cmdr.getBody();
            assertEquals("Pethes 2 b", body.getName());
            assertEquals(BODY_TYPE.PLANET, body.getType());
            assertStarSystem(cmdr.getStarSystem());
            assertNull(cmdr.getStation());
        }
        Ship ship = cmdr.getShip();
        assertShip(ship);
        Collection<Ship> ships = cmdr.getShips();
        assertEquals(1, ships.size());
        assertEquals(7, cmdr.getRank("Combat"));
        assertEquals(7, cmdr.getRank("Trade"));
        assertEquals(5, cmdr.getRank("Explore"));
        assertEquals(5, cmdr.getRank("Empire"));
        assertEquals(9, cmdr.getRank("Federation"));
        assertEquals(0, cmdr.getRank("CQC"));
        Collection<InventoryEntry> inventory = cmdr.getInventory();
        assertEquals(10, inventory.size());
        Item item = galaxy.findItemByName("thermalcoolingunits").get();
        assertEquals(3, cmdr.getCount(item));
        item = galaxy.findItemByName("nickel").get();
        assertEquals(15, cmdr.getCount(item));
        item = galaxy.findItemByName("shieldemitters").get();
        assertEquals(27, cmdr.getCount(item));
        item = galaxy.findItemByName("bulkscandata").get();
        assertEquals(50, cmdr.getCount(item));

    }

    public void assertShip(Ship ship){
        assertEquals(31, ship.getSid());
        assertEquals("Empire_Courier", ship.getType());
        assertEquals("Spacer", ship.getName());
        assertEquals("A42MO", ship.getIdent());
        assertEquals(7, ship.getFuel(), 0);
        assertEquals(8, ship.getTank(), 0);
        Collection<Slot> slots = ship.getSlots();
        assertEquals(25, slots.size());
        Slot slot = ship.getSlot("MediumHardpoint1").get();
        assertEquals(2, slot.getPriority());
        assertEquals(true, slot.isActive());
        Module module = slot.getModule();
        assertEquals("Hpt_BasicMissileRack_Fixed_Medium", module.getName());
        assertEquals(6, module.getAmmoClip(), 0);
        assertEquals(18, module.getAmmoHopper(), 0);
        assertEquals(0.987233, module.getHealth(), 0);
        assertEquals(512400, (long)module.getCost());
        assertNull(module.getBlueprint());
        assertNull(module.getBlueprintLevel());
        slot = ship.getSlot("MainEngines").get();
        assertEquals(0, slot.getPriority());
        assertEquals(false, slot.isActive());
        module = slot.getModule();
        assertEquals("Int_Engine_Size3_Class5_Fast", module.getName());
        assertNull(module.getAmmoClip());
        assertNull(module.getAmmoHopper());
        assertEquals(0.995649, module.getHealth(), 0);
        assertEquals(5103952, (long)module.getCost());
        assertEquals("Engine_Dirty", module.getBlueprint());
        assertEquals(3, (long)module.getBlueprintLevel());
        slot = ship.getSlot("Slot02_Size3").get();
        assertEquals(1, slot.getPriority());
        assertEquals(true, slot.isActive());
        module = slot.getModule();
        assertEquals("Int_CargoRack_Size3_Class1", module.getName());
        assertNull(module.getAmmoClip());
        assertNull(module.getAmmoHopper());
        assertEquals(1, module.getHealth(), 0);
        assertEquals(10563, (long)module.getCost());
        assertNull(module.getBlueprint());
        assertNull(module.getBlueprintLevel());
    }

    private void assertStarSystem(StarSystem starSystem){
        assertEquals("Pethes", starSystem.getName());
        assertEquals(-20.656, starSystem.getX(), 0);
        assertEquals(-32.469, starSystem.getY(), 0);
        assertEquals(-11.906, starSystem.getZ(), 0);
        assertEquals(0, starSystem.getPopulation());
        assertEquals(SECURITY_LEVEL.MEDIUM, starSystem.getSecurity());
        assertEquals(POWER.GROM, starSystem.getPower());
        assertEquals(POWER_STATE.CONTROL, starSystem.getPowerState());
        assertEquals(0, starSystem.getIncome());

        MinorFaction faction = starSystem.getControllingFaction();
        assertEquals("Pethes Purple Fortune Corp.", faction.getName());
        assertEquals(FACTION.FEDERATION, faction.getFaction());
        assertEquals(GOVERNMENT.CORPORATE, faction.getGovernment());

        Collection<Body> bodies = starSystem.getBodies();
        assertEquals(1, bodies.size());

        Collection<MinorFactionState> factions = starSystem.getFactions();
        assertEquals(7, factions.size());
        MinorFactionState state = factions.stream().filter(f -> "Crew of Pethes".equals(f.getFaction().getName())).findAny().get();
        assertEquals(STATE_TYPE.BOOM, state.getState());
        assertEquals(0.112, state.getInfluence(), 0.001);
        List<STATE_TYPE> states = state.getStates(STATE_STATUS.PENDING).collect(Collectors.toList());
        assertEquals(1, states.size());
        assertThat(states, hasItems(STATE_TYPE.FAMINE));
        states = state.getStates(STATE_STATUS.RECOVERY).collect(Collectors.toList());
        assertEquals(0, states.size());
        states = state.getStates(STATE_STATUS.ACTIVE).collect(Collectors.toList());
        assertEquals(1, states.size());
        assertThat(states, hasItems(STATE_TYPE.BOOM));
        faction = state.getFaction();
        assertEquals("Crew of Pethes", faction.getName());
        assertEquals(FACTION.FEDERATION, faction.getFaction());
        assertEquals(GOVERNMENT.ANARCHY, faction.getGovernment());

        state = factions.stream().filter(f -> "Pethes Purple Fortune Corp.".equals(f.getFaction().getName())).findAny().get();
        assertEquals(STATE_TYPE.NONE, state.getState());
        assertEquals(0.371, state.getInfluence(), 0.001);
        states = state.getStates(STATE_STATUS.PENDING).collect(Collectors.toList());
        assertEquals(1, states.size());
        assertThat(states, hasItems(STATE_TYPE.WAR));
        states = state.getStates(STATE_STATUS.RECOVERY).collect(Collectors.toList());
        assertEquals(1, states.size());
        assertThat(states, hasItems(STATE_TYPE.BOOM));
        states = state.getStates(STATE_STATUS.ACTIVE).collect(Collectors.toList());
        assertEquals(1, states.size());
        assertThat(states, hasItems(STATE_TYPE.NONE));
        faction = state.getFaction();
        assertEquals("Pethes Purple Fortune Corp.", faction.getName());
        assertEquals(FACTION.FEDERATION, faction.getFaction());
        assertEquals(GOVERNMENT.CORPORATE, faction.getGovernment());
    }

    private void assertStarSystem2(StarSystem starSystem){
        assertEquals("Wolf 124", starSystem.getName());
        assertEquals(-7.250, starSystem.getX(), 0);
        assertEquals(-27.156, starSystem.getY(), 0);
        assertEquals(-19.094, starSystem.getZ(), 0);
        assertEquals(0, starSystem.getPopulation());
        assertEquals(SECURITY_LEVEL.HIGH, starSystem.getSecurity());
        assertEquals(POWER.HUDSON, starSystem.getPower());
        assertEquals(POWER_STATE.EXPLOITED, starSystem.getPowerState());
        assertEquals(0, starSystem.getIncome());

        MinorFaction faction = starSystem.getControllingFaction();
        assertEquals("Green Party of Wolf 124", faction.getName());
        assertEquals(FACTION.FEDERATION, faction.getFaction());
        assertEquals(GOVERNMENT.DEMOCRACY, faction.getGovernment());

        Collection<Body> bodies = starSystem.getBodies();
        assertEquals(1, bodies.size());

        Collection<MinorFactionState> factions = starSystem.getFactions();
        assertEquals(7, factions.size());
        MinorFactionState state = factions.stream().filter(f -> "Conservatives of Wolf 124".equals(f.getFaction().getName())).findAny().get();
        assertEquals(STATE_TYPE.BOOM, state.getState());
        assertEquals(0.093, state.getInfluence(), 0.001);
        List<STATE_TYPE> states = state.getStates(STATE_STATUS.PENDING).collect(Collectors.toList());
        assertEquals(0, states.size());
        states = state.getStates(STATE_STATUS.RECOVERY).collect(Collectors.toList());
        assertEquals(0, states.size());
        states = state.getStates(STATE_STATUS.ACTIVE).collect(Collectors.toList());
        assertEquals(1, states.size());
        assertThat(states, hasItems(STATE_TYPE.BOOM));
        faction = state.getFaction();
        assertEquals("Conservatives of Wolf 124", faction.getName());
        assertEquals(FACTION.INDEPENDENT, faction.getFaction());
        assertEquals(GOVERNMENT.DICTATORSHIP, faction.getGovernment());

        state = factions.stream().filter(f -> "League of Wolf 124 Liberty Party".equals(f.getFaction().getName())).findAny().get();
        assertEquals(STATE_TYPE.WAR, state.getState());
        assertEquals(0.030, state.getInfluence(), 0.001);
        states = state.getStates(STATE_STATUS.PENDING).collect(Collectors.toList());
        assertEquals(1, states.size());
        assertThat(states, hasItems(STATE_TYPE.BOOM));
        states = state.getStates(STATE_STATUS.RECOVERY).collect(Collectors.toList());
        assertEquals(0, states.size());
        states = state.getStates(STATE_STATUS.ACTIVE).collect(Collectors.toList());
        assertEquals(1, states.size());
        assertThat(states, hasItems(STATE_TYPE.WAR));
        faction = state.getFaction();
        assertEquals("League of Wolf 124 Liberty Party", faction.getName());
        assertEquals(FACTION.INDEPENDENT, faction.getFaction());
        assertEquals(GOVERNMENT.DICTATORSHIP, faction.getGovernment());
    }

    private void assertStation(Station station){
        assertEquals("Willis Port", station.getName());
        assertEquals(STATION_TYPE.OCELLUS_STARPORT, station.getType());
        assertEquals("Wolf 124", station.getStarSystem().getName());
        MinorFaction faction = station.getFaction();
        assertEquals("Green Party of Wolf 124", faction.getName());
        assertEquals(FACTION.FEDERATION, faction.getFaction());
        assertEquals(GOVERNMENT.DEMOCRACY, faction.getGovernment());
        assertEquals(ECONOMIC_TYPE.INDUSTRIAL, station.getEconomic());
        assertEquals(137.572388, station.getDistance(), 0);
    }

    public void testImportEvent() throws Exception {

    }

    public void testDockedEvent() throws Exception {
        LOG.info("Testing import docked event");
        EventImporter importer = new EventImporter(galaxy);
        StartupEvents events = createStartupEvents();
        LoadGameEvent loadGameEvent = new LoadGameEvent(parse(LOADGAME_DOCKED_EVENT));
        events.init(loadGameEvent);
        LocationEvent locationEvent = new LocationEvent(parse(LOCATION_DOCKED_EVENT));
        events.init(locationEvent);
        importer.importEvent(events);
        Optional<Commander> cmdr = importer.getImportedCmdr();
        assertCmdr(cmdr.get(), true);
        DockedEvent dockedEvent = new DockedEvent(parse(DOCKED_EVENT));
        importer.importEvent(dockedEvent);
        assertStation(cmdr.get().getStation());
    }

    public void testFSDJumpEvent() throws Exception {
        LOG.info("Testing import fsd jump event");
        EventImporter importer = new EventImporter(galaxy);
        StartupEvents events = createStartupEvents();
        importer.importEvent(events);
        FSDJumpEvent fsdJumpEvent = new FSDJumpEvent(parse(JUMP_EVENT));
        importer.importEvent(fsdJumpEvent);
        Optional<Commander> cmdr = importer.getImportedCmdr();
        assertStarSystem2(cmdr.get().getStarSystem());
    }

    @After
    public void tearDown() throws Exception {
        galaxy.close();
    }
}
