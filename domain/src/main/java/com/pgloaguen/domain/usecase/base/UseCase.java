package com.pgloaguen.domain.usecase.base;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

/**
 * Created by paul on 19/01/2017.
 */

public abstract class UseCase<R, P> {

    private final Scheduler runScheduler;
    private final Scheduler postScheduler;

    public UseCase(Scheduler runScheduler, Scheduler postScheduler) {
        this.runScheduler = runScheduler;
        this.postScheduler = postScheduler;
    }

    private ObservableTransformer<R, R> applySchedulers() {
        return upstream -> upstream.subscribeOn(runScheduler).observeOn(postScheduler);
    }

    protected abstract Observable<R> build(P param);

    public Observable<R> execute(P param) {
        return build(param).compose(applySchedulers());
    }
}
