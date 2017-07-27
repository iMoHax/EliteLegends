package ru.elite.store.jpa.entity;

import ru.elite.entity.Commander;
import ru.elite.entity.Ship;
import ru.elite.entity.Slot;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "SHIPS")
public class ShipImpl implements Ship {
    @Id
    @GeneratedValue
    private Long id;

    private Long eid;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CommanderImpl.class, optional = false)
    @JoinColumn(name = "cmdr_id", updatable = false, nullable = false)
    private Commander cmdr;

    @Column(updatable = false, nullable = false)
    private long sid;

    @Column(updatable = false, nullable = false)
    private String type;

    private String name;

    private String ident;

    private double fuel;

    private double tank;

    @OneToMany(mappedBy="ship", fetch = FetchType.LAZY, targetEntity = SlotImpl.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name="name")
    private Map<String, Slot> slots = new HashMap<>();

    protected ShipImpl() {
    }

    protected ShipImpl(Commander commander, long sid, String type) {
        this.cmdr = commander;
        this.sid = sid;
        this.type = type;
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
    public Commander getCmdr() {
        return cmdr;
    }

    @Override
    public void removeCmdr(){
        this.cmdr = null;
    }

    @Override
    public long getSid() {
        return sid;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIdent() {
        return ident;
    }

    @Override
    public void setIdent(String ident) {
        this.ident = ident;
    }

    @Override
    public double getFuel() {
        return fuel;
    }

    @Override
    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    @Override
    public double getTank() {
        return tank;
    }

    @Override
    public void setTank(double tank) {
        this.tank = tank;
    }

    @Override
    public Collection<Slot> getSlots() {
        return Collections.unmodifiableCollection(slots.values());
    }

    @Override
    public Slot addSlot(String name) {
        Slot slot = slots.get(name);
        if (slot == null){
            slot = new SlotImpl(this, name);
            slots.put(name, slot);
        }
        return slot;
    }

    @Override
    public boolean removeSlot(Slot slot) {
        boolean removed = slots.remove(slot.getName()) != null;
        slot.removeShip();
        return removed;
    }

    @Override
    public void clearSlots() {
        slots.values().forEach(Slot::removeShip);
        slots.clear();
    }

    @Override
    public Optional<Slot> getSlot(String name) {
        return Optional.ofNullable(slots.get(name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ShipImpl){
            ShipImpl ship = (ShipImpl) o;
            if (id != null && ship.id != null && !Objects.equals(id, ship.id)) return false;
        }
        if (!(o instanceof Ship)) return false;

        Ship ship = (Ship) o;
        return Objects.equals(cmdr, ship.getCmdr()) &&
               Objects.equals(sid, ship.getSid()) &&
               Objects.equals(type, ship.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cmdr, sid, type);
    }
}
