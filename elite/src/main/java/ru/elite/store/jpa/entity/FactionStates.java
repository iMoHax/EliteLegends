package ru.elite.store.jpa.entity;

import ru.elite.core.STATE_STATUS;
import ru.elite.core.STATE_TYPE;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
public class FactionStates {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private STATE_TYPE state;

    @Column(nullable = false)
    private STATE_STATUS status;


    public FactionStates() {
    }

    public FactionStates(STATE_TYPE state, STATE_STATUS status) {
        this.state = state;
        this.status = status;
    }

    public STATE_TYPE getState() {
        return state;
    }

    public void setState(STATE_TYPE state) {
        this.state = state;
    }

    public STATE_STATUS getStatus() {
        return status;
    }

    public void setStatus(STATE_STATUS status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactionStates)) return false;

        FactionStates that = (FactionStates) o;

        return Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}
