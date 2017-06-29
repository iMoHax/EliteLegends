package ru.elite.legends.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.legends.entities.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class QuestsManager {
    private final static Logger LOG = LoggerFactory.getLogger(QuestsManager.class);
    private final Collection<Quest> quests = new ArrayList<>();
    private final Collection<InvalidateListener> listeners = new ArrayList<>();

    public QuestsManager() {
    }

    public Collection<Quest> getQuests() {
        return quests;
    }

    public void add(Quest quest){
        LOG.debug("Add quest: {}", quest);
        quests.add(quest);
    }

    public void remove(Quest quest){
        LOG.debug("Remove quest: {}", quest);
        quests.remove(quest);
    }

    private Optional<Quest> getQuest(String questId){
        Optional<Quest> quest = quests.stream().filter(q -> questId.equals(q.getId())).findAny();
        if (!quest.isPresent()){
            LOG.warn("Quest with ID = {} not found", questId);
        }
        return quest;
    }


    private Optional<Stage> getStage(Quest quest, String stageId){
        Optional<Stage> stage = quest.getStage(stageId);
        if (!stage.isPresent()){
            LOG.warn("Stage with ID = {} not found, quest = {}", stageId, quest);
        }
        return stage;
    }

    private void changeStatus(Quest quest, QUEST_STATUS status){
        QUEST_STATUS old = quest.getStatus();
        if (old == status) return;
        LOG.debug("Change status of quest {}, old = {}, new ={}", quest, old, status);
        quest.setStatus(status);
        fireChangeStatus(quest, old, status);
    }

    private void changeStatus(Stage stage, QUEST_STATUS status){
        QUEST_STATUS old = stage.getStatus();
        if (old == status) return;
        LOG.debug("Change status of stage {}, old = {}, new ={}", stage, old, status);
        stage.setStatus(status);
        fireChangeStatus(stage, old, status);
    }

    private void changeStatus(Stage stage, Action action, boolean active){
        boolean old = action.isActive();
        if (old == active) return;
        LOG.debug("Change status of action {}, old = {}, new ={}, stage = {}", action, old, active, stage);
        action.setActive(active);
        fireChangeStatus(stage, action, old, active);
    }

    private void changeStatus(Stage stage, EventHandler event, boolean active){
        boolean old = event.isActive();
        if (old == active) return;
        LOG.debug("Change status of event {}, old = {}, new ={}, stage = {}", event, old, active, stage);
        event.setActive(active);
        fireChangeStatus(stage, event, old, active);
    }

    private void changeStage(Quest quest, Stage stage){
        Stage old = quest.getStage();
        if (old == stage) return;
        LOG.debug("Change stage of quest {}, old = {}, new ={}", quest, old, stage);
        quest.setStage(stage);
        fireChangeStage(quest, old, stage);
    }


    public void activate(String questId){
        getQuest(questId).ifPresent(q -> changeStatus(q, QUEST_STATUS.ACTIVE));
    }

    public void complete(String questId){
        getQuest(questId).ifPresent(q -> changeStatus(q, QUEST_STATUS.COMPLETE));
    }

    public void fail(String questId){
        getQuest(questId).ifPresent(q -> changeStatus(q, QUEST_STATUS.FAILED));
    }

    public void goTo(String questId, String stageId, boolean complete){
        getQuest(questId).ifPresent(q -> getStage(q, stageId).ifPresent(s -> {
            changeStatus(q.getStage(), complete ? QUEST_STATUS.COMPLETE : QUEST_STATUS.FAILED);
            changeStatus(s, QUEST_STATUS.ACTIVE);
            changeStage(q, s);
        }));
    }

    public void activate(String questId, String stageId){
        getQuest(questId).ifPresent(q -> getStage(q, stageId).ifPresent(s -> changeStatus(s, QUEST_STATUS.ACTIVE)));
    }

    public void complete(String questId, String stageId){
        getQuest(questId).ifPresent(q -> getStage(q, stageId).ifPresent(s -> changeStatus(s, QUEST_STATUS.COMPLETE)));
    }

    public void fail(String questId, String stageId){
        getQuest(questId).ifPresent(q -> getStage(q, stageId).ifPresent(s -> changeStatus(s, QUEST_STATUS.FAILED)));
    }


    /*********  LISTENER *************/

    private void fireChangeStatus(Quest quest, QUEST_STATUS oldStatus, QUEST_STATUS newStatus){
        listeners.forEach(l -> l.change(quest, oldStatus, newStatus));
    }


    private void fireChangeStatus(Stage stage, QUEST_STATUS oldStatus, QUEST_STATUS newStatus){
        listeners.forEach(l -> l.change(stage, oldStatus, newStatus));
    }

    private void fireChangeStatus(Stage stage, Action action, boolean oldActive, boolean newActive){
        listeners.forEach(l -> l.change(stage, action, oldActive, newActive));
    }

    private void fireChangeStatus(Stage stage, EventHandler eventHandler, boolean oldActive, boolean newActive){
        listeners.forEach(l -> l.change(stage, eventHandler, oldActive, newActive));
    }

    private void fireChangeStage(Quest quest, Stage oldStage, Stage newStage){
        listeners.forEach(l -> l.change(quest, oldStage, newStage));
    }

    public void addListener(InvalidateListener listener){
        listeners.add(listener);
    }

    public void removeListener(InvalidateListener listener){
        listeners.remove(listener);
    }

    public interface InvalidateListener {

        void change(Quest quest, QUEST_STATUS oldStatus, QUEST_STATUS newStatus);
        void change(Stage stage, QUEST_STATUS oldStatus, QUEST_STATUS newStatus);
        void change(Stage stage, Action action, boolean oldActive, boolean newActive);
        void change(Stage stage, EventHandler event, boolean oldActive, boolean newActive);
        void change(Quest quest, Stage oldStage, Stage newStage);

    }
}
