package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.usecase.GetUserRepoDetailsUseCase;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.presenter.RemoteDataWithRefreshingStatePresenter;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by paul on 26/01/2017.
 */

public class RepoDetailsPresenter extends RemoteDataWithRefreshingStatePresenter<RepoDetailsEntity, GetUserRepoDetailsUseCase.Param> {

    private String repoName;
    private String username;

    @Inject
    public RepoDetailsPresenter(UseCase<RepoDetailsEntity, GetUserRepoDetailsUseCase.Param> useCase) {
        super(useCase);
    }

    public void init(String username, String repoName) {
        this.username = username;
        this.repoName = repoName;
    }

    @Override
    public Observable<RepoDetailsEntity> executeUseCase(UseCase<RepoDetailsEntity, GetUserRepoDetailsUseCase.Param> useCase) {
        return useCase.execute(GetUserRepoDetailsUseCase.Param.create(username, repoName));
    }
}
