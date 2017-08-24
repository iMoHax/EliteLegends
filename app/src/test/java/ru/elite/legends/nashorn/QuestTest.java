package ru.elite.legends.nashorn;

import org.junit.Test;
import ru.elite.legends.entities.*;

import javax.script.Bindings;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;

public class QuestTest extends NashornTest {

    @Test
    public void testInit() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(new InputStreamReader(ActionTest.class.getResourceAsStream("/quest1.js"), "utf-8"), bindings);
        Quest quest = (Quest) bindings.get("result");
        assertNotNull(quest);
        assertEquals("q1", quest.getId());
        assertEquals("Тайная комната", quest.getCaption());
        assertEquals("Вы попали в закрытую комнату, вам нужно выбратся из нее", quest.getDescription());
        assertEquals(QUEST_STATUS.NONE, quest.getStatus());
        assertEquals("s1", quest.getStage().getId());
        assertEquals(QUEST_STATUS.ACTIVE, quest.getStage().getStatus());
        Collection<Stage> stages = quest.getStages();
        assertEquals(6, stages.size());
    }

    @Test
    public void testActiveStatus() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(new InputStreamReader(ActionTest.class.getResourceAsStream("/quest1.js")), bindings);
        Quest quest = (Quest) bindings.get("result2");
        assertNotNull(quest);
        assertEquals(QUEST_STATUS.ACTIVE, quest.getStatus());
    }

    @Test
    public void testChangeStatus() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(new InputStreamReader(ActionTest.class.getResourceAsStream("/quest1.js")), bindings);
        Quest quest = (Quest) bindings.get("result");
        assertNotNull(quest);
        assertEquals(QUEST_STATUS.NONE, quest.getStatus());
        quest.setStatus(QUEST_STATUS.COMPLETE);
        assertEquals(QUEST_STATUS.COMPLETE, quest.getStatus());
    }

    @Test
    public void testChangeStage() throws Exception {
        Bindings bindings = createGlobalBindings();
        engine.eval(new InputStreamReader(ActionTest.class.getResourceAsStream("/quest1.js")), bindings);
        Quest quest = (Quest) bindings.get("result");
        assertNotNull(quest);
        assertEquals("s1", quest.getStage().getId());
        Optional<Stage> stage = quest.getStageById("s3");
        assertTrue(stage.isPresent());
        quest.setStage(stage.get());
        assertEquals(stage.get(), quest.getStage());

    }

}
