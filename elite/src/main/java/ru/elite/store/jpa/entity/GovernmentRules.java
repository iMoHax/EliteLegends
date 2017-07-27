package ru.elite.store.jpa.entity;

import ru.elite.core.GOVERNMENT;
import ru.elite.core.ITEM_STATE_TYPE;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
public class GovernmentRules {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GOVERNMENT government;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ITEM_STATE_TYPE state;

    protected GovernmentRules() {
    }

    public GovernmentRules(GOVERNMENT government, ITEM_STATE_TYPE state) {
        this.government = government;
        this.state = state;
    }

    public GOVERNMENT getGovernment() {
        return government;
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
        if (!(o instanceof GovernmentRules)) return false;

        GovernmentRules that = (GovernmentRules) o;

        return Objects.equals(state, that.state) &&
               Objects.equals(government, that.government);
    }

    @Override
    public int hashCode() {
        return Objects.hash(government, state);
    }

}
