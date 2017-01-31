package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.BaseActivityTest;
import com.pgloaguen.mycleanarchitectureexample.base.fragment.BaseFragmentWithRemoteDataWithRefreshingState;
import com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import javax.inject.Inject;

import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.displayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.emptyState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.errorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.errorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.loadingState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.loadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.refreshingState;
import static org.mockito.Mockito.doAnswer;

/**
 * Created by paul on 30/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ListUserRepoActivityTest extends BaseActivityTest {

    @Rule
    public ActivityTestRule<ListUserRepoActivity> mActivityRule = new ActivityTestRule<>(ListUserRepoActivity.class, false, false);

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    @Inject
    ListUserRepoPresenter presenter;

    private ListUserRepoRobot listUserRepoRobot = new ListUserRepoRobot();

    @Before
    public void setUp() {
        init();
        activityComponentTest.inject(this);
        mActivityRule.launchActivity(null);
    }

    @Test
    public void stateEmpty() throws Throwable {
        givenUiStateIs(emptyState());
        listUserRepoRobot.isEmptyState();
    }

    @Test
    public void stateLoading() throws Throwable {
        givenUiStateIs(loadingState());
        listUserRepoRobot.isLoadingState();
    }

    @Test
    public void stateDisplayData() throws Throwable {
        givenUiStateIs(displayDataState(Collections.singletonList(RepoEntity.create(0, "name", "desc"))));
        listUserRepoRobot.isDisplayDataState();
    }

    @Test
    public void stateRefreshing() throws Throwable {
        givenUiStateIs(refreshingState(Collections.singletonList(RepoEntity.create(0, "name", "desc"))));
        listUserRepoRobot.isRefreshingState();
    }

    @Test
    public void stateError() throws Throwable {
        givenUiStateIs(errorState("Error"));
        listUserRepoRobot.isErrorState();
    }

    @Test
    public void stateLoadingWithError() throws Throwable {
        givenUiStateIs(loadingWithErrorState("Error"));
        listUserRepoRobot.isLoadingWithErrorState();
    }

    @Test
    public void stateErrorWithData() throws Throwable {
        givenUiStateIs(errorWithDisplayDataState("Error", Collections.singletonList(RepoEntity.create(0, "name", "desc"))));
        listUserRepoRobot.isErrorStateWithData();
    }

    @Test
    public void possibleToSwipeWhenStateError() throws Throwable {
        givenUiStateIs(errorState("Error"));
        doAnswer(invocation -> {
            givenUiStateIs(displayDataState(Collections.singletonList(RepoEntity.create(0, "name", "desc"))));
            return null;
        }).when(presenter).askForRefresh();

        listUserRepoRobot.isErrorState();
        listUserRepoRobot.swipeRefresh();
        listUserRepoRobot.isDisplayDataState();
    }

    @Test
    public void possibleToSwipeWhenStateEmpty() throws Throwable {
        givenUiStateIs(emptyState());
        doAnswer(invocation -> {
            givenUiStateIs(displayDataState(Collections.singletonList(RepoEntity.create(0, "name", "desc"))));
            return null;
        }).when(presenter).askForRefresh();

        listUserRepoRobot.isEmptyState();
        listUserRepoRobot.swipeRefresh();
        listUserRepoRobot.isDisplayDataState();
    }

    private void givenUiStateIs(RemoteDataWithRefreshingState state) throws Throwable {
        uiThreadTestRule.runOnUiThread(() ->
                ((BaseFragmentWithRemoteDataWithRefreshingState) mActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment)).update(state));
    }
}