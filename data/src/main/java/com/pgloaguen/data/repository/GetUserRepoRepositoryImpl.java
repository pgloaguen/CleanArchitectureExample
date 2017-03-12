package com.pgloaguen.data.repository;


import com.pgloaguen.data.cache.Cache;
import com.pgloaguen.data.model.Repo;
import com.pgloaguen.data.net.GetUserRepoEndpoint;
import com.pgloaguen.data.net.utils.ConnectionFilter;
import com.pgloaguen.data.net.utils.ConnectionUtils;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.FavoriteRepoRepository;
import com.pgloaguen.domain.repository.GetUserRepoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoRepositoryImpl implements GetUserRepoRepository {

    private final GetUserRepoEndpoint userRepoWS;
    private final RepoEntityTransformer repoEntityTransformer;
    private final Cache<Repo> cache;
    private final ConnectionUtils connectionUtils;
    private final FavoriteRepoRepository favoriteRepoRepository;

    @Inject
    public GetUserRepoRepositoryImpl(
            GetUserRepoEndpoint userRepoWS,
            RepoEntityTransformer repoEntityTransformer,
            Cache<Repo> cache,
            ConnectionUtils connectionUtils,
            FavoriteRepoRepository favoriteRepoRepository) {
        this.userRepoWS = userRepoWS;
        this.repoEntityTransformer = repoEntityTransformer;
        this.cache = cache;
        this.connectionUtils = connectionUtils;
        this.favoriteRepoRepository = favoriteRepoRepository;
    }

    @Override
    public Single<List<RepoEntity>> fetchUserRepo(String user) {
        return Observable.just(true)
                .compose(new ConnectionFilter<>(connectionUtils))
                .flatMap(b -> userRepoWS.list(user).flatMap(it -> cache.save(user, it).andThen(Maybe.just(it)).toSingle()).flatMapObservable(Observable::fromIterable))
                .map(repoEntityTransformer::transform)
                .flatMap(this::isFavorite)
                .toList();
    }

    @Override
    public Maybe<List<RepoEntity>> fetchLastUserRepoResult(String user) {
        return cache.getAll(user)
                .flatMapObservable(Observable::fromIterable)
                .map(repoEntityTransformer::transform)
                .flatMap(this::isFavoriteLastFetch)
                .toList()
                .flatMapMaybe(it -> it.isEmpty() ? Maybe.empty() : Maybe.just(it));
    }

    @Override
    public Observable<List<RepoEntity>> registerRepoUpdated(String user) {

        Observable<List<RepoEntity>> cacheRegister = cache.registerList(user)
                .flatMapSingle(r -> Observable.fromIterable(r).map(repoEntityTransformer::transform).flatMap(this::isFavoriteLastFetch).toList());
        
        return Observable.merge(
                cacheRegister,
                 Observable.concat(
                         fetchLastUserRepoResult(user).flatMapObservable(Observable::fromIterable).map(RepoEntity::id).toList().toObservable(),
                         cacheRegister.flatMap(Observable::fromIterable).map(RepoEntity::id).toList().toObservable())
                         .switchMap(ids -> Observable.fromIterable(ids)
                                 .flatMap(favoriteRepoRepository::registerFavoriteUpdate)
                                 .flatMap(__ -> fetchLastUserRepoResult(user).toObservable())));
    }

    private Observable<RepoEntity> isFavorite(RepoEntity repoEntity) {
        return Observable.zip(Observable.just(repoEntity), favoriteRepoRepository.isFavorite(repoEntity.id()).toObservable(), RepoEntity::create);
    }

    private Observable<RepoEntity> isFavoriteLastFetch(RepoEntity repoEntity) {
        return Observable.zip(Observable.just(repoEntity), favoriteRepoRepository.isFavoriteLastFetch(repoEntity.id()).toObservable(), RepoEntity::create);
    }
}
