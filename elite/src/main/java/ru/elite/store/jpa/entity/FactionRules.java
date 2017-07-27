package ru.elite.store.jpa.entity;

import ru.elite.core.FACTION;
import ru.elite.core.ITEM_STATE_TYPE;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
public class FactionRules {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FACTION faction;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ITEM_STATE_TYPE state;

    protected FactionRules() {
    }

    public FactionRules(FACTION faction, ITEM_STATE_TYPE state) {
        this.faction = faction;
        this.state = state;
    }

    public FACTION getFaction() {
        return faction;
    }

    public ITEM_STATE_TYPE getState() {
        return state;
    }

    protected void setState(ITEM_STATE_TYPE state) {
        this.state = state;
    }

    public boolean isLegal(){
        return state.isLegal();
    }

    public boolean isIllegal(){
        return state.isIllegal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactionRules)) return false;

        FactionRules that = (FactionRules) o;

        return Objects.equals(state, that.state) &&
               Objects.equals(faction, that.faction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faction, state);
    }

}
