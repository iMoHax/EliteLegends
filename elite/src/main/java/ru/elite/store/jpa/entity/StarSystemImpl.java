package ru.elite.store.jpa.entity;

import ru.elite.core.*;
import ru.elite.entity.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "StarSystem.findAll", query = "select s from StarSystemImpl s"),
    @NamedQuery(name = "StarSystem.findByName", query = "select s from StarSystemImpl s where s.name = :name"),
    @NamedQuery(name = "StarSystem.findByEID", query = "select s from StarSystemImpl s where s.eid = :eid"),
    @NamedQuery(name = "StarSystem.deleteAll", query = "delete from StarSystemImpl s")
})
@Table(name = "ELITE_STAR_SYSTEMS")
public class StarSystemImpl implements StarSystem {

    @Id
    @GeneratedValue
    private Long id;

    private Long eid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    @Column(nullable = false)
    private double z;

    @Column(nullable = false)
    private long population;

    @OneToMany(mappedBy = "starSystem", fetch = FetchType.LAZY, targetEntity = MinorFactionStateImpl.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinorFactionState> factions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MinorFactionImpl.class)
    @JoinColumn(name = "faction_id")
    private MinorFaction faction;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SECURITY_LEVEL security;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private POWER power;

    @Column(name = "power_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private POWER_STATE powerState;

    @Column(nullable = false)
    private long income;

    @OneToMany(mappedBy = "starSystem", fetch = FetchType.LAZY, targetEntity = StationImpl.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Station> stations = new ArrayList<>();

    @OneToMany(mappedBy = "starSystem", fetch = FetchType.LAZY, targetEntity = BodyImpl.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Body> bodies = new ArrayList<>();

    @Column(name = "modified", nullable = false)
    private LocalDateTime modifiedTime;

    protected StarSystemImpl() {
    }

    public StarSystemImpl(String name, double x, double y, double z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.population = 0;
        this.income = 0;
        this.security = SECURITY_LEVEL.NONE;
        this.power = POWER.NONE;
        this.powerState = POWER_STATE.NONE;
        this.modifiedTime = LocalDateTime.now();
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public long getPopulation() {
        return population;
    }

    @Override
    public void setPopulation(long population) {
        this.population = population;
    }

    @Override
    public long getIncome() {
        return income;
    }

    @Override
    public void setIncome(long income) {
        this.income = income;
    }

    @Override
    public Collection<MinorFactionState> getFactions() {
        List<? extends MinorFactionState> f = factions;
        return Collections.unmodifiableCollection(f);
    }

    @Override
    public MinorFactionState addFaction(MinorFaction faction, STATE_TYPE state, float influence) {
        MinorFactionState factionState = new MinorFactionStateImpl(this, faction, state, influence);
        factions.add(factionState);
        return factionState;
    }

    @Override
    public boolean removeFaction(MinorFactionState factionState) {
        boolean removed = factions.remove(factionState);
        factionState.removeStarSystem();
        return removed;
    }

    @Override
    public void clearFactions() {
        factions.forEach(MinorFactionState::removeStarSystem);
        factions.clear();
    }

    @Override
    public MinorFaction getControllingFaction() {
        return faction;
    }

    @Override
    public void setControllingFaction(MinorFaction faction) {
        this.faction = faction;
    }

    @Override
    public SECURITY_LEVEL getSecurity() {
        return security;
    }

    @Override
    public void setSecurity(SECURITY_LEVEL security) {
        this.security = security;
    }

    @Override
    public POWER getPower() {
        return power;
    }

    public POWER_STATE getPowerState() {
        return powerState;
    }

    @Override
    public void setPower(POWER power, POWER_STATE state) {
        this.power = power;
        this.powerState = state;
    }

    @Override
    public Collection<Station> get() {
        List<? extends Station> s = stations;
        return Collections.unmodifiableCollection(s);
    }

    @Override
    public Station addStation(String name, STATION_TYPE type, double distance) {
        Station station = new StationImpl(this, name, type, distance);
        stations.add(station);
        return station;
    }

    @Override
    public boolean removeStation(Station station) {
        boolean removed = stations.remove(station);
        station.removeStarSystem();
        return removed;
    }

    @Override
    public void clearStations() {
        stations.forEach(Station::removeStarSystem);
        stations.clear();
    }

    @Override
    public Collection<Body> getBodies() {
        List<? extends Body> s = bodies;
        return Collections.unmodifiableCollection(s);
    }

    @Override
    public Body addBody(String name, BODY_TYPE type) {
        Body body = new BodyImpl(this, name, type);
        bodies.add(body);
        return body;
    }

    @Override
    public boolean removeBody(Body body) {
        boolean removed = bodies.remove(body);
        body.removeStarSystem();
        return removed;
    }

    @Override
    public void clearBodies() {
        bodies.forEach(Body::removeStarSystem);
        bodies.clear();
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof StarSystemImpl){
            StarSystemImpl system = (StarSystemImpl) o;
            if (id != null && system.id != null && !Objects.equals(id, system.id)) return false;
        }
        if (!(o instanceof StarSystem)) return false;

        StarSystem system = (StarSystem) o;

        return Objects.equals(name, system.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
