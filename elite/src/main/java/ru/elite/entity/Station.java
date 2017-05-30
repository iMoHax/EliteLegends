package ru.elite.entity;

import ru.elite.core.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface Station {
    long getId();

    StarSystem getStarSystem();

    String getName();
    void setName(String name);

    STATION_TYPE getType();
    void setType(STATION_TYPE type);

    MinorFaction getFaction();
    void setFaction(MinorFaction faction);

    double getDistance();
    void setDistance(double distance);

    Collection<SERVICE_TYPE> getServices();
    void add(SERVICE_TYPE service);
    void remove(SERVICE_TYPE service);
    boolean has(SERVICE_TYPE service);

    ECONOMIC_TYPE getEconomic();
    void setEconomic(ECONOMIC_TYPE economic);

    ECONOMIC_TYPE getSubEconomic();
    void setSubEconomic(ECONOMIC_TYPE economic);

    Collection<Offer> get();
    void addAll(Collection<Offer> offers);
    void clearOffers();

    Offer add(Offer offer);
    boolean remove(Offer offer);

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

}
