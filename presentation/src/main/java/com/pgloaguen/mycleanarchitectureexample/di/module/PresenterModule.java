package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.domain.interactor.GetUserRepoInteractor;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.mycleanarchitectureexample.di.scope.ActivityScope;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;
import com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListRepoPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by paul on 19/01/2017.
 */

@Module
public class PresenterModule {

    @ActivityScope
    @Provides
    public ListRepoPresenter provideListRepoPresenter(GetUserRepoUseCase useCase) {
        return new ListRepoPresenter(useCase);
    }

}
