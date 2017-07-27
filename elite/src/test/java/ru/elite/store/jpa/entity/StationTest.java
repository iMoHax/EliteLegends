package ru.elite.store.jpa.entity;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.core.ECONOMIC_TYPE;
import ru.elite.core.OFFER_TYPE;
import ru.elite.core.SERVICE_TYPE;
import ru.elite.core.STATION_TYPE;
import ru.elite.entity.*;
import ru.elite.store.jpa.JPATest;

import static org.hamcrest.CoreMatchers.hasItems;

import java.time.LocalDateTime;
import java.util.Collection;

public class StationTest extends JPATest {
    private final static Logger LOG = LoggerFactory.getLogger(StationTest.class);

    private final StarSystem STAR_SYSTEM = DEFAULT_GALAXY.EURYALE;
    private final MinorFaction CONTROLLING_FACTION = DEFAULT_GALAXY.EGU;
    private final String STATION_NAME = "EG Main HQ";
    private final STATION_TYPE STATION_TYPE = ru.elite.core.STATION_TYPE.CORIOLIS_STARPORT;
    private final double DISTANCE = 888.88;
    private final long EID = 555;
    private final ECONOMIC_TYPE ECONOMIC = ECONOMIC_TYPE.INDUSTRIAL;
    private final ECONOMIC_TYPE SUB_ECONOMIC = ECONOMIC_TYPE.REFINERY;
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.of(2017,7,1,8,45);



    public Station createStation(){
        Station station = new StationImpl(STAR_SYSTEM, STATION_NAME, STATION_TYPE, DISTANCE);
        station.setEID(EID);
        station.setFaction(CONTROLLING_FACTION);
        station.setEconomic(ECONOMIC);
        station.setSubEconomic(SUB_ECONOMIC);
        station.setModifiedTime(MODIFIED_TIME);
        return station;
    }

    public void assertStation(Station station){
        assertEquals(STAR_SYSTEM, station.getStarSystem());
        assertEquals(STATION_NAME, station.getName());
        assertEquals(STATION_TYPE, station.getType());
        assertEquals(DISTANCE, station.getDistance(), 0.0);
        assertEquals(EID, station.getEID().longValue());
        assertEquals(CONTROLLING_FACTION, station.getFaction());
        assertEquals(ECONOMIC, station.getEconomic());
        assertEquals(SUB_ECONOMIC, station.getSubEconomic());
        assertEquals(MODIFIED_TIME, station.getModifiedTime());
    }

    @Test
    public void createTest(){
        LOG.info("Create station test");
        Station station = createStation();
        assertStation(station);
    }

