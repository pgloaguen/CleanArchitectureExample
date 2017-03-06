package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.fragment.BaseFragmentWithPresenterCache;
import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterCache;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by paul on 26/01/2017.
 */

public class RepoDetailsFragment extends BaseFragmentWithPresenterCache<RepoDetailsPresenter> implements RepoDetailsView {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_REPONAME = "reponame";

    @Inject
    RepoDetailsPresenter presenter;

    @Inject
    PresenterCache<RepoDetailsPresenter> presenterCache;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_favorite)
    ImageView favorite;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override @NonNull
    protected PresenterCache<RepoDetailsPresenter> getCache() {
        return presenterCache;
    }

    @Override @NonNull
    protected RepoDetailsPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void init(@NonNull RepoDetailsPresenter presenter) {
        presenter.attach(this, getArguments().getString(KEY_USERNAME), getArguments().getString(KEY_REPONAME));
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
    }

    protected void displayFirstFetchLoadingScreen() {
        descTextView.setText(R.string.loading);
        descTextView.setOnClickListener(null);
        favorite.setVisibility(View.GONE);
    }

    protected void displayEmptyScreen() {
        descTextView.setText("");
        toolbar.setTitle("");
        descTextView.setOnClickListener(null);
        favorite.setVisibility(View.GONE);
    }

    protected void displayDataScreen(RepoDetailsEntity viewModel) {
        toolbar.setTitle(viewModel.name());
        descTextView.setText(viewModel.desc());
        descTextView.setOnClickListener(null);
        favorite.setImageResource(viewModel.isFavorite() ? R.drawable.ic_favorite_on : R.drawable.ic_favorite_off);
        favorite.setVisibility(View.VISIBLE);
        favorite.setOnClickListener(__ -> presenter.toggleFavorite(viewModel));
    }

    protected void displayErrorScreen(String error) {
        descTextView.setText(error);
        favorite.setVisibility(View.GONE);
        descTextView.setOnClickListener(view -> presenter.retry());
    }

    @Override
    public void notify(@NonNull RepoDetailsPresenter.StateValue state, @NonNull RepoDetailsPresenter.VM model) {
        switch (state) {
            case EMPTY:
                displayEmptyScreen();
                break;
            case LOADING:
                displayFirstFetchLoadingScreen();
                break;
            case DISPLAY:
                displayDataScreen(model.data);
                break;
            case DISPLAY_ERROR:
                displayErrorScreen(model.error.getMessage());
                break;
        }
    }
}
