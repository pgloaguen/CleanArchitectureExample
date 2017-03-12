package com.pgloaguen.mycleanarchitectureexample.di.module;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.FavoriteRepoRepository;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;
import com.pgloaguen.domain.repository.GetUserRepoRepository;
import com.pgloaguen.domain.usecase.FavoriteRepo;
import com.pgloaguen.domain.usecase.FavoriteRepoWithId;
import com.pgloaguen.domain.usecase.FavoriteRepoDetails;
import com.pgloaguen.domain.usecase.GetUserRepo;
import com.pgloaguen.domain.usecase.GetUserRepoDetails;
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
    public UseCase<List<RepoEntity>, GetUserRepo.Param> provideGetUserRepo(GetUserRepoRepository repository) {
        return new GetUserRepo(repository, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @AppScope
    @Provides
    public UseCase<RepoDetailsEntity, GetUserRepoDetails.Param> provideGetUserRepoDetails(GetUserRepoDetailsRepository repository) {
        return new GetUserRepoDetails(repository, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @AppScope
    @Provides
    public FavoriteRepoWithId provideFavoriteRepoUseCase(FavoriteRepoRepository repository) {
        return new FavoriteRepoWithId(repository, Schedulers.io(), AndroidSchedulers.mainThread());
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
