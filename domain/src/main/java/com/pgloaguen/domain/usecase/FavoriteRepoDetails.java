package com.pgloaguen.domain.usecase;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.usecase.base.UseCase;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by polo on 04/03/2017.
 */

public class FavoriteRepoDetails extends UseCase<RepoDetailsEntity, RepoDetailsEntity> {

    private FavoriteRepoWithId favoriteRepo;

    public FavoriteRepoDetails(FavoriteRepoWithId favoriteRepo, Scheduler runScheduler, Scheduler postScheduler) {
        super(runScheduler, postScheduler);
        this.favoriteRepo = favoriteRepo;
    }

    @Override
    protected Observable<RepoDetailsEntity> build(RepoDetailsEntity param) {
        return favoriteRepo.execute(param.id()).map(isFavorite -> RepoDetailsEntity.create(param, isFavorite));
    }
}
