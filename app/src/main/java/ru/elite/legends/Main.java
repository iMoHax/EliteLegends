package ru.elite.legends;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.Commander;
import ru.elite.legends.controllers.EventsManager;
import ru.elite.legends.controllers.QuestsManager;
import ru.elite.legends.locale.Localization;
import ru.elite.legends.nashorn.NashornController;
import ru.elite.legends.view.ViewManager;
import ru.elite.store.jpa.GalaxyStore;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class Main extends Application {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);
    private static EntityManagerFactory EMF;
    private static Stage primaryStage;
    public static ViewManager viewManager;
    public static EventsManager eventsManager;
    public static QuestsManager questsManager;
    public static NashornController scriptController;
    public static GalaxyStore galaxy;
    public static Commander cmdr;
    private static EDLogWatcher logWatcher;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        initDatabase();
        initServices();
        loadMainScene();
        loadResources();
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        logWatcher.shutdown();
        galaxy.close();
        EMF.close();
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

    private void initServices() {
        eventsManager = new EventsManager();
        questsManager = new QuestsManager();
        initScripts();
        initLogWatcher();
        eventsManager.register(questsManager);
    }

    private void initScripts(){
        try {
            scriptController = new NashornController();
        } catch (ScriptException e) {
            LOG.error("Error on init scripts: {}", e);
            throw new RuntimeException(e);
        }
        scriptController.init(questsManager, cmdr);
        QuestsLoader loader = new QuestsLoader(scriptController);
        loader.load("quests");
    }

    private void initLogWatcher(){
        EDLogHandler handler = new EDLogHandler(galaxy, eventsManager);
        logWatcher = new EDLogWatcher(handler);
        logWatcher.run();
    }

    private void initDatabase(){
        EMF = Persistence.createEntityManagerFactory("ru.elite.legends.JPAUnit");
        EntityManager manager = EMF.createEntityManager();
        galaxy = new GalaxyStore(manager);
    }

    public static void updateCmdr(Commander cmdr){
        Main.cmdr = cmdr;
        scriptController.setCmdr(cmdr);
    }

    public static void copyToClipboard(String string){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(string);
        clipboard.setContent(content);
    }

}
