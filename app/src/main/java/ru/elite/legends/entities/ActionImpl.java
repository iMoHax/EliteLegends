package ru.elite.legends.entities;

public abstract class ActionImpl implements Action {
    private final String id;
    private final String description;
    private boolean active;
    private boolean auto;

    public ActionImpl(String id, String description) {
        this.id = id;
        this.description = description;
        this.active = true;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
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
    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", auto=" + auto +
                '}';
    }
}
