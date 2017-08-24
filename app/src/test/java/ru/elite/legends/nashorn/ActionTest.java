package ru.elite.legends.nashorn;

import org.junit.Test;
import ru.elite.legends.entities.Action;

import javax.script.*;

public class ActionTest extends NashornTest {
    private final static String INIT_SCRIPT =
            "var toDown = {" +
            "    id: \"toDown\"," +
            "    description: \"Спустится по лестнице\"," +
            "    action: function(){" +
            "        executed = true;" +
            "    }" +
            "};" +
            "var result = asAction(toDown)," +
            "    executed = false;";
    private final static String INIT_NOT_ACTIVE_AUTO_SCRIPT =
            "var toUp = {" +
            "    id: \"toUp\"," +
            "    active: false," +
            "    auto: true, " +
            "    description: \"Поднятся по лестнице\"," +
            "    action: function(){" +
            "        executed = true;" +
            "    }" +
            "};" +
            "var result = asAction(toUp)," +
            "    executed = false;";

    @Test
    public void testInit() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        Action action = (Action) bindings.get("result");
        assertNotNull(action);
        assertEquals("toDown", action.getId());
        assertEquals("Спустится по лестнице", action.getDescription());
        assertEquals(true, action.isActive());
        assertEquals(false, action.isAuto());
    }

    @Test
    public void testActiveChange() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        Action action = (Action) bindings.get("result");
        assertNotNull(action);
        assertEquals(true, action.isActive());
        action.setActive(false);
        assertEquals(false, action.isActive());
    }

    @Test
    public void testOnAction() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        Action action = (Action) bindings.get("result");
        boolean isExecuted = (boolean)bindings.get("executed");
        assertNotNull(action);
        assertFalse(isExecuted);
        action.action();
        isExecuted = (boolean)bindings.get("executed");
        assertTrue(isExecuted);
    }

    @Test
    public void testNoActiveInit() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_NOT_ACTIVE_AUTO_SCRIPT, bindings);
        Action action = (Action) bindings.get("result");
        assertNotNull(action);
        assertEquals("toUp", action.getId());
        assertEquals("Поднятся по лестнице", action.getDescription());
        assertEquals(false, action.isActive());
        assertEquals(true, action.isAuto());
    }

}
