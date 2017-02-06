package com.pgloaguen.mycleanarchitectureexample.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterCache;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.RemoteDataWithRefreshingStatePresenter;
import com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState;

import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.DISPLAY_DATA_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.EMPTY_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ERROR_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ERROR_WITH_DATA_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.LOADING_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.LOADING_WITH_ERROR_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.LoadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.REFRESHING_STATE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.RefreshingState;

/**
 * Created by paul on 27/01/2017.
 */

public abstract class BaseFragmentWithRemoteDataWithRefreshingState<S, P extends RemoteDataWithRefreshingStatePresenter<S, ?>>
        extends BaseFragment implements PresenterListener<RemoteDataWithRefreshingState<S>> {

    private static final String KEY_PRESENTER = "key_presenter";

    private P presenter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (getActivity().isFinishing() || !isRemoving()) {
            outState.putString(KEY_PRESENTER, getCache().storePresenter(presenter));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            presenter = getCache().getPresenter(savedInstanceState.getString(KEY_PRESENTER, ""));
        }

        if (presenter == null) {
            presenter = createPresenter();
        }

        init(presenter);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity().isFinishing() || isRemoving()) {
            presenter.destroy();
        }
        presenter = null;
    }

    @NonNull
    protected abstract PresenterCache<P> getCache();

    @NonNull
    protected abstract P createPresenter();

    protected abstract void init(@NonNull P presenter);

    @Override
    public void update(@NonNull RemoteDataWithRefreshingState<S> viewModel) {
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
