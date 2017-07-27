package ru.elite.entity;

public interface Slot {

    String getName();

    Ship getShip();
    void removeShip();

    boolean isActive();
    void setActive(boolean on);

    int getPriority();
    void setPriority(int priority);

    Module getModule();
    Module setModule(String name);
    Module swapModule(Module module);
    void removeModule();

    default boolean isModule(String name){
        Module module = getModule();
        return module != null && name.equals(module.getName());
    }
}
