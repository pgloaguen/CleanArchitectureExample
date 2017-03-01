package com.pgloaguen.data.repository;

import com.pgloaguen.data.cache.Cache;
import com.pgloaguen.data.model.Repo;
import com.pgloaguen.data.net.GetUserRepoEndpoint;
import com.pgloaguen.data.net.utils.ConnectionFilter;
import com.pgloaguen.data.net.utils.ConnectionUtils;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.repository.FavoriteRepoRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by paul on 19/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUserRepoRepositoryImplTest {

    @Mock
    GetUserRepoEndpoint userRepoWS;

    @Mock
    Consumer<List<RepoEntity>> consumer;

    @Mock
    RepoEntityTransformer transformer;

    @Mock
    ConnectionUtils connectionUtils;

    @Mock
    Cache<Repo> cache;

    @Mock
    FavoriteRepoRepository favoriteRepoRepository;

    @Test
    public void fetchUserRepoHappyCase() throws Exception {
        List<Repo> repos = Collections.singletonList(mock(Repo.class));
        given(userRepoWS.list(anyString())).willReturn(Single.just(repos));
        given(transformer.transform(any())).willReturn(RepoEntity.create(0, "name", "desc", true));
        given(connectionUtils.isConnected()).willReturn(true);
        given(cache.save(anyString(), anyList())).willReturn(Completable.complete());
        given(favoriteRepoRepository.isFavorite(anyLong())).willReturn(Single.just(true));

        new GetUserRepoRepositoryImpl(userRepoWS, transformer, cache, connectionUtils, favoriteRepoRepository).fetchUserRepo("")
            .test()
            .assertValue(repoEntities -> !repoEntities.isEmpty());

        verify(cache, times(1)).save(anyString(), anyList());
    }

    @Test
    public void fetchUserRepoNoNetworkThrowError() throws Exception {
        given(connectionUtils.isConnected()).willReturn(false);

        new GetUserRepoRepositoryImpl(userRepoWS, transformer, cache, connectionUtils, favoriteRepoRepository).fetchUserRepo("")
                .test()
                .assertError(ConnectionFilter.NoConnectedException.class);
    }

    @Test
    public void fetchLastUserRepoResultHappyCase() throws Exception {
        List<Repo> repos = Collections.singletonList(mock(Repo.class));
        given(cache.getAll(anyString())).willReturn(Maybe.just(repos));
        given(transformer.transform(any())).willReturn(mock(RepoEntity.class));

        new GetUserRepoRepositoryImpl(userRepoWS, transformer, cache, connectionUtils, favoriteRepoRepository).fetchLastUserRepoResult("")
                .test()
                .assertValue(repoEntities -> !repoEntities.isEmpty());
    }

    @Test
    public void fetchLastUserRepoResultEmpty() throws Exception {
        given(cache.getAll(anyString())).willReturn(Maybe.empty());
        new GetUserRepoRepositoryImpl(userRepoWS, transformer, cache, connectionUtils, favoriteRepoRepository).fetchLastUserRepoResult("")
                .test()
                .assertNoValues();
    }
}