    @Test
    public void offersTest(){
        final Item OFFER1_ITEM = DEFAULT_GALAXY.ALGAE;
        final long OFFER1_SELL_COUNT = 44500;
        final double OFFER1_SELL_PRICE = 455.2;
        final long OFFER1_BUY_COUNT = 4000;
        final double OFFER1_BUY_PRICE = 546.6;

        final Item OFFER2_ITEM = DEFAULT_GALAXY.GOLD;
        final long OFFER2_BUY_COUNT = 100;
        final double OFFER2_BUY_PRICE = 1546.3;

        final Item OFFER3_ITEM = DEFAULT_GALAXY.ANACONDA;
        final long OFFER3_SELL_COUNT = 1;
        final double OFFER3_SELL_PRICE = 180456771.6;

        final Item OFFER4_ITEM = DEFAULT_GALAXY.FSD_A5;
        final long OFFER4_SELL_COUNT = 1;
        final double OFFER4_SELL_PRICE = 566444;

        LOG.info("Station offers test");
        Station station = createStation();

        Collection<Offer> offers = station.get();
        assertEquals(0, offers.size());

        final Offer offer1_sell = station.addOffer(OFFER1_ITEM, OFFER_TYPE.SELL, OFFER1_SELL_PRICE, OFFER1_SELL_COUNT);
        final Offer offer1_buy = station.addOffer(OFFER1_ITEM, OFFER_TYPE.BUY, OFFER1_BUY_PRICE, OFFER1_BUY_COUNT);
        final Offer offer2 = station.addOffer(OFFER2_ITEM, OFFER_TYPE.BUY, OFFER2_BUY_PRICE, OFFER2_BUY_COUNT);
        final Offer offer3 = station.addOffer(OFFER3_ITEM, OFFER_TYPE.SELL, OFFER3_SELL_PRICE, OFFER3_SELL_COUNT);
        final Offer offer4 = station.addOffer(OFFER4_ITEM, OFFER_TYPE.SELL, OFFER4_SELL_PRICE, OFFER4_SELL_COUNT);
        assertEquals(station, offer1_sell.getStation());
        assertEquals(station, offer1_buy.getStation());
        assertEquals(station, offer2.getStation());
        assertEquals(station, offer3.getStation());
        assertEquals(station, offer4.getStation());

        offers = station.get();
        assertThat(offers, hasItems(offer1_buy, offer1_sell, offer2, offer3, offer4));
        assertEquals(5, offers.size());
        assertEquals(2, station.get(OFFER_TYPE.BUY).count());
        assertEquals(3, station.get(OFFER_TYPE.SELL).count());

        Offer actual = station.get(OFFER_TYPE.SELL, OFFER1_ITEM).get();
        assertEquals(OFFER1_ITEM, actual.getItem());
        assertEquals(OFFER1_SELL_COUNT, actual.getCount());
        assertEquals(OFFER1_SELL_PRICE, actual.getPrice(), 0.0);
        assertEquals(offer1_sell, actual);

        actual = station.get(OFFER_TYPE.BUY, OFFER1_ITEM).get();
        assertEquals(OFFER1_ITEM, actual.getItem());
        assertEquals(OFFER1_BUY_COUNT, actual.getCount());
        assertEquals(OFFER1_BUY_PRICE, actual.getPrice(), 0.0);
        assertEquals(offer1_buy, actual);

        assertEquals(false, station.get(OFFER_TYPE.SELL, OFFER2_ITEM).isPresent());
        actual = station.get(OFFER_TYPE.BUY, OFFER2_ITEM).get();
        assertEquals(OFFER2_ITEM, actual.getItem());
        assertEquals(OFFER2_BUY_COUNT, actual.getCount());
        assertEquals(OFFER2_BUY_PRICE, actual.getPrice(), 0.0);
        assertEquals(offer2, actual);

        actual = station.get(OFFER_TYPE.SELL, OFFER3_ITEM).get();
        assertEquals(OFFER3_ITEM, actual.getItem());
        assertEquals(OFFER3_SELL_COUNT, actual.getCount());
        assertEquals(OFFER3_SELL_PRICE, actual.getPrice(), 0.0);
        assertEquals(false, station.get(OFFER_TYPE.BUY, OFFER3_ITEM).isPresent());
        assertEquals(offer3, actual);

        actual = station.get(OFFER_TYPE.SELL, OFFER4_ITEM).get();
        assertEquals(OFFER4_ITEM, actual.getItem());
        assertEquals(OFFER4_SELL_COUNT, actual.getCount());
        assertEquals(OFFER4_SELL_PRICE, actual.getPrice(), 0.0);
        assertEquals(false, station.get(OFFER_TYPE.BUY, OFFER4_ITEM).isPresent());
        assertEquals(offer4, actual);

        LOG.info("Remove offers test");

        station.removeOffer(offer1_sell);
        assertNull(offer1_sell.getStation());
        assertEquals(true, station.get(OFFER_TYPE.BUY, OFFER1_ITEM).isPresent());
        assertEquals(false, station.get(OFFER_TYPE.SELL, OFFER1_ITEM).isPresent());
        assertEquals(4, station.get().size());
        assertEquals(2, station.get(OFFER_TYPE.BUY).count());
        assertEquals(2, station.get(OFFER_TYPE.SELL).count());

        station.removeOffer(offer3);
        assertNull(offer3.getStation());
        assertEquals(false, station.get(OFFER_TYPE.SELL, OFFER3_ITEM).isPresent());
        assertEquals(false, station.get(OFFER_TYPE.BUY, OFFER3_ITEM).isPresent());
        assertEquals(3, station.get().size());
        assertEquals(2, station.get(OFFER_TYPE.BUY).count());
        assertEquals(1, station.get(OFFER_TYPE.SELL).count());

        station.clearOffers();
        assertEquals(0, station.get().size());
        assertEquals(0, station.get(OFFER_TYPE.BUY).count());
        assertEquals(0, station.get(OFFER_TYPE.SELL).count());
        assertNull(offer1_sell.getStation());
        assertNull(offer1_buy.getStation());
        assertNull(offer2.getStation());
        assertNull(offer3.getStation());
        assertNull(offer4.getStation());
    }

