package ru.elite.legends.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class QuestImpl implements Quest {

    private final String id;

    private String caption;
    private String description;
    private Stage stage;
    private Collection<Stage> stages;
    private QUEST_STATUS status;

    public QuestImpl(String id) {
        this.id = id;
        stages = new ArrayList<>();
        status = QUEST_STATUS.NONE;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Collection<Stage> getStages() {
        return stages;
    }

    public void addStage(Stage stage) {
        stages.add(stage);
    }

    public void removeStage(Stage stage) {
        stages.remove(stage);
    }

    @Override
    public QUEST_STATUS getStatus() {
        return status;
    }

    @Override
    public void setStatus(QUEST_STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestImpl)) return false;
        QuestImpl quest = (QuestImpl) o;
        return Objects.equals(id, quest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Quest{" +
                "id='" + id + '\'' +
                ", stage=" + stage +
                ", status=" + status +
                '}';
    }
}
