package com.pgloaguen.mycleanarchitectureexample.base.fragment;

import android.support.v4.app.Fragment;

import com.pgloaguen.mycleanarchitectureexample.base.activity.BaseActivity;
import com.pgloaguen.mycleanarchitectureexample.di.ActivityComponent;

/**
 * Created by paul on 26/01/2017.
 */

public abstract class BaseFragment extends Fragment {

    protected ActivityComponent activityComponent() {
        return ((BaseActivity) getActivity()).activityComponent();
    }
}
