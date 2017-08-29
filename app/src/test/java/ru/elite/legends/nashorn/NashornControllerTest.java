package ru.elite.legends.nashorn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.elite.entity.Commander;
import ru.elite.legends.controllers.QuestsManager;
import ru.elite.legends.entities.Quest;
import ru.elite.legends.entities.Stage;
import ru.elite.store.jpa.entity.CommanderImpl;
import ru.elite.store.jpa.entity.StarSystemImpl;

import javax.script.Bindings;
import java.io.InputStreamReader;
import java.util.Locale;

public class NashornControllerTest extends Assert {
    private QuestsManager manager;
    private NashornController controller;

    @Before
    public void setUp() throws Exception {
        manager = new QuestsManager();
        controller = new NashornController();
        controller.init(manager, new TestCommander("Mo"));
    }


    @Test
    public void testMustache() throws Exception {
        Commander cmdr = new TestCommander("Mo");
        cmdr.setStarSystem(new TestStarSystem("Pethes",0,0,0));
        controller.setCmdr(cmdr);
        Bindings bindings = controller.createBindings();
        controller.load(new InputStreamReader(NashornControllerTest.class.getResourceAsStream("/quest_mush.js")), bindings);
        Quest quest = (Quest) bindings.get("result");
        assertEquals("Добро пожаловать Mo", quest.getCaption());
        assertEquals("Совершите патрулирование вокруг системы Pethes", quest.getDescription());
        Stage stage = quest.getStage();
        assertEquals("<p>Приветствую тебя пилот Mo. Первая твоя задача совершить патрулирования системы Pethes."+
                     "В случае обнаружения угрозы, доложить и приступить к ликвидации немендленно. Все ясно коммандир?</p>", stage.getText());
    }

    @Test
    public void testChangeCmdr() throws Exception {
        Bindings bindings = controller.createBindings();
        controller.load(new InputStreamReader(NashornControllerTest.class.getResourceAsStream("/quest_mush.js")), bindings);
        Quest quest = (Quest) bindings.get("result");
        assertEquals("Добро пожаловать Mo", quest.getCaption());
        controller.setCmdr(new TestCommander("Hax"));
        assertEquals("Добро пожаловать Hax", quest.getCaption());

    }

    @Test
    public void testChangeSystem() throws Exception {
        Commander cmdr = new TestCommander("Mo");
        cmdr.setStarSystem(new TestStarSystem("Pethes",0,0,0));
        controller.setCmdr(cmdr);
        Bindings bindings = controller.createBindings();
        controller.load(new InputStreamReader(NashornControllerTest.class.getResourceAsStream("/quest_mush.js")), bindings);
        Quest quest = (Quest) bindings.get("result");
        assertEquals("Совершите патрулирование вокруг системы Pethes", quest.getDescription());
        cmdr.setStarSystem(new TestStarSystem("Euryale",1,1,1));
        assertEquals("Совершите патрулирование вокруг системы Euryale", quest.getDescription());

    }

    @Test
    public void testChangeLocale() throws Exception {
        Bindings bindings = controller.createBindings();
        controller.load(new InputStreamReader(NashornControllerTest.class.getResourceAsStream("/lang_test/quest.js")), bindings);
        Quest quest = (Quest) bindings.get("result");
        assertEquals("Добро пожаловать Mo", quest.getCaption());
        controller.setLocale(Locale.ENGLISH);
        controller.load(new InputStreamReader(NashornControllerTest.class.getResourceAsStream("/lang_test/quest.js")), bindings);
        quest = (Quest) bindings.get("result");
        assertEquals("Welcome Mo", quest.getCaption());

    }


    public static class TestCommander extends CommanderImpl {
        public TestCommander(String name) {
            super(name);
        }
    }

    public static class TestStarSystem extends StarSystemImpl {
        public TestStarSystem(String name, double x, double y, double z) {
            super(name, x, y, z);
        }
    }
}
