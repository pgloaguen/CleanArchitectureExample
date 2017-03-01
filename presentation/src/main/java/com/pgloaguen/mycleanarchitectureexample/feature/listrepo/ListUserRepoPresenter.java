package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.FavoriteRepo;
import com.pgloaguen.domain.usecase.GetUserRepoUseCase;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.base.state.StateMachine;
import com.pgloaguen.mycleanarchitectureexample.navigator.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

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
        LOAD, NEW_DATA, NO_DATA, ERROR
    }

    public static class ListUserRepoViewModel {
        @NonNull
        public List<RepoEntity> repoEntities;

        @Nullable
        public Throwable error;

        public ListUserRepoViewModel(@NonNull List<RepoEntity> repoEntities, @Nullable Throwable error) {
            this.repoEntities = repoEntities;
            this.error = error;
        }

        public ListUserRepoViewModel() {
            this(Collections.emptyList(), null);
        }
    }

    private final FavoriteRepo favoriteRepoUseCase;
    private final Navigator navigator;
    private final UseCase<List<RepoEntity>, GetUserRepoUseCase.Param> getUserRepoUseCase;
    private final StateMachine<StateValue, EventValue, ListUserRepoViewModel> stateMachine;


    @Inject
    public ListUserRepoPresenter(UseCase<List<RepoEntity>, GetUserRepoUseCase.Param> getUserRepoUseCase, FavoriteRepo favoriteRepoUseCase, Navigator navigator) {
        this.getUserRepoUseCase = getUserRepoUseCase;
        this.favoriteRepoUseCase = favoriteRepoUseCase;
        this.navigator = navigator;
        this.stateMachine = initStateMachine();
    }

    private StateMachine<StateValue, EventValue, ListUserRepoViewModel> initStateMachine() {
        StateMachine.State<StateValue, EventValue, ListUserRepoViewModel> stateShowEmpty = new StateMachine.State<>(SHOW_EMPTY);
        StateMachine.State<StateValue, EventValue, ListUserRepoViewModel> stateShowLoading = new StateMachine.State<>(SHOW_LOADING);
        StateMachine.State<StateValue, EventValue, ListUserRepoViewModel> stateShowRefreshing = new StateMachine.State<>(SHOW_REFRESHING);
        StateMachine.State<StateValue, EventValue, ListUserRepoViewModel> stateShowLoadingWithError = new StateMachine.State<>(SHOW_LOADING_WITH_ERROR);
        StateMachine.State<StateValue, EventValue, ListUserRepoViewModel> stateShowData = new StateMachine.State<>(SHOW_DATA);
        StateMachine.State<StateValue, EventValue, ListUserRepoViewModel> stateShowDataWithError = new StateMachine.State<>(SHOW_DATA_WITH_ERROR);
        StateMachine.State<StateValue, EventValue, ListUserRepoViewModel> stateShowError = new StateMachine.State<>(SHOW_ERROR);

        stateShowEmpty
                .addEvent(LOAD, stateShowLoading);

        stateShowData
                .addEvent(LOAD, stateShowRefreshing);

        stateShowDataWithError
                .addEvent(LOAD, stateShowLoadingWithError);

        stateShowError
                .addEvent(LOAD, stateShowLoadingWithError);

        stateShowLoading
                .addEvent(NEW_DATA, stateShowData)
                .addEvent(NO_DATA, stateShowEmpty)
                .addEvent(ERROR, stateShowError);

        stateShowRefreshing
                .addEvent(NEW_DATA, stateShowData)
                .addEvent(NO_DATA, stateShowEmpty)
                .addEvent(ERROR, stateShowDataWithError);

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
        }).subscribe(s -> view.notify(s.id, s.data == null ? new ListUserRepoViewModel() : s.data));
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

    }

    private void loadRepo(String username, boolean invalidate) {
        stateMachine.nextState(LOAD, it -> it);
        getUserRepoUseCase.execute(GetUserRepoUseCase.Param.create(username, invalidate))
                .map(repos -> new ListUserRepoViewModel(repos, null))
                .onErrorReturn(t -> new ListUserRepoViewModel(new ArrayList<>(), t))
                .subscribe(vm -> {
                    if (vm.error == null) {
                        stateMachine.nextState(NEW_DATA, __ -> vm);
                    } else {
                        stateMachine.nextState(ERROR, it -> new ListUserRepoViewModel(it.repoEntities, vm.error));
                    }
                });
    }
}
