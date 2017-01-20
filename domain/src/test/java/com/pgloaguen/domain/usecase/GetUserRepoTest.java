package com.pgloaguen.domain.usecase;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.interactor.GetUserRepoInteractor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by paul on 19/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUserRepoTest {

    @Mock
    GetUserRepoInteractor interactor;
    @Mock Consumer<List<RepoEntity>> consumerSuccess;
    @Mock Consumer<? super Throwable> consumerError;

    private GetUserRepoUseCase userRepo;

    @Before
    public void setup() {
        userRepo = new GetUserRepoUseCase(interactor, Schedulers.io(), Schedulers.io());
    }


    @Test
    public void execute() throws Exception {
        List<RepoEntity> values = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        given(interactor.listUserRepo(anyString())).willReturn(Observable.just(values));

        userRepo.execute("").test().assertValue(new Predicate<List<RepoEntity>>() {
            @Override
            public boolean test(List<RepoEntity> repoEntities) throws Exception {
                return !repoEntities.isEmpty();
            }
        });
    }

    @Test
    public void executeFailed() throws Exception {
        given(interactor.listUserRepo(anyString())).willReturn(Observable.<List<RepoEntity>>error(new NullPointerException("null")));

        userRepo.execute("").test().assertError(NullPointerException.class).assertNoValues();
    }

}