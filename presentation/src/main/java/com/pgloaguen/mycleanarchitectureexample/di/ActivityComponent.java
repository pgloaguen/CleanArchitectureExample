package com.pgloaguen.mycleanarchitectureexample.di;

import com.pgloaguen.mycleanarchitectureexample.di.module.ActivityModule;
import com.pgloaguen.mycleanarchitectureexample.di.scope.ActivityScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoActivity;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoFragment;
import com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsActivity;
import com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsFragment;

import dagger.Component;

/**
 * Created by paul on 20/01/2017.
 */

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(ListUserRepoActivity mainActivity);
    void inject(RepoDetailsActivity activity);
    void inject(ListUserRepoFragment listUserRepoFragment);
    void inject(RepoDetailsFragment repoDetailsFragment);
}
