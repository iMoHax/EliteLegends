package ru.elite.legends.view;

import javafx.scene.Parent;

public interface ViewController {

    void reinitialise();

    void show(Parent owner, Parent content, boolean toggle);

    void close();

    void refresh();


}
