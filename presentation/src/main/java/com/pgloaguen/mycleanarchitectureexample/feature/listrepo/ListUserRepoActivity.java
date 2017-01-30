package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.activity.BaseActivityWithRemoteDataWithRefreshingState;
import com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.LoadingWithErrorState;


public class ListUserRepoActivity extends BaseActivityWithRemoteDataWithRefreshingState<List<RepoEntity>>  {

    @Inject
    ListUserRepoPresenter presenter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.progress)
    ContentLoadingProgressBar progressBar;

    @BindView(R.id.error_screen)
    View errorScreen;

    @BindView(R.id.error)
    TextView errorTextView;

    private RepoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activityComponent().inject(this);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RepoAdapter();
        recycler.setAdapter(adapter);
        adapter.setListener(presenter::onRepoClick);
        swipeRefreshLayout.setOnRefreshListener(presenter::askForRefresh);
        presenter.init(this);
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
        super.onDestroy();
        presenter.onDestroy();
    }

    private void hideAllProgress() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.hide();
    }

    private void showError(String error) {
        errorTextView.setText(error);
        errorScreen.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        errorScreen.setVisibility(GONE);
    }

    private void clearData() {
        adapter.setData(new ArrayList<>());
    }

    private void setData(List<RepoEntity> repoEntities) {
        adapter.setData(repoEntities);
    }

    @Override
    protected void displayFirstFetchLoadingScreen() {
        progressBar.show();
        clearData();
        hideError();
    }

    @Override
    protected void displayRefreshingScreen(RemoteDataWithRefreshingState.RefreshingState<List<RepoEntity>> model) {
        if (!swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(true);
        hideError();
        setData(model.datas());
    }

    @Override
    protected void displayLoadingWithErrorScreen(LoadingWithErrorState viewModel) {
        progressBar.show();
        showError(viewModel.error());
        clearData();
    }

    @Override
    protected void displayEmptyScreen() {
        hideAllProgress();
        hideError();
        clearData();
    }

    @Override
    protected void displayDataScreen(@NonNull RemoteDataWithRefreshingState.DisplayDataState<List<RepoEntity>> model) {
        hideAllProgress();
        hideError();
        setData(model.datas());
    }

    @Override
    protected void displayErrorWithDataScreen(ErrorWithDisplayDataState<List<RepoEntity>> model) {
        hideAllProgress();
        hideError();
        setData(model.datas());
        Toast.makeText(this, model.error(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void displayErrorScreen(@NonNull RemoteDataWithRefreshingState.ErrorState model) {
        hideAllProgress();
        showError(model.error());
        clearData();
    }
}
