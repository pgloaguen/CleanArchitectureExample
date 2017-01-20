package com.pgloaguen.data.interactor;


import android.util.Log;

import com.pgloaguen.data.model.Repo;
import com.pgloaguen.data.net.GetUserRepoWS;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.interactor.GetUserRepoInteractor;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoWSInteractor implements GetUserRepoInteractor {

    private final GetUserRepoWS userRepoWS;
    private final RepoEntityTransformer repoEntityTransformer;

    public GetUserRepoWSInteractor(GetUserRepoWS userRepoWS, RepoEntityTransformer repoEntityTransformer) {
        this.userRepoWS = userRepoWS;
        this.repoEntityTransformer = repoEntityTransformer;
    }

    @Override
    public Observable<List<RepoEntity>> listUserRepo(String user) {
        return userRepoWS.list(user).flatMapObservable(Observable::fromIterable).map(repoEntityTransformer::transform).toList().flatMapObservable(Observable::just);
    }
}
