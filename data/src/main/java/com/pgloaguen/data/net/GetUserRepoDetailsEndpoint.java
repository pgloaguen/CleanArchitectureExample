package com.pgloaguen.data.net;

import com.pgloaguen.data.model.Repo;
import com.pgloaguen.data.model.RepoDetails;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by paul on 19/01/2017.
 */

public interface GetUserRepoDetailsEndpoint {
    @GET("/repos/{user}/{repoName}")
    Single<RepoDetails> fetch(@Path("user") String user, @Path("repoName") String repoName);
}
