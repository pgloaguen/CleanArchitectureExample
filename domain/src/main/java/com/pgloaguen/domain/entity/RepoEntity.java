package com.pgloaguen.domain.entity;

import com.google.auto.value.AutoValue;

/**
 * Created by paul on 19/01/2017.
 */

@AutoValue
public abstract class RepoEntity {
    public abstract long id();
    public abstract String name();
    public abstract String desc();

    public static RepoEntity create(long id, String name, String desc) {
        return new AutoValue_RepoEntity(id, name, desc);
    }
}
