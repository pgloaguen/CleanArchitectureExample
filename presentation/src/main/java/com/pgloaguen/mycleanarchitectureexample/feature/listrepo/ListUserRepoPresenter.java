package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.navigator.Navigator;
import com.pgloaguen.mycleanarchitectureexample.presenter.RemoteDataListWithRefreshingStatePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by paul on 20/01/2017.
 */

public class ListUserRepoPresenter extends RemoteDataListWithRefreshingStatePresenter<RepoEntity, String> {

    private final Navigator navigator;

    @Inject
    public ListUserRepoPresenter(UseCase<List<RepoEntity>, String> getUserRepoUseCase, Navigator navigator) {
        super(getUserRepoUseCase);
        this.navigator = navigator;
    }

    @Override
    public Observable<List<RepoEntity>> executeUseCase(@NonNull UseCase<List<RepoEntity>, String> useCase) {
        return useCase.execute("pgloaguen")
                    .flatMapSingle(l -> Observable.fromIterable(l).map(r -> r.desc().isEmpty() ? RepoEntity.create(r.id(), r.name(), "No description") : r).toList());

    }

    public void onRepoClick(RepoEntity repoEntity){
        navigator.showRepoDetails("pgloaguen", repoEntity.name());
    }
}
