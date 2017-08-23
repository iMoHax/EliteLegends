package ru.elite.legends.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class StageImpl implements Stage {
    private final String id;

    private String text;
    private final Collection<Action> actions;
    private final Collection<EventHandler> events;
    private QUEST_STATUS status;

    public StageImpl(String id) {
        this.id = id;
        actions = new ArrayList<>();
        events = new ArrayList<>();
        status = QUEST_STATUS.NONE;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Collection<Action> getActions() {
        return actions;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    @Override
    public Collection<EventHandler> getEvents() {
        return events;
    }

    public void addEvent(EventHandler event) {
        events.add(event);
    }

    public void removeEvent(EventHandler event) {
        events.remove(event);
    }

    @Override
    public QUEST_STATUS getStatus() {
        return status;
    }

    @Override
    public void setStatus(QUEST_STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StageImpl)) return false;
        StageImpl stage = (StageImpl) o;
        return Objects.equals(id, stage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Stage{" +
                "id='" + id + '\'' +
                '}';
    }
}
