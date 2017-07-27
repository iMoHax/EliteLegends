package ru.elite.store.jpa.entity;

import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;
import ru.elite.core.ITEM_STATE_TYPE;
import ru.elite.entity.Group;
import ru.elite.entity.Item;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NamedQueries({
        @NamedQuery(name = "Item.findAll", query = "select i from ItemImpl i"),
        @NamedQuery(name = "Item.findByName", query = "select i from ItemImpl i where i.name = :name"),
        @NamedQuery(name = "Item.findByEID", query = "select i from ItemImpl i where i.eid = :eid"),
        @NamedQuery(name = "Item.deleteAll", query = "delete from ItemImpl")
})
@Table(name = "ELITE_ITEMS")
public class ItemImpl implements Item {

    @Id
    @GeneratedValue
    private Long id;

    private Long eid;

    @Column(updatable = false, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = GroupImpl.class, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ElementCollection
    @CollectionTable(name = "ELITE_ITEMS_FACTION_RULES",  joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "faction_id" )
    private List<FactionRules> factionRules = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "ELITE_ITEMS_GOVERNMENT_RULES",  joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "government_id" )
    private List<GovernmentRules> governmentRules = new ArrayList<>();

    protected ItemImpl() {
    }

    public ItemImpl(String name, Group group) {
        this.name = name;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Long getEID() {
        return eid;
    }

    @Override
    public void setEID(Long eid) {
        this.eid = eid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
    }

    public List<FactionRules> getFactionRules() {
        return factionRules;
    }

    public void setFactionRules(List<FactionRules> factionRules) {
        this.factionRules = factionRules;
    }

    public List<GovernmentRules> getGovernmentRules() {
        return governmentRules;
    }

    public void setGovernmentRules(List<GovernmentRules> governmentRules) {
        this.governmentRules = governmentRules;
    }


    @Override
    public boolean isLegal(FACTION faction) {
        return factionRules.stream().anyMatch(r -> r.getFaction() == faction && r.isLegal());
    }

    @Override
    public boolean isIllegal(FACTION faction) {
        return factionRules.stream().anyMatch(r -> r.getFaction() == faction && r.isIllegal());
    }

    @Override
    public boolean isLegal(GOVERNMENT government) {
        return governmentRules.stream().anyMatch(r -> r.getGovernment() == government && r.isLegal());
    }

    @Override
    public boolean isIllegal(GOVERNMENT government) {
        return governmentRules.stream().anyMatch(r -> r.getGovernment() == government && r.isIllegal());
    }

    @Override
    public Collection<FACTION> getIllegalFactions() {
        return factionRules.stream().filter(FactionRules::isIllegal).map(FactionRules::getFaction).collect(Collectors.toList());
    }

    @Override
    public void setIllegal(FACTION faction, boolean illegal) {
        if (illegal){
            Optional<FactionRules> rule = factionRules.stream().filter(r -> r.getFaction() == faction).findAny();
            if (rule.isPresent()){
                rule.get().setState(ITEM_STATE_TYPE.ILLEGAL);
            } else {
                factionRules.add(new FactionRules(faction, ITEM_STATE_TYPE.ILLEGAL));
            }
        } else {
            factionRules.removeIf(r -> r.getFaction() == faction && r.isIllegal());
        }
    }

    @Override
    public Collection<GOVERNMENT> getIllegalGovernments() {
        return governmentRules.stream().filter(GovernmentRules::isIllegal).map(GovernmentRules::getGovernment).collect(Collectors.toList());
    }

    @Override
    public void setIllegal(GOVERNMENT government, boolean illegal) {
        if (illegal){
            Optional<GovernmentRules> rule = governmentRules.stream().filter(r -> r.getGovernment() == government).findAny();
            if (rule.isPresent()){
                rule.get().setState(ITEM_STATE_TYPE.ILLEGAL);
            } else {
                governmentRules.add(new GovernmentRules(government, ITEM_STATE_TYPE.ILLEGAL));
            }
        } else {
            governmentRules.removeIf(r -> r.getGovernment() == government && r.isIllegal());
        }
    }

    @Override
    public Collection<FACTION> getLegalFactions() {
        return factionRules.stream().filter(FactionRules::isLegal).map(FactionRules::getFaction).collect(Collectors.toList());
    }

    @Override
    public void setLegal(FACTION faction, boolean legal) {
        if (legal){
            Optional<FactionRules> rule = factionRules.stream().filter(r -> r.getFaction() == faction).findAny();
            if (rule.isPresent()){
                rule.get().setState(ITEM_STATE_TYPE.LEGAL);
            } else {
                factionRules.add(new FactionRules(faction, ITEM_STATE_TYPE.LEGAL));
            }
        } else {
            factionRules.removeIf(r -> r.getFaction() == faction && r.isLegal());
        }
    }

    @Override
    public Collection<GOVERNMENT> getLegalGovernments() {
        return governmentRules.stream().filter(GovernmentRules::isLegal).map(GovernmentRules::getGovernment).collect(Collectors.toList());
    }

    @Override
    public void setLegal(GOVERNMENT government, boolean legal) {
        if (legal){
            Optional<GovernmentRules> rule = governmentRules.stream().filter(r -> r.getGovernment() == government).findAny();
            if (rule.isPresent()){
                rule.get().setState(ITEM_STATE_TYPE.LEGAL);
            } else {
                governmentRules.add(new GovernmentRules(government, ITEM_STATE_TYPE.LEGAL));
            }
        } else {
            governmentRules.removeIf(r -> r.getGovernment() == government && r.isLegal());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ItemImpl){
            ItemImpl item = (ItemImpl) o;
            if (id != null && item.id != null && !Objects.equals(id, item.id)) return false;
        }
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;
        return Objects.equals(name, item.getName()) &&
               Objects.equals(group, item.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, group);
    }

}
