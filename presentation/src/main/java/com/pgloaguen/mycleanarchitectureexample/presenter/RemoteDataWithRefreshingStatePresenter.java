package com.pgloaguen.mycleanarchitectureexample.presenter;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.EmptyState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LoadingState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LoadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.RefreshingState;
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
                .flatMap(__ -> executeUseCase(useCase).map(RemoteDataWithRefreshingState::displayDataState))
                .onErrorReturn(t -> getErrorState(loadingState, t.getMessage()))
                .startWith(loadingState)
                .subscribe(this::notify));
    }


    private RemoteDataWithRefreshingState<T> getLoadingState(RemoteDataWithRefreshingState<T> currentState) {
        if (currentState == null || currentState instanceof EmptyState) {
            return loadingState();
        } else if(currentState instanceof DisplayDataState) {
            return refreshingState(((DisplayDataState<T>)currentState).datas());
        } else if(currentState instanceof ErrorWithDisplayDataState) {
            return refreshingState(((ErrorWithDisplayDataState<T>)currentState).datas());
        } else if(currentState instanceof ErrorState) {
            return loadingWithErrorState(((ErrorState)currentState).error());
        } else if(currentState instanceof LoadingState || currentState instanceof LoadingWithErrorState || currentState instanceof RefreshingState) {
            return currentState;
        } else {
            throw new IllegalStateException("Impossible to get next loading state for " + currentState.toString());
        }
    }

    private RemoteDataWithRefreshingState<T> getErrorState(RemoteDataWithRefreshingState<T> currentState, String error) {
        if (currentState instanceof LoadingState || currentState instanceof LoadingWithErrorState) {
            return errorState(error);
        } else if(currentState instanceof RefreshingState) {
            return errorWithDisplayDataState(error, ((RefreshingState<T>)currentState).datas());
        } else {
            throw new IllegalStateException("Impossible to get error state for " + currentState.toString());
        }
    }
}
