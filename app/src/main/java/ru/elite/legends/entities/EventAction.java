package ru.elite.legends.entities;

public abstract class EventAction extends Action {
    private final EVENT_TYPE type;

    protected EventAction(EVENT_TYPE type, String description) {
        super(description);
        this.type = type;
    }
}
