package com.pgloaguen.data.di;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

/**
 * Created by paul on 19/01/2017.
 */
@Module
public class CacheModule {


    private final Context context;

    public CacheModule(Context context) {
        this.context = context;
    }

    @Provides
    SharedPreferences sharedPreferences() {
        return context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
    }
}
