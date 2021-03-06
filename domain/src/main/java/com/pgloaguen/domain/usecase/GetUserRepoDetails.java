package com.pgloaguen.domain.usecase;


import com.google.auto.value.AutoValue;
import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;
import com.pgloaguen.domain.usecase.base.UseCase;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoDetails extends UseCase<RepoDetailsEntity, GetUserRepoDetails.Param> {

    private final GetUserRepoDetailsRepository repository;

    public GetUserRepoDetails(GetUserRepoDetailsRepository repository, Scheduler runScheduler, Scheduler postScheduler) {
        super(runScheduler, postScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<RepoDetailsEntity> build(Param param) {
        return repository.fetchUserRepoDetails(param.username(), param.repoName()).toObservable();
    }

    @AutoValue
    public static abstract class Param {
        public abstract String username();
        public abstract String repoName();
        public abstract boolean invalidateData();

        public static Param create(String username, String repoName, boolean invalidateData) {
            return new AutoValue_GetUserRepoDetails_Param(username, repoName, invalidateData);
        }
    }
}
