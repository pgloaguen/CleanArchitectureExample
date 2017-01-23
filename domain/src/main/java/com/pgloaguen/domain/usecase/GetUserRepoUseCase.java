package com.pgloaguen.domain.usecase;


import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoRepository;
import com.pgloaguen.domain.usecase.base.UseCase;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoUseCase extends UseCase<List<RepoEntity>, String> {

    private final GetUserRepoRepository repository;

    public GetUserRepoUseCase(GetUserRepoRepository repository, Scheduler runScheduler, Scheduler postScheduler) {
        super(runScheduler, postScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<List<RepoEntity>> build(String param) {
        return repository.listUserRepo(param);
    }
}
