package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by polo on 22/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ListUserRepoPresenterTest {

    @Mock
    PresenterListener<ListUserRepoViewModel> presenterListener;

    @Mock
    GetUserRepoUseCase useCase;

    @InjectMocks
    ListUserRepoPresenter presenter;

    List<RepoEntity> answer;
    NullPointerException errorAnswer;

    @Before
    public void setup() {
        answer = new ArrayList<>();
        answer.add(RepoEntity.create(1, "name", "desc"));
        errorAnswer = new NullPointerException("null");
    }

    @Test
    public void initHappyCase() {

        given(useCase.execute(anyString())).willReturn(Observable.just(answer));

        presenter.onCreate(presenterListener);

        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(Collections.emptyList(), null, true));
        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(answer, null, false));
    }

    @Test
    public void initErrorCase() {
        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));

        presenter.onCreate(presenterListener);

        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(Collections.emptyList(), null, true));
        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(Collections.emptyList(), errorAnswer, false));
    }

    @Test
    public void initAndRefreshHappyCase() {
        given(useCase.execute(anyString())).willReturn(Observable.just(answer));

        presenter.onCreate(presenterListener);
        presenter.askForRefresh();

        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(Collections.emptyList(), null, true));
        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(answer, null, true));
        verify(presenterListener, times(2)).update(ListUserRepoViewModel.create(answer, null, false));
    }

    @Test
    public void initHappyCaseRefreshErrorCase() {
        given(useCase.execute(anyString())).willReturn(Observable.just(answer));
        presenter.onCreate(presenterListener);

        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));
        presenter.askForRefresh();

        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(Collections.emptyList(), null, true));
        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(answer, null, false));

        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(answer, null, true));
        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(Collections.emptyList(), errorAnswer, false));
    }

    @Test
    public void initErrorThenRefreshHappyCase() {
        given(useCase.execute(anyString())).willReturn(Observable.error(errorAnswer));
        presenter.onCreate(presenterListener);

        given(useCase.execute(anyString())).willReturn(Observable.just(answer));
        presenter.askForRefresh();

        verify(presenterListener, times(2)).update(ListUserRepoViewModel.create(Collections.emptyList(), null, true));
        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(Collections.emptyList(), errorAnswer, false));
        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(answer, null, false));
    }

    @Test
    public void onlyOneRequestAtATime() {
        TestScheduler testScheduler = new TestScheduler();
        given(useCase.execute(anyString())).willReturn(Observable.just(answer).delay(10, TimeUnit.SECONDS, testScheduler));
        presenter.onCreate(presenterListener);
        presenter.askForRefresh();
        presenter.askForRefresh();

        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS);

        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(Collections.emptyList(), null, true));
        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(answer, null, false));
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

        verify(presenterListener, times(1)).update(ListUserRepoViewModel.create(answer, null, false));
    }
}