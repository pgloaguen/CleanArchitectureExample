package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by paul on 20/01/2017.
 */

public class ListRepoPresenter {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private PresenterListener<ListUserRepoViewModel> listener;
    private final GetUserRepoUseCase getUserRepoUseCase;

    private List<RepoEntity> data;

    public ListRepoPresenter(GetUserRepoUseCase getUserRepoUseCase) {
        this.getUserRepoUseCase = getUserRepoUseCase;
    }

    public void onCreate(PresenterListener<ListUserRepoViewModel> listener) {
        this.listener = listener;
        data = null;
        notify(ListUserRepoViewModel.create(new ArrayList<>(), null, true));
        executeUseCase();
    }

    public void onDestroy() {
        compositeDisposable.dispose();
        this.listener = null;
    }

    public void askForRefresh() {
        notify(ListUserRepoViewModel.create(new ArrayList<>(data), null, true));
        executeUseCase();
    }

    private void notify(ListUserRepoViewModel model) {
        listener.update(model);
    }

    private void executeUseCase() {
        compositeDisposable.dispose();
        compositeDisposable.clear();
        compositeDisposable.add(getUserRepoUseCase.execute("pgloaguen").subscribe(this::onSuccess, this::onError));
    }

    private void onSuccess(List<RepoEntity> repoEntities) {
        data = repoEntities;
        notify(ListUserRepoViewModel.create(new ArrayList<>(data), null, false));
    }

    private void onError(Throwable throwable) {
        data = null;
        notify(ListUserRepoViewModel.create(new ArrayList<>(), throwable, false));
    }
}
