package ru.elite.entity;

public interface Module {
    Long getEID();
    void setEID(Long eid);

    Slot getSlot();
    void setSlot(Slot slot);
    void removeSlot();

    String getName();

    String getBlueprint();
    Integer getBlueprintLevel();
    void setBlueprint(String blueprint, Integer level);

    Double getHealth();
    void setHealth(Double health);

    Long getAmmoClip();
    void setAmmoClip(Long ammoClip);

    Long getAmmoHopper();
    void setAmmoHopper(Long ammoHopper);

    Long getCost();
    void setCost(Long cost);
}
