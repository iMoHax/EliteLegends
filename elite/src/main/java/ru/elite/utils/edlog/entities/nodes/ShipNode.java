package ru.elite.utils.edlog.entities.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.store.imp.entities.ShipData;
import ru.elite.store.imp.entities.ShipDataBase;
import ru.elite.store.imp.entities.SlotData;

import java.util.*;
import java.util.stream.Collectors;

public class ShipNode {
    private final JsonNode node;

    public ShipNode(JsonNode node) {
        this.node = node;
    }

    public int getShipId(){
        JsonNode n = node.get("ShipID");
        if (n == null || !n.isNumber()){
            throw new IllegalArgumentException("Event don't have correct ShipID attribute");
        }
        return n.asInt();
    }

    @Nullable
    public String getShipType(){
        JsonNode n = node.get("Ship");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public String getShipName(){
        JsonNode n = node.get("ShipName");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public String getShipIdent(){
        JsonNode n = node.get("ShipIdent");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public Double getFuelLevel(){
        JsonNode n = node.get("FuelLevel");
        return n != null && n.isNumber() ? n.asDouble() : null;
    }

    @Nullable
    public Double getFuelCapacity(){
        JsonNode n = node.get("FuelCapacity");
        return n != null && n.isNumber() ? n.asDouble() : null;
    }

    @Nullable
    public Collection<ModuleNode> getModules(){
        JsonNode n = node.get("Modules");
        if (n == null || !n.isArray()) {
            return null;
        }
        Collection<ModuleNode> nodes = new ArrayList<>();
        for (Iterator<JsonNode> iterator = n.elements(); iterator.hasNext(); ) {
            JsonNode jsonNode = iterator.next();
            nodes.add(new ModuleNode(jsonNode));
        }
        return nodes;
    }

    public ShipData asImportData(){
        Collection<ModuleNode> nodes = ShipNode.this.getModules();
        final List<SlotData> slots = nodes != null ? nodes.stream().map(ModuleNode::asImportData).collect(Collectors.toList()) : null;
        return new ShipDataBase() {
            @Override
            public long getSid() {
                return ShipNode.this.getShipId();
            }

            @Override
            public String getType() {
                return ShipNode.this.getShipType();
            }

            @Override
            public Optional<String> getName() {
                return Optional.ofNullable(ShipNode.this.getShipName());
            }

            @Override
            public Optional<String> getIdent() {
                return Optional.ofNullable(ShipNode.this.getShipIdent());
            }

            @Override
            public Optional<Double> getFuel() {
                return Optional.ofNullable(ShipNode.this.getFuelLevel());
            }

            @Override
            public Optional<Double> getTank() {
                return Optional.ofNullable(ShipNode.this.getFuelCapacity());
            }

            @Nullable
            @Override
            public Collection<SlotData> getSlots() {
                return slots;
            }
        };
    }

}
