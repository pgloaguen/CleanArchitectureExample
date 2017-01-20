package com.pgloaguen.domain.usecase.base;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

/**
 * Created by paul on 19/01/2017.
 */

public abstract class BaseUseCase<T> {

    private final Scheduler runScheduler;
    private final Scheduler postScheduler;

    public BaseUseCase(Scheduler runScheduler, Scheduler postScheduler) {
        this.runScheduler = runScheduler;
        this.postScheduler = postScheduler;
    }

    protected ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(runScheduler).observeOn(postScheduler);
            }
        };
    }
}
