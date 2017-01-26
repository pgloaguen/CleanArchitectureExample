package com.pgloaguen.data.repository;


import com.pgloaguen.data.transformer.RepoDetailsEntityTransformer;
import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;

import io.reactivex.Observable;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoDetailsRepositoryImpl implements GetUserRepoDetailsRepository {

    private final com.pgloaguen.data.net.GetUserRepoDetailsEndpoint userRepoWS;
    private final RepoDetailsEntityTransformer repoEntityTransformer;

    public GetUserRepoDetailsRepositoryImpl(com.pgloaguen.data.net.GetUserRepoDetailsEndpoint userRepoWS, RepoDetailsEntityTransformer repoEntityTransformer) {
        this.userRepoWS = userRepoWS;
        this.repoEntityTransformer = repoEntityTransformer;
    }

    @Override
    public Observable<RepoDetailsEntity> fetchUserRepoDetails(String user, String repoName) {
        return userRepoWS.fetch(user, repoName).map(repoEntityTransformer::transform).flatMapObservable(Observable::just);
    }
}
