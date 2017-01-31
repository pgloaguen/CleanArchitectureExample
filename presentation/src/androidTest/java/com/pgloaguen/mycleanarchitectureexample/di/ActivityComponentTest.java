package com.pgloaguen.mycleanarchitectureexample.di;

import com.pgloaguen.mycleanarchitectureexample.di.module.ActivityModule;
import com.pgloaguen.mycleanarchitectureexample.di.module.MockPresenterModule;
import com.pgloaguen.mycleanarchitectureexample.di.scope.ActivityScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoActivityTest;
import com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsActivityTest;

import dagger.Component;

/**
 * Created by paul on 31/01/2017.
 */

@ActivityScope
@Component(dependencies = {AppComponentTest.class}, modules = {ActivityModule.class, MockPresenterModule.class})
public interface ActivityComponentTest extends ActivityComponent {
    void inject(ListUserRepoActivityTest test);
    void inject(RepoDetailsActivityTest repoDetailsActivityTest);
}
