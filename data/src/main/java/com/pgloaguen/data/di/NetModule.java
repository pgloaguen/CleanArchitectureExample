package com.pgloaguen.data.di;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.pgloaguen.data.model.GitHubAdapterFactory;
import com.pgloaguen.data.net.utils.ConnectionUtils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by paul on 19/01/2017.
 */
@Module
public class NetModule {


    private final Context context;

    public NetModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public ConnectionUtils provideConnectionUtils() {
        return new ConnectionUtils(((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)));
    }

    @Singleton
    @Provides
    public HttpLoggingInterceptor okHttpLogger() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logging;
    }

    @Singleton
    @Provides
    public OkHttpClient okHttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Singleton
    @Provides
    public Gson gsonBuilder() {
        return new GsonBuilder().registerTypeAdapterFactory(GitHubAdapterFactory.create()).create();
    }

    @Singleton
    @Provides
    public Converter.Factory converterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Singleton
    @Provides
    @Named("githubUrl")
    public String baseUrl() {
        return "https://api.github.com/";
    }


    @Singleton
    @Provides
    public Retrofit retrofit(@Named("githubUrl") String baseUrl, OkHttpClient client, Converter.Factory factory) {
        return new Retrofit.Builder().baseUrl(baseUrl).client(client).addConverterFactory(factory).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }
}
