package ru.elite.legends.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class Quest {

    private final String id;

    private String caption;
    private String description;
    private Stage stage;
    private Collection<Stage> stages;
    private QUEST_STATUS status;

    public Quest(String id) {
        this.id = id;
        stages = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Collection<Stage> getStages() {
        return stages;
    }

    public void addStage(Stage stage) {
        stages.add(stage);
    }

    public void removeStage(Stage stage) {
        stages.remove(stage);
    }

    public Optional<Stage> getStage(String id){
        return stages.stream().filter(s -> s.getId().equals(id)).findAny();
    }

    public QUEST_STATUS getStatus() {
        return status;
    }

    public void setStatus(QUEST_STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quest)) return false;
        Quest quest = (Quest) o;
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
