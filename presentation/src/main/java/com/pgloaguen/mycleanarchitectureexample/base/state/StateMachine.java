package com.pgloaguen.mycleanarchitectureexample.base.state;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by paul on 27/02/2017.
 */

public class StateMachine<I, E, D> {

    private PublishSubject<Event<E, D>> eventPublisher = PublishSubject.create();
    private BehaviorSubject<State<I, E, D>> stateObservable;

    public StateMachine(State<I, E, D> firstState) {
        stateObservable = BehaviorSubject.create();
        eventPublisher.scan(firstState, this::nextState).subscribe(stateObservable);
    }

    public Observable<State<I, E, D>> observeState() {
        return stateObservable;
    }

    public void nextState(E event, Reducer<D> data) {
        eventPublisher.onNext(new Event<>(event, data));
    }

    public void nextState(E event) {
        nextState(event, __ -> null);
    }

    private State<I, E, D> nextState(State<I, E, D> state, Event<E, D> event) {
        State<I, E, D> newState = state.nextState(event.id, event.reducer.updateData(state.data));
        if (newState == null) {
            throw new IllegalStateException("The event " + event.id + " is not handle by the state " + state);
        }
        return newState;
    }

    public interface Reducer<D>  {
        D updateData(D oldData);
    }

    private static class Event<E, D> {
        private E id;
        Reducer<D> reducer;

        private Event(E id, Reducer<D> reducer) {
            this.id = id;
            this.reducer = reducer;
        }
    }

    public static class State<I, E, D> {
        public final I id;
        public final D data;
        private final HashMap<E, State<I, E, D>> events;

        public State(I id) {
           this(id, null);
        }

        public State(I id, D data) {
            this(id, data, new HashMap<>());
        }

        private State(I id, D data, HashMap<E, State<I, E, D>> events) {
            this.id = id;
            this.data = data;
            this.events = events;
        }

        public State<I, E, D> addEvent(E event, State<I, E, D> nextState) {
            events.put(event, nextState);
            return this;
        }

        State<I, E, D> nextState(E event, D data) {
            return events.containsKey(event) ? events.get(event).copyWithData(data) : null;
        }

        State<I, E, D> copyWithData(D data) {
            return new State<>(id, data, events);
        }

        @Override
        public String toString() {
            return "State{" +
                    "id=" + id +
                    '}';
        }
    }
}
