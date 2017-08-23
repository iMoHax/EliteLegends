package ru.elite.legends.entities;

public abstract class EventHandlerImpl implements EventHandler {
    private final String id;
    private final EVENT_TYPE type;
    private boolean active;

    protected EventHandlerImpl(String id, EVENT_TYPE type) {
        this.id = id;
        this.type = type;
        this.active = true;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public EVENT_TYPE getType() {
        return type;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
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
