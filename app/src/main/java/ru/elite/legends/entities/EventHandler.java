package ru.elite.legends.entities;

public abstract class EventHandler {
    private final String id;
    private final EVENT_TYPE type;
    private boolean active;

    protected EventHandler(String id, EVENT_TYPE type) {
        this.id = id;
        this.type = type;
        this.active = true;
    }

    public abstract void handle();

    public String getId() {
        return id;
    }

    public EVENT_TYPE getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "EventHandler{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", active=" + active +
                '}';
    }
}
