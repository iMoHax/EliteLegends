package ru.elite.store.jpa.entity;

import ru.elite.core.GROUP_TYPE;
import ru.elite.entity.Group;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Group.findAll", query = "select g from GroupImpl g"),
        @NamedQuery(name = "Group.findByName", query = "select g from GroupImpl g where g.name = :name"),
        @NamedQuery(name = "Group.deleteAll", query = "delete from GroupImpl")
})
@Table(name = "ELITE_ITEM_GROUPS")
public class GroupImpl implements Group {
    @Id
    @GeneratedValue
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GROUP_TYPE type;

    public GroupImpl() {
    }

    public GroupImpl(String name, GROUP_TYPE type) {
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GROUP_TYPE getType() {
        return type;
    }

    @Override
    public void setType(GROUP_TYPE type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof GroupImpl){
            GroupImpl group = (GroupImpl) o;
            if (id != null && group.id != null && !Objects.equals(id, group.id)) return false;
        }
        if (!(o instanceof Group)) return false;

        Group group = (Group) o;
        return Objects.equals(name, group.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
