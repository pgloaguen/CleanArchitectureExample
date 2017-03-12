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
    public abstract boolean isFavorite();

    public static RepoDetailsEntity create(long id, String name, String desc, boolean isFavorite) {
        return new AutoValue_RepoDetailsEntity(id, name, desc, isFavorite);
    }

    public static RepoDetailsEntity create(RepoDetailsEntity repoDetailsEntity, boolean isFavorite) {
        return new AutoValue_RepoDetailsEntity(repoDetailsEntity.id(), repoDetailsEntity.name(), repoDetailsEntity.desc(), isFavorite);
    }
}
