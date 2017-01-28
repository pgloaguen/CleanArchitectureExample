package com.pgloaguen.mycleanarchitectureexample.presenter;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.DISPLAY_DATA_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.EMPTY_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ERROR_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ERROR_WITH_DATA_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LOADING_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LOADING_WITH_ERROR_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.REFRESHING_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.RefreshingState;
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
    private PresenterListener<RemoteDataWithRefreshingState<List<T>>> listener;
    private RemoteDataWithRefreshingState<List<T>> currentState;

    public RemoteDataListWithRefreshingStatePresenter(UseCase<List<T>, P> usecase) {
        this.usecase = usecase;
    }

    public void init(PresenterListener<RemoteDataWithRefreshingState<List<T>>> listener) {
        this.listener = listener;
        notify(emptyState());
    }

    public void onStart() {
        executeUseCase(currentState);
    }

    public void onStop() {
        compositeDisposable.clear();
    }

    public void onDestroy() {
        compositeDisposable.dispose();
        this.listener = null;
    }

    public void askForRefresh() {
        executeUseCase(currentState);
    }

    public abstract Observable<List<T>> executeUseCase(@NonNull UseCase<List<T>, P> useCase);

    private void notify(@NonNull RemoteDataWithRefreshingState<List<T>> newState) {
        if (currentState == null || !currentState.equals(newState)) {
            listener.update(newState);
        }
        currentState = newState;
    }

    private void executeUseCase(@NonNull RemoteDataWithRefreshingState<List<T>> currentState) {
        RemoteDataWithRefreshingState<List<T>> loadingState = getLoadingState(currentState);
        compositeDisposable.clear();
        compositeDisposable.add(
                Observable.just(true)
                        .flatMap(__ -> executeUseCase(usecase).map(this::getDisplayState))
                        .onErrorReturn(t -> getErrorState(loadingState, t.getMessage()))
                        .startWith(loadingState)
                        .subscribe(this::notify));
    }


    @NonNull
    private RemoteDataWithRefreshingState<List<T>> getDisplayState(@NonNull List<T> l) {
        return l.isEmpty() ? emptyState() : displayDataState(l);
    }

    @NonNull
    private RemoteDataWithRefreshingState<List<T>> getLoadingState(@NonNull RemoteDataWithRefreshingState<List<T>> currentState) {
        switch (currentState.state()) {
            case DISPLAY_DATA_STATE:
                return refreshingState(((DisplayDataState<List<T>>) currentState).datas());
            case EMPTY_STATE:
                return loadingState();
            case ERROR_STATE:
                return loadingWithErrorState(((ErrorState) currentState).error());
            case ERROR_WITH_DATA_STATE:
                return refreshingState(((ErrorWithDisplayDataState<List<T>>) currentState).datas());
            case LOADING_STATE:
            case LOADING_WITH_ERROR_STATE:
            case REFRESHING_STATE:
            default:
                return currentState;
        }
    }

    @NonNull
    private RemoteDataWithRefreshingState<List<T>> getErrorState(@NonNull RemoteDataWithRefreshingState<List<T>> currentState, String error) {
        switch (currentState.state()) {
            case LOADING_STATE:
            case LOADING_WITH_ERROR_STATE:
                return errorState(error);
            case REFRESHING_STATE:
                return errorWithDisplayDataState(error, ((RefreshingState<List<T>>) currentState).datas());
            case DISPLAY_DATA_STATE:
            case EMPTY_STATE:
            case ERROR_STATE:
            case ERROR_WITH_DATA_STATE:
            default:
                throw new IllegalStateException("Impossible to get error state for " + currentState.toString());
        }
    }
}
