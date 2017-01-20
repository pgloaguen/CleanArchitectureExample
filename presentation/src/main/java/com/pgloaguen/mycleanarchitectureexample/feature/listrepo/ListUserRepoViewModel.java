package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.pgloaguen.domain.entity.RepoEntity;

import java.util.List;

/**
 * Created by paul on 20/01/2017.
 */

@AutoValue
public abstract class ListUserRepoViewModel {
    @NonNull
    public abstract List<RepoEntity> datas();

    @Nullable
    public abstract Throwable error();

    public abstract boolean isLoading();

    public static ListUserRepoViewModel create(@NonNull List<RepoEntity> datas, @Nullable Throwable error, boolean isLoading) {
        return new AutoValue_ListUserRepoViewModel(datas, error, isLoading);
    }
}
