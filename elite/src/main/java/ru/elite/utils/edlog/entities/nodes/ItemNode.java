package ru.elite.utils.edlog.entities.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import ru.elite.store.imp.entities.InventoryEntryData;
import ru.elite.store.imp.entities.InventoryEntryDataBase;

import java.util.Optional;

public class ItemNode {
    private final JsonNode node;
    private final String defaultGroup;

    public ItemNode(JsonNode node, String defaultGroup) {
        this.node = node;
        this.defaultGroup = defaultGroup;
    }

    public String getName(){
        JsonNode n = node.get("Name");
        if (n == null){
            throw new IllegalArgumentException("Item node don't have Name attribute");
        }
        return n.asText();
    }

    public long getCount(){
        JsonNode n = node.get("Count");
        if (n == null || !n.isNumber()){
            throw new IllegalArgumentException("Item node don't have Count attribute");
        }
        return n.asLong();
    }

    public InventoryEntryData asImportData(){
        return new InventoryEntryDataBase() {
            @Override
            public String getName() {
                return ItemNode.this.getName();
            }

            @Override
            public long getCount() {
                return ItemNode.this.getCount();
            }

            @Override
            public Optional<String> getGroupName() {
                return Optional.ofNullable(defaultGroup);
            }
        };
    }
}
