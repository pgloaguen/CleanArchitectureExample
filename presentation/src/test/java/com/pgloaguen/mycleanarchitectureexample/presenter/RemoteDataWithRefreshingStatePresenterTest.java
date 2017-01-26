package com.pgloaguen.mycleanarchitectureexample.presenter;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by paul on 26/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class RemoteDataWithRefreshingStatePresenterTest {


    @Mock
    PresenterListener<RemoteDataWithRefreshingState<String>> presenterListener;

    @Mock
    UseCase<String, String> useCase;

    RemoteDataWithRefreshingStatePresenter<String, String> presenter;

    String answer;
    NullPointerException errorAnswer;


    @Before
    public void setup() {
        presenter = new RemoteDataWithRefreshingStatePresenter<String, String>(useCase) {
            @Override
            public Observable<String> executeUseCase(UseCase<String, String> useCase) {
                return useCase.execute("");
            }
        };
        answer = "test";
        errorAnswer = new NullPointerException("null");
    }

    @Test
    public void initHappyCase() {

        given(useCase.execute(anyString())).willReturn(Observable.just(answer));

        presenter.onCreate(presenterListener);

        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.loadingState());
        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.displayDataState(answer));
    }

    @Test
    public void initErrorCase() {
        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));

        presenter.onCreate(presenterListener);

        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.loadingState());
        verify(presenterListener, times(1)).update(any(RemoteDataWithRefreshingState.ErrorState.class));
    }

    @Test
    public void initAndRefreshHappyCase() {
        given(useCase.execute(anyString())).willReturn(Observable.just(answer));

        presenter.onCreate(presenterListener);
        presenter.askForRefresh();

        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.loadingState());
        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.refreshingState(answer));
        verify(presenterListener, times(2)).update(RemoteDataWithRefreshingState.displayDataState(answer));
    }

    @Test
    public void initHappyCaseRefreshErrorCase() {
        given(useCase.execute(anyString())).willReturn(Observable.just(answer));
        presenter.onCreate(presenterListener);

        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));
        presenter.askForRefresh();

        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.loadingState());
        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.displayDataState(answer));

        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.refreshingState(answer));
        verify(presenterListener, times(1)).update(any(RemoteDataWithRefreshingState.ErrorState.class));
    }

    @Test
    public void initErrorThenRefreshHappyCase() {
        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));
        presenter.onCreate(presenterListener);

        given(useCase.execute(anyString())).willReturn(Observable.just(answer));
        presenter.askForRefresh();

        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.loadingState());
        verify(presenterListener, times(1)).update(any(RemoteDataWithRefreshingState.ErrorState.class));
        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.refreshingState(null));
        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.displayDataState(answer));
    }

    @Test
    public void onlyOneRequestAtATime() {
        TestScheduler testScheduler = new TestScheduler();
        given(useCase.execute(anyString())).willReturn(Observable.just(answer).delay(10, TimeUnit.SECONDS, testScheduler));
        presenter.onCreate(presenterListener);
        presenter.askForRefresh();
        presenter.askForRefresh();

        testScheduler.advanceTimeBy(20, TimeUnit.SECONDS);

        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.loadingState());
        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.displayDataState(answer));
    }

    @Test
    public void destroyNoMoreRequestShouldBeSend() {

        TestScheduler testScheduler = new TestScheduler();
        given(useCase.execute(anyString())).willReturn(Observable.just(answer).delay(10, TimeUnit.SECONDS, testScheduler));
        presenter.onCreate(presenterListener);
        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS);
        presenter.askForRefresh();
        presenter.onDestroy();
        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS);

        verify(presenterListener, times(1)).update(RemoteDataWithRefreshingState.displayDataState(answer));
    }

}