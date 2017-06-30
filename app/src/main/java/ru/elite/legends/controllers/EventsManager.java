package ru.elite.legends.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.legends.entities.*;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class EventsManager {
    private final static Logger LOG = LoggerFactory.getLogger(EventsManager.class);
    private final List<Collection<EventHandler>> eventHandlers = new ArrayList<>(EVENT_TYPE.values().length);
    private final List<ReentrantLock> locks = new ArrayList<>(EVENT_TYPE.values().length);

    public EventsManager() {
        for (int i = 0; i < EVENT_TYPE.values().length; i++) {
            eventHandlers.add(new ArrayList<>(50));
            locks.add(new ReentrantLock());
        }
    }

    private int getIndex(EVENT_TYPE eventType){
        return  eventType.ordinal();
    }

    private void add(EventHandler eventHandler){
        LOG.trace("Add event handle {}", eventHandler);
        int eventIndex = getIndex(eventHandler.getType());
        locks.get(eventIndex).lock();
        try {
            eventHandlers.get(eventIndex).add(eventHandler);
        } finally {
            locks.get(eventIndex).unlock();
        }
    }

    private void remove(EventHandler eventHandler){
        LOG.trace("Remove event handler {}", eventHandler);
        int eventIndex = getIndex(eventHandler.getType());
        locks.get(eventIndex).lock();
        try {
            eventHandlers.get(eventIndex).remove(eventHandler);
        } finally {
            locks.get(eventIndex).unlock();
        }
    }

    private EventHandler[] get(EVENT_TYPE type){
        int eventIndex = getIndex(type);
        locks.get(eventIndex).lock();
        try {
            return eventHandlers.get(eventIndex).toArray(new EventHandler[0]);
        } finally {
            locks.get(eventIndex).unlock();
        }
    }

    public void register(QuestsManager manager){
        LOG.debug("Register quests");
        manager.getQuests().stream()
                .filter(q -> q.getStatus().isActive())
                .forEach(this::add);
        manager.addListener(new QuestInvalidateListener());

    }

    public void fireEvent(EVENT_TYPE eventType){
        LOG.trace("Fire event {}", eventType);
        EventHandler[] handlers = get(eventType);
        for (EventHandler handler : handlers) {
            handler.handle();
        }
    }

    private void add(Quest quest){
        quest.getStages().stream()
                .filter(s -> s.getStatus().isActive())
                .forEach(this::add);
    }

    private void add(Stage stage){
        stage.getEvents().stream()
                .filter(EventHandler::isActive)
                .forEach(this::add);
    }

    private void remove(Quest quest){
        quest.getStages().stream()
                .filter(s -> s.getStatus().isActive())
                .forEach(this::remove);
    }

    private void remove(Stage stage){
        stage.getEvents().stream()
                .filter(EventHandler::isActive)
                .forEach(this::remove);
    }

    private final class QuestInvalidateListener implements QuestsManager.InvalidateListener {

        @Override
        public void change(Quest quest, QUEST_STATUS oldStatus, QUEST_STATUS newStatus) {
            if (oldStatus.isActive() && !newStatus.isActive()){
                LOG.debug("Unregistered quest {}", quest);
                remove(quest);
            } else
            if (!oldStatus.isActive() && newStatus.isActive()){
                LOG.debug("Registered quest {}", quest);
                add(quest);
            }
        }

        @Override
        public void change(Stage stage, QUEST_STATUS oldStatus, QUEST_STATUS newStatus) {
            if (oldStatus.isActive() && !newStatus.isActive()){
                LOG.debug("Unregistered stage {}", stage);
                remove(stage);
            } else
            if (!oldStatus.isActive() && newStatus.isActive()){
                LOG.debug("Registered quest {}", stage);
                add(stage);
            }
        }

        @Override
        public void change(Stage stage, Action action, boolean oldActive, boolean newActive) {
        }

        @Override
        public void change(Stage stage, EventHandler eventHandler, boolean oldActive, boolean newActive) {
            if (oldActive && !newActive){
                LOG.debug("Unregistered event handler {}", eventHandler);
                remove(eventHandler);
            } else
            if (!oldActive && newActive){
                LOG.debug("Registered event handler {}", eventHandler);
                add(eventHandler);
            }
        }

        @Override
        public void change(Quest quest, Stage oldStage, Stage newStage) {
        }
    }
}
