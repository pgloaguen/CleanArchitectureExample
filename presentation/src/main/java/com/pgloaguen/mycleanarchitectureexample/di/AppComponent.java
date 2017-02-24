package com.pgloaguen.mycleanarchitectureexample.di;

import com.pgloaguen.data.di.DataComponent;
import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.GetUserRepoDetailsUseCase;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterCache;
import com.pgloaguen.mycleanarchitectureexample.di.module.PresenterCacheModule;
import com.pgloaguen.mycleanarchitectureexample.di.module.UseCaseModule;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter;
import com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter;

import java.util.List;

import dagger.Component;

/**
 * Created by paul on 19/01/2017.
 */

@AppScope
@Component(modules = {UseCaseModule.class, PresenterCacheModule.class}, dependencies = {DataComponent.class})
public interface AppComponent {
    UseCase<List<RepoEntity>, GetUserRepoUseCase.Param> provideGetUserRepoUseCase();
    UseCase<RepoDetailsEntity, GetUserRepoDetailsUseCase.Param> provideGetUserDetailsRepoUseCase();

    PresenterCache<ListUserRepoPresenter> provideListUserRepoPresenterPresenterCache();
    PresenterCache<RepoDetailsPresenter> provideRepoDetailsPresenterPresenterCache();
}
