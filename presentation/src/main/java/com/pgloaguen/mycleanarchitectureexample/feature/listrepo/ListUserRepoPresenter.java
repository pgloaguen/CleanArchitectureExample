package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by paul on 20/01/2017.
 */

public class ListUserRepoPresenter {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private PresenterListener<ListUserRepoViewModel> listener;
    private final GetUserRepoUseCase getUserRepoUseCase;

    @NonNull
    private List<RepoEntity> data = new ArrayList<>();
    private ListUserRepoViewModel currentModel;

    public ListUserRepoPresenter(GetUserRepoUseCase getUserRepoUseCase) {
        this.getUserRepoUseCase = getUserRepoUseCase;
    }

    public void onCreate(PresenterListener<ListUserRepoViewModel> listener) {
        this.listener = listener;
        data.clear();
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
        if (currentModel == null || !currentModel.equals(model)) {
            listener.update(model);
        }
        currentModel = model;
    }

    private void executeUseCase() {
        compositeDisposable.clear();
        compositeDisposable.add(getUserRepoUseCase.execute("pgloaguen").subscribe(this::onSuccess, this::onError));
    }

    private void onSuccess(@NonNull List<RepoEntity> repoEntities) {
        data.clear();
        data.addAll(repoEntities);
        notify(ListUserRepoViewModel.create(new ArrayList<>(repoEntities), null, false));
    }

    private void onError(Throwable throwable) {
        data.clear();
        notify(ListUserRepoViewModel.create(new ArrayList<>(), throwable, false));
    }
}
