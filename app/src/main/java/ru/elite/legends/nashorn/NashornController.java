package ru.elite.legends.nashorn;

import ru.elite.entity.Commander;
import ru.elite.legends.controllers.QuestsManager;

import javax.script.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class NashornController {
    private final ScriptEngine engine;

    public NashornController() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
    }

    public ScriptContext createContext(QuestsManager manager, Commander commander){
        ScriptContext context = new SimpleScriptContext();
        context.setAttribute("manager", manager, ScriptContext.GLOBAL_SCOPE);
        context.setAttribute("cmdr", commander, ScriptContext.GLOBAL_SCOPE);
        return context;
    }

    public void load(String filename, ScriptContext context) throws FileNotFoundException, ScriptException {
        engine.eval(new FileReader(filename), context);
    }

    public void eval(){
        Invocable invocable = (Invocable) engine;
    }
}
