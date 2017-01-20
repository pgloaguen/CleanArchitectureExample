package com.pgloaguen.data.di;

import com.pgloaguen.domain.interactor.GetUserRepoInteractor;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by paul on 19/01/2017.
 */

@Singleton
@Component(modules = {NetModule.class, InteractorModule.class})
public interface DataComponent {
    GetUserRepoInteractor getUserRepoInteractor();
}
