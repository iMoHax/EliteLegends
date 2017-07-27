package ru.elite.store.jpa.entity;

import ru.elite.core.OFFER_TYPE;
import ru.elite.entity.Item;
import ru.elite.entity.Offer;
import ru.elite.entity.Station;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NamedQueries({
    @NamedQuery(name = "Offer.findInStationByItem", query = "select o from OfferImpl o where  o.station.id = :stationId and o.item.id = :itemId and o.type = :offerType"),
    @NamedQuery(name = "Offer.deleteAllFromStationByItem", query = "delete from OfferImpl o where  o.station.id = :stationId and o.item.id = :itemId")
})
@Table(name = "ELITE_OFFERS")
public class OfferImpl implements Offer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StationImpl.class, optional = false)
    @JoinColumn(name = "station_id", updatable = false, nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ItemImpl.class, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OFFER_TYPE type;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private long count;

    @Column(name = "modified", nullable = false)
    private LocalDateTime modifiedTime;

    protected OfferImpl() {
    }

    protected OfferImpl(Station station, Item item, OFFER_TYPE type, double price, long count) {
        this.station = station;
        this.item = item;
        this.type = type;
        this.price = price;
        this.count = count;
        this.modifiedTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    @Override
    public Station getStation() {
        return station;
    }

    @Override
    public void removeStation() {
        this.station = null;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public OFFER_TYPE getType() {
        return type;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    @Override
    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof OfferImpl){
            OfferImpl offer = (OfferImpl) o;
            if (id != null && offer.id != null && !Objects.equals(id, offer.id)) return false;
        }
        if (!(o instanceof Offer)) return false;

        Offer offer = (Offer) o;

        return  Objects.equals(type, offer.getType()) &&
                Objects.equals(item, offer.getItem()) &&
                Objects.equals(station, offer.getStation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, item, type);
    }

}
