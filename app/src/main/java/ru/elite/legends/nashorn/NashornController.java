package ru.elite.legends.nashorn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.Commander;
import ru.elite.legends.controllers.QuestsManager;

import javax.script.*;
import java.io.*;
import java.util.Locale;

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
        engine.eval(new InputStreamReader(NashornController.class.getResourceAsStream("/js/mustache.min.js")), globalBindings);
    }

    public void init(QuestsManager manager, Commander commander){
        LOG.info("Init bindings");
        globalBindings.put("manager", manager);
        setCmdr(commander);
        setLocale(Locale.getDefault());
    }

    public void setCmdr(Commander cmdr){
        LOG.info("Change cmdr to {}", cmdr);
        globalBindings.put("___cmdr", cmdr);
        try {
            engine.eval("context.cmdr = ___cmdr", globalBindings);
        } catch (ScriptException e) {
            LOG.error("Error on change context:", e);
        }
    }

    public void setLocale(Locale locale){
        LOG.info("Change locale to {}", locale);
        globalBindings.put("___locale", locale);
        try {
            engine.eval("context.locale = ___locale", globalBindings);
        } catch (ScriptException e) {
            LOG.error("Error on change context:", e);
        }
    }

    public Bindings createBindings(){
        return engine.createBindings();
    }

    public void load(File file, Bindings bindings) throws FileNotFoundException, ScriptException {
        LOG.info("Load script {}", file);
        load(new FileReader(file), bindings);
    }

    public void load(Reader reader, Bindings bindings) throws FileNotFoundException, ScriptException {
        engine.eval(reader, bindings);
    }

    public void eval(String script, Bindings bindings) throws ScriptException {
        LOG.info("Execute script: {}", script);
        engine.eval(script, bindings);
    }

}
