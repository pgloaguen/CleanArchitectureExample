package com.pgloaguen.mycleanarchitectureexample.base;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;

import com.pgloaguen.data.di.DaggerDataComponent;
import com.pgloaguen.data.di.NetModule;
import com.pgloaguen.data.di.RepositoryModule;
import com.pgloaguen.mycleanarchitectureexample.CleanApplication;
import com.pgloaguen.mycleanarchitectureexample.base.activity.BaseActivity;
import com.pgloaguen.mycleanarchitectureexample.di.ActivityComponentTest;
import com.pgloaguen.mycleanarchitectureexample.di.AppComponentTest;
import com.pgloaguen.mycleanarchitectureexample.di.DaggerActivityComponentTest;
import com.pgloaguen.mycleanarchitectureexample.di.DaggerAppComponentTest;
import com.pgloaguen.mycleanarchitectureexample.di.module.ActivityModule;
import com.pgloaguen.mycleanarchitectureexample.di.module.MockPresenterModule;
import com.pgloaguen.mycleanarchitectureexample.di.module.MockUseCaseModule;
import com.pgloaguen.mycleanarchitectureexample.utils.RxIdlingResource;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by paul on 31/01/2017.
 */

public class BaseActivityTest {

    protected AppComponentTest appComponentTest;
    protected ActivityComponentTest activityComponentTest;

    protected void init() {
        RxIdlingResource rxIdlingResource = new RxIdlingResource();
        Espresso.registerIdlingResources(rxIdlingResource);
        RxJavaPlugins.setScheduleHandler(rxIdlingResource);

        appComponentTest = DaggerAppComponentTest.builder()
                .mockUseCaseModule(new MockUseCaseModule())
                .mockPresenterModule(new MockPresenterModule())
                .dataComponent(
                        DaggerDataComponent
                                .builder()
                                .netModule(new NetModule(InstrumentationRegistry.getTargetContext()))
                                .repositoryModule(new RepositoryModule())
                                .build())
                .build();

        activityComponentTest = DaggerActivityComponentTest.builder()
                .appComponentTest(appComponentTest)
                .mockPresenterModule(new MockPresenterModule())
                .activityModule(new ActivityModule(null))
                .build();

        ((CleanApplication)InstrumentationRegistry.getTargetContext().getApplicationContext()).setAppComponent(appComponentTest);
        BaseActivity.setActivityComponent(activityComponentTest);
    }
}
