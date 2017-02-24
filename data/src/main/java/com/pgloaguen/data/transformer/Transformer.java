package com.pgloaguen.data.transformer;

/**
 * Created by paul on 22/02/2017.
 */

public interface Transformer<F, T> {
    T transform(F from);
}
