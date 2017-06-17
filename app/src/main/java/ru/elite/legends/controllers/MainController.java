package ru.elite.legends.controllers;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.legends.QuestsLoader;
import ru.elite.legends.models.ActionModel;
import ru.elite.legends.models.QuestModel;
import ru.elite.legends.models.StageModel;
import ru.elite.legends.view.ViewController;

public class MainController implements ViewController {
    private final static Logger LOG = LoggerFactory.getLogger(MainController.class);

    @FXML
    private ListView<QuestModel> questsList;

    @FXML
    private WebView questText;

    @FXML
    private ListView<ActionModel> actionsList;

    private WebEngine webEngine;
    private StageModel stageModel;

    @FXML
    private void initialize(){
        webEngine = questText.getEngine();
        stageModel = new StageModel();
        questsList.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> {
            if (n != null) showQuest(n);
        });
        stageModel.textProperty().addListener((ov, o, n) -> setText(n));
        actionsList.setItems(stageModel.getActions());
        actionsList.setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2){
                ActionModel action = actionsList.getSelectionModel().getSelectedItem();
                if (action != null){
                    action.execute();
                }
            }
        });
        reinitialise();
    }

    private void setText(String text){
        String html = "<!DOCTYPE html><html><head></head><body>"+text+"</body></html>";
        webEngine.loadContent(html);
    }

    private void showQuest(QuestModel quest){
        stageModel.setStage(quest.getStage());
    }


    // TODO: implement
    @Override
    public void reinitialise() {
        questsList.getItems().clear();
        QuestsManager manager = QuestsLoader.load();
        manager.getQuests().stream().forEach(q -> questsList.getItems().add(new QuestModel(q)));
        manager.addListener(this::refresh);
    }

    @Override
    public void show(Parent owner, Parent content, boolean toggle) {

    }

    @Override
    public void close() {

    }

    @Override
    public void refresh() {
        QuestModel quest = questsList.getSelectionModel().getSelectedItem();
        if (quest != null){
            quest.refresh();
            showQuest(quest);
        }
    }

}
