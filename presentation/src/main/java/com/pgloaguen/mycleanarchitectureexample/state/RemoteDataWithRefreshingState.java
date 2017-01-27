package com.pgloaguen.mycleanarchitectureexample.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * Created by paul on 25/01/2017.
 */

public abstract class RemoteDataWithRefreshingState<D> {

    @AutoValue
    public abstract static class LoadingState<D> extends RemoteDataWithRefreshingState<D> {
    }


    @AutoValue
    public abstract static class LoadingWithErrorState<D> extends RemoteDataWithRefreshingState<D> {
        public abstract String error();
    }

    @AutoValue
    public abstract static class RefreshingState<D> extends RemoteDataWithRefreshingState<D> {
        @Nullable
        public abstract D datas();
    }

    @AutoValue
    public abstract static class EmptyState<D> extends RemoteDataWithRefreshingState<D> {
    }


    @AutoValue
    public abstract static class DisplayDataState<D> extends RemoteDataWithRefreshingState<D> {
        @Nullable
        public abstract D datas();
    }

    @AutoValue
    public abstract static class ErrorState<D> extends RemoteDataWithRefreshingState<D> {
        @NonNull
        public abstract String error();
    }

    @AutoValue
    public abstract static class ErrorWithDisplayDataState<D> extends RemoteDataWithRefreshingState<D> {

        public abstract String error();

        @Nullable
        public abstract D datas();
    }

    public static <D> RemoteDataWithRefreshingState<D> loadingState() {
        return new AutoValue_RemoteDataWithRefreshingState_LoadingState<D>();
    }

    public static <D> RemoteDataWithRefreshingState<D> refreshingState(D data) {
        return new AutoValue_RemoteDataWithRefreshingState_RefreshingState<D>(data);
    }

    public static <D> RemoteDataWithRefreshingState<D> loadingWithErrorState(String message) {
        return new AutoValue_RemoteDataWithRefreshingState_LoadingWithErrorState<D>(message);
    }

    public static <D> RemoteDataWithRefreshingState<D> displayDataState(D data) {
        return new AutoValue_RemoteDataWithRefreshingState_DisplayDataState<D>(data);
    }

    public static <D> RemoteDataWithRefreshingState<D> emptyState(D data) {
        return new AutoValue_RemoteDataWithRefreshingState_EmptyState<D>();
    }

    public static <D> RemoteDataWithRefreshingState<D> errorState(@NonNull String messageError) {
        return new AutoValue_RemoteDataWithRefreshingState_ErrorState<D>(messageError);
    }

    public static <D> RemoteDataWithRefreshingState<D> errorWithDisplayDataState(String message, D data) {
        return new AutoValue_RemoteDataWithRefreshingState_ErrorWithDisplayDataState<D>(message, data);
    }

}
