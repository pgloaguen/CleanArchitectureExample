package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.content.Context;
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
import com.pgloaguen.mycleanarchitectureexample.base.fragment.BaseFragmentWithPresenterCache;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterCache;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ListUserRepoFragment extends BaseFragmentWithPresenterCache<ListUserRepoPresenter> implements ListUserRepoView {

    @Inject
    ListUserRepoPresenter presenter;

    @Inject
    PresenterCache<ListUserRepoPresenter> presenterCache;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityComponent().inject(this);
    }

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

    @NonNull
    @Override
    protected PresenterCache<ListUserRepoPresenter> getCache() {
        return presenterCache;
    }

    @NonNull
    @Override
    protected ListUserRepoPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void init(@NonNull ListUserRepoPresenter presenter) {
        presenter.attach(this);
        adapter.setListener(new RepoAdapter.OnRepoClick() {
            @Override
            public void onRepoClick(RepoEntity repoEntity) {
                presenter.onRepoClick(repoEntity);
            }

            @Override
            public void onRepoFavorite(RepoEntity repoEntity) {
                presenter.onFavoriteClick(repoEntity);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(presenter::askForRefresh);
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
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

    protected void displayFirstFetchLoadingScreen() {
        progressBar.setVisibility(VISIBLE);
        clearData();
        hideError();
    }

    protected void displayRefreshingScreen(List<RepoEntity> repos) {
        progressBar.setVisibility(GONE);
        hideError();
        setData(repos);
    }

    protected void displayLoadingWithErrorScreen(@NonNull String error) {
        progressBar.setVisibility(GONE);
        showError(error);
        clearData();
    }

    protected void displayEmptyScreen() {
        hideAllProgress();
        hideError();
        clearData();
    }

    protected void displayDataScreen(@NonNull List<RepoEntity> repos) {
        hideAllProgress();
        hideError();
        setData(repos);
    }

    protected void displayErrorWithDataScreen(@NonNull List<RepoEntity> repos, @NonNull String errorMessage) {
        hideAllProgress();
        hideError();
        setData(repos);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    protected void displayErrorScreen(@NonNull String errorMessage) {
        hideAllProgress();
        showError(errorMessage);
        clearData();
    }

    @Override
    public void notify(@NonNull ListUserRepoPresenter.StateValue state, @NonNull ListUserRepoPresenter.VM model) {
        switch (state) {
            case SHOW_EMPTY:
                displayEmptyScreen();
                break;
            case SHOW_LOADING:
                displayFirstFetchLoadingScreen();
                break;
            case SHOW_REFRESHING:
                displayRefreshingScreen(model.repoEntities);
                break;
            case SHOW_LOADING_WITH_ERROR:
                displayLoadingWithErrorScreen(model.error.getMessage());
                break;
            case SHOW_DATA:
                displayDataScreen(model.repoEntities);
                break;
            case SHOW_DATA_WITH_ERROR:
                displayErrorWithDataScreen(model.repoEntities, model.error.getMessage());
                break;
            case SHOW_ERROR:
                displayErrorScreen(model.error.getMessage());
                break;
        }
    }
}
