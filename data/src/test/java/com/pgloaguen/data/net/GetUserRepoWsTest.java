package com.pgloaguen.data.net;

import com.pgloaguen.data.di.DaggerDataComponentTest;

import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Test
    public void getUserRepoWsWorks() {
        DaggerDataComponentTest.create().inject(this);

        retrofit.create(GetUserRepoWS.class).list("pgloaguen").test().assertNoErrors();
    }

}
