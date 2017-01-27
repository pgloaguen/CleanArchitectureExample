package com.pgloaguen.mycleanarchitectureexample.presenter;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.displayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.errorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.errorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.loadingState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.loadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.refreshingState;

/**
 * Created by paul on 26/01/2017.
 */

public abstract class RemoteDataWithRefreshingStatePresenter<T, P> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UseCase<T, P> useCase;
    private T data = null;
    private String error = null;

    private PresenterListener<RemoteDataWithRefreshingState<T>> listener;
    private RemoteDataWithRefreshingState<T> currentModel;

    public RemoteDataWithRefreshingStatePresenter(UseCase<T, P> useCase) {
        this.useCase = useCase;
    }

    public void init(PresenterListener<RemoteDataWithRefreshingState<T>> listener) {
        this.listener = listener;
        data = null;
        notify(loadingState());
    }

    public void onStart() {
        executeUseCase();
    }

    public void onStop() {
        compositeDisposable.clear();
    }

    public void onDestroy() {
        compositeDisposable.dispose();
        this.listener = null;
    }

    public void askForRefresh() {
        if (data != null) {
            notify(refreshingState(data));
        } else if (error != null) {
            notify(loadingWithErrorState(error));
        }
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
        this.error = null;
        notify(displayDataState(data));
    }

    private void onError(Throwable throwable) {
        this.error = throwable.getMessage();
        notify(data == null ? errorState(error) : errorWithDisplayDataState(error, data));
    }
}
