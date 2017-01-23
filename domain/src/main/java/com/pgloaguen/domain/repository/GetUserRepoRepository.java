package com.pgloaguen.domain.repository;

import com.pgloaguen.domain.entity.RepoEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by paul on 19/01/2017.
 */

public interface GetUserRepoRepository {
    Observable<List<RepoEntity>> listUserRepo(String user);
}
