package ru.elite.store.imp.entities;


import java.util.Optional;

public interface SlotData {

    String getName();
    Optional<Boolean> isActive();
    Optional<Integer> getPriority();
    Optional<String> getModuleName();
    Optional<String> getBlueprint();
    Optional<Integer> getBlueprintLevel();
    Optional<Double> getHealth();
    Optional<Long> getAmmoClip();
    Optional<Long> getAmmoHopper();
    Optional<Long> getCost();

}