    @Test
    public void servicesTest(){
        LOG.info("Station services test");
        Station station = createStation();
        Collection<SERVICE_TYPE> services = station.getServices();
        assertEquals(0, services.size());
        station.addService(SERVICE_TYPE.MARKET);
        station.addService(SERVICE_TYPE.SHIPYARD);
        station.addService(SERVICE_TYPE.BLACK_MARKET);

        services = station.getServices();
        assertThat(services, hasItems(SERVICE_TYPE.MARKET, SERVICE_TYPE.SHIPYARD, SERVICE_TYPE.BLACK_MARKET));
        assertEquals(3, services.size());

        assertEquals(true, station.hasService(SERVICE_TYPE.MARKET));
        assertEquals(true, station.hasService(SERVICE_TYPE.SHIPYARD));
        assertEquals(true, station.hasService(SERVICE_TYPE.BLACK_MARKET));
        assertEquals(false, station.hasService(SERVICE_TYPE.LARGE_LANDPAD));

        station.removeService(SERVICE_TYPE.MEDIUM_LANDPAD);
        station.removeService(SERVICE_TYPE.SHIPYARD);
        services = station.getServices();
        assertThat(services, hasItems(SERVICE_TYPE.MARKET));
        assertEquals(2, services.size());
        assertEquals(true, station.hasService(SERVICE_TYPE.MARKET));
        assertEquals(false, station.hasService(SERVICE_TYPE.SHIPYARD));
        assertEquals(true, station.hasService(SERVICE_TYPE.BLACK_MARKET));

        station.clearServices();
        services = station.getServices();
        assertEquals(0, services.size());

    }

