package com.pgloaguen.data.model;


import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Owner {

    @SerializedName("login")
    public abstract String login();

    @SerializedName("id")
    public abstract long id();

    @SerializedName("avatar_url")
    public abstract String avatarUrl();

    @SerializedName("gravatar_id")
    public abstract String gravatarId();

    @SerializedName("url")
    public abstract String url();

    @SerializedName("html_url")
    public abstract String htmlUrl();

    @SerializedName("followers_url")
    public abstract String followersUrl();

    @SerializedName("following_url")
    public abstract String followingUrl();

    @SerializedName("gists_url")
    public abstract String gistsUrl();

    @SerializedName("starred_url")
    public abstract String starredUrl();

    @SerializedName("subscriptions_url")
    public abstract String subscriptionsUrl();

    @SerializedName("organizations_url")
    public abstract String organizationsUrl();

    @SerializedName("repos_url")
    public abstract String reposUrl();

    @SerializedName("events_url")
    public abstract String eventsUrl();

    @SerializedName("received_events_url")
    public abstract String receivedEventsUrl();

    @SerializedName("type")
    public abstract String type();

    @SerializedName("site_admin")
    public abstract boolean siteAdmin();


    public static TypeAdapter<Owner> typeAdapter(Gson gson) {
        return new AutoValue_Owner.GsonTypeAdapter(gson);
    }
}

