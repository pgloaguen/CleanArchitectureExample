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
    public abstract boolean isFavorite();

    public static RepoEntity create(long id, String name, String desc, boolean isFavorite) {
        return new AutoValue_RepoEntity(id, name, desc, isFavorite);
    }

    public static RepoEntity create(RepoEntity repoEntity, boolean isFavorite) {
        return new AutoValue_RepoEntity(repoEntity.id(), repoEntity.name(), repoEntity.desc(), isFavorite);
    }
}
