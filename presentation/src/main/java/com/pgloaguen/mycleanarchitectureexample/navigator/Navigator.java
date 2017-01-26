package com.pgloaguen.mycleanarchitectureexample.navigator;

import android.app.Activity;
import android.content.Intent;

import com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsActivity;

import javax.inject.Inject;

/**
 * Created by paul on 26/01/2017.
 */

public class Navigator {

    private final Activity activity;

    @Inject
    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void showRepoDetails(String username, String repoName) {
        activity.startActivity(RepoDetailsActivity.buildIntent(activity, username, repoName));
    }

}
