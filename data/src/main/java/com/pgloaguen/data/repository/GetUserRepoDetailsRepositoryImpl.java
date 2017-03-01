package com.pgloaguen.data.repository;


import com.pgloaguen.data.net.GetUserRepoDetailsEndpoint;
import com.pgloaguen.data.net.utils.ConnectionFilter;
import com.pgloaguen.data.net.utils.ConnectionUtils;
import com.pgloaguen.data.transformer.RepoDetailsEntityTransformer;
import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.repository.FavoriteRepoRepository;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoDetailsRepositoryImpl implements GetUserRepoDetailsRepository {

    private final com.pgloaguen.data.net.GetUserRepoDetailsEndpoint userRepoWS;
    private final RepoDetailsEntityTransformer repoEntityTransformer;
    private final ConnectionUtils connectionUtils;
    private final FavoriteRepoRepository favoriteRepoRepository;

    @Inject
    public GetUserRepoDetailsRepositoryImpl(GetUserRepoDetailsEndpoint userRepoWS, RepoDetailsEntityTransformer repoEntityTransformer, ConnectionUtils connectionUtils, FavoriteRepoRepository favoriteRepoRepository) {
        this.userRepoWS = userRepoWS;
        this.repoEntityTransformer = repoEntityTransformer;
        this.connectionUtils = connectionUtils;
        this.favoriteRepoRepository = favoriteRepoRepository;
    }

    @Override
    public Single<RepoDetailsEntity> fetchUserRepoDetails(String user, String repoName) {
        return Observable.just(true)
                .compose(new ConnectionFilter<>(connectionUtils))
                .flatMapSingle(b -> userRepoWS.fetch(user, repoName))
                .map(repoEntityTransformer::transform)
                .flatMap(this::isFavorite)
                .firstOrError();
    }

    private Observable<RepoDetailsEntity> isFavorite(RepoDetailsEntity repoDetailsEntity) {
        return Observable.zip(Observable.just(repoDetailsEntity), favoriteRepoRepository.isFavorite(repoDetailsEntity.id()).toObservable(), RepoDetailsEntity::create);
    }
}
