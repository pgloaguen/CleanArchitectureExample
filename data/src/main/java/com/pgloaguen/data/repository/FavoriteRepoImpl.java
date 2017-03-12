package com.pgloaguen.data.repository;

import android.support.annotation.NonNull;

import com.pgloaguen.data.cache.Cache;
import com.pgloaguen.domain.repository.FavoriteRepoRepository;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
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
        return cache.save(getKey(repoId), true);
    }

    @Override
    public Completable unfavoriteRepo(long repoId) {
        return cache.save(getKey(repoId), false);
    }

    @Override
    public Single<Boolean> isFavorite(long repoId) {
        return cache.get(getKey(repoId)).switchIfEmpty(Maybe.just(false)).toSingle();
    }

    @Override
    public Maybe<Boolean> isFavoriteLastFetch(long repoId) {
        return isFavorite(repoId).toMaybe();
    }

    @Override
    public Observable<Boolean> registerFavoriteUpdate(long repoId) {
        return cache.register(getKey(repoId));
    }

    @NonNull
    private String getKey(long repoId) {
        return String.valueOf(repoId);
    }
}
