package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.data.model.RepoDetails;
import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;
import com.pgloaguen.domain.repository.GetUserRepoRepository;
import com.pgloaguen.domain.usecase.GetUserRepoDetailsUseCase;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;

import java.util.List;

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
    public UseCase<List<RepoEntity>, String> provideGetUserRepo(GetUserRepoRepository repository) {
        return new GetUserRepoUseCase(repository, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @AppScope
    @Provides
    public UseCase<RepoDetailsEntity, GetUserRepoDetailsUseCase.Param> provideGetUserRepoDetails(GetUserRepoDetailsRepository repository) {
        return new GetUserRepoDetailsUseCase(repository, Schedulers.io(), AndroidSchedulers.mainThread());
    }

}
