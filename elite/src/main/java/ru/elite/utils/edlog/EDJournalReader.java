package ru.elite.utils.edlog;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.elite.utils.edlog.entities.DockedEvent;
import ru.elite.utils.edlog.entities.FSDJumpEvent;

import java.io.IOException;

public class EDJournalReader extends LogReader {
    private final static Logger LOG = LoggerFactory.getLogger(EDJournalReader.class);
    private final static String LOG_FILE_PATTERN = ".+Journal\\..+\\.log$";
    private final static String EVENT_ATTR = "event";
    private final ObjectMapper mapper;

    public EDJournalReader() {
        super(LOG_FILE_PATTERN);
        this.mapper = new ObjectMapper();
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
            String event = eventNode.get(EVENT_ATTR).asText();
            switch (event){
                case "Docked": docked(new DockedEvent(eventNode));
                    break;
                case "FSDJump": jump(new FSDJumpEvent(eventNode));
                    break;
                case "Undocked": undock();
                    break;
            }

        } catch (IOException e) {
            LOG.error("Error on parse journal line: {}", line);
            LOG.error("",e);
        }


    }

    protected void docked(DockedEvent dockedEvent) {
        LOG.debug("Docked to station: {} / {}", dockedEvent.getStarSystem(), dockedEvent.getStation());
    }

    protected void jump(FSDJumpEvent jumpEvent) {
        LOG.debug("Jump to system {}, coordinates: {}, {}, {}", jumpEvent.getStarSystem(), jumpEvent.getX(), jumpEvent.getY(), jumpEvent.getZ());
    }

    protected void undock() {
        LOG.debug("Undocked");
    }


}
