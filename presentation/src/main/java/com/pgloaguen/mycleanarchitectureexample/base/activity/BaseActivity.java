package com.pgloaguen.mycleanarchitectureexample.base.activity;

import android.support.v7.app.AppCompatActivity;

import com.pgloaguen.mycleanarchitectureexample.CleanApplication;
import com.pgloaguen.mycleanarchitectureexample.di.ActivityComponent;
import com.pgloaguen.mycleanarchitectureexample.di.AppComponent;
import com.pgloaguen.mycleanarchitectureexample.di.DaggerActivityComponent;
import com.pgloaguen.mycleanarchitectureexample.di.module.ActivityModule;

/**
 * Created by paul on 26/01/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private AppComponent getApplicationComponent() {
        return ((CleanApplication) getApplication()).getAppComponent();
    }

    public ActivityComponent activityComponent() {
        return DaggerActivityComponent.builder().appComponent(getApplicationComponent()).activityModule(new ActivityModule(this)).build();
    }
}
