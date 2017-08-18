package ru.elite.utils.edlog.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.utils.edlog.EventImporter;
import ru.elite.utils.edlog.entities.events.*;

public class JournalImportHandler implements JournalHandler {
    private final static Logger LOG = LoggerFactory.getLogger(JournalImportHandler.class);
    private final EventImporter importer;
    private StartupEvents startupEvents;

    public JournalImportHandler(EventImporter importer) {
        this.importer = importer;
    }

    @Override
    public void onEvent(JournalEvent event) {
    }

    @Override
    public void jump(FSDJumpEvent event) {
        importer.importEvent(event);
    }

    @Override
    public void dock(DockedEvent event) {
        importer.importEvent(event);
    }

    @Override
    public void touchdown(TouchdownEvent event) {
        importer.importEvent(event);
    }

    @Override
    public void supercruiseExit(SupercruiseExitEvent event) {
        importer.importEvent(event);
    }

    @Override
    public void undock(UndockedEvent event) {
        importer.importEvent(event);
    }

    @Override
    public void liftoff(LiftoffEvent event) {
        importer.importEvent(event);
    }

    @Override
    public void cargo(CargoEvent event) {
        startupEvents = new StartupEvents();
        startupEvents.init(event);
    }

    @Override
    public void loadout(LoadoutEvent event) {
        if (startupEvents == null){
            LOG.warn("Startup events not initialised");
        } else {
            startupEvents.init(event);
        }
    }

    @Override
    public void materials(MaterialsEvent event) {
        if (startupEvents == null){
            LOG.warn("Startup events not initialised");
        } else {
            startupEvents.init(event);
        }
    }

    @Override
    public void loadGame(LoadGameEvent event) {
        if (startupEvents == null){
            LOG.warn("Startup events not initialised");
        } else {
            startupEvents.init(event);
        }
    }

    @Override
    public void rank(RankEvent event) {
        if (startupEvents == null){
            LOG.warn("Startup events not initialised");
        } else {
            startupEvents.init(event);
        }
    }

    @Override
    public void location(LocationEvent event) {
        if (startupEvents == null){
            LOG.warn("Startup events not initialised");
        } else {
            startupEvents.init(event);
            if (startupEvents.needInit()){
                LOG.warn("Startup events initialise not complete");
            } else {
                importer.importEvent(startupEvents);
                startupEvents = null;
            }
        }
    }
}
