package com.pgloaguen.data.di;

import com.pgloaguen.data.interactor.GetUserRepoWSInteractor;
import com.pgloaguen.data.net.GetUserRepoWS;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.interactor.GetUserRepoInteractor;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by paul on 19/01/2017.
 */

@Module
public class InteractorModule {

    @Provides
    @Singleton
    public GetUserRepoInteractor buildGetUserRepoWSInteractor(Retrofit retrofit) {
        return new GetUserRepoWSInteractor(retrofit.create(GetUserRepoWS.class), new RepoEntityTransformer());
    }
}
