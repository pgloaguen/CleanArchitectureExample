package com.pgloaguen.mycleanarchitectureexample.base.presenter;

import android.support.annotation.NonNull;

/**
 * Created by paul on 20/01/2017.
 */

public interface PresenterListener<T> {
    void update(@NonNull T viewModel);
}
