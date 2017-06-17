package ru.elite.legends.view;

import javafx.scene.Parent;

import java.util.Optional;

public interface DialogController<T, R> extends ViewController {

    Optional<R> showDialog(Parent owner, Parent content, T context);


}
