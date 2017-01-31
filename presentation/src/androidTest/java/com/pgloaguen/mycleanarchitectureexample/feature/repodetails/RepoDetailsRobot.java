package com.pgloaguen.mycleanarchitectureexample.feature.repodetails;

import android.support.test.espresso.ViewInteraction;

import com.pgloaguen.domain.entity.RepoDetailsEntity;
import com.pgloaguen.mycleanarchitectureexample.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.pgloaguen.mycleanarchitectureexample.utils.MatchersUtils.withToolbarTitle;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by paul on 31/01/2017.
 */

public class RepoDetailsRobot {


    public void isLoadingWithErrorState(String erreur) {
        onTextView().check(matches(withText(R.string.loading)));
    }

    public void isErrorStateWithData(String erreur, RepoDetailsEntity repoDetailsEntity) {
        isToolbarDisplayTitle(repoDetailsEntity.name());
        onTextView().check(matches(withText(repoDetailsEntity.desc())));
    }

    public void isErrorState(String erreur) {
        onTextView().check(matches(withText(erreur)));
    }

    public void isRefreshingState(RepoDetailsEntity repoDetailsEntity) {
        onTextView().check(matches(withText(R.string.refreshing)));
        isToolbarDisplayTitle(repoDetailsEntity.name());
    }

    public void isDisplayDataState(RepoDetailsEntity repoDetailsEntity) {
        isToolbarDisplayTitle(repoDetailsEntity.name());
        onTextView().check(matches(withText(repoDetailsEntity.desc())));
    }

    public void isLoadingState() {
        onTextView().check(matches(withText(R.string.loading)));
    }

    public void isEmptyState() {
        onTextView().check(matches(withText("")));
    }

    private void isToolbarDisplayTitle(String title) {
        onToolbarView().check(matches(withToolbarTitle(is(title))));
    }

    private ViewInteraction onToolbarView() {
        return onView(withId(R.id.toolbar));
    }

    private ViewInteraction onTextView() {
        return onView(withId(R.id.desc));
    }



}
