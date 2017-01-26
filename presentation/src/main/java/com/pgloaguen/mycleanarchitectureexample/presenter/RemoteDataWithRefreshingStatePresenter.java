package com.pgloaguen.mycleanarchitectureexample.presenter;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by paul on 26/01/2017.
 */

public abstract class RemoteDataWithRefreshingStatePresenter<T, P> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UseCase<T, P> useCase;
    private T data = null;

    private PresenterListener<RemoteDataWithRefreshingState<T>> listener;
    private RemoteDataWithRefreshingState<T> currentModel;

    public RemoteDataWithRefreshingStatePresenter(UseCase<T, P> useCase) {
        this.useCase = useCase;
    }

    public void init(PresenterListener<RemoteDataWithRefreshingState<T>> listener) {
        this.listener = listener;
        data = null;
        notify(RemoteDataWithRefreshingState.loadingState());
    }

    public void onStart() {
        executeUseCase();
    }

    public void onStop() {
        compositeDisposable.dispose();
    }

    public void onDestroy() {
        this.listener = null;
    }

    public void askForRefresh() {
        notify(RemoteDataWithRefreshingState.refreshingState(data));
        executeUseCase();
    }

    public abstract Observable<T> executeUseCase(UseCase<T, P> useCase);

    private void notify(RemoteDataWithRefreshingState<T> model) {
        if (currentModel == null || !currentModel.equals(model)) {
            listener.update(model);
        }
        currentModel = model;
    }

    private void executeUseCase() {
        compositeDisposable.clear();
        compositeDisposable.add(executeUseCase(useCase).subscribe(this::onSuccess, this::onError));
    }

    private void onSuccess(@NonNull T data) {
        this.data = data;
        notify(RemoteDataWithRefreshingState.displayDataState(data));
    }

    private void onError(Throwable throwable) {
        this.data = null;
        notify(RemoteDataWithRefreshingState.errorState(throwable.getMessage()));
    }
}
