package com.pgloaguen.data.di;

import com.pgloaguen.domain.repository.GetUserRepoRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by paul on 19/01/2017.
 */

@Singleton
@Component(modules = {NetModule.class, RepositoryModule.class})
public interface DataComponent {
    GetUserRepoRepository getUserRepoRepository();
}
