package ru.elite.store.jpa.entity;

import ru.elite.core.ECONOMIC_TYPE;
import ru.elite.core.OFFER_TYPE;
import ru.elite.core.SERVICE_TYPE;
import ru.elite.core.STATION_TYPE;
import ru.elite.entity.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "Station.getNamesInSystem", query = "select s.name from StationImpl s where s.starSystem.id = :starSystemId"),
        @NamedQuery(name = "Station.findInSystemByName", query = "select s from StationImpl s where s.name = :name and s.starSystem.id = :starSystemId"),
        @NamedQuery(name = "Station.findByEID", query = "select s from StationImpl s where s.eid = :eid"),
        @NamedQuery(name = "Station.deleteFromSystemByName", query = "delete from StationImpl s where s.name = :name and s.starSystem.id = :starSystemId")
})
@Table(name = "ELITE_STATIONS")
public class StationImpl implements Station {

    @Id
    @GeneratedValue
    private Long id;

    private Long eid;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StarSystemImpl.class, optional = false)
    @JoinColumn(name = "system_id", updatable = false, nullable = false)
    private StarSystem starSystem;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private STATION_TYPE type;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = MinorFactionImpl.class)
    @JoinColumn(name = "faction_id")
    private MinorFaction faction;

    @Column(nullable = false)
    private double distance;

    @Enumerated(EnumType.STRING)
    private ECONOMIC_TYPE economic;

    @Column(name = "economic2")
    @Enumerated(EnumType.STRING)
    private ECONOMIC_TYPE subEconomic;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ELITE_STATION_SERVICES", joinColumns = @JoinColumn(name = "station_id"))
    @Column(name = "service_id")
    private final Set<SERVICE_TYPE> services;
    {
        this.services = EnumSet.noneOf(SERVICE_TYPE.class);
    }

    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY, targetEntity = OfferImpl.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

    @Column(name = "modified", nullable = false)
    private LocalDateTime modifiedTime;


    protected StationImpl() {
    }

    protected StationImpl(StarSystem starSystem, String name, STATION_TYPE type, double distance) {
        this.starSystem = starSystem;
        this.name = name;
        this.type = type;
        this.distance = distance;
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
    public StarSystem getStarSystem() {
        return starSystem;
    }

    @Override
    public void removeStarSystem() {
        this.starSystem = null;
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
    public STATION_TYPE getType() {
        return type;
    }

    @Override
    public void setType(STATION_TYPE type) {
        this.type = type;
    }

    @Override
    public MinorFaction getFaction() {
        return faction;
    }

    @Override
    public void setFaction(MinorFaction faction) {
        this.faction = faction;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public ECONOMIC_TYPE getEconomic() {
        return economic;
    }

    @Override
    public void setEconomic(ECONOMIC_TYPE economic) {
        this.economic = economic;
    }

    @Override
    public ECONOMIC_TYPE getSubEconomic() {
        return subEconomic;
    }

    @Override
    public void setSubEconomic(ECONOMIC_TYPE subEconomic) {
        this.subEconomic = subEconomic;
    }

    @Override
    public Set<SERVICE_TYPE> getServices() {
        return services;
    }

    @Override
    public void addService(SERVICE_TYPE service) {
        services.add(service);
    }

    @Override
    public void removeService(SERVICE_TYPE service) {
        services.remove(service);
    }

    @Override
    public boolean hasService(SERVICE_TYPE service) {
        return services.contains(service);
    }

    @Override
    public void clearServices() {
        services.clear();
    }

    @Override
    public Collection<Offer> get() {
        List<? extends Offer> o = offers;
        return Collections.unmodifiableCollection(o);
    }

    @Override
    public Offer addOffer(Item item, OFFER_TYPE type, double price, long count) {
        Offer offer = new OfferImpl(this, item, type, price, count);
        offers.add(offer);
        return offer;
    }

    @Override
    public boolean removeOffer(Offer offer) {
        boolean removed = offers.remove(offer);
        offer.removeStation();
        return removed;
    }

    @Override
    public void clearOffers() {
        offers.forEach(Offer::removeStation);
        offers.clear();
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
        if (o instanceof StationImpl){
            StationImpl station = (StationImpl) o;
            if (id != null && station.id != null && !Objects.equals(id, station.id)) return false;
        }
        if (!(o instanceof Station)) return false;

        Station station = (Station) o;

        return Objects.equals(name, station.getName()) &&
               Objects.equals(starSystem, station.getStarSystem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(starSystem, name);
    }

}
