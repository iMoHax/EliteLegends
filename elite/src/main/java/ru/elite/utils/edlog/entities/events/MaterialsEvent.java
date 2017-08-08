package ru.elite.utils.edlog.entities.events;

import com.fasterxml.jackson.databind.JsonNode;
import ru.elite.core.DefaultGalaxy;
import ru.elite.utils.edlog.entities.nodes.ItemNode;

import java.util.ArrayList;
import java.util.Collection;

public class MaterialsEvent extends JournalEventImpl {

    public MaterialsEvent(JsonNode node) {
        super(node);
    }

    public Collection<ItemNode> getMaterials(){
        JsonNode n = node.get("Raw");
        if (n == null || !n.isArray()) {
            throw new IllegalArgumentException("Event Materials don't have Raw attribute");
        }
        Collection<ItemNode> nodes = new ArrayList<>();
        for (JsonNode jsonNode : n) {
            nodes.add(new ItemNode(jsonNode, DefaultGalaxy.GROUP_MATERIALS));
        }
        return nodes;
    }

    public Collection<ItemNode> getManufactured(){
        JsonNode n = node.get("Manufactured");
        if (n == null || !n.isArray()) {
            throw new IllegalArgumentException("Event Materials don't have Manufactured attribute");
        }
        Collection<ItemNode> nodes = new ArrayList<>();
        for (JsonNode jsonNode : n) {
            nodes.add(new ItemNode(jsonNode, DefaultGalaxy.GROUP_MANUFACTURED));
        }
        return nodes;
    }

    public Collection<ItemNode> getDatas(){
        JsonNode n = node.get("Encoded");
        if (n == null || !n.isArray()) {
            throw new IllegalArgumentException("Event Materials don't have Encoded attribute");
        }
        Collection<ItemNode> nodes = new ArrayList<>();
        for (JsonNode jsonNode : n) {
            nodes.add(new ItemNode(jsonNode, DefaultGalaxy.GROUP_DATAS));
        }
        return nodes;
    }

}
