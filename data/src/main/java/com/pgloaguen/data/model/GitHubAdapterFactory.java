package com.pgloaguen.data.model;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class GitHubAdapterFactory implements TypeAdapterFactory {
    public static GitHubAdapterFactory create() {
        return new AutoValueGson_GitHubAdapterFactory();
    }
}