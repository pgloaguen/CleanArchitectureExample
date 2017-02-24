package com.pgloaguen.data.repository;

import com.pgloaguen.data.cache.Cache;
import com.pgloaguen.data.cache.NoCache;
import com.pgloaguen.data.model.Repo;
import com.pgloaguen.data.net.GetUserRepoEndpoint;
import com.pgloaguen.data.net.utils.ConnectionUtils;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.entity.RepoEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by paul on 19/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUserRepoEndpointTest {

    @Mock
    GetUserRepoEndpoint userRepoWS;

    @Mock
    Consumer<List<RepoEntity>> consumer;

    @Mock
    RepoEntityTransformer transformer;

    @Mock
    ConnectionUtils connectionUtils;

    Cache<Repo> cache = new NoCache<>();

    @Test
    public void listUserRepo() throws Exception {

        given(userRepoWS.list(anyString())).willReturn(Single.just(Collections.singletonList(mock(Repo.class))));
        given(transformer.transform(any())).willReturn(mock(RepoEntity.class));
        given(connectionUtils.isConnected()).willReturn(true);

        Single<List<RepoEntity>> o = new GetUserRepoRepositoryImpl(userRepoWS, transformer, cache, connectionUtils).fetchUserRepo("");
        o.subscribe(consumer);

        verify(consumer).accept(anyList());
    }
}