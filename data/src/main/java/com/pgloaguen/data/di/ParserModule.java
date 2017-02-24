package com.pgloaguen.data.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pgloaguen.data.model.GitHubAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by paul on 21/02/2017.
 */

@Module
public class ParserModule {
    @Singleton
    @Provides
    public Gson gsonBuilder() {
        return new GsonBuilder().registerTypeAdapterFactory(GitHubAdapterFactory.create()).create();
    }
}
