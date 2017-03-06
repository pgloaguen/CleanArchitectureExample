package com.pgloaguen.data.cache;

import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class SharedPreferenceCache<T> implements Cache<T> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static final String PARSER_SEP = "::::";

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static final String EMPTY = "";

    private final Gson gson;
    private final SharedPreferences sharedPreferences;
    private final Class<T> tClass;

    public SharedPreferenceCache(SharedPreferences sharedPreferences, Gson gson, Class<T> tClass) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        this.tClass = tClass;
    }

    @Override
    public Maybe<T> get(String key) {
        return getAll(key).flatMapObservable(Observable::fromIterable).firstElement();
    }

    @Override
    public Maybe<List<T>> getAll(String key) {
        return Maybe
                .just(sharedPreferences.getString(key, EMPTY))
                .flatMapObservable(s -> Observable.fromArray(s.split(PARSER_SEP)))
                .map(json -> gson.fromJson(json, tClass))
                .toList()
                .flatMapMaybe(l -> l.isEmpty() ? Maybe.never() : Maybe.just(l))
                .onErrorComplete();
    }

    @Override
    public Completable save(String key, List<T> p) {
        return Observable
                .fromIterable(p)
                .flatMapSingle(this::toJson)
                .reduce((s, s2) -> s + PARSER_SEP + s2)
                .doOnSuccess(s -> sharedPreferences.edit().putString(key, s).apply())
                .ignoreElement();

    }

    @Override
    public Completable save(String key, T p) {
        return toJson(p).doOnSuccess(s -> sharedPreferences.edit().putString(key, s).apply()).toCompletable();
    }

    @Override
    public Observable<T> register(String key) {
        return Observable.create(e -> sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, s) -> {
            if (s.equals(key)) {
                get(key).subscribe(e::onNext);
            }
        }));
    }

    @Override
    public Observable<List<T>> registerList(String key) {
        return Observable.create(e -> sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, s) -> {
            if (s.equals(key)) {
                getAll(key).subscribe(e::onNext);
            }
        }));
    }

    private Single<String> toJson(T p) {
        return Single
                .just(p)
                .map(gson::toJson);
    }
}