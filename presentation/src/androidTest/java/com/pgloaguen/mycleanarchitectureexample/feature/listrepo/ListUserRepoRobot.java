package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.test.espresso.ViewInteraction;
import android.view.View;

import com.pgloaguen.mycleanarchitectureexample.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.pgloaguen.mycleanarchitectureexample.utils.ViewActionUtils.withCustomConstraints;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.not;

/**
 * Created by paul on 30/01/2017.
 */

public class ListUserRepoRobot {


    private ViewInteraction onErrorView() {
        return onView(withId(R.id.error));
    }

    private ViewInteraction onErrorScreen() {
        return onView(withId(R.id.error_screen));
    }

    private ViewInteraction onProgressView() {
        return onView(withId(R.id.progress));
    }

    private void recyclerHasData() {
        onRecyclerView().check(matches(hasDescendant(any(View.class))));
    }

    private ViewInteraction onRecyclerView() {
        return onView(withId(R.id.recycler));
    }

    private void progressIsDisplayed() {
        onProgressView().check(matches(isDisplayed()));
    }

    private void progressIsNotDisplayed() {
        onProgressView().check(matches(not(isDisplayed())));
    }


    private void recyclerHasNoDataDisplayed() {
        onRecyclerView().check(matches(not(hasDescendant(any(View.class)))));
    }

    private void errorScreenIsNotDisplayed() {
        onErrorScreen().check(matches(not(isDisplayed())));
        onErrorView().check(matches(not(isDisplayed())));
    }

    private void errorScreenIsDisplayed() {
        onErrorScreen().check(matches(isDisplayed()));
        onErrorView().check(matches(isDisplayed()));
    }

    public void swipeRefresh() {
        onView(withId(R.id.swipeRefreshLayout))
                .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));
    }

    public void isLoadingWithErrorState() {
        progressIsNotDisplayed();
        recyclerHasNoDataDisplayed();
        errorScreenIsDisplayed();
    }

    public void isErrorStateWithData() {
        progressIsNotDisplayed();
        recyclerHasData();
        errorScreenIsNotDisplayed();
    }

    public void isErrorState() {
        progressIsNotDisplayed();
        recyclerHasNoDataDisplayed();
        errorScreenIsDisplayed();
    }

    public void isRefreshingState() {
        progressIsNotDisplayed();
        recyclerHasData();
        errorScreenIsNotDisplayed();
    }

    public void isDisplayDataState() {
        progressIsNotDisplayed();
        recyclerHasData();
        errorScreenIsNotDisplayed();
    }

    public void isLoadingState() {
        progressIsDisplayed();
        recyclerHasNoDataDisplayed();
        errorScreenIsNotDisplayed();
    }

    public void isEmptyState() {
        progressIsNotDisplayed();
        recyclerHasNoDataDisplayed();
        errorScreenIsNotDisplayed();
    }
}
