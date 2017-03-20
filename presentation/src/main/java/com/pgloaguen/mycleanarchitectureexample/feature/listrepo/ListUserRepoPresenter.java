package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.domain.usecase.FavoriteRepo;
import com.pgloaguen.domain.usecase.GetUserRepo;
import com.pgloaguen.domain.usecase.base.UseCase;
import com.pgloaguen.mycleanarchitectureexample.navigator.Navigator;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

import static io.reactivex.Observable.fromIterable;

/**
 * Created by paul on 01/03/2017.
 */

public class ListUserRepoPresenter {

    private CompositeDisposable viewDisposable = new CompositeDisposable();

    private final FavoriteRepo favoriteRepoUseCase;
    private final Navigator navigator;
    private final UseCase<List<RepoEntity>, GetUserRepo.Param> getUserRepoUseCase;
    private final BehaviorSubject<ListUserRepoVM>  process;
    private final Observable<ListUserRepoVM> firstFetchObservable;

    @Inject
    public ListUserRepoPresenter(UseCase<List<RepoEntity>, GetUserRepo.Param> getUserRepoUseCase, FavoriteRepo favoriteRepoUseCase, Navigator navigator) {
        this.getUserRepoUseCase = getUserRepoUseCase;
        this.favoriteRepoUseCase = favoriteRepoUseCase;
        this.navigator = navigator;
        this.process = BehaviorSubject.create();
        this.firstFetchObservable = loadRepo(ListUserRepoVM.empty(), "pgloaguen", false).takeUntil(vm -> vm.loadState == ListUserRepoVM.LoadState.NONE);
    }

    public void attach(ListUserRepoView view) {

        firstFetchObservable.subscribe(process);

        viewDisposable.addAll(
                view.refresh().switchMap(__ -> process.take(1).doOnNext(vm -> loadRepo(vm, "pgloaguen", true).subscribe(process))).subscribe(),
                view.favorite().doOnNext(it -> favoriteRepoUseCase.execute(it).zipWith(process.take(1), this::createNewVMFrom).subscribe(process)).subscribe(),
                view.itemClick().subscribe(r -> navigator.showRepoDetails("pgloaguen", r.name())));

        process.subscribe(view::notify);
    }

    public void detach() {
        viewDisposable.clear();
    }

    public void destroy() {
        process.onComplete();
    }

    private ListUserRepoVM createNewVMFrom(RepoEntity rUpdated, ListUserRepoVM vm) {
        return replaceRepoEntityInList(rUpdated, vm.repoEntities).map(newRepos -> ListUserRepoVM.data(newRepos, vm.loadState)).blockingGet();
    }

    private Single<List<RepoEntity>> replaceRepoEntityInList(RepoEntity rUpdated, List<RepoEntity> repoEntities) {
        return fromIterable(repoEntities)
                .map(r -> r.id() == rUpdated.id() ? rUpdated : r)
                .toList();
    }

    private Observable<ListUserRepoVM> loadRepo(ListUserRepoVM last, String username, boolean invalidate) {
        return getUserRepoUseCase
                .execute(GetUserRepo.Param.create(username, invalidate))
                .map(ListUserRepoVM::data)
                .onErrorReturn(t -> ListUserRepoVM.error(last.repoEntities, t))
                .startWith(invalidate ? ListUserRepoVM.refreshing(last.repoEntities) : ListUserRepoVM.loading());
    }
}
