package ru.elite.store.jpa.entity;

import ru.elite.core.*;
import ru.elite.entity.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "Body.getNamesInSystem", query = "select b.name from BodyImpl b where b.starSystem.id = :starSystemId"),
        @NamedQuery(name = "Body.findInSystemByName", query = "select b from BodyImpl b where b.name = :name and b.starSystem.id = :starSystemId"),
        @NamedQuery(name = "Body.findByEID", query = "select b from BodyImpl b where b.eid = :eid"),
        @NamedQuery(name = "Body.deleteFromSystemByName", query = "delete from BodyImpl b where b.name = :name and b.starSystem.id = :starSystemId")
})
@Table(name = "ELITE_BODIES")
public class BodyImpl implements Body {

    @Id
    @GeneratedValue
    private Long id;

    private Long eid;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StarSystemImpl.class, optional = false)
    @JoinColumn(name = "system_id", updatable = false, nullable = false)
    private StarSystem starSystem;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BODY_TYPE type;


    protected BodyImpl() {
    }

    protected BodyImpl(StarSystem starSystem, String name, BODY_TYPE type) {
        this.starSystem = starSystem;
        this.name = name;
        this.type = type;
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
    public StarSystem getStarSystem() {
        return starSystem;
    }

    @Override
    public void removeStarSystem() {
        this.starSystem = null;
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
    public BODY_TYPE getType() {
        return type;
    }

    @Override
    public void setType(BODY_TYPE type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof BodyImpl){
            BodyImpl body = (BodyImpl) o;
            if (id != null && body.id != null && !Objects.equals(id, body.id)) return false;
        }
        if (!(o instanceof Body)) return false;

        Body station = (Body) o;

        return Objects.equals(name, station.getName()) &&
               Objects.equals(starSystem, station.getStarSystem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(starSystem, name);
    }

}
