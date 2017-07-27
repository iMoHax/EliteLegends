package ru.elite.entity;

import org.jetbrains.annotations.NotNull;
import ru.elite.core.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public interface Station extends Comparable<Station> {
    Long getEID();
    void setEID(Long eid);

    StarSystem getStarSystem();
    void removeStarSystem();

    String getName();
    void setName(String name);

    STATION_TYPE getType();
    void setType(STATION_TYPE type);

    MinorFaction getFaction();
    void setFaction(MinorFaction faction);

    double getDistance();
    void setDistance(double distance);

    Collection<SERVICE_TYPE> getServices();
    void addService(SERVICE_TYPE service);
    void removeService(SERVICE_TYPE service);
    boolean hasService(SERVICE_TYPE service);
    void clearServices();

    ECONOMIC_TYPE getEconomic();
    void setEconomic(ECONOMIC_TYPE economic);

    ECONOMIC_TYPE getSubEconomic();
    void setSubEconomic(ECONOMIC_TYPE economic);

    Collection<Offer> get();
    Offer addOffer(Item item, OFFER_TYPE type, double price, long count);
    boolean removeOffer(Offer offer);
    void clearOffers();

    default Stream<Offer> get(OFFER_TYPE type){
        return get().stream().filter(o -> o.getType() == type);
    }

    default Optional<Offer> get(OFFER_TYPE type, Item item){
        return get(type).filter(o -> o.getItem().equals(item)).findAny();
    }

    default Optional<Offer> getOffer(OFFER_TYPE type, Item item){
        return get(type).filter(o -> o.getItem().equals(item)).findAny().filter(o -> !MarketUtils.isIncorrect(o));
    }

    default Stream<Offer> getSellOffers(){
        return MarketUtils.getOffers(get(OFFER_TYPE.SELL));
    }

    default Stream<Offer> getBuyOffers(){
        return MarketUtils.getOffers(get(OFFER_TYPE.BUY));
    }

    default Optional<Offer> getSell(Item item){
        return getOffer(OFFER_TYPE.SELL, item);
    }

    default Optional<Offer> getBuy(Item item){
        return getOffer(OFFER_TYPE.BUY, item);
    }

    LocalDateTime getModifiedTime();
    void setModifiedTime(LocalDateTime time);



    @Override
    default int compareTo(@NotNull Station other) {
        Objects.requireNonNull(other, "Not compare with null");
        if (this == other) return 0;
        int cmp = Double.compare(getDistance(), other.getDistance());
        if (cmp != 0) return cmp;
        String name = getName();
        String otherName = other.getName();
        return name != null ? otherName != null ? name.compareTo(otherName) : -1 : 0;
    }
}
