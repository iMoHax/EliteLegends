package ru.elite.legends.entities;

public abstract class Action {
    private final String description;

    public Action(String description) {
        this.description = description;
    }

    public abstract void complete();

    public String getDescription() {
        return description;
    }

    public boolean isActive(){
        return true;
    }

    @Override
    public String toString() {
        return "Action{" +
                "description='" + description + '\'' +
                "active='" + isActive() + '\'' +
                '}';
    }
}
