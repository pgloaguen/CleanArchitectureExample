package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.GetUserRepo;
import com.pgloaguen.mycleanarchitectureexample.navigator.Navigator;

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
    ListUserRepoView listUserRepoView;

    @Mock
    GetUserRepo useCase;

    @Mock
    Navigator navigator;

    @InjectMocks
    ListUserRepoPresenter presenter;

    List<RepoEntity> answer;
    NullPointerException errorAnswer;

    @Before
    public void setup() {
        answer = new ArrayList<>();
        answer.add(RepoEntity.create(1, "name", "desc", false));
        errorAnswer = new NullPointerException("null");
    }

    @Test
    public void presenterDisplayDetailsWhenRepoClick() {
        presenter.attach(listUserRepoView);

        presenter.onRepoClick(answer.get(0));
        verify(navigator).showRepoDetails("pgloaguen", answer.get(0).name());
    }
}