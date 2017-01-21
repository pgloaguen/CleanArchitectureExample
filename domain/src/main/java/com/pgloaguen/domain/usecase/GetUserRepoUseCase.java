package com.pgloaguen.domain.usecase;


import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.interactor.GetUserRepoInteractor;
import com.pgloaguen.domain.usecase.base.BaseUseCase;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoUseCase extends BaseUseCase<List<RepoEntity>> {

    private final GetUserRepoInteractor interactor;

    public GetUserRepoUseCase(GetUserRepoInteractor interactor, Scheduler runScheduler, Scheduler postScheduler) {
        super(runScheduler, postScheduler);
        this.interactor = interactor;
    }

    public Observable<List<RepoEntity>> execute(String username) {
        return interactor.listUserRepo(username).compose(applySchedulers());
    }
}
