package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;

/**
 * Created by paul on 01/03/2017.
 */

public interface ListUserRepoView {
    void notify(@NonNull ListUserRepoPresenter.StateValue state, @NonNull  ListUserRepoPresenter.ListUserRepoViewModel model);
}
