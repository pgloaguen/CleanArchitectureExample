package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.mycleanarchitectureexample.base.BaseActivityTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_DATA;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_DATA_WITH_ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_EMPTY;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_LOADING;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_LOADING_WITH_ERROR;
import static com.pgloaguen.mycleanarchitectureexample.feature.listrepo.ListUserRepoPresenter.StateValue.SHOW_REFRESHING;
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

    private List<RepoEntity> defaultData = Collections.singletonList(RepoEntity.create(0, "name", "desc", false));

    @Before
    public void setUp() {
        init();
        activityComponentTest.inject(this);
        mActivityRule.launchActivity(null);
    }

    @Test
    public void stateEmpty() throws Throwable {
        givenUiStateIs(SHOW_EMPTY, new ListUserRepoPresenter.ListUserRepoViewModel(Collections.emptyList(), null));
        listUserRepoRobot.isEmptyState();
    }

    @Test
    public void stateLoading() throws Throwable {
        givenUiStateIs(SHOW_LOADING, new ListUserRepoPresenter.ListUserRepoViewModel(Collections.emptyList(), null));
        listUserRepoRobot.isLoadingState();
    }

    @Test
    public void stateDisplayData() throws Throwable {
        givenUiStateIs(SHOW_DATA, new ListUserRepoPresenter.ListUserRepoViewModel(defaultData, null));
        listUserRepoRobot.isDisplayDataState();
    }

    @Test
    public void stateRefreshing() throws Throwable {
        givenUiStateIs(SHOW_REFRESHING, new ListUserRepoPresenter.ListUserRepoViewModel(defaultData, null));
        listUserRepoRobot.isRefreshingState();
    }

    @Test
    public void stateError() throws Throwable {
        givenUiStateIs(SHOW_ERROR, new ListUserRepoPresenter.ListUserRepoViewModel(Collections.emptyList(),  new NullPointerException("error")));
        listUserRepoRobot.isErrorState();
    }

    @Test
    public void stateLoadingWithError() throws Throwable {
        givenUiStateIs(SHOW_LOADING_WITH_ERROR, new ListUserRepoPresenter.ListUserRepoViewModel(defaultData,  new NullPointerException("error")));
        listUserRepoRobot.isLoadingWithErrorState();
    }



    @Test
    public void stateErrorWithData() throws Throwable {
        givenUiStateIs(SHOW_DATA_WITH_ERROR, new ListUserRepoPresenter.ListUserRepoViewModel(defaultData, new NullPointerException("error")));
        listUserRepoRobot.isErrorStateWithData();
    }

    @Test
    public void possibleToSwipeWhenStateError() throws Throwable {
        givenUiStateIs(SHOW_ERROR, new ListUserRepoPresenter.ListUserRepoViewModel(new ArrayList<RepoEntity>(), new NullPointerException("error")));
        doAnswer(invocation -> {
            givenUiStateIs(SHOW_DATA, new ListUserRepoPresenter.ListUserRepoViewModel(defaultData, null));
            return null;
        }).when(presenter).askForRefresh();

        listUserRepoRobot.isErrorState();
        listUserRepoRobot.swipeRefresh();
        listUserRepoRobot.isDisplayDataState();
    }

    @Test
    public void possibleToSwipeWhenStateEmpty() throws Throwable {
        givenUiStateIs(SHOW_EMPTY, new ListUserRepoPresenter.ListUserRepoViewModel());
        doAnswer(invocation -> {
            givenUiStateIs(SHOW_DATA, new ListUserRepoPresenter.ListUserRepoViewModel(defaultData, null));
            return null;
        }).when(presenter).askForRefresh();

        listUserRepoRobot.isEmptyState();
        listUserRepoRobot.swipeRefresh();
        listUserRepoRobot.isDisplayDataState();
    }

    private void givenUiStateIs(ListUserRepoPresenter.StateValue state, ListUserRepoPresenter.ListUserRepoViewModel data) throws Throwable {
        uiThreadTestRule.runOnUiThread(() ->
                ((ListUserRepoFragment) mActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment)).notify(state, data));
    }
}