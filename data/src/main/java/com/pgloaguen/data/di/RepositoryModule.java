package com.pgloaguen.data.di;

import com.pgloaguen.data.repository.GetUserRepoWSRepository;
import com.pgloaguen.data.net.GetUserRepoWS;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.repository.GetUserRepoRepository;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by paul on 19/01/2017.
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public GetUserRepoRepository buildGetUserRepoWSInteractor(Retrofit retrofit) {
        return new GetUserRepoWSRepository(retrofit.create(GetUserRepoWS.class), new RepoEntityTransformer());
    }
}
