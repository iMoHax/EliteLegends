package ru.elite.legends.nashorn;

import org.junit.Test;
import ru.elite.legends.entities.Action;
import ru.elite.legends.entities.EventHandler;
import ru.elite.legends.entities.QUEST_STATUS;
import ru.elite.legends.entities.Stage;

import javax.script.Bindings;
import java.util.Collection;
import java.util.Optional;

public class StageTest extends NashornTest {
    private final static String INIT_SCRIPT =
            "var start = {\n" +
            "    id: \"s1\",\n" +
            "    text: \"<p>Вы находитесь в центре комнаты, перед вами три двери в какую пойдете?</p>\"\n" +
            "};"+
            "var stage2 = {\n" +
            "    id: \"s2\",\n" +
            "    text: \"<p>Вы находитесь в центре комнаты, перед вами три двери в какую пойдете?</p>\"\n" +
            "};"+
            "var stage3 = {\n" +
            "    id: \"s3\",\n" +
            "    text: \"<p>Вы находитесь в центре комнаты, перед вами три двери в какую пойдете?</p>\"\n" +
            "};"+
            "var toUp = {\n" +
            "    id: \"toUp\",\n" +
            "    description: \"Поднятся по лестнице\",\n" +
            "    action: function(){\n" +
            "        executed = true;\n" +
            "    }\n" +
            "};\n" +
            "var toDown = {\n" +
            "    id: \"toDown\",\n" +
            "    description: \"Спустится по лестнице\",\n" +
            "    action: function(){\n" +
            "        executed = true;\n" +
            "    }\n" +
            "};\n" +
            "var toStart = {\n" +
            "    id: \"toStart\",\n" +
            "    description: \"Вернутся в начало\",\n" +
            "    action: function(){\n" +
            "        executed = true;\n" +
            "    }\n" +
            "};\n" +
            "var jumpTo = {" +
            "    id: \"jumpTo\"," +
            "    type: \"JUMP\"," +
            "    action: function(){" +
            "        executed = true;" +
            "    }" +
            "};" +
            "var docked = {" +
            "    id: \"docked\"," +
            "    active: false," +
            "    type: \"DOCKED\"," +
            "    action: function(){" +
            "        executed = true;" +
            "    }" +
            "};" +
            "start.actions = asActions([toUp, toDown, toStart]);" +
            "start.events = asEvents([jumpTo, docked]);" +
            "stage2.actions = asActions([toUp, toDown, toStart]);" +
            "stage3.events = asEvents([jumpTo, docked]);" +
            "var result = asStage(start)," +
            "    result2 = asStage(stage2)," +
            "    result3 = asStage(stage3)," +
            "    executed = false;";

    @Test
    public void testInit() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        Stage stage = (Stage) bindings.get("result");
        assertNotNull(stage);
        assertEquals("s1", stage.getId());
        assertEquals("<p>Вы находитесь в центре комнаты, перед вами три двери в какую пойдете?</p>", stage.getText());
        Collection<Action> actions = stage.getActions();
        assertEquals(3, actions.size());
        Collection<EventHandler> events = stage.getEvents();
        assertEquals(2, events.size());
    }

    @Test
    public void testNoEvents() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        Stage stage = (Stage) bindings.get("result2");
        assertNotNull(stage);
        assertEquals("s2", stage.getId());
        Collection<Action> actions = stage.getActions();
        assertEquals(3, actions.size());
        Collection<EventHandler> events = stage.getEvents();
        assertEquals(0, events.size());
    }

    @Test
    public void testNoActions() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        Stage stage = (Stage) bindings.get("result3");
        assertNotNull(stage);
        assertEquals("s3", stage.getId());
        Collection<Action> actions = stage.getActions();
        assertEquals(0, actions.size());
        Collection<EventHandler> events = stage.getEvents();
        assertEquals(2, events.size());
    }

    @Test
    public void testChangeStatus() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(INIT_SCRIPT, bindings);
        Stage stage = (Stage) bindings.get("result");
        assertNotNull(stage);
        assertEquals(QUEST_STATUS.NONE, stage.getStatus());
        stage.setStatus(QUEST_STATUS.COMPLETE);
        assertEquals(QUEST_STATUS.COMPLETE, stage.getStatus());
    }

}
