package ru.elite.legends.view;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import ru.elite.legends.locale.Localization;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ViewManager {
    private final Map<String, ViewController> controllers;
    private final Map<String, Parent> views;
    private final ViewController mainController;
    private final Parent mainView;

    private ViewManager(Parent mainView, ViewController mainController) {
        controllers = new HashMap<>();
        views = new HashMap<>();
        this.mainView = mainView;
        this.mainController = mainController;
    }

    public static ViewManager loadMainView(URL fxml, String stylesheet) throws IOException, InstantiationException {
        FXMLLoader loader = newLoader(fxml);
        Parent view = loader.load();
        if (stylesheet != null) {
            view.getStylesheets().add(stylesheet);
        }
        Object controller = loader.getController();
        if (!(controller instanceof ViewController)){
            throw new InstantiationException("Controller must implement ViewController interface");
        }
        return new ViewManager(view, (ViewController) controller);
    }

    private static FXMLLoader newLoader(URL url){
        FXMLLoader loader = new FXMLLoader(url, Localization.getResources());
        return loader;
    }

    private void addStylesheets(Parent view) {
        view.getStylesheets().addAll(mainView.getStylesheets());
    }


    public void load(String viewId, URL fxml) throws IOException, InstantiationException {
        FXMLLoader loader =  newLoader(fxml);
        Parent view = loader.load();
        addStylesheets(view);
        Object controller = loader.getController();
        if (!(controller instanceof ViewController)){
            throw new InstantiationException("Controller must implement ViewController interface");
        }
        views.put(viewId, view);
        controllers.put(viewId, (ViewController) controller);
    }

    public Parent getMainView() {
        return mainView;
    }

    public Parent getView(String id){
        return views.get(id);
    }

    public ViewController getController(String id){
        return controllers.get(id);
    }

    public void show(String viewId){
        getController(viewId).show(mainView, getView(viewId), false);
    }

    public void toggle(String viewId){
        getController(viewId).show(mainView, getView(viewId), true);
    }

    public <T,R> Optional<R> showDialog(String viewId, T context){
        ViewController controller = getController(viewId);
        //noinspection unchecked
        return ((DialogController<T,R>) controller).showDialog(mainView, getView(viewId), context);
    }

    public void close(String viewId){
        getController(viewId).close();
    }

    public void closeAll(){
        controllers.values().forEach(ViewController::close);
        mainController.close();
    }

    public void reinitAll(){
        mainController.reinitialise();
        controllers.values().forEach(ViewController::reinitialise);
    }


    public static Optional<String> showTextDialog(String title, String header, String content){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait();
    }

    public static void showInfo(String title, String header, String message){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        dialog.showAndWait();
    }

    public static void showException(Throwable ex){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Localization.getString("dialog.exception.title", "Exception Dialog"));
        String text = ex.getLocalizedMessage();
        alert.setHeaderText(text != null ? text : ex.getMessage());

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label(Localization.getString("dialog.exception.label.stacktrace", "The exception stacktrace was:"));
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }


}
