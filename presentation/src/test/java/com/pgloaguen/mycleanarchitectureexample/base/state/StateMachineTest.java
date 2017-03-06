package com.pgloaguen.mycleanarchitectureexample.base.state;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.observers.TestObserver;

import static com.pgloaguen.mycleanarchitectureexample.base.state.StateMachineTest.EventValue.ERROR;
import static com.pgloaguen.mycleanarchitectureexample.base.state.StateMachineTest.EventValue.LOAD;
import static com.pgloaguen.mycleanarchitectureexample.base.state.StateMachineTest.EventValue.NEW_DATA;
import static com.pgloaguen.mycleanarchitectureexample.base.state.StateMachineTest.EventValue.NO_DATA;
import static com.pgloaguen.mycleanarchitectureexample.base.state.StateMachineTest.StateValue.SHOW_DATA;
import static com.pgloaguen.mycleanarchitectureexample.base.state.StateMachineTest.StateValue.SHOW_EMPTY;
import static com.pgloaguen.mycleanarchitectureexample.base.state.StateMachineTest.StateValue.SHOW_ERROR;
import static com.pgloaguen.mycleanarchitectureexample.base.state.StateMachineTest.StateValue.SHOW_LOADING;

/**
 * Created by paul on 27/02/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class StateMachineTest {

    public enum StateValue {
        SHOW_EMPTY, SHOW_LOADING, SHOW_DATA, SHOW_ERROR
    }

    public enum EventValue {
        LOAD, NEW_DATA, NO_DATA, ERROR
    }

    StateMachine.State<StateValue, EventValue, String> stateShowEmpty = new StateMachine.State<>(SHOW_EMPTY);
    StateMachine.State<StateValue, EventValue, String> stateShowLoading = new StateMachine.State<>(SHOW_LOADING);
    StateMachine.State<StateValue, EventValue, String> stateShowData = new StateMachine.State<>(SHOW_DATA);
    StateMachine.State<StateValue, EventValue, String> stateShowError = new StateMachine.State<>(SHOW_ERROR);

    @Before
    public void setup() {
        stateShowEmpty
                .addEvent(LOAD, stateShowLoading);

        stateShowLoading
                .addEvent(NEW_DATA, stateShowData)
                .addEvent(NO_DATA, stateShowEmpty)
                .addEvent(ERROR, stateShowError);

        stateShowData
                .addEvent(ERROR, stateShowError);

        stateShowError
                .addEvent(LOAD, stateShowLoading);
    }

    @Test
    public void testAnExceptionOccuredWhenAStateDoesNotHandleTheEvent() throws InterruptedException {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        TestObserver<StateMachine.State<StateValue, EventValue, String>> testObservable = stateMachine.observeState().test();
        stateMachine.nextState(ERROR);
        testObservable.assertError(IllegalStateException.class);
    }

    @Test
    public void testAnExceptionNotOccuredWhenAStateDoesNotHandleTheEventAndErrorIsIgnored() throws InterruptedException {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        TestObserver<StateMachine.State<StateValue, EventValue, String>> testObservable = stateMachine.observeState().test();
        stateMachine.nextState(ERROR, true);
        testObservable.assertValueAt(1, state -> state.id == SHOW_EMPTY);
    }


    @Test
    public void testInit() {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        stateMachine.observeState().test().assertValueAt(0, state -> state.id == SHOW_EMPTY);
    }

    @Test
    public void testInitWhenSomeEventAlreadySend() {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        stateMachine.nextState(LOAD);

        stateMachine.observeState().test().assertValueAt(0, state -> state.id == SHOW_LOADING);
    }

    @Test
    public void testLoadingStep() {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        TestObserver<StateMachine.State<StateValue, EventValue, String>> testObservable = stateMachine.observeState().test();
        stateMachine.nextState(LOAD);

        testObservable
                .assertValueAt(1, state -> state.id == SHOW_LOADING && state.data == null);
    }

    @Test
    public void testShowDataStep() {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        TestObserver<StateMachine.State<StateValue, EventValue, String>> testObservable = stateMachine.observeState().test();
        stateMachine.nextState(LOAD);
        stateMachine.nextState(NEW_DATA, __ -> "newData");

        testObservable
                .assertValueAt(2, state -> state.id == SHOW_DATA && state.data.equals("newData"));
    }

    @Test
    public void testShowEmptyStep() {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        TestObserver<StateMachine.State<StateValue, EventValue, String>> testObservable = stateMachine.observeState().test();
        stateMachine.nextState(LOAD);
        stateMachine.nextState(NO_DATA);

        testObservable
                .assertValueAt(2, state -> state.id == SHOW_EMPTY && state.data == null);
    }

    @Test
    public void testErrorEmptyStep() {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        TestObserver<StateMachine.State<StateValue, EventValue, String>> testObservable = stateMachine.observeState().test();
        stateMachine.nextState(LOAD);
        stateMachine.nextState(ERROR);

        testObservable
                .assertValueAt(2, state -> state.id == SHOW_ERROR && state.data == null);
    }

    @Test
    public void testErrorWithDataStep() {
        StateMachine<StateValue, EventValue, String> stateMachine = new StateMachine<>(stateShowEmpty);
        TestObserver<StateMachine.State<StateValue, EventValue, String>> testObservable = stateMachine.observeState().test();
        stateMachine.nextState(LOAD);
        stateMachine.nextState(NEW_DATA, __ -> "newData");
        stateMachine.nextState(ERROR, it -> it);

        testObservable
                .assertValueAt(3, state -> state.id == SHOW_ERROR && "newData".equals(state.data));
    }
}