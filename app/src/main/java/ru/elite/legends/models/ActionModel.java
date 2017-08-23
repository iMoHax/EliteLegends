package ru.elite.legends.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.elite.legends.entities.Action;

public class ActionModel {
    private final Action action;
    private final StringProperty description;

    public ActionModel(Action action) {
        this.action = action;
        description = new SimpleStringProperty();
        refresh();
    }

    public void refresh(){
        description.setValue(action.getDescription());
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void execute(){
        action.action();
    }

    @Override
    public String toString() {
        return action.getDescription();
    }
}
