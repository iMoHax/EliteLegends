package ru.elite.store.jpa.entity;

import ru.elite.entity.*;

import javax.persistence.*;
import java.util.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "Commander.findAll", query = "select c from CommanderImpl c"),
        @NamedQuery(name = "Commander.getNames", query = "select c.name from CommanderImpl c"),
        @NamedQuery(name = "Commander.findByName", query = "select c from CommanderImpl c where c.name = :name"),
        @NamedQuery(name = "Commander.findByEID", query = "select c from CommanderImpl c where c.eid = :eid"),
        @NamedQuery(name = "Commander.deleteAll", query = "delete from CommanderImpl")
})
@Table(name = "COMMANDERS")
public class CommanderImpl implements Commander {
    @Id
    @GeneratedValue
    private Long id;

    private Long eid;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = ShipImpl.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ship_id")
    private Ship ship;

    private double credits;

    private boolean landed;

    private boolean dead;

    @OneToMany(mappedBy="cmdr", fetch = FetchType.LAZY, targetEntity = InventoryEntryImpl.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name="item")
    private Map<Item, InventoryEntry> inventory = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "RANKS_STATUS",  joinColumns = @JoinColumn(name = "cmdr_id"))
    @Column(name = "rank_id" )
    private Map<String, Rank> ranks = new HashMap<>();

    @OneToOne(fetch = FetchType.LAZY, targetEntity = StarSystemImpl.class, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "system_id")
    private StarSystem starSystem;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = StationImpl.class, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "station_id")
    private Station station;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = BodyImpl.class, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "body_id")
    private Body body;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy="cmdr", fetch = FetchType.LAZY, targetEntity = ShipImpl.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ship> ships = new HashSet<>();


    protected CommanderImpl() {
    }

    public CommanderImpl(String name) {
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Ship getShip() {
        return ship;
    }

    @Override
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    @Override
    public double getCredits() {
        return credits;
    }

    @Override
    public void setCredits(double credits) {
        this.credits = credits;
    }

    @Override
    public StarSystem getStarSystem() {
        return starSystem;
    }

    @Override
    public void setStarSystem(StarSystem starSystem) {
        this.starSystem = starSystem;
    }

    @Override
    public Station getStation() {
        return station;
    }

    @Override
    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public boolean isLanded() {
        return landed;
    }

    @Override
    public void setLanded(boolean landed) {
        this.landed = landed;
        if (!landed){
            latitude = null;
            longitude = null;
        }
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int getRank(String type){
        Rank rank = ranks.get(type);
        if (rank != null){
            return rank.getValue();
        }
        return 0;
    }

    @Override
    public void setRank(String type, int value){
        Rank rank = ranks.get(type);
        if (rank != null){
            rank.setValue(value);
        } else {
            rank = new Rank(type, value);
            ranks.put(type, rank);
        }
    }

    @Override
    public void resetRanks() {
        ranks.clear();
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public Collection<InventoryEntry> getInventory() {
        return Collections.unmodifiableCollection(inventory.values());
    }

    @Override
    public InventoryEntry addItem(Item item, long count) {
        InventoryEntry entry = inventory.get(item);
        if (entry != null){
            long sum = count + entry.getCount();
            entry.setCount(sum);
            if (sum <= 0){
                inventory.remove(item);
                entry.removeCmdr();
            }
        } else {
            if (count < 0) return null;
            entry = new InventoryEntryImpl(this, item, count);
            inventory.put(item, entry);
        }
        return entry;
    }

    @Override
    public boolean removeItem(Item item) {
        InventoryEntry entry = inventory.get(item);
        boolean removed = inventory.remove(item) != null;
        entry.removeCmdr();
        return removed;
    }

    @Override
    public void clearItems() {
        inventory.values().forEach(InventoryEntry::removeCmdr);
        inventory.clear();
    }

    @Override
    public long getCount(Item item) {
        InventoryEntry entry = inventory.get(item);
        return entry != null ? entry.getCount() : 0;
    }

    @Override
    public Set<Ship> getShips() {
        return Collections.unmodifiableSet(ships);
    }

    @Override
    public Ship addShip(long sid, String type) {
        Ship ship = new ShipImpl(this, sid, type);
        ships.add(ship);
        return ship;
    }

    @Override
    public boolean removeShip(Ship ship) {
        if (ship.equals(this.ship)){
            this.ship = null;
        }
        boolean removed = ships.remove(ship);
        ship.removeCmdr();
        return removed;
    }

    @Override
    public void clearShips() {
        ship = null;
        ships.forEach(Ship::removeCmdr);
        ships.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof CommanderImpl){
            CommanderImpl cmdr = (CommanderImpl) o;
            if (id != null && cmdr.id != null && !Objects.equals(id, cmdr.id)) return false;
        }
        if (!(o instanceof Commander)) return false;

        Commander cmdr = (Commander) o;

        return Objects.equals(name, cmdr.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
