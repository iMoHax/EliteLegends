package ru.elite.legends.nashorn;

import org.junit.Test;
import ru.elite.core.BODY_TYPE;
import ru.elite.core.STATION_TYPE;
import ru.elite.entity.Body;
import ru.elite.entity.Commander;
import ru.elite.entity.StarSystem;
import ru.elite.entity.Station;
import ru.elite.store.jpa.entity.CommanderImpl;
import ru.elite.store.jpa.entity.StarSystemImpl;

import javax.script.Bindings;

public class HelperTest extends NashornTest {

    @Test
    public void testSystemCheck() throws Exception {
        Commander cmdr = new CommanderImpl("Mo");
        StarSystem starSystem = new StarSystemImpl("Sol", 0, 0, 0);
        StarSystem starSystem2 = new StarSystemImpl("HR 477", 4, 2, 3);
        final String IS_SYSTEM_SCRIPT = "var result = helper.isSystem('Sol');";
        Bindings bindings = createGlobalBindings();
        engine.put("cmdr", cmdr);

        engine.eval(IS_SYSTEM_SCRIPT, bindings);
        boolean result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setStarSystem(starSystem);
        engine.eval(IS_SYSTEM_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertTrue(result);

        cmdr.setStarSystem(starSystem2);
        engine.eval(IS_SYSTEM_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);
    }

    @Test
    public void testDockedCheck() throws Exception {
        Commander cmdr = new CommanderImpl("Mo");
        StarSystem starSystem = new StarSystemImpl("Sol", 0, 0, 0);
        StarSystem starSystem2 = new StarSystemImpl("HR 477", 4, 2, 3);
        Station station = starSystem.addStation("Gagarin Terminal", STATION_TYPE.OUTPOST, 4);
        Station station2 = starSystem.addStation("Korolev Dock", STATION_TYPE.CIVILIAN_OUTPOST, 3);
        Station station3 = starSystem2.addStation("Gagarin Terminal", STATION_TYPE.OUTPOST, 4);

        final String IS_DOCKED_SCRIPT = "var result = helper.isDocked('Sol','Gagarin Terminal');";
        Bindings bindings = createGlobalBindings();
        engine.put("cmdr", cmdr);

        engine.eval(IS_DOCKED_SCRIPT, bindings);
        boolean result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setStarSystem(starSystem);
        engine.eval(IS_DOCKED_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setStation(station);
        engine.eval(IS_DOCKED_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertTrue(result);

        cmdr.setStation(station2);
        engine.eval(IS_DOCKED_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setStarSystem(starSystem2);
        engine.eval(IS_DOCKED_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setStation(station3);
        engine.eval(IS_DOCKED_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

    }

    @Test
    public void testLandingCheck() throws Exception {
        Commander cmdr = new CommanderImpl("Mo");
        StarSystem starSystem = new StarSystemImpl("Sol", 0, 0, 0);
        StarSystem starSystem2 = new StarSystemImpl("HR 477", 4, 2, 3);
        Body body = starSystem.addBody("Earth", BODY_TYPE.PLANET);
        Body body2 = starSystem.addBody("Pluto", BODY_TYPE.PLANET);
        Body body3 = starSystem2.addBody("Earth", BODY_TYPE.PLANET);

        final String IS_LANDING_SCRIPT = "var result = helper.isLanded('Sol','Earth',51.8828812,128.3322449,0.1);";
        Bindings bindings = createGlobalBindings();
        engine.put("cmdr", cmdr);

        engine.eval(IS_LANDING_SCRIPT, bindings);
        boolean result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setStarSystem(starSystem);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setBody(body);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setLatitude(51.8828812);
        cmdr.setLongitude(128.3322449);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setLanded(true);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertTrue(result);

        cmdr.setLatitude(51.88);
        cmdr.setLongitude(128.33);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertTrue(result);

        cmdr.setLatitude(51.88);
        cmdr.setLongitude(129);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setLatitude(52);
        cmdr.setLongitude(128.33);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setStarSystem(starSystem);
        cmdr.setBody(body2);
        cmdr.setLanded(true);
        cmdr.setLatitude(51.8828812);
        cmdr.setLongitude(128.3322449);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);

        cmdr.setStarSystem(starSystem2);
        cmdr.setBody(body3);
        cmdr.setLanded(true);
        cmdr.setLatitude(51.8828812);
        cmdr.setLongitude(128.3322449);
        engine.eval(IS_LANDING_SCRIPT, bindings);
        result = (boolean) bindings.get("result");
        assertFalse(result);
    }


}
