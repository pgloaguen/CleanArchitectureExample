package com.pgloaguen.data.cache;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by paul on 21/02/2017.
 */

public class NoCache<T> implements Cache<T> {

    @Override
    public Maybe<T> get(String key) {
        return Maybe.never();
    }

    @Override
    public Maybe<List<T>> getAll(String key) {
        return Maybe.never();
    }

    @Override
    public Completable save(String key, List<T> p) {
        return Completable.complete();
    }

    @Override
    public Completable save(String key, T p) {
        return Completable.complete();
    }

    @Override
    public Observable<T> register(String key) {
        return Observable.empty();
    }

    @Override
    public Observable<List<T>> registerList(String key) {
        return Observable.empty();
    }
}
