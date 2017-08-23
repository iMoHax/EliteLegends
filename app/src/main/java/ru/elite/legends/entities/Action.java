package ru.elite.legends.entities;

public interface Action {
    void action();

    String getId();

    String getDescription();

    boolean isActive();
    void setActive(boolean active);

    boolean isAuto();
}
