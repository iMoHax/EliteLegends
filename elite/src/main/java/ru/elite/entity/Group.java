package ru.elite.entity;

import ru.elite.core.GROUP_TYPE;

public interface Group {
    String getName();

    GROUP_TYPE getType();
    void setType(GROUP_TYPE type);

}
