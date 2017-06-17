package ru.elite.legends.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Stage {
    private final String id;

    private String text;
    private Collection<Action> actions;
    private QUEST_STATUS status;

    public Stage(String id) {
        this.id = id;
        actions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Collection<Action> getActions() {
        return actions;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public QUEST_STATUS getStatus() {
        return status;
    }

    public void setStatus(QUEST_STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stage)) return false;
        Stage stage = (Stage) o;
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
