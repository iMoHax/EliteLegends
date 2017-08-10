package ru.elite.store.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.Galaxy;
import ru.elite.entity.StarSystem;
import ru.elite.entity.Station;
import ru.elite.store.GalaxyService;
import ru.elite.store.imp.entities.CommanderData;
import ru.elite.store.imp.entities.StarSystemData;
import ru.elite.store.imp.entities.StationData;

import java.io.IOException;
import java.util.Collection;

public class SimpleImporter extends AbstractImporter {
    private final static Logger LOG = LoggerFactory.getLogger(SimpleImporter.class);

    @Override
    protected void before() throws IOException {
    }

    @Override
    protected void after() throws IOException {
    }

    @Override
    protected boolean next() throws IOException {
        throw new UnsupportedOperationException("Is SimpleImporter, next() unsupported, use importStation or importSystem");
    }

    @Override
    protected StarSystemData getSystem() {
        throw new UnsupportedOperationException("Is SimpleImporter, getSystem() unsupported, use importStation or importSystem");
    }

    @Override
    protected CommanderData getCmdr() {
        throw new UnsupportedOperationException("Is SimpleImporter, getCmdr() unsupported, use importCmdr");
    }

    public Station importStation(GalaxyService galaxyService, StarSystemData importData){
        StarSystem system = impSystem(galaxyService, importData, false);
        if (system != null) {
            Collection<StationData> stations = importData.getStations();
            if (stations == null || stations.isEmpty()){
                LOG.warn("Station data not found");
                return null;
            }
            StationData stationData = stations.iterator().next();
            return impStation(galaxyService, system, stationData);
        } else {
            LOG.warn("System {} not found", importData.getName());
            return null;
        }
    }

    public StarSystem importSystem(GalaxyService galaxyService, StarSystemData importData){
        return impSystem(galaxyService, importData);
    }

}
