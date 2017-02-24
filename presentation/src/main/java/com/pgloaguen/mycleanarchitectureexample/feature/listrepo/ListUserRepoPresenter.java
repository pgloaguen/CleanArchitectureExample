package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.RemoteDataWithRefreshingStatePresenter;
import com.pgloaguen.mycleanarchitectureexample.navigator.Navigator;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by paul on 20/01/2017.
 */

public class ListUserRepoPresenter extends RemoteDataWithRefreshingStatePresenter<List<RepoEntity>, GetUserRepoUseCase.Param> {

    private final Navigator navigator;

    @Inject
    public ListUserRepoPresenter(UseCase<List<RepoEntity>, GetUserRepoUseCase.Param> getUserRepoUseCase, Navigator navigator) {
        super(getUserRepoUseCase);
        this.navigator = navigator;
    }

    @Override
    public Observable<List<RepoEntity>> executeUseCase(@NonNull UseCase<List<RepoEntity>, GetUserRepoUseCase.Param> useCase, boolean invalidateData) {
        return useCase.execute(GetUserRepoUseCase.Param.create("pgloaguen", invalidateData))
                    .flatMapSingle(l -> Observable.fromIterable(l).map(r -> r.desc().isEmpty() ? RepoEntity.create(r.id(), r.name(), "No description") : r).toList());

    }

    public void onRepoClick(RepoEntity repoEntity){
        navigator.showRepoDetails("pgloaguen", repoEntity.name());
    }
}
