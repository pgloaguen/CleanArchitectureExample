package com.pgloaguen.mycleanarchitectureexample.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * Created by paul on 25/01/2017.
 */

public abstract class RemoteDataWithRefreshingState<D> {

    @AutoValue
    public abstract static class ErrorState<D> extends RemoteDataWithRefreshingState<D> {
        @NonNull
        public abstract String message();
    }

    @AutoValue
    public abstract static class DisplayDataState<D> extends RemoteDataWithRefreshingState<D> {
        @Nullable
        public abstract D datas();
    }

    @AutoValue
    public abstract static class LoadingState<D> extends RemoteDataWithRefreshingState<D>{}

    @AutoValue
    public abstract static class RefreshingState<D> extends RemoteDataWithRefreshingState<D>{
        @Nullable
        public abstract D datas();
    }


    public static <D>RemoteDataWithRefreshingState<D> errorState(@NonNull String messageError) {
        return new AutoValue_RemoteDataWithRefreshingState_ErrorState(messageError);
    }

    public static <D>RemoteDataWithRefreshingState<D> loadingState() {
        return new AutoValue_RemoteDataWithRefreshingState_LoadingState();
    }

    public static <D>RemoteDataWithRefreshingState<D> refreshingState(D data) {
        return new AutoValue_RemoteDataWithRefreshingState_RefreshingState(data);
    }

    public static <D>RemoteDataWithRefreshingState<D> displayDataState( D data) {
        return new AutoValue_RemoteDataWithRefreshingState_DisplayDataState(data);
    }

}
