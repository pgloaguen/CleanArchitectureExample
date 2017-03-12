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
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
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

    private GetUserRepo userRepo;

    @Before
    public void setup() {
        userRepo = new GetUserRepo(repository, Schedulers.io(), Schedulers.io());
    }

    @Test
    public void getUserRepoUseCaseHappyCase() throws Exception {
        List<RepoEntity> values = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        List<RepoEntity> lastValues = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        given(repository.fetchUserRepo(anyString())).willReturn(Single.just(values));
        given(repository.fetchLastUserRepoResult(anyString())).willReturn(Maybe.just(lastValues));
        given(repository.registerRepoUpdated(anyString())).willReturn(Observable.empty());

        userRepo.execute(GetUserRepo.Param.create("", false)).test()
                .await()
                .assertValue(repoEntities -> repoEntities == lastValues);
    }

    @Test
    public void getUserRepoUseCaseWithRepoUpdateHappyCase() throws Exception {
        List<RepoEntity> values = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        List<RepoEntity> lastValues = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        List<RepoEntity> updateValues1 = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        List<RepoEntity> updateValues2 = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        Observable<List<RepoEntity>> repoUpdate = Observable.<List<RepoEntity>>create(e -> {
            e.onNext(updateValues1);
            e.onNext(updateValues2);
            e.onComplete();
        });

        given(repository.fetchUserRepo(anyString())).willReturn(Single.just(values));
        given(repository.fetchLastUserRepoResult(anyString())).willReturn(Maybe.just(lastValues));
        given(repository.registerRepoUpdated(anyString())).willReturn(repoUpdate);

        userRepo.execute(GetUserRepo.Param.create("", false))
            .test()
            .await()
            .assertValueAt(0, repoEntities -> repoEntities == lastValues)
            .assertValueAt(1, repoEntities -> repoEntities == updateValues1)
            .assertValueAt(2, repoEntities -> repoEntities == updateValues2);
    }

    @Test
    public void getUserRepoUseCaseWithFailedRepoUpdateHappyCase() throws Exception {
        List<RepoEntity> values = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        List<RepoEntity> lastValues = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        final List<RepoEntity> updateValues1 = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));

        AtomicBoolean firstTime = new AtomicBoolean(true);
        Observable<List<RepoEntity>> repoUpdate = Observable.create(e -> {
            if (firstTime.getAndSet(false)) {
                e.onError(new NullPointerException());
            } else {
                e.onNext(updateValues1);
                e.onComplete();
            }
        });

        given(repository.fetchUserRepo(anyString())).willReturn(Single.just(values));
        given(repository.fetchLastUserRepoResult(anyString())).willReturn(Maybe.just(lastValues));
        given(repository.registerRepoUpdated(anyString())).willReturn(repoUpdate);

        userRepo.execute(GetUserRepo.Param.create("", false))
                .test().await()
                .assertNoErrors()
                .assertValueAt(1, r -> r == updateValues1);
    }

    @Test
    public void getUserRepoUseCaseExceptionThrownSwitchToFetchFreshData() throws Exception {
        List<RepoEntity> values = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        given(repository.fetchUserRepo(anyString())).willReturn(Single.just(values));
        given(repository.fetchLastUserRepoResult(anyString())).willReturn(Maybe.error(new NullPointerException("null")));
        given(repository.registerRepoUpdated(anyString())).willReturn(Observable.empty());

        userRepo.execute(GetUserRepo.Param.create("", false))
                .test()
                .await()
                .assertValue(repoEntities -> repoEntities == values);
    }

    @Test
    public void getUserRepoUseCaseEmptySwitchToFetchFreshData() throws Exception {
        List<RepoEntity> values = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        given(repository.fetchLastUserRepoResult(anyString())).willReturn(Maybe.empty());
        given(repository.fetchUserRepo(anyString())).willReturn(Single.just(values));
        given(repository.registerRepoUpdated(anyString())).willReturn(Observable.empty());

        userRepo.execute(GetUserRepo.Param.create("", false)).test()
                .await()
                .assertValue(repoEntities -> repoEntities == values);
    }

    @Test
    public void getUserRepoUseCaseForceUpdateHappyCase() throws Exception {
        List<RepoEntity> values = Arrays.asList(mock(RepoEntity.class), mock(RepoEntity.class));
        given(repository.fetchUserRepo(anyString())).willReturn(Single.just(values));
        given(repository.registerRepoUpdated(anyString())).willReturn(Observable.empty());

        userRepo.execute(GetUserRepo.Param.create("", true)).test()
                .await()
                .assertValue(repoEntities -> repoEntities == values);
    }

    @Test
    public void getUserRepoUseCaseForceUpdateError() throws Exception {
        given(repository.fetchUserRepo(anyString())).willReturn(Single.error(new NullPointerException("null")));
        given(repository.registerRepoUpdated(anyString())).willReturn(Observable.empty());

        userRepo.execute(GetUserRepo.Param.create("", true)).test()
                .await()
                .assertError(NullPointerException.class);
    }
}