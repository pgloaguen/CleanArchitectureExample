package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.fragment.BaseFragmentWithPresenterCache;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterCache;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

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
        this.presenter = presenter;
        presenter.attach(this);
    }

    @Override
    public Observable<Object> refresh() {
        return RxSwipeRefreshLayout.refreshes(swipeRefreshLayout);
    }

    @Override
    public Observable<RepoEntity> favorite() {
        return adapter.favorite();
    }

    @Override
    public Observable<RepoEntity> itemClick() {
        return adapter.itemClick();
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
    }

    @Override
    public void notify(@NonNull ListUserRepoVM model) {
        setData(model.repoEntities);
        handleError(model);
        handleLoadingState(model);
    }

    private void setData(List<RepoEntity> repoEntities) {
        adapter.setData(repoEntities);
    }

    private void handleError(@NonNull ListUserRepoVM model) {
        if (model.error == null) {
            hideError();
        } else if(model.repoEntities.isEmpty()) {
            showError(model.error.getMessage());
        } else {
            Toast.makeText(getActivity(), model.error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showError(String error) {
        errorTextView.setText(error);
        errorScreen.setVisibility(VISIBLE);
    }

    private void hideError() {
        errorScreen.setVisibility(GONE);
    }

    private void handleLoadingState(@NonNull ListUserRepoVM model) {
        switch(model.loadState) {
            case NONE:
                hideAllProgress();
                break;
            case LOADING:
                displayLoading(VISIBLE);
                break;
            case REFRESHING:
                displayRefreshing();
                break;
        }
    }

    private void hideAllProgress() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(GONE);
    }

    private void displayRefreshing() {
        progressBar.setVisibility(GONE);
        swipeRefreshLayout.setRefreshing(true);
    }

    private void displayLoading(int visible) {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(visible);
    }
}
