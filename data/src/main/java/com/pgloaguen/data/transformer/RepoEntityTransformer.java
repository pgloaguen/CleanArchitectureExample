package com.pgloaguen.data.transformer;

import com.pgloaguen.data.model.Repo;
import com.pgloaguen.domain.entity.RepoEntity;

/**
 * Created by paul on 19/01/2017.
 */

public class RepoEntityTransformer implements Transformer<Repo, RepoEntity>{

    public RepoEntity transform(Repo repo) {
        return RepoEntity.create(repo.id(), repo.name(), repo.description() == null ? "" : repo.description(), false);
    }

}
