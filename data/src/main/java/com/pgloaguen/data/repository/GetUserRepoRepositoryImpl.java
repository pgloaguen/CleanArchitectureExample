package com.pgloaguen.data.repository;


import com.pgloaguen.data.cache.Cache;
import com.pgloaguen.data.model.Repo;
import com.pgloaguen.data.net.GetUserRepoEndpoint;
import com.pgloaguen.data.net.utils.ConnectionFilter;
import com.pgloaguen.data.net.utils.ConnectionUtils;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.entity.RepoEntity;
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

    @Inject
    public GetUserRepoRepositoryImpl(
            GetUserRepoEndpoint userRepoWS,
            RepoEntityTransformer repoEntityTransformer,
            Cache<Repo> cache,
            ConnectionUtils connectionUtils) {
        this.userRepoWS = userRepoWS;
        this.repoEntityTransformer = repoEntityTransformer;
        this.cache = cache;
        this.connectionUtils = connectionUtils;
    }

    @Override
    public Single<List<RepoEntity>> fetchUserRepo(String user) {
        return Observable.just(true)
                .compose(new ConnectionFilter<>(connectionUtils))
                .flatMap(b -> userRepoWS.list(user).flatMap(it -> cache.save(user, it).andThen(Maybe.just(it)).toSingle()).flatMapObservable(Observable::fromIterable))
                .map(repoEntityTransformer::transform)
                .toList();
    }

    @Override
    public Maybe<List<RepoEntity>> fetchLastUserRepoResult(String user) {
        return cache.getAll(user)
                .flatMapObservable(Observable::fromIterable)
                .map(repoEntityTransformer::transform)
                .toList()
                .flatMapMaybe(it -> it.isEmpty() ? Maybe.empty() : Maybe.just(it));
    }
}
