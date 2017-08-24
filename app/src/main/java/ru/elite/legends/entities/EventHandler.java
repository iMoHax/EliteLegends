package ru.elite.legends.entities;

public interface EventHandler {
    String getId();

    EVENT_TYPE getType();

    boolean isActive();
    void setActive(boolean active);

    void action();

}
