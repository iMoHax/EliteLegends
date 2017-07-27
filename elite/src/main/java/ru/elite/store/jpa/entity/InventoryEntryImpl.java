package ru.elite.store.jpa.entity;

import ru.elite.entity.Commander;
import ru.elite.entity.InventoryEntry;
import ru.elite.entity.Item;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "INVENTORY")
public class InventoryEntryImpl implements InventoryEntry {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CommanderImpl.class, optional = false)
    @JoinColumn(name = "cmdr_id", updatable = false, nullable = false)
    private Commander cmdr;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ItemImpl.class, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private long count;

    protected InventoryEntryImpl() {
    }

    protected InventoryEntryImpl(Commander commander, Item item, long count) {
        this.cmdr = commander;
        this.item = item;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Commander getCmdr() {
        return cmdr;
    }

    @Override
    public void removeCmdr(){
        this.cmdr = null;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof InventoryEntryImpl){
            InventoryEntryImpl entry = (InventoryEntryImpl) o;
            if (id != null && entry.id != null && !Objects.equals(id, entry.id)) return false;
        }
        if (!(o instanceof InventoryEntry)) return false;

        InventoryEntry entry = (InventoryEntry) o;
        return Objects.equals(item, entry.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }
}
