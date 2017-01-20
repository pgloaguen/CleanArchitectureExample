package com.pgloaguen.data.transformer;


import com.pgloaguen.data.model.Repo;
import com.pgloaguen.domain.entity.RepoEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by paul on 19/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class RepoEntityTransformerTest {

    @Mock
    Repo repo;

    @Test
    public void transformAllDataFilled() {

        given(repo.id()).willReturn(1l);
        given(repo.name()).willReturn("toto");
        given(repo.description()).willReturn("desc");

        RepoEntity repoEntity = new RepoEntityTransformer().transform(repo);

        assertTrue(repoEntity.id() == repo.id());
        assertTrue(repoEntity.name().equals(repo.name()));
        assertTrue(repoEntity.desc().equals(repo.description()));
    }

    @Test
    public void transformDescIsNull() {

        given(repo.id()).willReturn(1l);
        given(repo.name()).willReturn("toto");
        given(repo.description()).willReturn(null);

        RepoEntity repoEntity = new RepoEntityTransformer().transform(repo);

        assertTrue(repoEntity.id() == repo.id());
        assertTrue(repoEntity.name().equals(repo.name()));
        assertNotNull(repoEntity.desc());
    }

}