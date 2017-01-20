package com.pgloaguen.data.interactor;

import com.pgloaguen.data.model.Repo;
import com.pgloaguen.data.net.GetUserRepoWS;
import com.pgloaguen.data.transformer.RepoEntityTransformer;
import com.pgloaguen.domain.entity.RepoEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
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
public class GetUserRepoWSInteractorTest {

    @Mock
    GetUserRepoWS userRepoWS;

    @Mock
    Consumer<List<RepoEntity>> consumer;

    @Mock
    RepoEntityTransformer transformer;

    @Test
    public void listUserRepo() throws Exception {

        given(userRepoWS.list(anyString())).willReturn(Single.just(Arrays.asList(mock(Repo.class))));
        given(transformer.transform(any())).willReturn(mock(RepoEntity.class));

        Observable<List<RepoEntity>> o = new GetUserRepoWSInteractor(userRepoWS, transformer).listUserRepo("");
        o.subscribe(consumer);

        verify(consumer).accept(anyList());
    }

}