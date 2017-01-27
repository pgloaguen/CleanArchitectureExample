package com.pgloaguen.mycleanarchitectureexample.presenter;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.displayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.emptyState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.errorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.errorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.loadingState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.loadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.refreshingState;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by paul on 26/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class RemoteDataListWithRefreshingStatePresenterTest {

    @Mock
    PresenterListener<RemoteDataWithRefreshingState<List<String>>> presenterListener;

    @Mock
    UseCase<List<String>, String> useCase;

    RemoteDataListWithRefreshingStatePresenter<String, String> presenter;

    List<String> answer;
    List<String> emptyAnswer;
    NullPointerException errorAnswer;

    @Before
    public void setup() {
        presenter = new RemoteDataListWithRefreshingStatePresenter<String, String>(useCase) {
            @Override
            public Observable<List<String>> executeUseCase(UseCase<List<String>, String> useCase) {
                return useCase.execute("");
            }
        };
        answer = new ArrayList<>();
        answer.add("test");
        emptyAnswer = new ArrayList<>();
        errorAnswer = new NullPointerException("null");
    }

    @Test
    public void initHappyCase() {

        given(useCase.execute(anyString())).willReturn(Observable.just(answer));

        presenter.init(presenterListener);
        presenter.onStart();

        verify(presenterListener, times(1)).update(emptyState(new ArrayList<>()));
        verify(presenterListener, times(1)).update(loadingState());
        verify(presenterListener, times(1)).update(displayDataState(answer));
    }

    @Test
    public void initErrorCase() {
        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));

        presenter.init(presenterListener);
        presenter.onStart();

        verify(presenterListener, times(1)).update(emptyState(new ArrayList<>()));
        verify(presenterListener, times(1)).update(loadingState());
        verify(presenterListener, times(1)).update(errorState(errorAnswer.getMessage()));
    }

    @Test
    public void initAndRefreshHappyCase() {
        given(useCase.execute(anyString())).willReturn(Observable.just(answer));

        presenter.init(presenterListener);
        presenter.onStart();
        presenter.askForRefresh();

        verify(presenterListener, times(1)).update(emptyState(new ArrayList<>()));
        verify(presenterListener, times(1)).update(loadingState());
        verify(presenterListener, times(1)).update(refreshingState(answer));
        verify(presenterListener, times(2)).update(displayDataState(answer));
    }

    @Test
    public void initHappyCaseRefreshErrorCase() {
        given(useCase.execute(anyString())).willReturn(Observable.just(answer));
        presenter.init(presenterListener);
        presenter.onStart();

        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));
        presenter.askForRefresh();

        verify(presenterListener, times(1)).update(emptyState(new ArrayList<>()));

        verify(presenterListener, times(1)).update(loadingState());
        verify(presenterListener, times(1)).update(displayDataState(answer));

        verify(presenterListener, times(1)).update(refreshingState(answer));
        verify(presenterListener, times(1)).update(errorWithDisplayDataState(errorAnswer.getMessage(), answer));
    }

    @Test
    public void initErrorThenRefreshHappyCase() {
        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));
        presenter.init(presenterListener);
        presenter.onStart();

        given(useCase.execute(anyString())).willReturn(Observable.just(answer));
        presenter.askForRefresh();

        verify(presenterListener, times(1)).update(emptyState(new ArrayList<>()));
        verify(presenterListener, times(1)).update(loadingState());
        verify(presenterListener, times(1)).update(errorState(errorAnswer.getMessage()));
        verify(presenterListener, times(1)).update(loadingWithErrorState(errorAnswer.getMessage()));
        verify(presenterListener, times(1)).update(displayDataState(answer));
    }

    @Test
    public void initEmptyAnswerThenAskToRefresh() {
        given(useCase.execute(anyString())).willReturn(Observable.just(emptyAnswer));

        presenter.init(presenterListener);
        presenter.onStart();

        given(useCase.execute(anyString())).willReturn(Observable.just(answer));
        presenter.askForRefresh();

        verify(presenterListener, times(2)).update(loadingState());
        verify(presenterListener, times(2)).update(emptyState(new ArrayList<>()));
        verify(presenterListener, times(1)).update(displayDataState(answer));
    }

    @Test
    public void onlyOneRequestAtATime() {
        TestScheduler testScheduler = new TestScheduler();
        given(useCase.execute(anyString())).willReturn(Observable.just(answer).delay(10, TimeUnit.SECONDS, testScheduler));
        presenter.init(presenterListener);
        presenter.onStart();
        presenter.askForRefresh();
        presenter.askForRefresh();

        testScheduler.advanceTimeBy(20, TimeUnit.SECONDS);

        verify(presenterListener, times(1)).update(loadingState());
        verify(presenterListener, times(1)).update(displayDataState(answer));
    }

    @Test
    public void destroyNoMoreRequestShouldBeSend() {

        TestScheduler testScheduler = new TestScheduler();
        given(useCase.execute(anyString())).willReturn(Observable.just(answer).delay(10, TimeUnit.SECONDS, testScheduler));
        presenter.init(presenterListener);
        presenter.onStart();
        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS);
        presenter.askForRefresh();
        presenter.onStop();
        presenter.onDestroy();
        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS);

        verify(presenterListener, times(1)).update(displayDataState(answer));
    }
}
