package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.BaseActivityTest;
import com.pgloaguen.mycleanarchitectureexample.base.fragment.BaseFragmentWithRemoteDataWithRefreshingState;
import com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.displayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.emptyState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.errorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.errorWithDisplayDataState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.loadingState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.loadingWithErrorState;
import static com.pgloaguen.mycleanarchitectureexample.base.state.RemoteDataWithRefreshingState.refreshingState;

/**
 * Created by paul on 31/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RepoDetailsActivityTest extends BaseActivityTest {

    @Rule
    public ActivityTestRule<RepoDetailsActivity> mActivityRule = new ActivityTestRule<>(RepoDetailsActivity.class, false, false);

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    @Inject
    public RepoDetailsPresenter presenter;

    private RepoDetailsRobot robot = new RepoDetailsRobot();


    @Before
    public void setup() {
        init();
        activityComponentTest.inject(this);
        mActivityRule.launchActivity(null);
    }

    @Test
    public void stateEmpty() throws Throwable {
        givenUiStateIs(emptyState());
        robot.isEmptyState();
    }

    @Test
    public void stateLoading() throws Throwable {
        givenUiStateIs(loadingState());
        robot.isLoadingState();
    }

    @Test
    public void stateError() throws Throwable {
        givenUiStateIs(errorState("error"));
        robot.isErrorState("error");
    }

    @Test
    public void stateData() throws Throwable {
        RepoDetailsEntity details = RepoDetailsEntity.create(0, "name", "desc");
        givenUiStateIs(displayDataState(details));
        robot.isDisplayDataState(details);
    }

    @Test
    public void stateRefreshing() throws Throwable {
        RepoDetailsEntity details = RepoDetailsEntity.create(0, "name", "desc");
        givenUiStateIs(refreshingState(details));
        robot.isRefreshingState(details);
    }

    @Test
    public void stateErrorWithData() throws Throwable {
        RepoDetailsEntity details = RepoDetailsEntity.create(0, "name", "desc");
        givenUiStateIs(errorWithDisplayDataState("error", details));
        robot.isErrorStateWithData("error", details);
    }

    @Test
    public void stateLoadingWithError() throws Throwable {
        givenUiStateIs(loadingWithErrorState("error"));
        robot.isLoadingWithErrorState("error");
    }

    private void givenUiStateIs(RemoteDataWithRefreshingState state) throws Throwable {
        uiThreadTestRule.runOnUiThread(() ->
                ((BaseFragmentWithRemoteDataWithRefreshingState) mActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container)).update(state));
    }
}
