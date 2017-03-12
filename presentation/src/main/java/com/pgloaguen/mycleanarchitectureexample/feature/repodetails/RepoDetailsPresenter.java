package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.domain.usecase.FavoriteRepoDetails;
import com.pgloaguen.domain.usecase.GetUserRepoDetails;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.base.state.StateMachine;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

import static com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter.EventValue.ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter.EventValue.LOAD;
import static com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter.EventValue.NEW_DATA;
import static com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter.EventValue.UPDATE_DATA;
import static com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter.StateValue.DISPLAY;
import static com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter.StateValue.DISPLAY_ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter.StateValue.EMPTY;
import static com.pgloaguen.mycleanarchitectureexample.feature.repodetails.RepoDetailsPresenter.StateValue.LOADING;

/**
 * Created by paul on 26/01/2017.
 */

public class RepoDetailsPresenter {

    private Disposable viewDisposable = Disposables.empty();

    public enum StateValue {
        EMPTY, LOADING, DISPLAY, DISPLAY_ERROR
    }

    public enum EventValue {
        LOAD, NEW_DATA, UPDATE_DATA, ERROR
    }

    public static class VM {
        public RepoDetailsEntity data;
        public Throwable error;

        public VM(RepoDetailsEntity data, Throwable error) {
            this.data = data;
            this.error = error;
        }
    }

    private final UseCase<RepoDetailsEntity, GetUserRepoDetails.Param> usecase;
    private final FavoriteRepoDetails favoriteUseCase;
    private final StateMachine<StateValue, EventValue, VM> stateMachine;

    private String repoName;
    private String username;

    @Inject
    public RepoDetailsPresenter(UseCase<RepoDetailsEntity, GetUserRepoDetails.Param> usecase, FavoriteRepoDetails favoriteUseCase) {
        this.usecase = usecase;
        this.favoriteUseCase = favoriteUseCase;

        StateMachine.State<StateValue, EventValue, VM> emptyState = new StateMachine.State<>(EMPTY);
        StateMachine.State<StateValue, EventValue, VM> loadingState = new StateMachine.State<>(LOADING);
        StateMachine.State<StateValue, EventValue, VM> displayState = new StateMachine.State<>(DISPLAY);
        StateMachine.State<StateValue, EventValue, VM> errorState = new StateMachine.State<>(DISPLAY_ERROR);

        emptyState.addEvent(LOAD, loadingState);

        loadingState
                .addEvent(UPDATE_DATA, loadingState)
                .addEvent(NEW_DATA, displayState)
                .addEvent(ERROR, errorState);

        displayState
                .addEvent(UPDATE_DATA, displayState);

        errorState
                .addEvent(LOAD, loadingState);

        stateMachine = new StateMachine<>(emptyState);
    }

    public void attach(RepoDetailsView view, String username, String repoName) {
        this.username = username;
        this.repoName = repoName;
        viewDisposable =
                stateMachine.observeState()
                        .doOnNext(s -> {
                            if (s.id == EMPTY) {
                                load(true);
                            }
                        }).subscribe(it -> view.notify(it.id, it.data));
    }

    public void retry() {
        load(true);
    }

    public void toggleFavorite(RepoDetailsEntity repoDetails) {
        favoriteUseCase
                .execute(repoDetails)
                .subscribe(r -> stateMachine.nextState(UPDATE_DATA, true, __ -> new VM(r, null)));
    }

    public void detach() {
        viewDisposable.dispose();
    }

    private void load(boolean invalidate) {
        stateMachine.nextState(LOAD, it -> it);
        usecase.execute(GetUserRepoDetails.Param.create(username, repoName, invalidate))
                .subscribe(
                        details -> stateMachine.nextState(NEW_DATA, __ -> new VM(details, null)),
                        error -> stateMachine.nextState(ERROR, it -> new VM(it == null ? null : it.data, error)));
    }
}
