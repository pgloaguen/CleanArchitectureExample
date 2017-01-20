package com.pgloaguen.data.net;

import com.pgloaguen.data.model.Repo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by paul on 19/01/2017.
 */

public interface GetUserRepoWS {
    @GET("/users/{user}/repos")
    Single<List<Repo>> list(@Path("user") String user);
}
