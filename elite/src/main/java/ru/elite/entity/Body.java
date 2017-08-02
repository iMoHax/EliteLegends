package ru.elite.entity;

import ru.elite.core.BODY_TYPE;

public interface Body {

    Long getEID();
    void setEID(Long eid);

    StarSystem getStarSystem();
    void removeStarSystem();

    String getName();
    void setName(String name);

    BODY_TYPE getType();
    void setType(BODY_TYPE type);


}
