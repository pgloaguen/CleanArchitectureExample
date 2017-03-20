package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pgloaguen.domain.entity.RepoEntity;

import java.util.Collections;
import java.util.List;

/**
 * Created by polo on 19/03/2017.
 */

public class ListUserRepoVM {

    public enum LoadState {
        NONE, LOADING, REFRESHING
    }


    @NonNull
    public final List<RepoEntity> repoEntities;

    @Nullable
    public final Throwable error;

    @NonNull
    public final LoadState loadState;

    private ListUserRepoVM(@NonNull List<RepoEntity> repoEntities, @NonNull LoadState loadState, @Nullable Throwable error) {
        this.repoEntities = repoEntities;
        this.loadState = loadState;
        this.error = error;
    }

    @Override
    public String toString() {
        return "ListUserRepoVM{" +
                "repoEntities=" + repoEntities +
                ", error=" + error +
                ", loadState=" + loadState +
                '}';
    }

    public static ListUserRepoVM data(@NonNull List<RepoEntity> data) {
        return new ListUserRepoVM(data, LoadState.NONE, null);
    }

    public static ListUserRepoVM data(@NonNull List<RepoEntity> data, @NonNull LoadState loadState) {
        return new ListUserRepoVM(data, loadState, null);
    }

    public static ListUserRepoVM loading() {
        return new ListUserRepoVM(Collections.emptyList(), LoadState.LOADING, null);
    }


    public static ListUserRepoVM refreshing(@NonNull List<RepoEntity> data) {
        return new ListUserRepoVM(data, LoadState.REFRESHING, null);
    }

    public static ListUserRepoVM refreshing(Throwable throwable) {
        return new ListUserRepoVM(Collections.emptyList(), LoadState.REFRESHING, throwable);
    }

    public static ListUserRepoVM empty() {
        return new ListUserRepoVM(Collections.emptyList(), LoadState.NONE, null);
    }

    public static ListUserRepoVM error(@Nullable List<RepoEntity> data, Throwable error) {
        return new ListUserRepoVM(data == null ? Collections.emptyList() : data, LoadState.NONE, error);
    }

    public static ListUserRepoVM error(Throwable error) {
        return new ListUserRepoVM(Collections.emptyList(), LoadState.NONE, error);
    }
}
