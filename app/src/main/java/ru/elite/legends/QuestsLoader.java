package ru.elite.legends;

import ru.elite.legends.controllers.QuestsManager;
import ru.elite.legends.entities.Action;
import ru.elite.legends.entities.Quest;
import ru.elite.legends.entities.Stage;

import java.util.Collection;

public class QuestsLoader {

    public static QuestsManager load(){
        QuestsManager manager = new QuestsManager();

        Action q1s1a1 = new Action("Quest 1 Stage 1 Action 1 - goto stage 2") {
            @Override
            public void complete() {
                manager.activate("q1","s2");
            }
        };
        Action q1s1a2 = new Action("Quest 1 Stage 1 Action 2 - active quest 2") {
            @Override
            public void complete() {
                manager.activate("q2");
            }
        };
        Action q1s2a1 = new Action("Quest 1 Stage 2 Action 1 - failed") {
            @Override
            public void complete() {
                manager.fail("q1");
            }
        };
        Action q1s2a2 = new Action("Quest 1 Stage 2 Action 2 - complete") {
            @Override
            public void complete() {
                manager.complete("q1");
            }
        };
        Action q1s2a3 = new Action("Quest 1 Stage 2 Action 3 - goto stage 1") {
            @Override
            public void complete() {
                manager.activate("q1", "s1");
            }
        };
        Action q2s1a1 = new Action("Quest 2 Stage 1 Action 1 - goto stage 2") {
            @Override
            public void complete() {
                manager.activate("q2","s2");
            }
        };
        Action q2s2a1 = new Action("Quest 2 Stage 2 Action 1 - goto stage 3") {
            @Override
            public void complete() {
                manager.activate("q2","s3");
            }
        };
        Action q2s2a2 = new Action("Quest 2 Stage 2 Action 2 - goto stage 2 in quest 1") {
            @Override
            public void complete() {
                manager.activate("q1","s2");
            }
        };
        Action q2s3a1 = new Action("Quest 2 Stage 3 Action 1 - goto stage 1") {
            @Override
            public void complete() {
                manager.activate("q2","s1");
            }
        };


        Stage q1s1 = new Stage("s1");
        q1s1.setText("QUEST 1 Stage 1");
        q1s1.addAction(q1s1a1);
        q1s1.addAction(q1s1a2);
        Stage q1s2 = new Stage("s2");
        q1s2.setText("QUEST 1 Stage 2");
        q1s2.addAction(q1s2a1);
        q1s2.addAction(q1s2a2);
        q1s2.addAction(q1s2a3);
        Stage q2s1 = new Stage("s1");
        q2s1.setText("QUEST 2 Stage 1");
        q2s1.addAction(q2s1a1);
        Stage q2s2 = new Stage("s2");
        q2s2.setText("QUEST 2 Stage 2");
        q2s2.addAction(q2s2a1);
        q2s2.addAction(q2s2a2);
        Stage q2s3 = new Stage("s3");
        q2s3.setText("QUEST 2 Stage 3");
        q2s3.addAction(q2s3a1);


        Quest q1 = new Quest("q1");
        q1.setCaption("Quest 1");
        q1.setDescription("Test quest");
        q1.addStage(q1s1);
        q1.addStage(q1s2);
        q1.setStage(q1s1);
        manager.add(q1);
        Quest q2 = new Quest("q2");
        q2.setCaption("Quest 2");
        q2.setDescription("Test quest");
        q2.addStage(q2s1);
        q2.addStage(q2s2);
        q2.addStage(q2s3);
        q2.setStage(q2s1);
        manager.add(q2);

        return manager;
    }
}
