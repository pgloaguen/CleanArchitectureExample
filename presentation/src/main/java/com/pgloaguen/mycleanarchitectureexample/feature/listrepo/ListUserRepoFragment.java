package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.fragment.BaseFragmentWithRemoteDataWithRefreshingState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.DisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.ErrorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.LoadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.RefreshingState;


public class ListUserRepoFragment extends BaseFragmentWithRemoteDataWithRefreshingState<List<RepoEntity>, ListUserRepoPresenter> {

    @Inject
    ListUserRepoPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.error_screen)
    View errorScreen;

    @BindView(R.id.error)
    TextView errorTextView;

    private RepoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        toolbar.setTitle(R.string.repositories);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RepoAdapter();
        recycler.setAdapter(adapter);
        return v;
    }

    @Override
    protected ListUserRepoPresenter createPresenter() {
        activityComponent().inject(this);
        return presenter;
    }

    @Override
    protected void init(ListUserRepoPresenter presenter) {
        presenter.init(this);
        adapter.setListener(presenter::onRepoClick);
        swipeRefreshLayout.setOnRefreshListener(presenter::askForRefresh);
    }

    private void hideAllProgress() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(GONE);
    }

    private void showError(String error) {
        errorTextView.setText(error);
        errorScreen.setVisibility(VISIBLE);
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
        progressBar.setVisibility(VISIBLE);
        clearData();
        hideError();
    }

    @Override
    protected void displayRefreshingScreen(RefreshingState<List<RepoEntity>> model) {
        progressBar.setVisibility(GONE);
        hideError();
        setData(model.datas());
    }

    @Override
    protected void displayLoadingWithErrorScreen(LoadingWithErrorState viewModel) {
        progressBar.setVisibility(GONE);
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
    protected void displayDataScreen(@NonNull DisplayDataState<List<RepoEntity>> model) {
        hideAllProgress();
        hideError();
        setData(model.datas());
    }

    @Override
    protected void displayErrorWithDataScreen(ErrorWithDisplayDataState<List<RepoEntity>> model) {
        hideAllProgress();
        hideError();
        setData(model.datas());
        Toast.makeText(getActivity(), model.error(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void displayErrorScreen(@NonNull ErrorState model) {
        hideAllProgress();
        showError(model.error());
        clearData();
    }
}
