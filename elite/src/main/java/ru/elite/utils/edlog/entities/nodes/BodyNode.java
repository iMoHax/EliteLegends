package ru.elite.utils.edlog.entities.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.core.BODY_TYPE;
import ru.elite.store.imp.entities.BodyData;
import ru.elite.store.imp.entities.BodyDataBase;
import ru.elite.store.imp.entities.InventoryEntryData;
import ru.elite.store.imp.entities.InventoryEntryDataBase;
import ru.elite.utils.edlog.EDConverter;

import java.util.Optional;

public class BodyNode {
    private final JsonNode node;

    public BodyNode(JsonNode node) {
        this.node = node;
    }

    @Nullable
    public String getBodyName(){
        JsonNode n = node.get("Body");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public BODY_TYPE getBodyType(){
        JsonNode n = node.get("BodyType");
        return n != null ? EDConverter.asBodyType(n.asText()) : null;
    }

    public BodyData asImportData(){
        return new BodyDataBase() {
            @Override
            public String getName() {
                return BodyNode.this.getBodyName();
            }

            @Override
            public BODY_TYPE getType() {
                return BodyNode.this.getBodyType();
            }
        };
    }

}
