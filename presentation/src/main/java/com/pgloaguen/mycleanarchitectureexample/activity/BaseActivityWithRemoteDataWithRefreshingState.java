package com.pgloaguen.mycleanarchitectureexample.activity;

import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.DISPLAY_DATA_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.EMPTY_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ERROR_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ERROR_WITH_DATA_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LOADING_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LOADING_WITH_ERROR_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.LoadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.REFRESHING_STATE;
import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.RefreshingState;

/**
 * Created by paul on 27/01/2017.
 */

public abstract class BaseActivityWithRemoteDataWithRefreshingState<S> extends BaseActivity implements PresenterListener<RemoteDataWithRefreshingState<S>> {

    @Override
    public void update(RemoteDataWithRefreshingState<S> viewModel) {
        switch (viewModel.state()) {
            case LOADING_STATE:
                displayFirstFetchLoadingScreen();
                break;
            case LOADING_WITH_ERROR_STATE:
                displayLoadingWithErrorScreen(((LoadingWithErrorState<S>) viewModel));
                break;
            case REFRESHING_STATE:
                displayRefreshingScreen(((RefreshingState<S>) viewModel));
                break;
            case EMPTY_STATE:
                displayEmptyScreen();
                break;
            case DISPLAY_DATA_STATE:
                displayDataScreen(((DisplayDataState<S>) viewModel));
                break;
            case ERROR_STATE:
                displayErrorScreen(((ErrorState) viewModel));
                break;
            case ERROR_WITH_DATA_STATE:
                displayErrorWithDataScreen(((ErrorWithDisplayDataState<S>) viewModel));
                break;
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
