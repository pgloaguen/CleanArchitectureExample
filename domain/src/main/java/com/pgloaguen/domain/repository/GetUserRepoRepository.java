package com.pgloaguen.domain.repository;

import com.pgloaguen.domain.entity.RepoEntity;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by paul on 19/01/2017.
 */

public interface GetUserRepoRepository {
    Single<List<RepoEntity>> fetchUserRepo(String user);
    Maybe<List<RepoEntity>> fetchLastUserRepoResult(String user);
}
