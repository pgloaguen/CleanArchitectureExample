package com.pgloaguen.data.di;

import com.pgloaguen.data.net.GetUserRepoDetailsWsTest;
import com.pgloaguen.data.net.GetUserRepoWsTest;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by paul on 19/01/2017.
 */

@Singleton
@Component(modules = {ParserModule.class, CacheModule.class, NetModule.class, RepositoryModule.class})
public interface DataComponentTest extends DataComponent {
    void inject(GetUserRepoWsTest getUserRepoWsTest);
    void inject(GetUserRepoDetailsWsTest getUserRepoDetailsWsTest);
}
