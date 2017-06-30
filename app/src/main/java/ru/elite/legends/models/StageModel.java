package ru.elite.legends.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.legends.entities.Action;
import ru.elite.legends.entities.Stage;

public class StageModel {
    private final static Logger LOG = LoggerFactory.getLogger(StageModel.class);

    private Stage stage;
    private final StringProperty text;
    private final ObservableList<ActionModel> actions;

    public StageModel() {
        text = new SimpleStringProperty();
        actions = FXCollections.observableArrayList();
    }

    public StageModel(Stage stage) {
        this();
        this.stage = stage;
        refresh();
    }

    public void refresh(){
        text.setValue(stage.getText());
        actions.clear();
        stage.getActions().stream().filter(Action::isActive).forEach(a -> actions.add(new ActionModel(a)));
    }

    public void setStage(Stage stage){
        LOG.debug("Change stage to {}", stage);
        this.stage = stage;
        refresh();
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public ObservableList<ActionModel> getActions() {
        return actions;
    }

    public boolean equalsStage(Stage otherStage) {
        return this.stage != null && this.stage.equals(otherStage);
    }
}
