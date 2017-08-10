package ru.elite.store.jpa.entity;

import ru.elite.entity.Module;
import ru.elite.entity.Slot;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SHIP_MODULES")
public class ModuleImpl implements Module {
    @Id
    @GeneratedValue
    private Long id;

    private Long eid;

    @OneToOne(targetEntity = SlotImpl.class, optional = false, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @Column(updatable = false, nullable = false)
    private String name;

    private String blueprint;
    @Column(name = "blueprint_lvl")
    private Integer blueprintLevel;

    private Double health;

    @Column(name = "ammo_clip")
    private Long ammoClip;
    @Column(name = "ammo_hopper")
    private Long ammoHopper;

    private Long cost;

    protected ModuleImpl() {
    }

    protected ModuleImpl(Slot slot, String name) {
        this.slot = slot;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Long getEID() {
        return eid;
    }

    @Override
    public void setEID(Long eid) {
        this.eid = eid;
    }

    @Override
    public Slot getSlot() {
        return slot;
    }

    @Override
    public void setSlot(Slot slot){
        this.slot = slot;
    }

    @Override
    public void removeSlot(){
        this.slot = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBlueprint() {
        return blueprint;
    }

    @Override
    public Integer getBlueprintLevel() {
        return blueprintLevel;
    }

    @Override
    public void setBlueprint(String blueprint, Integer level) {
        this.blueprint = blueprint;
        this.blueprintLevel = level;
    }

    @Override
    public Double getHealth() {
        return health;
    }

    @Override
    public void setHealth(Double health) {
        this.health = health;
    }

    @Override
    public Long getAmmoClip() {
        return ammoClip;
    }

    @Override
    public void setAmmoClip(Long ammoClip) {
        this.ammoClip = ammoClip;
    }

    @Override
    public Long getAmmoHopper() {
        return ammoHopper;
    }

    @Override
    public void setAmmoHopper(Long ammoHopper) {
        this.ammoHopper = ammoHopper;
    }

    @Override
    public Long getCost() {
        return cost;
    }

    @Override
    public void setCost(Long cost) {
        this.cost = cost;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ModuleImpl){
            ModuleImpl module = (ModuleImpl) o;
            if (id != null && module.id != null && !Objects.equals(id, module.id)) return false;
        }
        if (!(o instanceof Module)) return false;

        Module module = (Module) o;
        return  Objects.equals(slot, module.getSlot()) &&
                Objects.equals(name, module.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(slot, name);
    }
}
