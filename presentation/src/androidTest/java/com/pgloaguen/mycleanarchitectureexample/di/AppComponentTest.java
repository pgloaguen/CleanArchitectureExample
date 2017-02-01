package com.pgloaguen.mycleanarchitectureexample.di;

import com.pgloaguen.data.di.DataComponent;
import com.pgloaguen.mycleanarchitectureexample.di.module.MockPresenterModule;
import com.pgloaguen.mycleanarchitectureexample.di.module.MockUseCaseModule;
import com.pgloaguen.mycleanarchitectureexample.di.module.PresenterCacheModule;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;

import dagger.Component;

/**
 * Created by paul on 31/01/2017.
 */

@AppScope
@Component(dependencies = {DataComponent.class}, modules = {MockUseCaseModule.class, MockPresenterModule.class, PresenterCacheModule.class})
public interface AppComponentTest extends AppComponent {
}
