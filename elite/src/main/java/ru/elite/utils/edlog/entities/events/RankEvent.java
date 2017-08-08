package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import ru.elite.core.DefaultGalaxy;
import ru.elite.utils.edlog.entities.nodes.ItemNode;

import java.util.*;

public class RankEvent extends JournalEventImpl {
    private final Map<String, Integer> ranks;

    public RankEvent(JsonNode node) {
        super(node);
        ranks = new HashMap<>();
        readRanks();
    }

    private void readRanks(){
        for (Iterator<Map.Entry<String,JsonNode>> iterator = node.fields(); iterator.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            JsonNode n = entry.getValue();
            if (n.isNumber()){
                ranks.put(entry.getKey(), n.asInt());
            }
        }
    }

    public Map<String, Integer> getRanks(){
        return ranks;
    }
}
