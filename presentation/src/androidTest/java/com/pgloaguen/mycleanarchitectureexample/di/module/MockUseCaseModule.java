package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;
import com.pgloaguen.domain.repository.GetUserRepoRepository;
import com.pgloaguen.domain.usecase.FavoriteRepoWithId;
import com.pgloaguen.domain.usecase.FavoriteRepoDetails;
import com.pgloaguen.domain.usecase.FavoriteRepo;
import com.pgloaguen.domain.usecase.GetUserRepoDetails;
import com.pgloaguen.domain.usecase.GetUserRepo;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.di.scope.AppScope;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.mock;

@Module
public class MockUseCaseModule {
    @AppScope
    @Provides
    public UseCase<List<RepoEntity>, GetUserRepo.Param> provideGetUserRepo(GetUserRepoRepository repository) {
        return mock(UseCase.class);
    }

    @AppScope
    @Provides
    public UseCase<RepoDetailsEntity, GetUserRepoDetails.Param> provideGetUserRepoDetails(GetUserRepoDetailsRepository repository) {
        return mock(UseCase.class);
    }

    @AppScope
    @Provides
    public FavoriteRepoWithId provideFavoriteRepo() {
        return mock(FavoriteRepoWithId.class);
    }


    @AppScope
    @Provides
    public FavoriteRepo provideFavoriteRepoEntity(FavoriteRepoWithId favoriteRepo) {
        return new FavoriteRepo(favoriteRepo, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @AppScope
    @Provides
    public FavoriteRepoDetails provideFavoriteRepoDetailsEntity(FavoriteRepoWithId favoriteRepo) {
        return new FavoriteRepoDetails(favoriteRepo, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}