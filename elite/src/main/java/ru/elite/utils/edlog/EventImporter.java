package ru.elite.utils.edlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.entity.Body;
import ru.elite.entity.Commander;
import ru.elite.entity.StarSystem;
import ru.elite.entity.Station;
import ru.elite.store.GalaxyService;
import ru.elite.store.imp.AbstractImporter;
import ru.elite.store.imp.entities.BodyData;
import ru.elite.store.imp.entities.CommanderData;
import ru.elite.store.imp.entities.StarSystemData;
import ru.elite.store.imp.entities.StationData;
import ru.elite.utils.edlog.entities.events.*;

import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.util.Optional;

public class EventImporter extends AbstractImporter {
    private final static Logger LOG = LoggerFactory.getLogger(EventImporter.class);
    private final GalaxyService galaxyService;
    private Optional<Commander> cmdr = Optional.empty();

    public EventImporter(GalaxyService galaxyService) {
        this.galaxyService = galaxyService;
    }

    @Override
    protected void before() throws IOException {
    }

    @Override
    protected void after() throws IOException {
    }

    @Override
    protected boolean next() throws IOException {
        throw new UnsupportedOperationException("Is EventImporter, next() unsupported, use importEvent");
    }

    @Override
    protected StarSystemData getSystem() {
        throw new UnsupportedOperationException("Is EventImporter, getSystem() unsupported, use importEvent");
    }

    @Override
    protected CommanderData getCmdr() {
        throw new UnsupportedOperationException("Is EventImporter, getCmdr() unsupported, use importEvent");
    }

    public Optional<Commander> getImportedCmdr() {
        return cmdr;
    }

    public void importEvent(StartupEvents events){
        LOG.debug("Import startup events: {}", events);
        transactional(() -> _importEvent(events));
    }

    public void importEvent(FSDJumpEvent event){
        LOG.debug("Import fsd jump event: {}", event);
        transactional(() -> _importEvent(event));
    }

    public void importEvent(DockedEvent event){
        LOG.debug("Import docked event: {}", event);
        transactional(() -> _importEvent(event));
    }

    public void importEvent(TouchdownEvent event){
        LOG.debug("Import touchdown event: {}", event);
        transactional(() -> _importEvent(event));
    }

    public void importEvent(SupercruiseExitEvent event){
        LOG.debug("Import super cruise exit event: {}", event);
        transactional(() -> _importEvent(event));
    }

    public void importEvent(UndockedEvent event){
        LOG.debug("Import undocked event: {}", event);
        transactional(() -> _importEvent(event));
    }

    public void importEvent(LiftoffEvent event){
        LOG.debug("Import liftoff event: {}", event);
        transactional(() -> _importEvent(event));
    }

    private void _importEvent(StartupEvents event){
        LOG.debug("Import startup events {}", event);
        cmdr = Optional.of(impCommander(galaxyService, event.asImportData()));
    }

    private void _importEvent(FSDJumpEvent event){
        StarSystemData data = event.getStarSystem().asImportData();
        StarSystem starSystem = impSystem(galaxyService, data, true);
        if (starSystem == null){
            LOG.warn("StarSystem {} not found", data.getName());
        } else {
            cmdr.ifPresent(c -> c.setStarSystem(starSystem));
        }
    }

    private void _importEvent(DockedEvent event){
        Optional<StarSystem> system = galaxyService.findStarSystemByName(event.getStarSystem());
        if (!system.isPresent()){
            LOG.warn("StarSystem {} not found", event.getStarSystem());
        } else {
            StationData data = event.getStation().asImportData();
            Station station = impStation(galaxyService, system.get(), data);
            if (station == null){
                LOG.warn("Station {} not found", data.getName());
            } else {
                cmdr.ifPresent(c -> c.setStation(station));
            }
        }
    }

    private void _importEvent(TouchdownEvent event){
        cmdr.ifPresent(c -> {
            if (event.isPlayerControlled()){
                c.setLongitude(event.getLongitude());
                c.setLatitude(event.getLatitude());
                c.setLanded(true);
            }
        });
    }

    private void _importEvent(SupercruiseExitEvent event){
        Optional<StarSystem> system = galaxyService.findStarSystemByName(event.getStarSystem());
        if (!system.isPresent()){
            LOG.warn("StarSystem {} not found", event.getStarSystem());
        } else {
            BodyData data = event.getBody().asImportData();
            Body body = impBody(galaxyService, system.get(), data);
            if (body == null){
                LOG.warn("Body {} not found", data.getName());
            } else {
                cmdr.ifPresent(c -> c.setBody(body));
            }
        }
    }

    private void _importEvent(UndockedEvent event){
        cmdr.ifPresent(c -> c.setStation(null));
    }

    private void _importEvent(LiftoffEvent event){
        cmdr.ifPresent(c -> {
            if (event.isPlayerControlled()){
                c.setLongitude(event.getLongitude());
                c.setLatitude(event.getLatitude());
                c.setLanded(false);
            }
        });
    }

    private void transactional(Runnable action){
        EntityTransaction transaction = galaxyService.startTransaction();
        try {
            action.run();
            transaction.commit();
        } catch (Exception e) {
            LOG.error("Error on import startup events:", e);
            transaction.rollback();
        }
    }
}
