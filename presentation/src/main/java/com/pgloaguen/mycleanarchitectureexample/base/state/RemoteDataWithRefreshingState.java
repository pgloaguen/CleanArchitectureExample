package com.pgloaguen.mycleanarchitectureexample.base.state;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by paul on 25/01/2017.
 */

public abstract class RemoteDataWithRefreshingState<D> {

    public static final int LOADING_STATE = 0;
    public static final int LOADING_WITH_ERROR_STATE = 1;
    public static final int REFRESHING_STATE = 2;
    public static final int EMPTY_STATE = 3;
    public static final int DISPLAY_DATA_STATE = 4;
    public static final int ERROR_STATE = 5;
    public static final int ERROR_WITH_DATA_STATE = 6;

    @IntDef({LOADING_STATE, LOADING_WITH_ERROR_STATE,REFRESHING_STATE,EMPTY_STATE,DISPLAY_DATA_STATE,ERROR_STATE,ERROR_WITH_DATA_STATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    @State
    public abstract int state();


    @AutoValue
    public abstract static class LoadingState<D> extends RemoteDataWithRefreshingState<D> {

        public static <D> TypeAdapter<LoadingState<D>> typeAdapter(Gson gson, TypeToken<? extends LoadingState<D>> typeToken) {
            return new AutoValue_RemoteDataWithRefreshingState_LoadingState.GsonTypeAdapter(gson, typeToken);
        }
    }


    @AutoValue
    public abstract static class LoadingWithErrorState<D> extends RemoteDataWithRefreshingState<D> {
        public abstract String error();


        public static <D>TypeAdapter<LoadingWithErrorState<D>> typeAdapter(Gson gson, TypeToken<? extends LoadingWithErrorState<D>> typeToken) {
            return new AutoValue_RemoteDataWithRefreshingState_LoadingWithErrorState.GsonTypeAdapter(gson, typeToken);
        }
    }

    @AutoValue
    public abstract static class RefreshingState<D> extends RemoteDataWithRefreshingState<D> {
        @NonNull
        public abstract D datas();


        public static <D>TypeAdapter<RefreshingState<D>> typeAdapter(Gson gson, TypeToken<? extends RefreshingState<D>> typeToken) {
            return new AutoValue_RemoteDataWithRefreshingState_RefreshingState.GsonTypeAdapter(gson, typeToken);
        }
    }

    @AutoValue
    public abstract static class EmptyState<D> extends RemoteDataWithRefreshingState<D> {

        public static <D> TypeAdapter<EmptyState<D>> typeAdapter(Gson gson, TypeToken<? extends EmptyState<D>> typeToken) {
            return new AutoValue_RemoteDataWithRefreshingState_EmptyState.GsonTypeAdapter(gson, typeToken);
        }
    }


    @AutoValue
    public abstract static class DisplayDataState<D> extends RemoteDataWithRefreshingState<D> {
        @NonNull
        public abstract D datas();


        public static <D> TypeAdapter<DisplayDataState<D>> typeAdapter(Gson gson, TypeToken<? extends DisplayDataState<D>> typeToken) {
            return new AutoValue_RemoteDataWithRefreshingState_DisplayDataState.GsonTypeAdapter(gson, typeToken);
        }
    }

    @AutoValue
    public abstract static class ErrorState<D> extends RemoteDataWithRefreshingState<D> {
        @NonNull
        public abstract String error();


        public static <D>TypeAdapter<ErrorState<D>> typeAdapter(Gson gson, TypeToken<? extends ErrorState<D>> typeToken) {
            return new AutoValue_RemoteDataWithRefreshingState_ErrorState.GsonTypeAdapter(gson, typeToken);
        }
    }

    @AutoValue
    public abstract static class ErrorWithDisplayDataState<D> extends RemoteDataWithRefreshingState<D> {

        public abstract String error();

        @NonNull
        public abstract D datas();


        public static <D>TypeAdapter<ErrorWithDisplayDataState<D>> typeAdapter(Gson gson, TypeToken<? extends ErrorWithDisplayDataState<D>> typeToken) {
            return new AutoValue_RemoteDataWithRefreshingState_ErrorWithDisplayDataState.GsonTypeAdapter(gson, typeToken);
        }
    }

    public static <D> RemoteDataWithRefreshingState<D> loadingState() {
        return new AutoValue_RemoteDataWithRefreshingState_LoadingState<>(LOADING_STATE);
    }

    public static <D> RemoteDataWithRefreshingState<D> refreshingState(@NonNull D data) {
        return new AutoValue_RemoteDataWithRefreshingState_RefreshingState<>(REFRESHING_STATE, data);
    }

    public static <D> RemoteDataWithRefreshingState<D> loadingWithErrorState(String message) {
        return new AutoValue_RemoteDataWithRefreshingState_LoadingWithErrorState<>(LOADING_WITH_ERROR_STATE, message);
    }

    public static <D> RemoteDataWithRefreshingState<D> displayDataState(@NonNull D data) {
        return new AutoValue_RemoteDataWithRefreshingState_DisplayDataState<>(DISPLAY_DATA_STATE, data);
    }

    public static <D> RemoteDataWithRefreshingState<D> emptyState() {
        return new AutoValue_RemoteDataWithRefreshingState_EmptyState<>(EMPTY_STATE);
    }

    public static <D> RemoteDataWithRefreshingState<D> errorState(@NonNull String messageError) {
        return new AutoValue_RemoteDataWithRefreshingState_ErrorState<>(ERROR_STATE, messageError);
    }

    public static <D> RemoteDataWithRefreshingState<D> errorWithDisplayDataState(String message, @NonNull D data) {
        return new AutoValue_RemoteDataWithRefreshingState_ErrorWithDisplayDataState<>(ERROR_WITH_DATA_STATE, message, data);
    }

}
