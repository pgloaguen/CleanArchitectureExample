package com.pgloaguen.data.di;

import com.pgloaguen.domain.repository.FavoriteRepoRepository;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;
import com.pgloaguen.domain.repository.GetUserRepoRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by paul on 19/01/2017.
 */

@Singleton
@Component(modules = {ParserModule.class, CacheModule.class, NetModule.class, RepositoryModule.class})
public interface DataComponent {
    GetUserRepoRepository getUserRepoRepository();
    GetUserRepoDetailsRepository getUserRepoDetailsRepository();
    FavoriteRepoRepository buildFavoriteRepoRepository();
}
