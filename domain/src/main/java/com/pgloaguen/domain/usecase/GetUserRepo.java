package com.pgloaguen.domain.usecase;


import com.google.auto.value.AutoValue;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoRepository;
import com.pgloaguen.domain.usecase.base.UseCase;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepo extends UseCase<List<RepoEntity>, GetUserRepo.Param> {

    private final GetUserRepoRepository repository;

    public GetUserRepo(GetUserRepoRepository repository, Scheduler runScheduler, Scheduler postScheduler) {
        super(runScheduler, postScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<List<RepoEntity>> build(Param param) {
        Observable<List<RepoEntity>> fetchData;
        if (param.invalidate()) {
            fetchData = repository.fetchUserRepo(param.username()).toObservable();
        } else {
            fetchData = repository.fetchLastUserRepoResult(param.username())
                        .onErrorResumeNext(Maybe.empty())
                        .switchIfEmpty(repository.fetchUserRepo(param.username()).toMaybe())
                        .toObservable();
        }

        return repository.registerRepoUpdated(param.username()).retry().startWith(fetchData);
    }

    @AutoValue
    public static abstract class Param {
        abstract String username();
        abstract boolean invalidate();

        public static Param create(String username, boolean invalidate) {
            return new AutoValue_GetUserRepo_Param(username, invalidate);
        }
    }
}
