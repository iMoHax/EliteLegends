package ru.elite.legends.models;

import javafx.beans.property.*;
import ru.elite.legends.entities.QUEST_STATUS;
import ru.elite.legends.entities.Quest;
import ru.elite.legends.entities.Stage;

public class QuestModel {
    private final Quest quest;
    private final StringProperty caption;
    private final StringProperty description;
    private final ObjectProperty<QUEST_STATUS> status;

    public QuestModel(Quest quest) {
        this.quest = quest;
        caption = new SimpleStringProperty();
        description = new SimpleStringProperty();
        status = new SimpleObjectProperty<>();
        refresh();
    }

    public void refresh(){
        caption.setValue(quest.getCaption());
        description.setValue(quest.getDescription());
        status.setValue(quest.getStatus());
    }

    public Stage getStage(){
        return quest.getStage();
    }

    public boolean equalsQuest(Quest otherQuest) {
        return this.quest.equals(otherQuest);
    }

    @Override
    public String toString() {
        return quest.getCaption();
    }
}
