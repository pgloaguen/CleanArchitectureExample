package com.pgloaguen.data.repository;

import com.pgloaguen.data.model.RepoDetails;
import com.pgloaguen.data.net.GetUserRepoDetailsEndpoint;
import com.pgloaguen.data.net.utils.ConnectionFilter;
import com.pgloaguen.data.net.utils.ConnectionUtils;
import com.pgloaguen.data.transformer.RepoDetailsEntityTransformer;
import com.pgloaguen.domain.entity.RepoDetailsEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Created by paul on 24/02/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class GetUserRepoDetailsRepositoryImplTest {

    @Mock
    GetUserRepoDetailsEndpoint userRepoWs;

    @Mock
    RepoDetailsEntityTransformer transformer;

    @Mock
    ConnectionUtils connectionUtils;

    @Test
    public void fetchHappyCase() {
        RepoDetails repoDetails = mock(RepoDetails.class);
        RepoDetailsEntity repoDetailsEntity = mock(RepoDetailsEntity.class);
        given(userRepoWs.fetch(anyString(), anyString())).willReturn(Single.just(repoDetails));
        given(transformer.transform(any())).willReturn(repoDetailsEntity);
        given(connectionUtils.isConnected()).willReturn(true);

        GetUserRepoDetailsRepositoryImpl repository = new GetUserRepoDetailsRepositoryImpl(userRepoWs, transformer, connectionUtils);
        repository.fetchUserRepoDetails("", "")
                .test()
                .assertValue(repoDetailsEntity);
    }

    @Test
    public void fetchNoNetworkError() {
        RepoDetails repoDetails = mock(RepoDetails.class);
        given(userRepoWs.fetch(anyString(), anyString())).willReturn(Single.just(repoDetails));
        given(connectionUtils.isConnected()).willReturn(false);

        GetUserRepoDetailsRepositoryImpl repository = new GetUserRepoDetailsRepositoryImpl(userRepoWs, transformer, connectionUtils);
        repository.fetchUserRepoDetails("", "")
                .test()
                .assertError(ConnectionFilter.NoConnectedException.class);
    }

}