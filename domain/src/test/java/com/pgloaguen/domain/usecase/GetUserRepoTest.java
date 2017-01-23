package com.pgloaguen.domain.usecase;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Created by paul on 19/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUserRepoTest {

    @Mock
    GetUserRepoRepository repository;

    private GetUserRepoUseCase userRepo;

    @Before
    public void setup() {
        userRepo = new GetUserRepoUseCase(repository, Schedulers.io(), Schedulers.io());
    }


    @Test
    public void getUserRepoUseCaseHappyCase() throws Exception {
        List<RepoEntity> values = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        given(repository.listUserRepo(anyString())).willReturn(Observable.just(values));

        TestObserver<List<RepoEntity>> testObserver = userRepo.execute("").test();
        testObserver.awaitTerminalEvent(500, TimeUnit.MILLISECONDS);
        testObserver.assertValue(new Predicate<List<RepoEntity>>() {
            @Override
            public boolean test(List<RepoEntity> repoEntities) throws Exception {
                return !repoEntities.isEmpty();
            }
        });
    }

    @Test
    public void getUserRepoUseCaseExceptionThrown() throws Exception {
        given(repository.listUserRepo(anyString())).willReturn(Observable.<List<RepoEntity>>error(new NullPointerException("null")));

        TestObserver<List<RepoEntity>> testObserver = userRepo.execute("").test();
        testObserver.awaitTerminalEvent(500, TimeUnit.MILLISECONDS);
        testObserver.assertError(NullPointerException.class).assertNoValues();
    }

}