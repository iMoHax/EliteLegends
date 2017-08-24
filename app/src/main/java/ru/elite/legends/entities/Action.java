package ru.elite.legends.entities;

public interface Action {
    String getId();

    String getDescription();

    boolean isActive();
    void setActive(boolean active);

    boolean isAuto();

    void action();

}
