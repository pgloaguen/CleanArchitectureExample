package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.PresenterListener;
import com.pgloaguen.mycleanarchitectureexample.presenter.RemoteDataWithRefreshingStatePresenter;
import com.pgloaguen.mycleanarchitectureexample.state.RemoteDataWithRefreshingState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by paul on 20/01/2017.
 */

public class ListUserRepoPresenter extends RemoteDataWithRefreshingStatePresenter<RepoEntity, String>{

    public ListUserRepoPresenter(UseCase<List<RepoEntity>, String> getUserRepoUseCase) {
        super(getUserRepoUseCase);
    }

    @Override
    public Observable<List<RepoEntity>> executeUseCase(UseCase<List<RepoEntity>, String> useCase) {
        return useCase.execute("pgloaguen");
    }
}
