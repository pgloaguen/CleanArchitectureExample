package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import android.support.annotation.NonNull;

public interface RepoDetailsView {
    void notify(@NonNull RepoDetailsPresenter.StateValue state, @NonNull RepoDetailsPresenter.VM model);
}