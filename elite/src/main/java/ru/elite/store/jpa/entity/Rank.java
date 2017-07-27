package ru.elite.store.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Rank {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int value;

    protected Rank() {
    }

    public Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rank)) return false;

        Rank rank = (Rank) o;

        return Objects.equals(name, rank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
