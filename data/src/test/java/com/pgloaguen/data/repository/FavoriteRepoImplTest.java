package com.pgloaguen.data.repository;

import com.pgloaguen.data.cache.Cache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Maybe;

import static org.mockito.BDDMockito.given;

/**
 * Created by paul on 27/02/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class FavoriteRepoImplTest {

    @Mock
    Cache<Boolean> cache;

    FavoriteRepoImpl favoriteRepo;

    @Before
    public void setup() {
        favoriteRepo = new FavoriteRepoImpl(cache);
    }

    @Test
    public void isFavoriteCachedAsTrue_ShouldReturnTrue() {
        given(cache.get("10")).willReturn(Maybe.just(true));
        favoriteRepo.isFavorite(10).test().assertValue(true);
    }

    @Test
    public void isFavoriteCachedAsFalse_ShouldReturnFalse() {
        given(cache.get("0")).willReturn(Maybe.just(false));
        favoriteRepo.isFavorite(0).test().assertValue(false);
    }

    @Test
    public void isFavoriteNotCached_ShouldReturnFalse() {
        given(cache.get("5")).willReturn(Maybe.empty());
        favoriteRepo.isFavorite(5).test().assertValue(false);
    }

    @Test
    public void setToFavorite_Success() {
        given(cache.save("5", true)).willReturn(Completable.complete());
        favoriteRepo.favoriteRepo(5).test().assertComplete();
    }

    @Test
    public void setToUnFavorite_Success() {
        given(cache.save("5", false)).willReturn(Completable.complete());
        favoriteRepo.unfavoriteRepo(5).test().assertComplete();
    }

}