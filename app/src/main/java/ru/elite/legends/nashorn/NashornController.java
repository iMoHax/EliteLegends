package ru.elite.legends.nashorn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.Commander;
import ru.elite.legends.controllers.QuestsManager;

import javax.script.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class NashornController {
    private final static Logger LOG = LoggerFactory.getLogger(NashornController.class);
    private final ScriptEngine engine;
    private Bindings globalBindings;

    public NashornController() throws ScriptException {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        globalBindings = engine.createBindings();
        engine.setBindings(globalBindings, ScriptContext.GLOBAL_SCOPE);
        injectLibs();
    }

    private void injectLibs()  throws ScriptException {
        engine.eval(new InputStreamReader(NashornController.class.getResourceAsStream("/js/legends-base.js")), globalBindings);
    }

    public void init(QuestsManager manager, Commander commander){
        LOG.info("Init bindings");
        globalBindings.put("manager", manager);
        globalBindings.put("cmdr", commander);
    }

    public void setCmdr(Commander cmdr){
        LOG.info("Change cmdr to {}", cmdr);
        globalBindings.put("cmdr", cmdr);
    }

    public Bindings createBindings(){
        return engine.createBindings();
    }

    public void load(File file, Bindings bindings) throws FileNotFoundException, ScriptException {
        LOG.info("Load script {}", file);
        engine.eval(new FileReader(file), bindings);
    }

    public void eval(String script, Bindings bindings) throws ScriptException {
        LOG.info("Execute script: {}", script);
        engine.eval(script, bindings);
    }

}
