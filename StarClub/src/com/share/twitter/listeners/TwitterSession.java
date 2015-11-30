package com.share.twitter.listeners;


import twitter4j.auth.AccessToken;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
public class TwitterSession {
	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String TWEET_USER_NAME = "user_name";
	private static final String SHARED = "Twitter_Preferences";

	public TwitterSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

		editor = sharedPref.edit();
	}

	public void storeAccessToken(AccessToken accessToken, String username) {
		editor.putString(TwitterApp.TWEET_AUTH_KEY, accessToken.getToken());
		editor.putString(TwitterApp.TWEET_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(TWEET_USER_NAME, username);

		editor.commit();
	}

	public void resetAccessToken() {
		editor.putString(TwitterApp.TWEET_AUTH_KEY, null);
		editor.putString(TwitterApp.TWEET_AUTH_SECRET_KEY, null);
		editor.putString(TWEET_USER_NAME, null);

		editor.commit();
	}

	public String getUsername() {
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

	public AccessToken getAccessToken() {
		String token = sharedPref.getString(TwitterApp.TWEET_AUTH_KEY, null);
		String tokenSecret = sharedPref.getString(TwitterApp.TWEET_AUTH_SECRET_KEY, null);

		if (token != null && tokenSecret != null)
			return new AccessToken(token, tokenSecret);
		else
			return null;
	}
}