    @Test
    public void disabledOffersTest(){
        final Item OFFER1_ITEM = DEFAULT_GALAXY.ALGAE;
        final long OFFER1_SELL_COUNT = 44500;
        final double OFFER1_SELL_PRICE = 455.2;
        final long OFFER1_BUY_COUNT = 4000;
        final double OFFER1_BUY_PRICE = 546.6;

        final Item OFFER2_ITEM = new ItemImpl("Illegal good", DEFAULT_GALAXY.FOODS);
        final long OFFER2_SELL_COUNT = 10500;
        final double OFFER2_SELL_PRICE = 555;
        final long OFFER2_BUY_COUNT = 3000;
        final double OFFER2_BUY_PRICE = 466;

        LOG.info("Station disabled offers test");

        Station station = createStation();
        OFFER2_ITEM.setIllegal(station.getFaction().getGovernment(), true);

        Collection<Offer> offers = station.get();
        assertEquals(0, offers.size());

        final Offer offer1_sell = station.addOffer(OFFER1_ITEM, OFFER_TYPE.SELL, OFFER1_SELL_PRICE, OFFER1_SELL_COUNT);
        final Offer offer1_buy = station.addOffer(OFFER1_ITEM, OFFER_TYPE.BUY, OFFER1_BUY_PRICE, OFFER1_BUY_COUNT);
        final Offer offer2_sell = station.addOffer(OFFER2_ITEM, OFFER_TYPE.SELL, OFFER2_SELL_PRICE, OFFER2_SELL_COUNT);
        final Offer offer2_buy = station.addOffer(OFFER2_ITEM, OFFER_TYPE.BUY, OFFER2_BUY_PRICE, OFFER2_BUY_COUNT);

        offers = station.get();
        assertThat(offers, hasItems(offer1_buy, offer1_sell, offer2_buy, offer2_sell));
        assertEquals(4, offers.size());
        assertEquals(2, station.get(OFFER_TYPE.BUY).count());
        assertEquals(2, station.get(OFFER_TYPE.SELL).count());
        assertEquals(0, station.getBuyOffers().count());
        assertEquals(0, station.getSellOffers().count());

        assertEquals(true, station.get(OFFER_TYPE.SELL, OFFER1_ITEM).isPresent());
        assertEquals(true, station.get(OFFER_TYPE.BUY, OFFER1_ITEM).isPresent());
        assertEquals(false, station.getOffer(OFFER_TYPE.SELL, OFFER1_ITEM).isPresent());
        assertEquals(false, station.getOffer(OFFER_TYPE.BUY, OFFER1_ITEM).isPresent());
        assertEquals(false, station.getSell(OFFER1_ITEM).isPresent());
        assertEquals(false, station.getBuy(OFFER1_ITEM).isPresent());

        assertEquals(true, station.get(OFFER_TYPE.SELL, OFFER2_ITEM).isPresent());
        assertEquals(true, station.get(OFFER_TYPE.BUY, OFFER2_ITEM).isPresent());
        assertEquals(false, station.getOffer(OFFER_TYPE.SELL, OFFER2_ITEM).isPresent());
        assertEquals(false, station.getOffer(OFFER_TYPE.BUY, OFFER2_ITEM).isPresent());
        assertEquals(false, station.getSell(OFFER2_ITEM).isPresent());
        assertEquals(false, station.getBuy(OFFER2_ITEM).isPresent());

        station.addService(SERVICE_TYPE.MARKET);
        assertEquals(1, station.getBuyOffers().count());
        assertEquals(1, station.getSellOffers().count());

        assertEquals(true, station.getOffer(OFFER_TYPE.SELL, OFFER1_ITEM).isPresent());
        assertEquals(true, station.getOffer(OFFER_TYPE.BUY, OFFER1_ITEM).isPresent());
        assertEquals(true, station.getSell(OFFER1_ITEM).isPresent());
        assertEquals(true, station.getBuy(OFFER1_ITEM).isPresent());

        assertEquals(false, station.getOffer(OFFER_TYPE.SELL, OFFER2_ITEM).isPresent());
        assertEquals(false, station.getOffer(OFFER_TYPE.BUY, OFFER2_ITEM).isPresent());
        assertEquals(false, station.getSell(OFFER2_ITEM).isPresent());
        assertEquals(false, station.getBuy(OFFER2_ITEM).isPresent());

        station.addService(SERVICE_TYPE.BLACK_MARKET);
        assertEquals(2, station.getBuyOffers().count());
        assertEquals(1, station.getSellOffers().count());

        assertEquals(true, station.getOffer(OFFER_TYPE.SELL, OFFER1_ITEM).isPresent());
        assertEquals(true, station.getOffer(OFFER_TYPE.BUY, OFFER1_ITEM).isPresent());
        assertEquals(true, station.getSell(OFFER1_ITEM).isPresent());
        assertEquals(true, station.getBuy(OFFER1_ITEM).isPresent());

        assertEquals(false, station.getOffer(OFFER_TYPE.SELL, OFFER2_ITEM).isPresent());
        assertEquals(true, station.getOffer(OFFER_TYPE.BUY, OFFER2_ITEM).isPresent());
        assertEquals(false, station.getSell(OFFER2_ITEM).isPresent());
        assertEquals(true, station.getBuy(OFFER2_ITEM).isPresent());

        station.removeService(SERVICE_TYPE.MARKET);
        assertEquals(1, station.getBuyOffers().count());
        assertEquals(0, station.getSellOffers().count());

        assertEquals(false, station.getOffer(OFFER_TYPE.SELL, OFFER1_ITEM).isPresent());
        assertEquals(false, station.getOffer(OFFER_TYPE.BUY, OFFER1_ITEM).isPresent());
        assertEquals(false, station.getSell(OFFER1_ITEM).isPresent());
        assertEquals(false, station.getBuy(OFFER1_ITEM).isPresent());

        assertEquals(false, station.getOffer(OFFER_TYPE.SELL, OFFER2_ITEM).isPresent());
        assertEquals(true, station.getOffer(OFFER_TYPE.BUY, OFFER2_ITEM).isPresent());
        assertEquals(false, station.getSell(OFFER2_ITEM).isPresent());
        assertEquals(true, station.getBuy(OFFER2_ITEM).isPresent());

    }
}