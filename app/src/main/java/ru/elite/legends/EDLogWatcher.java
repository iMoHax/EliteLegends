package ru.elite.legends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.utils.edlog.LogWatcher;

import java.io.File;
import java.io.IOException;

public class EDLogWatcher {
    private final static Logger LOG = LoggerFactory.getLogger(EDLogWatcher.class);
    private final static String LOG_DIR = System.getProperty("user.home") + File.separator + "Saved Games" + File.separator + "Frontier Developments" + File.separator + "Elite Dangerous";

    private final LogWatcher watcher;


    public EDLogWatcher(EDLogHandler handler) {
        this.watcher = new LogWatcher(handler);
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


}
