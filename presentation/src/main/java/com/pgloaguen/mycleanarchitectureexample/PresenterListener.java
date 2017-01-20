package com.pgloaguen.mycleanarchitectureexample;

/**
 * Created by paul on 20/01/2017.
 */

public interface PresenterListener<T> {
    void update(T viewModel);
}
