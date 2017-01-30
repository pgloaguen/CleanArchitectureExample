package com.pgloaguen.mycleanarchitectureexample.presenter;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import java.util.Collection;

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

public abstract class RemoteDataWithRefreshingStatePresenter<T, P> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UseCase<T, P> useCase;

    private PresenterListener<RemoteDataWithRefreshingState<T>> listener;
    private RemoteDataWithRefreshingState<T> currentState;

    public RemoteDataWithRefreshingStatePresenter(UseCase<T, P> useCase) {
        this.useCase = useCase;
    }

    public void init(PresenterListener<RemoteDataWithRefreshingState<T>> listener) {
        this.listener = listener;
    }

    public void onCreate() {
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

    public abstract Observable<T> executeUseCase(UseCase<T, P> useCase);

    private void notify(RemoteDataWithRefreshingState<T> model) {
        if (currentState == null || !currentState.equals(model)) {
            listener.update(model);
        }
        currentState = model;
    }

    private void executeUseCase(RemoteDataWithRefreshingState<T> currentState) {
        RemoteDataWithRefreshingState<T> loadingState = getLoadingState(currentState);
        compositeDisposable.clear();
        compositeDisposable.add(
                Observable.just(true)
                .flatMap(__ -> executeUseCase(useCase).map(this::getDisplayState))
                .onErrorReturn(t -> getErrorState(loadingState, t.getMessage()))
                .startWith(loadingState)
                .subscribe(this::notify));
    }

    @NonNull
    private RemoteDataWithRefreshingState<T> getDisplayState(@NonNull T l) {
        if (l instanceof Collection) {
            return ((Collection) l).isEmpty() ? emptyState() : displayDataState(l);
        } else {
            return displayDataState(l);
        }
    }

    @NonNull
    private RemoteDataWithRefreshingState<T> getLoadingState(@NonNull RemoteDataWithRefreshingState<T> currentState) {
        switch (currentState.state()) {
            case DISPLAY_DATA_STATE:
                return refreshingState(((DisplayDataState<T>) currentState).datas());
            case EMPTY_STATE:
                return loadingState();
            case ERROR_STATE:
                return loadingWithErrorState(((ErrorState) currentState).error());
            case ERROR_WITH_DATA_STATE:
                return refreshingState(((ErrorWithDisplayDataState<T>) currentState).datas());
            case LOADING_STATE:
            case LOADING_WITH_ERROR_STATE:
            case REFRESHING_STATE:
            default:
                return currentState;
        }
    }

    @NonNull
    private RemoteDataWithRefreshingState<T> getErrorState(@NonNull RemoteDataWithRefreshingState<T> currentState, String error) {
        switch (currentState.state()) {
            case LOADING_STATE:
            case LOADING_WITH_ERROR_STATE:
                return errorState(error);
            case REFRESHING_STATE:
                return errorWithDisplayDataState(error, ((RefreshingState<T>) currentState).datas());
            case DISPLAY_DATA_STATE:
            case EMPTY_STATE:
            case ERROR_STATE:
            case ERROR_WITH_DATA_STATE:
            default:
                throw new IllegalStateException("Impossible to get error state for " + currentState.toString());
        }
    }
}
