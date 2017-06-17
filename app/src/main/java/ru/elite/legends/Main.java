package ru.elite.legends;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.legends.locale.Localization;
import ru.elite.legends.view.ViewManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class Main extends Application {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);
    private static Stage primaryStage;
    private static ViewManager viewManager;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        loadMainScene();
        loadResources();
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.print("Exception in thread \"" + t.getName() + "\" ");
            e.printStackTrace(System.err);
            LOG.error("", e);
            ViewManager.showException(e);
        });
        launch(args);
    }

    public static void changeLocale(Locale locale) {
        Localization.setLocale(locale);
        primaryStage.hide();
        viewManager.closeAll();
        try {
            loadMainScene();
            loadResources();
        } catch (IOException | InstantiationException e) {
            LOG.error("Error on create views", e);
            System.exit(1);
            e.printStackTrace();
        }
        primaryStage.show();
    }

    private static void loadMainScene() throws IOException, InstantiationException {
        viewManager = ViewManager.loadMainView(Main.class.getResource("/view/main.fxml"), getUrl("style.css").toExternalForm());

        primaryStage.setTitle(Localization.getString("view.main.title"));
        primaryStage.setMinHeight(300);
        primaryStage.setScene(new Scene(viewManager.getMainView()));
        primaryStage.setOnCloseRequest((we) -> {
            viewManager.closeAll();
        });

    }

    private static void loadResources() throws IOException, InstantiationException {
//        viewManager.load("settings", getUrl(("settings.fxml")));
    }

    private static URL getUrl(String filename) throws MalformedURLException {
        File file = new File("conf" + File.separator + filename);
        if (file.exists()) return file.toURI().toURL();
        return Main.class.getResource("/view/" + filename);
    }

    public static void copyToClipboard(String string){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(string);
        clipboard.setContent(content);
    }

}
