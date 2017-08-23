package ru.elite.legends.entities;

import java.util.Collection;
import java.util.Optional;

public interface Quest {
    String getId();

    String getCaption();

    String getDescription();

    Stage getStage();
    void setStage(Stage stage);

    Collection<Stage> getStages();

    QUEST_STATUS getStatus();
    void setStatus(QUEST_STATUS status);

    default Optional<Stage> getStage(String id){
        return getStages().stream().filter(s -> s.getId().equals(id)).findAny();
    }

}
