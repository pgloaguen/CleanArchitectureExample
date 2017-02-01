package com.pgloaguen.mycleanarchitectureexample.base.presenter;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState;

import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.DISPLAY_DATA_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.EMPTY_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ERROR_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ERROR_WITH_DATA_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.LOADING_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.LOADING_WITH_ERROR_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.REFRESHING_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.RefreshingState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.displayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.emptyState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.errorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.errorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.loadingState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.loadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.refreshingState;

/**
 * Created by paul on 26/01/2017.
 */

public abstract class RemoteDataWithRefreshingStatePresenter<T, P> {

    private Disposable disposable = Disposables.empty();

    @NonNull
    private final UseCase<T, P> useCase;

    private RemoteDataWithRefreshingState<T> currentState ;

    private PresenterListener<RemoteDataWithRefreshingState<T>> listener;
    private boolean isReInit;


    public RemoteDataWithRefreshingStatePresenter(@NonNull UseCase<T, P> useCase) {
        this.useCase = useCase;
    }

    public void init(PresenterListener<RemoteDataWithRefreshingState<T>> listener) {
        isReInit = currentState != null;
        this.listener = listener;
    }

    public void onCreate() {
        notify(isReInit && currentState != null ? currentState : emptyState(), isReInit);
    }

    public void onStart() {
        if (shouldExecuteUseCaseWhenOnstart(currentState)) {
            executeUseCase(currentState);
        }
    }

    private boolean shouldExecuteUseCaseWhenOnstart(@NonNull RemoteDataWithRefreshingState<T> currentState) {
        int state = currentState.state();
        return state == LOADING_STATE || state == LOADING_WITH_ERROR_STATE || state == REFRESHING_STATE || (state == EMPTY_STATE && !isReInit);
    }

    public void onStop() {
        disposable.dispose();
    }

    public void onDestroy() {
        this.listener = null;
    }

    public void askForRefresh() {
        executeUseCase(currentState);
    }

    private void notify(@NonNull RemoteDataWithRefreshingState<T> model, boolean forceUpdate) {
        if (forceUpdate || currentState == null || !currentState.equals(model)) {
            listener.update(model);
        }
        currentState = model;
    }

    private void notify(@NonNull RemoteDataWithRefreshingState<T> model) {
        notify(model, false);
    }


    protected abstract Observable<T> executeUseCase(UseCase<T, P> useCase);

    private void executeUseCase(@NonNull RemoteDataWithRefreshingState<T> currentState) {
        disposable.dispose();
        RemoteDataWithRefreshingState<T> loadingState = getLoadingState(currentState);
        disposable = Observable.just(true)
                .flatMap(__ -> executeUseCase(useCase).map(this::getDisplayState))
                .onErrorReturn(t -> getErrorState(loadingState, t.getMessage()))
                .startWith(loadingState)
                .subscribe(this::notify);
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
