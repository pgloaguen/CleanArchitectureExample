package com.pgloaguen.data.net.utils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public class ConnectionFilter<T> implements ObservableTransformer<T, T> {

    private final ConnectionUtils connectionUtils;

    @Inject
    public ConnectionFilter(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return connectionUtils.isConnected() ? upstream : Observable.error(new NoConnectedException());
    }

    public static class NoConnectedException extends RuntimeException {
        NoConnectedException() {
            super("No connexion");
        }
    }
}