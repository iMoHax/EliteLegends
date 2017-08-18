package ru.elite.utils.edlog;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.utils.edlog.entities.JournalHandler;
import ru.elite.utils.edlog.entities.events.*;

import java.io.IOException;

public class EDJournalReader extends LogReader {
    private final static Logger LOG = LoggerFactory.getLogger(EDJournalReader.class);
    private final static String LOG_FILE_PATTERN = ".+Journal\\..+\\.log$";
    private final static String EVENT_ATTR = "event";
    private final ObjectMapper mapper;
    private JournalHandler handler;

    public EDJournalReader() {
        super(LOG_FILE_PATTERN);
        this.mapper = new ObjectMapper();
    }

    public void setHandler(JournalHandler handler){
        this.handler = handler;
    }

    @Override
    protected void outLine(String line) {
        super.outLine(line);
        if (line.trim().isEmpty()) return;
        try {
            JsonNode eventNode = mapper.readTree(line);
            if (!eventNode.has(EVENT_ATTR)){
                LOG.warn("Attribute {} not found, skip", EVENT_ATTR);
                LOG.debug("Line: {}", line);
                return;
            }
            String eventType = eventNode.get(EVENT_ATTR).asText();

            switch (eventType){
                case "FSDJump": handler.jump(new FSDJumpEvent(eventNode));
                    break;
                case "Docked": handler.dock(new DockedEvent(eventNode));
                    break;
                case "Touchdown": handler.touchdown(new TouchdownEvent(eventNode));
                    break;
                case "SupercruiseExit": handler.supercruiseExit(new SupercruiseExitEvent(eventNode));
                    break;
                case "Undocked": handler.undock(new UndockedEvent(eventNode));
                    break;
                case "Liftoff": handler.liftoff(new LiftoffEvent(eventNode));
                    break;
                case "Cargo": handler.cargo(new CargoEvent(eventNode));
                    break;
                case "Loadout": handler.loadout(new LoadoutEvent(eventNode));
                    break;
                case "Materials": handler.materials(new MaterialsEvent(eventNode));
                    break;
                case "LoadGame": handler.loadGame(new LoadGameEvent(eventNode));
                    break;
                case "Rank": handler.rank(new RankEvent(eventNode));
                    break;
                case "Location": handler.location(new LocationEvent(eventNode));
                    break;

            }
            handler.onEvent(new JournalEventImpl(eventNode));
        } catch (IOException e) {
            LOG.error("Error on parse journal line: {}", line);
            LOG.error("",e);
        }


    }
}
