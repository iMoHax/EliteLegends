package ru.elite.store.jpa.entity;

import ru.elite.entity.Module;
import ru.elite.entity.Ship;
import ru.elite.entity.Slot;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SHIP_SLOTS")
public class SlotImpl implements Slot {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ShipImpl.class, optional = false)
    @JoinColumn(name = "ship_id", updatable = false, nullable = false)
    private Ship ship;

    @Column(updatable = false, nullable = false)
    private String name;

    private boolean active;

    private int priority;

    @OneToOne(mappedBy = "slot", fetch = FetchType.LAZY, targetEntity = ModuleImpl.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Module module;

    protected SlotImpl() {
    }


    protected SlotImpl(Ship ship, String name) {
        this.ship = ship;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Ship getShip() {
        return ship;
    }

    @Override
    public void removeShip(){
        this.ship = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean on) {
        this.active = on;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public Module setModule(String name) {
        Module old = this.module;
        if (old != null){
            old.removeSlot();
        }
        Module module = new ModuleImpl(this, name);
        this.module = module;
        return module;
    }

    @Override
    public Module swapModule(Module module) {
        Module old = this.module;
        if (old != null){
            old.removeSlot();
        }
        module.setSlot(this);
        this.module = module;
        return old;
    }

    @Override
    public void removeModule() {
        if (module != null){
            module.removeSlot();
            module = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof SlotImpl){
            SlotImpl slot = (SlotImpl) o;
            if (id != null && slot.id != null && !Objects.equals(id, slot.id)) return false;
        }
        if (!(o instanceof Slot)) return false;

        Slot slot = (Slot) o;
        return Objects.equals(name, slot.getName()) &&
               Objects.equals(ship, slot.getShip());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ship);
    }
}
