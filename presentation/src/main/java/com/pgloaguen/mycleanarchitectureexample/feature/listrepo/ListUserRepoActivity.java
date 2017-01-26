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
import com.pgloaguen.mycleanarchitectureexample.di.DaggerActivityComponent;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;


public class ListUserRepoActivity extends AppCompatActivity implements PresenterListener<RemoteDataWithRefreshingState<List<RepoEntity>>> {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DaggerActivityComponent.builder().appComponent(((CleanApplication)getApplication()).getAppComponent()).build().inject(this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
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
        recycler.setAdapter(new RepoAdapter(model.datas()));
    }

    private void displayFirstFetchLoadingScreen() {
        progressBar.show();
        recycler.setAdapter(null);
        errorTextView.setVisibility(GONE);
    }

    private void displayDataScreen(@NonNull RemoteDataWithRefreshingState.DisplayDataState<List<RepoEntity>> model) {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.hide();
        errorTextView.setVisibility(GONE);
        recycler.setAdapter(new RepoAdapter(model.datas()));
    }

    private void displayErrorScreen(@NonNull RemoteDataWithRefreshingState.ErrorState model) {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.hide();
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(model.message());
        recycler.setAdapter(null);
    }
}
