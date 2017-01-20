package com.pgloaguen.mycleanarchitectureexample.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by paul on 20/01/2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}