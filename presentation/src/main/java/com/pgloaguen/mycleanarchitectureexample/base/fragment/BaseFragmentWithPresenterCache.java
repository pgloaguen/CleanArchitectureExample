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

    private P presenter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (getActivity().isFinishing() || !isRemoving()) {
            outState.putString(KEY_PRESENTER, getCache().storePresenter(presenter));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            presenter = getCache().getPresenter(savedInstanceState.getString(KEY_PRESENTER, ""));
        }

        if (presenter == null) {
            presenter = createPresenter();
        }

        init(presenter);
    }

    @NonNull
    protected abstract PresenterCache<P> getCache();

    @NonNull
    protected abstract P createPresenter();

    protected abstract void init(@NonNull P presenter);

}
