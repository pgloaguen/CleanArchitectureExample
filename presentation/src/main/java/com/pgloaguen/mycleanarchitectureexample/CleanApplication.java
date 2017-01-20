package com.pgloaguen.mycleanarchitectureexample;

import android.app.Application;

import com.pgloaguen.data.di.DaggerDataComponent;
import com.pgloaguen.mycleanarchitectureexample.di.AppComponent;
import com.pgloaguen.mycleanarchitectureexample.di.DaggerAppComponent;
import com.pgloaguen.mycleanarchitectureexample.di.module.UseCaseModule;

/**
 * Created by paul on 20/01/2017.
 */

public class CleanApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().useCaseModule(new UseCaseModule()).dataComponent(DaggerDataComponent.create()).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
