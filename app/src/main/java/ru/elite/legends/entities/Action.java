package ru.elite.legends.entities;

public abstract class Action {
    private final String id;
    private final String description;
    private boolean active;

    public Action(String id, String description) {
        this.id = id;
        this.description = description;
        this.active = true;
    }

    public abstract void complete();

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }
}
