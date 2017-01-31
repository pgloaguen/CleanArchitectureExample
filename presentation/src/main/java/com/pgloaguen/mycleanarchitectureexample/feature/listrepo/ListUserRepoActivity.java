package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.activity.BaseActivity;


public class ListUserRepoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
