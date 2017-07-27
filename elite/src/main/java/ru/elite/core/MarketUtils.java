package ru.elite.core;

import ru.elite.entity.Item;
import ru.elite.entity.Offer;
import ru.elite.entity.Station;

import java.util.stream.Stream;

public class MarketUtils {

    public static boolean isIncorrect(Offer offer){
        return offer != null && isIncorrect(offer.getStation(), offer.isIllegal(), offer.getType());
    }

    public static boolean isIncorrect(Station station, Item item, OFFER_TYPE type){
        return isIncorrect(station, item.isIllegal(station), type);
    }

    public static boolean isIncorrect(Station station, boolean isIllegal, OFFER_TYPE type){
        if (type == OFFER_TYPE.SELL){
            return isIllegal || !station.hasService(SERVICE_TYPE.MARKET);
        } else {
            return isIllegal ? !station.hasService(SERVICE_TYPE.BLACK_MARKET) : !station.hasService(SERVICE_TYPE.MARKET);
        }
    }

    public static Stream<Offer> getOffers(Stream<Offer> offers){
        return offers.filter(o -> !isIncorrect(o));
    }

}
