package com.pgloaguen.mycleanarchitectureexample.di.module;

import android.app.Activity;

import com.pgloaguen.mycleanarchitectureexample.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by paul on 26/01/2017.
 */

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return activity;
    }
}
