package ru.elite.legends.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.legends.entities.QUEST_STATUS;
import ru.elite.legends.entities.Quest;
import ru.elite.legends.entities.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class QuestsManager {
    private final static Logger LOG = LoggerFactory.getLogger(QuestsManager.class);
    private final Collection<Quest> quests;
    private final Collection<InvalidateListener> listeners;

    public QuestsManager() {
        quests = new ArrayList<>();
        listeners = new ArrayList<>();
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

    public void activate(String questId){
        getQuest(questId).ifPresent(q -> q.setStatus(QUEST_STATUS.ACTIVE));
        invalidate();
    }

    public void complete(String questId){
        getQuest(questId).ifPresent(q -> q.setStatus(QUEST_STATUS.COMPLETE));
        invalidate();
    }

    public void fail(String questId){
        getQuest(questId).ifPresent(q -> q.setStatus(QUEST_STATUS.FAILED));
        invalidate();
    }

    public void activate(String questId, String stageId){
        getQuest(questId).ifPresent(q -> getStage(q, stageId).ifPresent(q::setStage));
        invalidate();
    }

    public void complete(String questId, String stageId){
        getQuest(questId).ifPresent(q -> getStage(q, stageId).ifPresent(s -> s.setStatus(QUEST_STATUS.COMPLETE)));
        invalidate();
    }

    public void fail(String questId, String stageId){
        getQuest(questId).ifPresent(q -> getStage(q, stageId).ifPresent(s -> s.setStatus(QUEST_STATUS.FAILED)));
        invalidate();
    }


    private void invalidate(){
        listeners.forEach(InvalidateListener::invalidate);
    }

    public void addListener(InvalidateListener listener){
        listeners.add(listener);
    }

    public void removeListener(InvalidateListener listener){
        listeners.remove(listener);
    }

    public interface InvalidateListener {
        void invalidate();
    }
}
