package com.pgloaguen.domain.interactor;

import com.pgloaguen.domain.entity.RepoEntity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by paul on 19/01/2017.
 */

public interface GetUserRepoInteractor {
    Observable<List<RepoEntity>> listUserRepo(String user);
}
