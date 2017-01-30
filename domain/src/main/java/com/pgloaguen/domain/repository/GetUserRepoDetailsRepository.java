package com.pgloaguen.domain.repository;

import com.pgloaguen.domain.entity.RepoDetailsEntity;

import io.reactivex.Observable;

/**
 * Created by paul on 19/01/2017.
 */

public interface GetUserRepoDetailsRepository {
    Observable<RepoDetailsEntity> fetchUserRepoDetails(String user, String repoName);
}
