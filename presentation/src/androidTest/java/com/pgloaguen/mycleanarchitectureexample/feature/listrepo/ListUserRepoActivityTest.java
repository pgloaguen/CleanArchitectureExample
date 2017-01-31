package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.base.BaseActivityTest;

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
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(emptyState()));
        listUserRepoRobot.isEmptyState();
    }

    @Test
    public void stateLoading() throws Throwable {
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(loadingState()));
        listUserRepoRobot.isLoadingState();
    }

    @Test
    public void stateDisplayData() throws Throwable {
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(displayDataState(Collections.singletonList(RepoEntity.create(0, "name", "desc")))));
        listUserRepoRobot.isDisplayDataState();
    }

    @Test
    public void stateRefreshing() throws Throwable {
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(refreshingState(Collections.singletonList(RepoEntity.create(0, "name", "desc")))));
        listUserRepoRobot.isRefreshingState();
    }

    @Test
    public void stateError() throws Throwable {
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(errorState("Error")));
        listUserRepoRobot.isErrorState();
    }

    @Test
    public void stateLoadingWithError() throws Throwable {
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(loadingWithErrorState("Error")));
        listUserRepoRobot.isLoadingWithErrorState();
    }

    @Test
    public void stateErrorWithData() throws Throwable {
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(errorWithDisplayDataState("Error", Collections.singletonList(RepoEntity.create(0, "name", "desc")))));
        listUserRepoRobot.isErrorStateWithData();
    }

    @Test
    public void possibleToSwipeWhenStateError() throws Throwable {
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(errorState("Error")));
        doAnswer(invocation -> {
            uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(displayDataState(Collections.singletonList(RepoEntity.create(0, "name", "desc")))));
            return null;
        }).when(presenter).askForRefresh();

        listUserRepoRobot.isErrorState();
        listUserRepoRobot.swipeRefresh();
        listUserRepoRobot.isDisplayDataState();
    }

    @Test
    public void possibleToSwipeWhenStateEmpty() throws Throwable {
        uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(emptyState()));
        doAnswer(invocation -> {
            uiThreadTestRule.runOnUiThread(() -> mActivityRule.getActivity().update(displayDataState(Collections.singletonList(RepoEntity.create(0, "name", "desc")))));
            return null;
        }).when(presenter).askForRefresh();

        listUserRepoRobot.isEmptyState();
        listUserRepoRobot.swipeRefresh();
        listUserRepoRobot.isDisplayDataState();
    }
}