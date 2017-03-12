package com.pgloaguen.mycleanarchitectureexample.di.module;


import com.pgloaguen.mycleanarchitectureexample.di.scope.ActivityScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by paul on 31/01/2017.
 */

@Module
public class MockPresenterModule {

    @Provides
    @ActivityScope
    public ListUserRepoPresenter provideListUserRepoPresenter2() {
        return mock(ListUserRepoPresenter.class);
    }
}
