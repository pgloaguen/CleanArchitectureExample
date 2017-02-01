package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterCache;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by paul on 01/02/2017.
 */

@Module
public class PresenterCacheModule {

    @AppScope
    @Provides
    public PresenterCache<Object> provideRemoteDataWithRefreshingStatePresenterPresenterManager() {
        return new PresenterCache<>();
    }
}
