package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.mycleanarchitectureexample.di.scope.ActivityScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by paul on 19/01/2017.
 */

@Module
public class PresenterModule {

    @ActivityScope
    @Provides
    public ListUserRepoPresenter provideListRepoPresenter(GetUserRepoUseCase useCase) {
        return new ListUserRepoPresenter(useCase);
    }

}
