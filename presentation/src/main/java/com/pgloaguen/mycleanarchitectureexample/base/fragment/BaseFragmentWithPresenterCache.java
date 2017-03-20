package com.pgloaguen.mycleanarchitectureexample.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.pgloaguen.mycleanarchitectureexample.base.presenter.PresenterCache;

/**
 * Created by paul on 27/01/2017.
 */

public abstract class BaseFragmentWithPresenterCache<P> extends BaseFragment {

    private static final String KEY_PRESENTER = "key_presenter";

    private P p;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (getActivity().isFinishing() || !isRemoving()) {
            outState.putString(KEY_PRESENTER, getCache().storePresenter(p));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            p = getCache().getPresenter(savedInstanceState.getString(KEY_PRESENTER, ""));
        }

        if (p == null) {
            p = createPresenter();
        }

        init(p);
    }

    @NonNull
    protected abstract PresenterCache<P> getCache();

    @NonNull
    protected abstract P createPresenter();

    protected abstract void init(@NonNull P presenter);

}
