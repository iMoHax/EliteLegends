package ru.elite.legends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.legends.controllers.EventsManager;
import ru.elite.legends.entities.EVENT_TYPE;
import ru.elite.utils.edlog.EDJournalReader;
import ru.elite.utils.edlog.LogWatcher;
import ru.elite.utils.edlog.entities.DockedEvent;
import ru.elite.utils.edlog.entities.FSDJumpEvent;

import java.io.File;
import java.io.IOException;

public class EDLogWatcher {
    private final static Logger LOG = LoggerFactory.getLogger(EDLogWatcher.class);
    private final static String LOG_DIR = System.getProperty("user.home") + File.separator + "Saved Games" + File.separator + "Frontier Developments" + File.separator + "Elite Dangerous";

    private final EventsManager manager;
    private final LogWatcher watcher;


    public EDLogWatcher(EventsManager manager) {
        this.manager = manager;
        this.watcher = new LogWatcher(new EDLogHandler());
    }

    public boolean isActive(){
        return watcher.isRun();
    }

    public boolean run(){
        LOG.info("Start ED log watcher, log dir {}", LOG_DIR);
        try {
            File dir = new File(LOG_DIR);
            if (dir.exists()){
                watcher.start(dir.toPath());
                return true;
            } else {
                LOG.error("Log dir not found");
            }
        } catch (IOException e) {
            LOG.error("Error on start log watcher", e);
        }
        return false;
    }

    public boolean stop(){
        LOG.info("Stop ED log watcher");
        watcher.stop();
        return true;
    }

    public void restart(){
        stop();
        run();
    }

    public void shutdown(){
        LOG.debug("Shutdown ED log watcher");
        stop();
    }

    private class EDLogHandler extends EDJournalReader {

        private EDLogHandler() {
        }

        @Override
        protected void docked(DockedEvent dockedEvent) {
            super.docked(dockedEvent);
            manager.fireEvent(EVENT_TYPE.DOCKED);
        }

        @Override
        protected void jump(FSDJumpEvent jumpEvent) {
            super.jump(jumpEvent);
            manager.fireEvent(EVENT_TYPE.JUMP);
        }
    }


}
