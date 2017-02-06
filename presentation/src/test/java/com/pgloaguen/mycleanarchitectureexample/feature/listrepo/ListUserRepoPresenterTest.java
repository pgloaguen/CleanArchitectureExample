package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.navigator.Navigator;
import com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Created by polo on 22/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ListUserRepoPresenterTest {

    @Mock
    PresenterListener<RemoteDataWithRefreshingState<List<RepoEntity>>> presenterListener;

    @Mock
    GetUserRepoUseCase useCase;

    @Mock
    Navigator navigator;

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
    public void presenterDisplayDetailsWhenRepoClick() {
        presenter.attach(presenterListener);

        presenter.onRepoClick(answer.get(0));
        verify(navigator).showRepoDetails("pgloaguen", answer.get(0).name());
    }
}