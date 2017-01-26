package com.pgloaguen.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;

@Generated("com.robohorse.robopojogenerator")
@AutoValue
public abstract class Owner{

	@SerializedName("gists_url")
	public abstract String gistsUrl();

	@SerializedName("repos_url")
	public abstract String reposUrl();

	@SerializedName("following_url")
	public abstract String followingUrl();

	@SerializedName("starred_url")
	public abstract String starredUrl();

	@SerializedName("login")
	public abstract String login();

	@SerializedName("followers_url")
	public abstract String followersUrl();

	@SerializedName("type")
	public abstract String type();

	@SerializedName("url")
	public abstract String url();

	@SerializedName("subscriptions_url")
	public abstract String subscriptionsUrl();

	@SerializedName("received_events_url")
	public abstract String receivedEventsUrl();

	@SerializedName("avatar_url")
	public abstract String avatarUrl();

	@SerializedName("events_url")
	public abstract String eventsUrl();

	@SerializedName("html_url")
	public abstract String htmlUrl();

	@SerializedName("site_admin")
	public abstract boolean siteAdmin();

	@SerializedName("id")
	public abstract int id();

	@SerializedName("gravatar_id")
	public abstract String gravatarId();

	@SerializedName("organizations_url")
	public abstract String organizationsUrl();

	public static TypeAdapter<Owner> typeAdapter(Gson gson) {
		return new AutoValue_Owner.GsonTypeAdapter(gson);
	}
}