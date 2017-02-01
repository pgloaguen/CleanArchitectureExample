package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.fragment.BaseFragmentWithRemoteDataWithRefreshingState;

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

public class RepoDetailsFragment extends BaseFragmentWithRemoteDataWithRefreshingState<RepoDetailsEntity, RepoDetailsPresenter> {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_REPONAME = "reponame";

    @Inject
    RepoDetailsPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.desc)
    TextView descTextView;

    public static RepoDetailsFragment create(String username, String repoName) {
        Bundle b = new Bundle();
        b.putString(KEY_USERNAME, username);
        b.putString(KEY_REPONAME, repoName);
        RepoDetailsFragment fragment = new RepoDetailsFragment();
        fragment.setArguments(b);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    protected RepoDetailsPresenter createPresenter() {
        activityComponent().inject(this);
        return presenter;
    }

    @Override
    protected void init(RepoDetailsPresenter presenter) {
        presenter.init(this, getArguments().getString(KEY_USERNAME), getArguments().getString(KEY_REPONAME));
    }

    @Override
    protected void displayFirstFetchLoadingScreen() {
        descTextView.setText(R.string.loading);
    }

    @Override
    protected void displayRefreshingScreen(RefreshingState<RepoDetailsEntity> viewModel) {
        toolbar.setTitle(viewModel.datas().name());
        descTextView.setText(R.string.refreshing);
    }

    @Override
    protected void displayLoadingWithErrorScreen(LoadingWithErrorState<RepoDetailsEntity> viewModel) {
        descTextView.setText(R.string.loading);
    }

    @Override
    protected void displayEmptyScreen() {
        descTextView.setText("");
        toolbar.setTitle("");
    }

    @Override
    protected void displayDataScreen(DisplayDataState<RepoDetailsEntity> viewModel) {
        toolbar.setTitle(viewModel.datas().name());
        descTextView.setText(viewModel.datas().desc());
    }

    @Override
    protected void displayErrorWithDataScreen(ErrorWithDisplayDataState<RepoDetailsEntity> viewModel) {
        toolbar.setTitle(viewModel.datas().name());
        descTextView.setText(viewModel.datas().desc());
        Toast.makeText(getActivity(), viewModel.error(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void displayErrorScreen(ErrorState viewModel) {
        descTextView.setText(viewModel.error());
    }
}
