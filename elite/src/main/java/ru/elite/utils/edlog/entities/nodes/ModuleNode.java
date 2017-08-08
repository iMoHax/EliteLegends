package ru.elite.utils.edlog.entities.nodes;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import ru.elite.store.imp.entities.SlotData;
import ru.elite.store.imp.entities.SlotDataBase;

import java.util.Optional;

public class ModuleNode {
    private final JsonNode node;

    public ModuleNode(JsonNode node) {
        this.node = node;
    }

    public String getSlot(){
        JsonNode n = node.get("Slot");
        if (n == null){
            throw new IllegalArgumentException("Event don't have Slot attribute");
        }
        return n.asText();
    }

    public String getName(){
        JsonNode n = node.get("Item");
        if (n == null){
            throw new IllegalArgumentException("Event don't have Item attribute");
        }
        return n.asText();
    }

    @Nullable
    public Boolean isOn(){
        JsonNode n = node.get("On");
        return n != null && n.isBoolean() ? n.asBoolean() : null;
    }

    @Nullable
    public Integer getPriority(){
        JsonNode n = node.get("Priority");
        return n != null && n.isNumber() ? n.asInt() : null;
    }

    @Nullable
    public Double getHealth(){
        JsonNode n = node.get("Health");
        return n != null && n.isNumber() ? n.asDouble() : null;
    }

    @Nullable
    public Long getCost(){
        JsonNode n = node.get("Value");
        return n != null && n.isNumber() ? n.asLong() : null;
    }

    @Nullable
    public Long getAmmoClip(){
        JsonNode n = node.get("AmmoInClip");
        return n != null && n.isNumber() ? n.asLong() : null;
    }

    @Nullable
    public Long getAmmoHopper(){
        JsonNode n = node.get("AmmoInHopper");
        return n != null && n.isNumber() ? n.asLong() : null;
    }

    @Nullable
    public String getBlueprint(){
        JsonNode n = node.get("EngineerBlueprint");
        return n != null ? n.asText() : null;
    }

    @Nullable
    public Integer getBlueprintLevel(){
        JsonNode n = node.get("EngineerLevel");
        return n != null && n.isNumber() ? n.asInt() : null;
    }

    public SlotData asImportData(){
        return new SlotDataBase() {
            @Override
            public String getName() {
                return ModuleNode.this.getSlot();
            }

            @Override
            public Optional<Boolean> isActive() {
                return Optional.of(ModuleNode.this.isOn());
            }

            @Override
            public Optional<Integer> getPriority() {
                return Optional.of(ModuleNode.this.getPriority());
            }

            @Override
            public Optional<String> getModuleName() {
                return Optional.of(ModuleNode.this.getName());
            }

            @Override
            public Optional<String> getBlueprint() {
                return Optional.of(ModuleNode.this.getBlueprint());
            }

            @Override
            public Optional<Integer> getBlueprintLevel() {
                return Optional.of(ModuleNode.this.getBlueprintLevel());
            }

            @Override
            public Optional<Double> getHealth() {
                return Optional.of(ModuleNode.this.getHealth());
            }

            @Override
            public Optional<Long> getAmmoClip() {
                return Optional.of(ModuleNode.this.getAmmoClip());
            }

            @Override
            public Optional<Long> getAmmoHopper() {
                return Optional.of(ModuleNode.this.getAmmoHopper());
            }

            @Override
            public Optional<Long> getCost() {
                return Optional.of(ModuleNode.this.getCost());
            }
        };
    }

}
