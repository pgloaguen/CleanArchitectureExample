package com.pgloaguen.data.cache;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by paul on 21/02/2017.
 */

public class CacheFactory {

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public CacheFactory(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }


    public <T> Cache<T> buildCache(Class<T> tClass) {
        return new SharedPreferenceCache<T>(sharedPreferences, gson, tClass);
    }

    public <T> Cache<T> buildNoCache() {
        return new NoCache<T>();
    }


}
