package com.pgloaguen.mycleanarchitectureexample.activity;

import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.EmptyState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LoadingState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LoadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.RefreshingState;

/**
 * Created by paul on 27/01/2017.
 */

public abstract class BaseActivityWithRemoteDataWithRefreshingState<S> extends BaseActivity implements PresenterListener<RemoteDataWithRefreshingState<S>> {

    @Override
    public void update(RemoteDataWithRefreshingState<S> viewModel) {
        if (viewModel instanceof LoadingState) {
            displayFirstFetchLoadingScreen();
        } else if(viewModel instanceof LoadingWithErrorState) {
            displayLoadingWithErrorScreen(((LoadingWithErrorState<S>) viewModel));
        } else if (viewModel instanceof RefreshingState) {
            displayRefreshingScreen(((RefreshingState<S>) viewModel));
        } else if(viewModel instanceof EmptyState) {
            displayEmptyScreen();
        } else if (viewModel instanceof DisplayDataState){
            displayDataScreen(((DisplayDataState<S>) viewModel));
        } else if(viewModel instanceof ErrorState) {
            displayErrorScreen(((ErrorState) viewModel));
        } else if(viewModel instanceof ErrorWithDisplayDataState) {
            displayErrorWithDataScreen(((ErrorWithDisplayDataState<S>) viewModel));
        } else {
            throw new IllegalStateException(viewModel.getClass() + " is not an handled state");
        }
    }

    protected abstract void displayFirstFetchLoadingScreen();
    protected abstract void displayRefreshingScreen(RefreshingState<S> viewModel);
    protected abstract void displayLoadingWithErrorScreen(LoadingWithErrorState<S> viewModel);

    protected abstract void displayEmptyScreen();
    protected abstract void displayDataScreen(DisplayDataState<S> viewModel);

    protected abstract void displayErrorWithDataScreen(ErrorWithDisplayDataState<S> viewModel);
    protected abstract void displayErrorScreen(ErrorState viewModel);

}
