package ru.elite.legends.nashorn;

import org.junit.Assert;
import org.junit.Before;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;

public class NashornTest extends Assert {
    protected ScriptEngine engine;

    @Before
    public void setUp() throws Exception {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new InputStreamReader(NashornTest.class.getResourceAsStream("/js/mustache.min.js")));
        engine.eval(new InputStreamReader(NashornTest.class.getResourceAsStream("/js/legends-base.js")));
    }

    protected Bindings createGlobalBindings(){
        Bindings bindings = engine.createBindings();
        bindings.putAll(engine.getBindings(ScriptContext.ENGINE_SCOPE));
        return bindings;
    }

}
