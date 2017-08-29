package ru.elite.legends;

import ru.elite.entity.Commander;
import ru.elite.legends.controllers.EventsManager;
import ru.elite.legends.entities.EVENT_TYPE;
import ru.elite.store.GalaxyService;
import ru.elite.utils.edlog.EDJournalReader;
import ru.elite.utils.edlog.EventImporter;
import ru.elite.utils.edlog.entities.JournalImportHandler;
import ru.elite.utils.edlog.entities.events.*;

import java.util.Optional;

public class EDLogHandler extends EDJournalReader {
    private final EventsManager manager;
    private final EventImporter importer;

    public EDLogHandler(GalaxyService galaxy, EventsManager manager) {
        super();
        importer = new EventImporter(galaxy);
        this.manager = manager;
        setSkipOld(false);
        setHandler(new EventHandler(importer));
    }


    private class EventHandler extends JournalImportHandler {
        public EventHandler(EventImporter importer) {
            super(importer);
        }

        @Override
        public void onEvent(JournalEvent event) {
            super.onEvent(event);
        }

        @Override
        public void jump(FSDJumpEvent event) {
            super.jump(event);
            manager.fireEvent(EVENT_TYPE.JUMP);
        }

        @Override
        public void dock(DockedEvent event) {
            super.dock(event);
            manager.fireEvent(EVENT_TYPE.DOCKED);
        }

        @Override
        public void touchdown(TouchdownEvent event) {
            super.touchdown(event);
        }

        @Override
        public void supercruiseExit(SupercruiseExitEvent event) {
            super.supercruiseExit(event);
        }

        @Override
        public void undock(UndockedEvent event) {
            super.undock(event);
        }

        @Override
        public void liftoff(LiftoffEvent event) {
            super.liftoff(event);
        }

        @Override
        public void cargo(CargoEvent event) {
            super.cargo(event);
        }

        @Override
        public void loadout(LoadoutEvent event) {
            super.loadout(event);
        }

        @Override
        public void materials(MaterialsEvent event) {
            super.materials(event);
        }

        @Override
        public void loadGame(LoadGameEvent event) {
            super.loadGame(event);
        }

        @Override
        public void rank(RankEvent event) {
            super.rank(event);
        }

        @Override
        public void location(LocationEvent event) {
            super.location(event);
            Optional<Commander> cmdr = importer.getImportedCmdr();
            cmdr.ifPresent(Main::updateCmdr);
        }
    }
}
