package com.pgloaguen.mycleanarchitectureexample.presenter;

import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.EmptyState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LoadingState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LoadingWithErrorState;
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
        notify(emptyState(new ArrayList<>()));
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

    public abstract Observable<List<T>> executeUseCase(UseCase<List<T>, P> useCase);

    private void notify(RemoteDataWithRefreshingState<List<T>> newState) {
        if (currentState == null || !currentState.equals(newState)) {
            listener.update(newState);
        }
        currentState = newState;
    }

    private void executeUseCase(RemoteDataWithRefreshingState<List<T>> currentState) {
        RemoteDataWithRefreshingState<List<T>> loadingState = getLoadingState(currentState);
        compositeDisposable.clear();
        compositeDisposable.add(
               Observable.just(true)
                       .flatMap(__ -> executeUseCase(usecase).map(l -> l.isEmpty() ? emptyState(l) : displayDataState(l)))
                       .onErrorReturn(t -> getErrorState(loadingState, t.getMessage()))
                       .startWith(loadingState)
                       .subscribe(this::notify));
    }

    private RemoteDataWithRefreshingState<List<T>> getLoadingState(RemoteDataWithRefreshingState<List<T>> currentState) {
        if (currentState == null || currentState instanceof EmptyState) {
            return loadingState();
        } else if(currentState instanceof DisplayDataState) {
            return refreshingState(((DisplayDataState<List<T>>)currentState).datas());
        } else if(currentState instanceof ErrorWithDisplayDataState) {
            return refreshingState(((ErrorWithDisplayDataState<List<T>>)currentState).datas());
        } else if(currentState instanceof ErrorState) {
            return loadingWithErrorState(((ErrorState)currentState).error());
        } else if(currentState instanceof LoadingState || currentState instanceof LoadingWithErrorState || currentState instanceof RefreshingState) {
            return currentState;
        } else {
            throw new IllegalStateException("Impossible to get next loading state for " + currentState.toString());
        }
    }

    private RemoteDataWithRefreshingState<List<T>> getErrorState(RemoteDataWithRefreshingState<List<T>> currentState, String error) {
        if (currentState instanceof LoadingState || currentState instanceof LoadingWithErrorState) {
            return errorState(error);
        } else if(currentState instanceof RefreshingState) {
            return errorWithDisplayDataState(error, ((RefreshingState<List<T>>)currentState).datas());
        } else {
            throw new IllegalStateException("Impossible to get error state for " + currentState.toString());
        }
    }
}
