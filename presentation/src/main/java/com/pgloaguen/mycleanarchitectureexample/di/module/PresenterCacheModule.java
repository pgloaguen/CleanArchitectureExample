package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterCache;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter;
import com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by paul on 01/02/2017.
 */

@Module
public class PresenterCacheModule {

    @AppScope
    @Provides
    public PresenterCache<ListUserRepoPresenter> provideListUserRepoPresenterPresenterCache() {
        return new PresenterCache<>();
    }

    @AppScope
    @Provides
    public PresenterCache<RepoDetailsPresenter> provideRepoDetailsPresenterPresenterCache() {
        return new PresenterCache<>();
    }
}
