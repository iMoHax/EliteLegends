package ru.elite.utils.edlog.entities;

import ru.elite.utils.edlog.entities.events.*;

public interface JournalHandler {

    void onEvent(JournalEvent event);

    void jump(FSDJumpEvent event);

    void dock(DockedEvent event);

    void touchdown(TouchdownEvent event);

    void supercruiseExit(SupercruiseExitEvent event);

    void undock(UndockedEvent event);

    void liftoff(LiftoffEvent event);

    void cargo(CargoEvent event);

    void loadout(LoadoutEvent event);

    void materials(MaterialsEvent event);

    void loadGame(LoadGameEvent event);

    void rank(RankEvent event);

    void location(LocationEvent event);
}
