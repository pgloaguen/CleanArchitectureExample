package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;
import com.pgloaguen.domain.repository.GetUserRepoRepository;
import com.pgloaguen.domain.usecase.GetUserRepoDetailsUseCase;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;

import java.util.List;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockUseCaseModule {
    @AppScope
    @Provides
    public UseCase<List<RepoEntity>, String> provideGetUserRepo(GetUserRepoRepository repository) {
        return mock(UseCase.class);
    }

    @AppScope
    @Provides
    public UseCase<RepoDetailsEntity, GetUserRepoDetailsUseCase.Param> provideGetUserRepoDetails(GetUserRepoDetailsRepository repository) {
        return mock(UseCase.class);
    }
}