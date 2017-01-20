package com.pgloaguen.mycleanarchitectureexample.di;

import com.pgloaguen.mycleanarchitectureexample.di.module.PresenterModule;
import com.pgloaguen.mycleanarchitectureexample.di.scope.ActivityScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoActivity;

import dagger.Component;

/**
 * Created by paul on 20/01/2017.
 */

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = PresenterModule.class)
public interface ActivityComponent {
    void inject(ListUserRepoActivity mainActivity);
}
