package com.pgloaguen.mycleanarchitectureexample.base.presenter;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by paul on 01/02/2017.
 */

public class PresenterCache<T> {

    private final HashMap<String, T> presenterCache = new HashMap<>();

    public PresenterCache() {}

    public String storePresenter(T object) {
        String key = UUID.randomUUID().toString();
        presenterCache.put(key, object);
        return key;
    }

    public void removePresenter(String key) {
        presenterCache.remove(key);
    }

    public T getPresenter(String key) {
        return presenterCache.remove(key);
    }
}
