package com.pgloaguen.domain.usecase;

import com.pgloaguen.domain.repository.FavoriteRepoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * Created by paul on 24/02/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FavoriteRepoWithIdTest {

    @Mock
    FavoriteRepoRepository repository;

    private FavoriteRepoWithId usecase;

    @Before
    public void setup() {
        usecase = new FavoriteRepoWithId(repository, Schedulers.io(), Schedulers.io());
    }

    @Test
    public void toggleUnfavoriteRepo() throws InterruptedException {
        given(repository.isFavorite(anyLong())).willReturn(Single.just(false));
        given(repository.favoriteRepo(anyLong())).willReturn(Completable.complete());

        usecase.execute(0L).test().await().assertValue(true);
    }


    @Test
    public void toggleFavoriteRepo() throws InterruptedException {
        given(repository.isFavorite(anyLong())).willReturn(Single.just(true));
        given(repository.unfavoriteRepo(anyLong())).willReturn(Completable.complete());

        usecase.execute(0L).test().await().assertValue(false);
    }

    @Test
    public void toggleFavoriteRepoFailed() throws InterruptedException {
        given(repository.isFavorite(anyLong())).willReturn(Single.just(true));
        given(repository.unfavoriteRepo(anyLong())).willReturn(Completable.error(new NullPointerException("null")));

        usecase.execute(0L).test().await().assertError(NullPointerException.class);
    }

}