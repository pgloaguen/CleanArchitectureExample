package com.pgloaguen.data.repository;


import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoRepositoryImpl implements GetUserRepoRepository {

    private final com.pgloaguen.data.net.GetUserRepoEndpoint userRepoWS;
    private final RepoEntityTransformer repoEntityTransformer;

    public GetUserRepoRepositoryImpl(com.pgloaguen.data.net.GetUserRepoEndpoint userRepoWS, RepoEntityTransformer repoEntityTransformer) {
        this.userRepoWS = userRepoWS;
        this.repoEntityTransformer = repoEntityTransformer;
    }

    @Override
    public Observable<List<RepoEntity>> listUserRepo(String user) {
        return userRepoWS.list(user).flatMapObservable(Observable::fromIterable).map(repoEntityTransformer::transform).toList().flatMapObservable(Observable::just);
    }
}
