package com.pgloaguen.data.net;

import android.content.Context;

import com.pgloaguen.data.di.DaggerDataComponentTest;
import com.pgloaguen.data.di.NetModule;
import com.pgloaguen.data.di.RepositoryModule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by paul on 19/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class GetUserRepoWsTest {

    @Inject
    Retrofit retrofit;

    @Mock
    Context context;

    @Test
    public void getUserRepoWsWorks() {
        DaggerDataComponentTest.builder().netModule(new NetModule(context)).repositoryModule(new RepositoryModule()).build().inject(this);

        retrofit.create(GetUserRepoEndpoint.class).list("pgloaguen").test().assertNoErrors();
    }

}
