package ru.elite.store.imp.entities;

import ru.elite.core.BODY_TYPE;

import java.util.Optional;

public interface BodyData {

    Optional<Long> getId();

    String getName();
    BODY_TYPE getType();

}
