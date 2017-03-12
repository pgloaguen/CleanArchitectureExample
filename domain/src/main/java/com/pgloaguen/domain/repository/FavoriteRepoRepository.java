package com.pgloaguen.domain.repository;


import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by paul on 24/02/2017.
 */

public interface FavoriteRepoRepository {
    Completable favoriteRepo(long repoId);
    Completable unfavoriteRepo(long repoId);
    Single<Boolean> isFavorite(long repoId);
    Maybe<Boolean> isFavoriteLastFetch(long repoId);
    Observable<Boolean> registerFavoriteUpdate(long repoId);
}
