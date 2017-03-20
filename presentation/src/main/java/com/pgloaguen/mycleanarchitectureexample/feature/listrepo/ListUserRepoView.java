package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.entity.RepoEntity;

import io.reactivex.Observable;

/**
 * Created by paul on 01/03/2017.
 */

public interface ListUserRepoView {
    void notify(@NonNull ListUserRepoVM model);
    Observable<Object> refresh();
    Observable<RepoEntity> favorite();
    Observable<RepoEntity> itemClick();
}
