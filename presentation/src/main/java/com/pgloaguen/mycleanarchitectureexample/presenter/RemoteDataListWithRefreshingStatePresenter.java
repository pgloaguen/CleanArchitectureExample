package com.pgloaguen.mycleanarchitectureexample.presenter;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by paul on 26/01/2017.
 */

public abstract class RemoteDataListWithRefreshingStatePresenter<T, P> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UseCase<List<T>, P> usecase;
    private final List<T> data = new ArrayList<>();

    private PresenterListener<RemoteDataWithRefreshingState<List<T>>> listener;
    private RemoteDataWithRefreshingState<List<T>> currentModel;

    public RemoteDataListWithRefreshingStatePresenter(UseCase<List<T>, P> usecase) {
        this.usecase = usecase;
    }

    public void onCreate(PresenterListener<RemoteDataWithRefreshingState<List<T>>> listener) {
        this.listener = listener;
        data.clear();
        notify(RemoteDataWithRefreshingState.loadingState());
        executeUseCase();
    }

    public void onDestroy() {
        compositeDisposable.dispose();
        this.listener = null;
    }

    public void askForRefresh() {
        notify(RemoteDataWithRefreshingState.refreshingState(new ArrayList<>(data)));
        executeUseCase();
    }

    public abstract Observable<List<T>> executeUseCase(UseCase<List<T>, P> useCase);

    private void notify(RemoteDataWithRefreshingState<List<T>> model) {
        if (currentModel == null || !currentModel.equals(model)) {
            listener.update(model);
        }
        currentModel = model;
    }

    private void executeUseCase() {
        compositeDisposable.clear();
        compositeDisposable.add(executeUseCase(usecase).subscribe(this::onSuccess, this::onError));
    }

    private void onSuccess(@NonNull List<T> repoEntities) {
        data.clear();
        data.addAll(repoEntities);
        notify(RemoteDataWithRefreshingState.displayDataState(repoEntities));
    }

    private void onError(Throwable throwable) {
        data.clear();
        notify(RemoteDataWithRefreshingState.errorState(throwable.getMessage()));
    }
}
