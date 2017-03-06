package com.pgloaguen.domain.usecase;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.base.UseCase;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by polo on 04/03/2017.
 */

public class FavoriteRepo extends UseCase<RepoEntity, RepoEntity> {

    private FavoriteRepoWithId favoriteRepo;

    public FavoriteRepo(FavoriteRepoWithId favoriteRepo, Scheduler runScheduler, Scheduler postScheduler) {
        super(runScheduler, postScheduler);
        this.favoriteRepo = favoriteRepo;
    }

    @Override
    protected Observable<RepoEntity> build(RepoEntity param) {
        return favoriteRepo.execute(param.id()).map(isFavorite -> RepoEntity.create(param, isFavorite));
    }
}
