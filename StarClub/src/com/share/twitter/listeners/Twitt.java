package com.share.twitter.listeners;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.share.twitter.listeners.TwitterApp.TwDialogListener;
import com.starclub.enrique.HomeActivity;



public class Twitt {

	private TwitterApp mTwitter;
	private Activity activity;
	private String twitt_msg[];

	public Twitt(Activity act, String consumer_key, String consumer_secret) {
		this.activity = act;
		mTwitter = new TwitterApp(activity, consumer_key, consumer_secret);
	}

	public void shareToTwitter(String msg[]) {
		this.twitt_msg = msg;
		mTwitter.setListener(mTwLoginDialogListener);

		if (mTwitter.hasAccessToken()) {
			showTwittDialog();
		} else {
			mTwitter.authorize();
		}
	}

	private void showTwittDialog() {

				if (twitt_msg.length == 0) {
					showToast("Twitt is empty!!!");
					return;
				} else if (twitt_msg[0].length() > 140) {
					showToast("Twitt is more than 140 characters not allowed!!!");
					try {
						((HomeActivity)activity).twitterPostDone("Twitter Posting Fail");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				
				new PostTwittTask().execute(twitt_msg);

	}

	

	private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onError(String value) {
			showToast("Login Failed");
			mTwitter.resetAccessToken();
			
			try {
				((HomeActivity)activity).twitterPostDone("Twitter Login Fail");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void onComplete(String value) {
			showTwittDialog();
		}
	};

	void showToast(final String msg) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
			}
		});

	}

	class PostTwittTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(activity);
			pDialog.setMessage("Posting Twitt...");
			pDialog.setCancelable(false);
			pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... twitt) {
			try {
				mTwitter.updateStatus(twitt);
				return "success";

			} catch (Exception e) {
				if (e.getMessage().toString().contains("duplicate")) {
					return "Posting Failed because of Duplicate message...";
				}
				e.printStackTrace();
				Log.e("POSTFAILED", e.getMessage());
				return "Posting Failed";
			}

		}

		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();

			if (null != result && result.equals("success")) {
//				showToast("Syndicated to Twitter");
				try {
					((HomeActivity)activity).twitterPostDone("Syndicated to Twitter");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
//				showToast(result);
				try {
					((HomeActivity)activity).twitterPostDone(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			super.onPostExecute(result);
		}
	}
}
