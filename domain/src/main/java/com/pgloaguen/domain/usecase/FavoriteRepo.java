package com.pgloaguen.domain.usecase;

import com.pgloaguen.domain.repository.FavoriteRepoRepository;
import com.pgloaguen.domain.usecase.base.UseCase;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by paul on 24/02/2017.
 */

public class FavoriteRepo extends UseCase<Boolean, Long> {

    private final FavoriteRepoRepository repository;

    public FavoriteRepo(FavoriteRepoRepository repository, Scheduler runScheduler, Scheduler postScheduler) {
        super(runScheduler, postScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> build(Long repoId) {
        return repository.isFavorite(repoId)
                .flatMap(isFavorite -> isFavorite ? repository.unfavoriteRepo(repoId).toSingleDefault(false) : repository.favoriteRepo(repoId).toSingleDefault(true))
                .toObservable();
    }
}
