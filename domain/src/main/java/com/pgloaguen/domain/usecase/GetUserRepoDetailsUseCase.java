package com.pgloaguen.domain.usecase;


import com.google.auto.value.AutoValue;
import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;
import com.pgloaguen.domain.repository.GetUserRepoRepository;
import com.pgloaguen.domain.usecase.base.UseCase;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by paul on 19/01/2017.
 */

public class GetUserRepoDetailsUseCase extends UseCase<RepoDetailsEntity, GetUserRepoDetailsUseCase.Param> {

    private final GetUserRepoDetailsRepository repository;

    public GetUserRepoDetailsUseCase(GetUserRepoDetailsRepository repository, Scheduler runScheduler, Scheduler postScheduler) {
        super(runScheduler, postScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<RepoDetailsEntity> build(Param param) {
        return repository.fetchUserRepoDetails(param.username(), param.repoName());
    }

    @AutoValue
    public static abstract class Param {
        public abstract String username();
        public abstract String repoName();

        public static Param create(String username, String repoName) {
            return new AutoValue_GetUserRepoDetailsUseCase_Param(username, repoName);
        }
    }
}
