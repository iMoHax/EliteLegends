package ru.elite.legends.nashorn;

import org.junit.Test;
import ru.elite.legends.entities.EVENT_TYPE;
import ru.elite.legends.entities.EventHandler;

import javax.script.Bindings;

public class EventTest extends NashornTest {
    private final static String INIT_SCRIPT =
            "var jumpTo = {" +
            "    id: \"jumpTo\"," +
            "    type: \"JUMP\"," +
            "    action: function(){" +
            "        executed = true;" +
            "    }" +
            "};" +
            "var result = asEvent(jumpTo)," +
            "    executed = false;";
    private final static String INIT_NOT_ACTIVE =
            "var docked = {" +
            "    id: \"docked\"," +
            "    active: false," +
            "    type: \"DOCKED\"," +
            "    action: function(){" +
            "        executed = true;" +
            "    }" +
            "};" +
            "var result = asEvent(docked)," +
            "    executed = false;";

    @Test
    public void testInit() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        EventHandler eventHandler = (EventHandler) bindings.get("result");
        assertNotNull(eventHandler);
        assertEquals("jumpTo", eventHandler.getId());
        assertEquals(EVENT_TYPE.JUMP, eventHandler.getType());
        assertEquals(true, eventHandler.isActive());
    }

    @Test
    public void testActiveChange() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        EventHandler eventHandler = (EventHandler) bindings.get("result");
        assertNotNull(eventHandler);
        assertEquals(true, eventHandler.isActive());
        eventHandler.setActive(false);
        assertEquals(false, eventHandler.isActive());
    }

    @Test
    public void testOnAction() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        EventHandler eventHandler = (EventHandler) bindings.get("result");
        boolean isExecuted = (boolean)bindings.get("executed");
        assertFalse(isExecuted);
        eventHandler.action();
        isExecuted = (boolean)bindings.get("executed");
        assertTrue(isExecuted);
    }

    @Test
    public void testNoActiveInit() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_NOT_ACTIVE, bindings);
        EventHandler eventHandler = (EventHandler) bindings.get("result");
        assertNotNull(eventHandler);
        assertEquals("docked", eventHandler.getId());
        assertEquals(EVENT_TYPE.DOCKED, eventHandler.getType());
        assertEquals(false, eventHandler.isActive());
    }

}
