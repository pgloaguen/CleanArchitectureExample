package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.activity.BaseActivity;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState.*;

/**
 * Created by paul on 26/01/2017.
 */

public class RepoDetailsActivity extends BaseActivity implements PresenterListener<RemoteDataWithRefreshingState<RepoDetailsEntity>> {

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

        presenter.init(getIntent().getStringExtra("username"), getIntent().getStringExtra("repoName"));
        presenter.onCreate(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void update(RemoteDataWithRefreshingState<RepoDetailsEntity> viewModel) {
        if (viewModel instanceof RemoteDataWithRefreshingState.LoadingState) {
            descTextView.setText("Loading ...");
        } else if (viewModel instanceof RefreshingState) {
            descTextView.setText("Refreshing ...");
        } else if (viewModel instanceof DisplayDataState) {
            descTextView.setText(((DisplayDataState<RepoDetailsEntity>) viewModel).datas().desc());
        } else if (viewModel instanceof ErrorState) {
            descTextView.setText(((ErrorState) viewModel).message());
        } else {
            throw new IllegalArgumentException("Unhandle state " + viewModel);
        }
    }

    public static Intent buildIntent(Context context, String username, String repoName) {
        return new Intent(context, RepoDetailsActivity.class).putExtra(KEY_USERNAME, username).putExtra(KEY_REPONAME, repoName);
    }
}
