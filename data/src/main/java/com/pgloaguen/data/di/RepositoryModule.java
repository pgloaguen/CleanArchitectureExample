package com.pgloaguen.data.di;

import android.content.Context;
import android.net.ConnectivityManager;

import com.pgloaguen.data.net.GetUserRepoDetailsEndpoint;
import com.pgloaguen.data.net.utils.ConnectionUtils;
import com.pgloaguen.data.repository.GetUserRepoDetailsRepositoryImpl;
import com.pgloaguen.data.net.GetUserRepoEndpoint;
import com.pgloaguen.data.repository.GetUserRepoRepositoryImpl;
import com.pgloaguen.data.transformer.RepoDetailsEntityTransformer;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.repository.GetUserRepoDetailsRepository;
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

    private final Context context;

    public RepositoryModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public ConnectionUtils provideConnectionUtils() {
        return new ConnectionUtils(((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)));
    }

    @Provides
    @Singleton
    public GetUserRepoRepository buildGetUserRepoRepository(Retrofit retrofit, ConnectionUtils connectionUtils) {
        return new GetUserRepoRepositoryImpl(retrofit.create(GetUserRepoEndpoint.class), new RepoEntityTransformer(), connectionUtils);
    }

    @Provides
    @Singleton
    public GetUserRepoDetailsRepository buildGetUserRepoDetailsRepository(Retrofit retrofit, ConnectionUtils connectionUtils) {
        return new GetUserRepoDetailsRepositoryImpl(retrofit.create(GetUserRepoDetailsEndpoint.class), new RepoDetailsEntityTransformer(), connectionUtils);
    }
}
