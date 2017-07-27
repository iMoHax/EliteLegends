package ru.elite.store.jpa.entity;

import ru.elite.core.STATE_STATUS;
import ru.elite.core.STATE_TYPE;
import ru.elite.entity.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Stream;

@Entity
@NamedQueries({
        @NamedQuery(name = "MinorFactionState.getNamesInSystem", query = "select f.faction.name from MinorFactionStateImpl f where f.starSystem.id = :starSystemId"),
        @NamedQuery(name = "MinorFactionState.findInSystemByFaction", query = "select f from MinorFactionStateImpl f where f.faction.id = :factionId and f.starSystem.id = :starSystemId"),
        @NamedQuery(name = "MinorFactionState.findByName", query = "select f from MinorFactionStateImpl f where f.faction.name = :name and f.starSystem.id = :starSystemId"),
        @NamedQuery(name = "MinorFactionState.deleteFromSystemByName", query = "delete from MinorFactionStateImpl f where f.faction.name = :name and f.starSystem.id = :starSystemId")
})
@Table(name = "ELITE_STAR_SYSTEM_FACTIONS")
public class MinorFactionStateImpl implements MinorFactionState {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StarSystemImpl.class, optional = false)
    @JoinColumn(name = "system_id", updatable = false, nullable = false)
    private StarSystem starSystem;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MinorFactionImpl.class, optional = false)
    @JoinColumn(name = "faction_id", updatable = false, nullable = false)
    private MinorFaction faction;

    @Column(nullable = false)
    private float influence;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ELITE_FACTION_STATES", joinColumns = @JoinColumn(name = "f_state_id"))
    @Column(name = "state_id", nullable = false)
    private Set<FactionStates> states = new HashSet<>();


    protected MinorFactionStateImpl() {
    }

    protected MinorFactionStateImpl(StarSystem starSystem, MinorFaction faction, STATE_TYPE state, float influence) {
        this.starSystem = starSystem;
        this.faction = faction;
        this.influence = influence;
        if (state != STATE_TYPE.NONE){
            states.add(new FactionStates(state, STATE_STATUS.ACTIVE));
        }
    }

    public Long getId() {
        return id;
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
    public MinorFaction getFaction() {
        return faction;
    }

    @Override
    public float getInfluence() {
        return influence;
    }

    @Override
    public void setInfluence(float influence) {
        this.influence = influence;
    }

    @Override
    public STATE_TYPE getState() {
        Optional<FactionStates> state = states.stream().filter(f -> f.getStatus() == STATE_STATUS.ACTIVE).findAny();
        return state.isPresent() ? state.get().getState() : STATE_TYPE.NONE;
    }

    @Override
    public void setState(STATE_TYPE state) {
        Optional<FactionStates> s = states.stream().filter(f -> f.getState() == state).findAny();
        if (s.isPresent()){
            if (s.get().getStatus() != STATE_STATUS.ACTIVE){
                states.removeIf(f -> f.getStatus() == STATE_STATUS.ACTIVE);
                s.get().setStatus(STATE_STATUS.ACTIVE);
            }
        } else {
            states.removeIf(f -> f.getStatus() == STATE_STATUS.ACTIVE);
            states.add(new FactionStates(state, STATE_STATUS.ACTIVE));
        }
    }

    @Override
    public Stream<STATE_TYPE> getStates(STATE_STATUS status) {
        return states.stream().filter(f -> f.getStatus() == status).map(FactionStates::getState);
    }

    @Override
    public void addState(STATE_TYPE state, STATE_STATUS status) {
        states.add(new FactionStates(state, status));
    }

    @Override
    public void setStateStatus(STATE_TYPE state, STATE_STATUS status) {
        Optional<FactionStates> s = states.stream().filter(f -> f.getState() == state).findAny();
        if (s.isPresent()){
            s.get().setStatus(status);
        } else {
            states.add(new FactionStates(state, status));
        }
    }

    @Override
    public boolean removeState(STATE_TYPE state) {
        return states.removeIf(f -> f.getState() == state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof MinorFactionStateImpl){
            MinorFactionStateImpl state = (MinorFactionStateImpl) o;
            if (id != null && state.id != null && !Objects.equals(id, state.id)) return false;
        }
        if (!(o instanceof MinorFactionState)) return false;

        MinorFactionState state = (MinorFactionState) o;

        return Objects.equals(faction, state.getFaction()) &&
               Objects.equals(starSystem, state.getStarSystem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(faction, starSystem);
    }

}
