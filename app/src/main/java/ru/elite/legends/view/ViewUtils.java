package ru.elite.legends.view;

import javafx.application.Platform;

public class ViewUtils {

    public static void doFX(Runnable runnable){
        if (Platform.isFxApplicationThread()){
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }


}
