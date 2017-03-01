package com.pgloaguen.data.repository;

import com.pgloaguen.data.cache.Cache;
import com.pgloaguen.domain.repository.FavoriteRepoRepository;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by paul on 24/02/2017.
 */

public class FavoriteRepoImpl implements FavoriteRepoRepository {

    private final Cache<Boolean> cache;

    public FavoriteRepoImpl(Cache<Boolean> cache) {
        this.cache = cache;
    }

    @Override
    public Completable favoriteRepo(long repoId) {
        return cache.save(String.valueOf(repoId), true);
    }

    @Override
    public Completable unfavoriteRepo(long repoId) {
        return cache.save(String.valueOf(repoId), false);
    }

    @Override
    public Single<Boolean> isFavorite(long repoId) {
        return cache.get(String.valueOf(repoId)).switchIfEmpty(Maybe.just(false)).toSingle();
    }
}
