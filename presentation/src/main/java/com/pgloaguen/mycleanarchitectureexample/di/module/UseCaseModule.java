package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.domain.repository.GetUserRepoRepository;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by paul on 19/01/2017.
 */

@Module
public class UseCaseModule {

    @AppScope
    @Provides
    public GetUserRepoUseCase provideGetUserRepo(GetUserRepoRepository repository) {
        return new GetUserRepoUseCase(repository, Schedulers.io(), AndroidSchedulers.mainThread());
    }

}
