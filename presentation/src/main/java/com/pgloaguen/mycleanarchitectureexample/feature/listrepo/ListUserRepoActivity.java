package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pgloaguen.mycleanarchitectureexample.CleanApplication;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.activity.BaseActivity;
import com.pgloaguen.mycleanarchitectureexample.di.DaggerActivityComponent;
import com.pgloaguen.mycleanarchitectureexample.di.module.ActivityModule;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;


public class ListUserRepoActivity extends BaseActivity implements PresenterListener<RemoteDataWithRefreshingState<List<RepoEntity>>> {

    @Inject
    ListUserRepoPresenter presenter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.progress)
    ContentLoadingProgressBar progressBar;

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
        presenter.onCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void update(RemoteDataWithRefreshingState<List<RepoEntity>> viewModel) {
        if (viewModel instanceof RemoteDataWithRefreshingState.LoadingState) {
            displayFirstFetchLoadingScreen();
        } else if(viewModel instanceof RemoteDataWithRefreshingState.ErrorState) {
            displayErrorScreen(((RemoteDataWithRefreshingState.ErrorState) viewModel));
        } else if (viewModel instanceof RemoteDataWithRefreshingState.DisplayDataState){
            displayDataScreen(((RemoteDataWithRefreshingState.DisplayDataState<List<RepoEntity>>) viewModel));
        } else if (viewModel instanceof RemoteDataWithRefreshingState.RefreshingState) {
            displayRefreshingScreen(((RemoteDataWithRefreshingState.RefreshingState<List<RepoEntity>>) viewModel));
        } else {
            throw new IllegalStateException(viewModel.getClass() + " is not an handled state");
        }
    }

    private void displayRefreshingScreen(RemoteDataWithRefreshingState.RefreshingState<List<RepoEntity>> model) {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.hide();
        errorTextView.setVisibility(GONE);
        adapter.setData(model.datas());
    }

    private void displayFirstFetchLoadingScreen() {
        progressBar.show();
        adapter.setData(new ArrayList<>());
        errorTextView.setVisibility(GONE);
    }

    private void displayDataScreen(@NonNull RemoteDataWithRefreshingState.DisplayDataState<List<RepoEntity>> model) {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.hide();
        errorTextView.setVisibility(GONE);
        adapter.setData(model.datas());
    }

    private void displayErrorScreen(@NonNull RemoteDataWithRefreshingState.ErrorState model) {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.hide();
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(model.message());
        adapter.setData(new ArrayList<>());
    }
}
