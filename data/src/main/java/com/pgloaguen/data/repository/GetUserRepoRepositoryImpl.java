package com.pgloaguen.data.repository;


import com.pgloaguen.data.net.GetUserRepoEndpoint;
import com.pgloaguen.data.net.utils.ConnectionFilter;
import com.pgloaguen.data.net.utils.ConnectionUtils;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoRepositoryImpl implements GetUserRepoRepository {

    private final com.pgloaguen.data.net.GetUserRepoEndpoint userRepoWS;
    private final RepoEntityTransformer repoEntityTransformer;
    private final ConnectionUtils connectionUtils;

    @Inject
    public GetUserRepoRepositoryImpl(GetUserRepoEndpoint userRepoWS, RepoEntityTransformer repoEntityTransformer, ConnectionUtils connectionUtils) {
        this.userRepoWS = userRepoWS;
        this.repoEntityTransformer = repoEntityTransformer;
        this.connectionUtils = connectionUtils;
    }

    @Override
    public Observable<List<RepoEntity>> listUserRepo(String user) {
        return Observable.just(true)
                .compose(new ConnectionFilter<>(connectionUtils))
                .flatMap(b -> userRepoWS.list(user)
                .flatMapObservable(Observable::fromIterable))
                .map(repoEntityTransformer::transform)
                .toList()
                .flatMapObservable(Observable::just);
    }
}
