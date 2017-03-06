package com.pgloaguen.data.cache;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by paul on 24/01/2017.
 */

public interface Cache<T> {
    Maybe<T> get(String key);
    Maybe<List<T>> getAll(String key);
    Completable save(String key, List<T> p);
    Completable save(String key, T p);
    Observable<T> register(String key);
    Observable<List<T>> registerList(String key);
}
