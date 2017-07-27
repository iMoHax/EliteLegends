package ru.elite.store.jpa.entity;

import ru.elite.core.FACTION;
import ru.elite.core.GOVERNMENT;
import ru.elite.entity.MinorFaction;
import ru.elite.entity.StarSystem;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "MinorFaction.findAll", query = "select f from MinorFactionImpl f"),
        @NamedQuery(name = "MinorFaction.getNames", query = "select f.name from MinorFactionImpl f"),
        @NamedQuery(name = "MinorFaction.findByName", query = "select f from MinorFactionImpl f where f.name = :name"),
        @NamedQuery(name = "MinorFaction.findByEID", query = "select f from MinorFactionImpl f where f.eid = :eid"),
        @NamedQuery(name = "MinorFaction.deleteAll", query = "delete from MinorFactionImpl")
})
@Table(name = "ELITE_FACTIONS")
public class MinorFactionImpl implements MinorFaction {
    @Id
    @GeneratedValue
    private Long id;

    private Long eid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FACTION faction;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GOVERNMENT government;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StarSystemImpl.class)
    @JoinColumn(name = "home_system_id")
    private StarSystem home;

    private boolean players;


    protected MinorFactionImpl() {
    }

    public MinorFactionImpl(String name, FACTION faction, GOVERNMENT government) {
        this.name = name;
        this.faction = faction;
        this.government = government;
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public FACTION getFaction() {
        return faction;
    }

    @Override
    public void setFaction(FACTION faction) {
        this.faction = faction;
    }

    @Override
    public GOVERNMENT getGovernment() {
        return government;
    }

    @Override
    public void setGovernment(GOVERNMENT government) {
        this.government = government;
    }

    @Override
    public StarSystem getHomeSystem() {
        return home;
    }

    @Override
    public void setHomeSystem(StarSystem home) {
        this.home = home;
    }

    @Override
    public boolean isPlayers() {
        return players;
    }

    @Override
    public void setPlayers(boolean players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof MinorFactionImpl){
            MinorFactionImpl faction = (MinorFactionImpl) o;
            if (id != null && faction.id != null && !Objects.equals(id, faction.id)) return false;
        }
        if (!(o instanceof MinorFactionImpl)) return false;

        MinorFaction that = (MinorFaction) o;

        return  Objects.equals(government, that.getGovernment()) &&
                Objects.equals(faction, that.getFaction()) &&
                Objects.equals(name, that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, government, faction);
    }

}
