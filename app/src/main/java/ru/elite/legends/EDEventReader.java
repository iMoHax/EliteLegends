package ru.elite.legends;

import ru.elite.legends.controllers.EventsManager;
import ru.elite.legends.entities.EVENT_TYPE;
import ru.elite.utils.edlog.EDJournalReader;
import ru.elite.utils.edlog.entities.events.DockedEvent;
import ru.elite.utils.edlog.entities.events.FSDJumpEvent;

public class EDEventReader extends EDJournalReader {
    private final EventsManager manager;

    public EDEventReader(EventsManager manager) {
        this.manager = manager;
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
