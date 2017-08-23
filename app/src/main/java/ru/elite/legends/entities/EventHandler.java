package ru.elite.legends.entities;

public interface EventHandler {
    void action();

    String getId();

    EVENT_TYPE getType();

    boolean isActive();
    void setActive(boolean active);
}
