package ru.elite.legends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.legends.nashorn.NashornController;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

public class QuestsLoader {
    private final static Logger LOG = LoggerFactory.getLogger(QuestsLoader.class);
    private final NashornController scriptController;

    public QuestsLoader(NashornController scriptController) {
        this.scriptController = scriptController;
    }

    public void load(String dir){
        LOG.info("Load quest from dir {}", dir);
        File file = new File(dir);
        if (!file.isDirectory()){
            LOG.error("{} is not dir", dir);
            return;
        }
        File[] files = file.listFiles(File::isDirectory);
        if(files == null){
            LOG.error("Not found quests in dir {}", file);
            return;
        }
        for (File questDir : files) {
            loadQuest(questDir);
        }
    }

    private void loadQuest(File dir){
        FilenameFilter filter = (f, name) -> name.matches("^quest.*\\.js$");
        File[] files = dir.listFiles(filter);
        if (files == null){
            LOG.warn("Not found quest.js file in dir {}", dir);
            return;
        }
        Bindings bindings = scriptController.createBindings();
        for (File file : files) {
            try {
                scriptController.load(file, bindings);
            } catch (FileNotFoundException | ScriptException e) {
                LOG.error("Error on load quest, file: {}", file);
                LOG.error("", e);
            }
        }
    }
}
