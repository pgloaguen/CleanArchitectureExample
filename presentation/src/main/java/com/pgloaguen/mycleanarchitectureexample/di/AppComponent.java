package com.pgloaguen.mycleanarchitectureexample.di;

import com.pgloaguen.data.di.DataComponent;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoActivity;
import com.pgloaguen.mycleanarchitectureexample.di.module.UseCaseModule;

import dagger.Component;

/**
 * Created by paul on 19/01/2017.
 */

@AppScope
@Component(modules = {UseCaseModule.class}, dependencies = {DataComponent.class})
public interface AppComponent {
    GetUserRepoUseCase provideGetUserRepoUseCase();
}
