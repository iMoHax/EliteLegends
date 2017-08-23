package ru.elite.legends.entities;

import java.util.Collection;

public interface Stage {
    String getId();

    String getText();

    Collection<Action> getActions();

    Collection<EventHandler> getEvents();

    QUEST_STATUS getStatus();

    void setStatus(QUEST_STATUS status);
}
