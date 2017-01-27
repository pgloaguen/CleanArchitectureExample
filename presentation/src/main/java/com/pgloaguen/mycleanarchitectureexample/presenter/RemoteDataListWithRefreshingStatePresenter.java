package com.pgloaguen.mycleanarchitectureexample.presenter;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.displayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.emptyState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.errorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.errorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.loadingState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.loadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.refreshingState;

/**
 * Created by paul on 26/01/2017.
 */

public abstract class RemoteDataListWithRefreshingStatePresenter<T, P> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UseCase<List<T>, P> usecase;
    private final List<T> data = new ArrayList<>();

    private PresenterListener<RemoteDataWithRefreshingState<List<T>>> listener;
    private RemoteDataWithRefreshingState<List<T>> currentModel;
    private String error;

    public RemoteDataListWithRefreshingStatePresenter(UseCase<List<T>, P> usecase) {
        this.usecase = usecase;
    }

    public void init(PresenterListener<RemoteDataWithRefreshingState<List<T>>> listener) {
        this.listener = listener;
        data.clear();
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
        if (error != null) {
            notify(loadingWithErrorState(error));
        } else if (data.isEmpty()) {
            notify(loadingState());
        } else {
            notify(refreshingState(new ArrayList<>(data)));
        }

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
        error = null;
        data.clear();
        data.addAll(repoEntities);
        notify(data.isEmpty() ? emptyState() : displayDataState(repoEntities));
    }

    private void onError(Throwable throwable) {
        error = throwable.getMessage();
        notify(data.isEmpty() ? errorState(throwable.getMessage()) : errorWithDisplayDataState(error, data));
    }
}
