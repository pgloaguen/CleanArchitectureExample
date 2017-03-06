package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.FavoriteRepo;
import com.pgloaguen.domain.usecase.GetUserRepo;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.base.state.StateMachine;
import com.pgloaguen.mycleanarchitectureexample.navigator.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.EventValue.DATA_UPDATE;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.EventValue.ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.EventValue.LOAD;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.EventValue.NEW_DATA;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.EventValue.NO_DATA;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_DATA;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_DATA_WITH_ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_EMPTY;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_LOADING;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_LOADING_WITH_ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_REFRESHING;

/**
 * Created by paul on 01/03/2017.
 */

public class ListUserRepoPresenter {

    private Disposable viewDisposable = Disposables.empty();

    public enum StateValue {
        SHOW_EMPTY, SHOW_LOADING, SHOW_REFRESHING, SHOW_LOADING_WITH_ERROR, SHOW_DATA, SHOW_DATA_WITH_ERROR, SHOW_ERROR
    }

    public enum EventValue {
        LOAD, NEW_DATA, DATA_UPDATE, NO_DATA, ERROR
    }

    public static class VM {
        @NonNull
        public List<RepoEntity> repoEntities;

        @Nullable
        public Throwable error;

        public VM(@NonNull List<RepoEntity> repoEntities, @Nullable Throwable error) {
            this.repoEntities = repoEntities;
            this.error = error;
        }

        public VM() {
            this(Collections.emptyList(), null);
        }
    }

    private final FavoriteRepo favoriteRepoUseCase;
    private final Navigator navigator;
    private final UseCase<List<RepoEntity>, GetUserRepo.Param> getUserRepoUseCase;
    private final StateMachine<StateValue, EventValue, VM> stateMachine;


    @Inject
    public ListUserRepoPresenter(UseCase<List<RepoEntity>, GetUserRepo.Param> getUserRepoUseCase, FavoriteRepo favoriteRepoUseCase, Navigator navigator) {
        this.getUserRepoUseCase = getUserRepoUseCase;
        this.favoriteRepoUseCase = favoriteRepoUseCase;
        this.navigator = navigator;
        this.stateMachine = initStateMachine();
    }

    private StateMachine<StateValue, EventValue, VM> initStateMachine() {
        StateMachine.State<StateValue, EventValue, VM> stateShowEmpty = new StateMachine.State<>(SHOW_EMPTY);
        StateMachine.State<StateValue, EventValue, VM> stateShowLoading = new StateMachine.State<>(SHOW_LOADING);
        StateMachine.State<StateValue, EventValue, VM> stateShowRefreshing = new StateMachine.State<>(SHOW_REFRESHING);
        StateMachine.State<StateValue, EventValue, VM> stateShowLoadingWithError = new StateMachine.State<>(SHOW_LOADING_WITH_ERROR);
        StateMachine.State<StateValue, EventValue, VM> stateShowData = new StateMachine.State<>(SHOW_DATA);
        StateMachine.State<StateValue, EventValue, VM> stateShowDataWithError = new StateMachine.State<>(SHOW_DATA_WITH_ERROR);
        StateMachine.State<StateValue, EventValue, VM> stateShowError = new StateMachine.State<>(SHOW_ERROR);

        stateShowEmpty
                .addEvent(LOAD, stateShowLoading);

        stateShowData
                .addEvent(LOAD, stateShowRefreshing)
                .addEvent(DATA_UPDATE, stateShowData);

        stateShowDataWithError
                .addEvent(LOAD, stateShowLoadingWithError)
                .addEvent(DATA_UPDATE, stateShowData);

        stateShowError
                .addEvent(LOAD, stateShowLoadingWithError);

        stateShowLoading
                .addEvent(NEW_DATA, stateShowData)
                .addEvent(NO_DATA, stateShowEmpty)
                .addEvent(ERROR, stateShowError);

        stateShowRefreshing
                .addEvent(NEW_DATA, stateShowData)
                .addEvent(NO_DATA, stateShowEmpty)
                .addEvent(ERROR, stateShowDataWithError)
                .addEvent(DATA_UPDATE, stateShowRefreshing);

        stateShowLoadingWithError
                .addEvent(NEW_DATA, stateShowData)
                .addEvent(NO_DATA, stateShowEmpty)
                .addEvent(ERROR, stateShowError);

        return new StateMachine<>(stateShowEmpty);
    }

    public void attach(ListUserRepoView view) {
        viewDisposable = stateMachine.observeState().doOnNext(it -> {
            if (it.id == SHOW_EMPTY) {
                loadRepo("pgloaguen", false);
            }
        }).subscribe(s -> view.notify(s.id, s.data == null ? new VM() : s.data));
    }

    public void detach() {
        viewDisposable.dispose();
    }

    public void askForRefresh() {
        loadRepo("pgloaguen", true);
    }

    public void onRepoClick(RepoEntity repoEntity){
        navigator.showRepoDetails("pgloaguen", repoEntity.name());
    }

    public void onFavoriteClick(RepoEntity repoEntity) {
        favoriteRepoUseCase.execute(repoEntity)
                .subscribe(repoFavoriteUpdated ->
                    stateMachine.nextState(DATA_UPDATE, true, it ->
                            new VM(
                            Observable.fromIterable(it.repoEntities)
                            .map(r -> r.id() == repoEntity.id() ? repoFavoriteUpdated : r)
                            .toList().blockingGet(), null)));
    }

    private void loadRepo(String username, boolean invalidate) {
        stateMachine.nextState(LOAD, it -> it);
        getUserRepoUseCase.execute(GetUserRepo.Param.create(username, invalidate))
                .map(repos -> new VM(repos, null))
                .onErrorReturn(t -> new VM(new ArrayList<>(), t))
                .subscribe(vm -> {
                    if (vm.error == null) {
                        stateMachine.nextState(NEW_DATA, __ -> vm);
                    } else {
                        stateMachine.nextState(ERROR, it -> new VM(it.repoEntities, vm.error));
                    }
                });
    }
}
