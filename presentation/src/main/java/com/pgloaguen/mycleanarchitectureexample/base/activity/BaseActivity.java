package com.pgloaguen.mycleanarchitectureexample.base.activity;

import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import com.pgloaguen.mycleanarchitectureexample.CleanApplication;
import com.pgloaguen.mycleanarchitectureexample.di.ActivityComponent;
import com.pgloaguen.mycleanarchitectureexample.di.AppComponent;
import com.pgloaguen.mycleanarchitectureexample.di.DaggerActivityComponent;
import com.pgloaguen.mycleanarchitectureexample.di.module.ActivityModule;

import static android.support.annotation.VisibleForTesting.NONE;

/**
 * Created by paul on 26/01/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private AppComponent getApplicationComponent() {
        return ((CleanApplication) getApplication()).getAppComponent();
    }

    public ActivityComponent activityComponent() {
        return activityComponent == null ? DaggerActivityComponent.builder().appComponent(getApplicationComponent()).activityModule(new ActivityModule(this)).build() : activityComponent;
    }

    @VisibleForTesting
    private static ActivityComponent activityComponent;

    @VisibleForTesting(otherwise = NONE)
    public static void setActivityComponent(ActivityComponent component) {
        activityComponent = component;
    }
}
