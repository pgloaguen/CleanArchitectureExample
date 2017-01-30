package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.activity.BaseActivityWithRemoteDataWithRefreshingState;
import com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.LoadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.RefreshingState;

/**
 * Created by paul on 26/01/2017.
 */

public class RepoDetailsActivity extends BaseActivityWithRemoteDataWithRefreshingState<RepoDetailsEntity> implements PresenterListener<RemoteDataWithRefreshingState<RepoDetailsEntity>> {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_REPONAME = "reponame";

    @Inject
    RepoDetailsPresenter presenter;

    @BindView(R.id.desc)
    TextView descTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);
        activityComponent().inject(this);

        presenter.init(this, getIntent().getStringExtra(KEY_USERNAME), getIntent().getStringExtra(KEY_REPONAME));
        presenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void displayFirstFetchLoadingScreen() {
        descTextView.setText("Loading ...");
    }

    @Override
    protected void displayRefreshingScreen(RefreshingState<RepoDetailsEntity> viewModel) {
        descTextView.setText("Refreshing ...");
    }

    @Override
    protected void displayLoadingWithErrorScreen(LoadingWithErrorState<RepoDetailsEntity> viewModel) {}

    @Override
    protected void displayEmptyScreen() {}

    @Override
    protected void displayDataScreen(DisplayDataState<RepoDetailsEntity> viewModel) {
        descTextView.setText(viewModel.datas().desc());
    }

    @Override
    protected void displayErrorWithDataScreen(ErrorWithDisplayDataState<RepoDetailsEntity> viewModel) {
        descTextView.setText(viewModel.error());
        Toast.makeText(this, viewModel.error(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void displayErrorScreen(ErrorState viewModel) {
        descTextView.setText(viewModel.error());
    }

    public static Intent buildIntent(Context context, String username, String repoName) {
        return new Intent(context, RepoDetailsActivity.class).putExtra(KEY_USERNAME, username).putExtra(KEY_REPONAME, repoName);
    }
}
