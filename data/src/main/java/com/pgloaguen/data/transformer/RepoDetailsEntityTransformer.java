package com.pgloaguen.data.transformer;

import com.pgloaguen.data.model.Repo;
import com.pgloaguen.data.model.RepoDetails;
import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.entity.RepoEntity;

/**
 * Created by paul on 19/01/2017.
 */

public class RepoDetailsEntityTransformer {

    public RepoDetailsEntity transform(RepoDetails repo) {
        return RepoDetailsEntity.create(repo.id(), repo.name(), repo.description() == null ? "" : repo.description());
    }

}
