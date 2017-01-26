package com.pgloaguen.domain.entity;

import com.google.auto.value.AutoValue;

/**
 * Created by paul on 19/01/2017.
 */

@AutoValue
public abstract class RepoDetailsEntity {
    public abstract long id();
    public abstract String name();
    public abstract String desc();

    public static RepoDetailsEntity create(long id, String name, String desc) {
        return new AutoValue_RepoDetailsEntity(id, name, desc);
    }
}
