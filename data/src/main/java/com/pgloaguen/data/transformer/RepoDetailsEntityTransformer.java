package com.pgloaguen.data.transformer;

import com.pgloaguen.data.model.RepoDetails;
import com.pgloaguen.domain.entity.RepoDetailsEntity;

/**
 * Created by paul on 19/01/2017.
 */

public class RepoDetailsEntityTransformer implements Transformer<RepoDetails, RepoDetailsEntity> {

    public RepoDetailsEntity transform(RepoDetails repo) {
        return RepoDetailsEntity.create(repo.id(), repo.name(), repo.description() == null ? "" : repo.description(), false);
    }

}
