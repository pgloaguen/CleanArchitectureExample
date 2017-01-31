package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.activity.BaseActivity;

/**
 * Created by paul on 26/01/2017.
 */

public class RepoDetailsActivity extends BaseActivity {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_REPONAME = "reponame";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, RepoDetailsFragment.create(getIntent().getStringExtra(KEY_USERNAME), getIntent().getStringExtra(KEY_REPONAME)))
                .commit();
    }

    public static Intent buildIntent(Context context, String username, String repoName) {
        return new Intent(context, RepoDetailsActivity.class).putExtra(KEY_USERNAME, username).putExtra(KEY_REPONAME, repoName);
    }
}
