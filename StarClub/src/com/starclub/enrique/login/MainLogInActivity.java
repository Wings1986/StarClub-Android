package com.starclub.enrique.login;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.facebook.AppEventsLogger;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gcm.GCMRegistrar;
import com.mycom.data.Global;
import com.mycom.data.MyConstants;
import com.mycom.data.Global.FB;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.Utils;
import com.starclub.enrique.GCMIntentService;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

public class MainLogInActivity extends BaseActivity {
	/** Called when the activity is first created. */

	public static MainLogInActivity g_mainLoginAcitivity;

	public Bitmap m_bmpAvatar = null;
	// public Profile m_user = null;
	public JSONObject m_user = null;
	ToggleButton btnAgree = null;

	/*
	 * Facebook posting
	 */
	// static final String PENDING_REQUEST_BUNDLE_KEY =
	// "com.facebook.samples.graphapi:PendingRequest";
	//
	// Session session;
	// boolean pendingRequest;

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (progress != null && progress.isShowing()) {
			progress.cancel();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		AppEventsLogger.activateApp(this, MyConstants.FACEBOOK_ID);

		RelativeLayout videoLayout = (RelativeLayout) findViewById(R.id.videoLayout);
		ImageView ivLogo = (ImageView) findViewById(R.id.ivLogo);
		
		
		String videoName = MyConstants.LOGINVIDEO_NAME;
		
		if (videoName.equals("")) {
			videoLayout.setVisibility(View.GONE);
			ivLogo.setVisibility(View.VISIBLE);
			ivLogo.setBackgroundResource(MyConstants.RES_LOGIN_LOGO);
		}
		else {
			
			videoLayout.setVisibility(View.VISIBLE);
			ivLogo.setVisibility(View.GONE);

			
			VideoView mVideoView = (VideoView) findViewById(R.id.firstVideo);

			
			int id = getResources().getIdentifier(videoName, "raw",
					getPackageName());
			String path = "android.resource://" + getPackageName() + "/" + id;

			Uri video = Uri.parse(path);
			mVideoView.setVideoURI(video);
			mVideoView.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.setLooping(true);
				}
			});
			mVideoView.requestFocus();
			mVideoView.start();
		}

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		Global.FB.pendingRequest = savedInstanceState.getBoolean(
				Global.FB.PENDING_REQUEST_BUNDLE_KEY, Global.FB.pendingRequest);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(Global.FB.PENDING_REQUEST_BUNDLE_KEY,
				Global.FB.pendingRequest);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UserDefault.init(this);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.activity_login);

		StarTracker.StarSendView(this, "Login");

		g_mainLoginAcitivity = this;

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
		} else {
			UserDefault.setStringForKey(regId, "deviceToken");
			System.out.println("GCM : Already registered");
		}

		Button btnFB = (Button) findViewById(R.id.btnLoginFacebook);
		btnFB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (btnAgree.isChecked()) {
					// if (!FBMainActivity.isLogIn) {
					// Intent intent = new Intent(MainLogInActivity.this,
					// FBMainActivity.class);
					// startActivityForResult(intent, 1000);
					// }
					// else {
					//
					// getProfileFb();
					// }

					onClickFBBtn();

				} else {
					Const.showMessage(
							"NOTE!",
							"To use this App you must agree to the terms of and conditions.",
							MainLogInActivity.this);
					return;
				}
			}
		});

		Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnAgree.isChecked()) {
					Intent intent = new Intent(MainLogInActivity.this,
							MainSignUpActivity.class);
					startActivity(intent);

				} else {
					Const.showMessage(
							"NOTE!",
							"To use this App you must agree to the terms of and conditions.",
							MainLogInActivity.this);
					return;
				}

			}
		});

		Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnAgree.isChecked()) {
					Intent intent = new Intent(MainLogInActivity.this,
							MainSignInActivity.class);
					startActivity(intent);
				} else {
					Const.showMessage(
							"NOTE!",
							"To use this App you must agree to the terms of and conditions.",
							MainLogInActivity.this);
					return;
				}

			}
		});

		btnAgree = (ToggleButton) findViewById(R.id.btnAgree);
		btnAgree.setChecked(true);

		TextView tvPrivacy = (TextView) findViewById(R.id.tvPrivacy);
		tvPrivacy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				StarTracker.StarSendView(g_mainLoginAcitivity, "Privacy");

				Intent intent = new Intent(MainLogInActivity.this,
						MainTermsActivity.class);
				intent.putExtra("title", "Privacy Policy");
				startActivity(intent);

			}
		});

		TextView tvTerms = (TextView) findViewById(R.id.tvTerms);
		tvTerms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				StarTracker.StarSendView(g_mainLoginAcitivity,
						"Terms of Service");

				Intent intent = new Intent(MainLogInActivity.this,
						MainTermsActivity.class);
				intent.putExtra("title", "Terms of Service");
				startActivity(intent);

			}
		});

		/* setup facebook */
		Global.FB.session = createSession();
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

	}

	ProgressDialog progress = null;

	public void showDialog() {
		progress = ProgressDialog.show(this, "processing request...",
				"Waiting for Facebook", true);
	}

	public void hideDialog() {
		// progress.hide();
		progress.dismiss();
	}

	public void getUserImage() {

		String userId = "";
		try {
			userId = m_user.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String profilePicURL = String.format(
				"http://graph.facebook.com/%s/picture?type=small", userId);

		AQuery androidAQuery = new AQuery(this);
		androidAQuery.ajax(profilePicURL, Bitmap.class, 0,
				new AjaxCallback<Bitmap>() {
					@Override
					public void callback(String url, Bitmap object,
							com.androidquery.callback.AjaxStatus status) {
						super.callback(url, object, status);
						m_bmpAvatar = object;
					}
				});

		progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("Login...");
		progress.show();

		gotoSignUp();
	}

	public void gotoSignUp() {
		new Thread() {
			public void run() {

				String userName = "";
				try {
					userName = m_user.getString("name");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String firstName = "";
				try {
					firstName = m_user.getString("first_name");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String lastName = "";
				try {
					lastName = m_user.getString("last_name");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String email = "";
				try {
					email = m_user.getString("email");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String birthday = null;
				try {
					birthday = m_user.getString("birthday");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if (birthday != null && birthday.length() > 0) {
						String s[] = birthday.split("/");
						birthday = s[2] + "-" + s[0] + "-" + s[1];
					}
				} catch (Exception e) {

				}

				String gender = "";
				try {
					gender = "" + m_user.getString("gender");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String city = "";// location.getCity();
				String state = ""; // location.getStreet();
				String country = ""; // location.getCountry();

				try {
					String strLocation = m_user.getJSONObject("location")
							.getString("name");

					String arry[] = strLocation.split(",");
					if (arry != null) {
						try {
							city = arry[0];
							state = arry[1];
							country = arry[2];
						} catch (Exception e) {

						}
					}
				} catch (Exception e) {
				}

				HttpParams myParams = new BasicHttpParams();

				HttpConnectionParams.setConnectionTimeout(myParams, 30000);
				HttpConnectionParams.setSoTimeout(myParams, 30000);

				DefaultHttpClient hc = new DefaultHttpClient(myParams);
				ResponseHandler<String> res = new BasicResponseHandler();

				String url = Utils.getSignUpUrl();
				HttpPost postMethod = new HttpPost(url);

				String responseBody = "";
				try {
					MultipartEntity reqEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					reqEntity.addPart("facebook", new StringBody("1"));
					reqEntity.addPart("cid", new StringBody("" + MyConstants.CID));
					reqEntity.addPart("ud_token",
							new StringBody(Utils.getDeviceToken()));
					reqEntity.addPart("ud_type", new StringBody("ANDROID"));

					if (userName != null && userName.length() > 0)
						reqEntity.addPart("username", new StringBody(userName));

					if (firstName != null && firstName.length() > 0)
						reqEntity.addPart("firstname",
								new StringBody(firstName));

					if (lastName != null && lastName.length() > 0)
						reqEntity.addPart("lastname", new StringBody(lastName));

					if (email != null && email.length() > 0)
						reqEntity.addPart("email", new StringBody(email));

					if (birthday != null && birthday.length() > 0)
						reqEntity.addPart("birthday", new StringBody(birthday));

					reqEntity.addPart("gender", new StringBody(gender));

					if (city != null && city.length() > 0)
						reqEntity.addPart("city", new StringBody(city));
					if (state != null && state.length() > 0)
						reqEntity.addPart("state", new StringBody(state));
					if (country != null && country.length() > 0)
						reqEntity.addPart("country", new StringBody(country));

					if (m_bmpAvatar != null) {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						m_bmpAvatar.compress(CompressFormat.JPEG, 75, bos);
						byte[] data = bos.toByteArray();

						ByteArrayBody bab = new ByteArrayBody(data,
								"picture.jpg");

						reqEntity.addPart("picture", bab);
					}

					postMethod.setEntity(reqEntity);
					responseBody = hc.execute(postMethod, res);

					System.out.println("post review = " + responseBody);
					JSONObject result = new JSONObject(responseBody);
					if (result != null) {
						boolean status = result.optBoolean("status");
						if (status) {

							JSONObject userInfo = result.optJSONObject("info");
							String token = result.optString("token");

							userInfo.put("token", token);

							UserDefault.setDictionaryForKey(userInfo,
									"user_info");
							m_handler.sendEmptyMessage(LOGIN_OK);

							return;
						}
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
				}

				m_handler.sendEmptyMessage(LOGIN_FAIL);
			}
		}.start();
	}

	public Handler m_handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// progress.hide();
			progress.dismiss();

			switch (msg.what) {
			case 0:

				StarTracker.StarSendEvent(g_mainLoginAcitivity, "App Event",
						"Login", "Logging in!");

				gotoExplorer();

				break;

			case 1:

				StarTracker.StarSendEvent(g_mainLoginAcitivity, "App Event",
						"Login", "Loggin Error!");

				Const.showMessage("", "Login failed.", MainLogInActivity.this);

				break;
			}

		}
	};

	public void gotoExplorer() {
		Intent intent = new Intent(MainLogInActivity.this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode,
	// android.content.Intent data) {
	//
	// if (resultCode == RESULT_OK) {
	// if (requestCode == 1000) {
	// getProfileFb();
	// return;
	// }
	// }
	// }

	private void onClickFBBtn() {
		if (Global.FB.session != null && Global.FB.session.isOpened()) {
			sendRequests();
		} else {

			Global.FB.pendingRequest = true;

			Session.OpenRequest openRequest = new Session.OpenRequest(this);

			if (openRequest != null) {
				openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
				openRequest.setPermissions(Arrays.asList("public_profile",
						"email", "user_birthday"));
				openRequest
						.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);

				Global.FB.session.openForPublish(openRequest
						.setCallback(new StatusCallback() {

							@Override
							public void call(Session session,
									SessionState state, Exception exception) {
								// TODO Auto-generated method stub
								if (exception != null) {
									new AlertDialog.Builder(
											MainLogInActivity.this)
											.setTitle("Insuffienct Permissions")
											.setMessage(exception.getMessage())
											.setPositiveButton("OK", null)
											.show();
									Global.FB.session = createSession();
								} else if (session.isOpened()) {
									sendRequests();
								}
							}
						}));

			}
		}
	}

	private void sendRequests() {

		if (Global.FB.session != null) {
			Log.d("FB", "groupToken request!");
			Request myreq = Request.newGraphPathRequest(Global.FB.session,
					"me", new Request.Callback() {
		    
	            @Override
						public void onCompleted(Response response) {
							Log.d("FB", "received token");
							GraphObject obj = response.getGraphObject();
							m_user = obj.getInnerJSONObject();
	            	
							System.out.println(m_user.toString());
	            	
							getUserImage();
	              }
		    
					});
			myreq.executeAsync();
		} else {
			Log.d("FB", "Session is closed");
		}
	}

	private Session createSession() {
		Session activeSession = Session.getActiveSession();
		if (activeSession == null || activeSession.getState().isClosed()) {
			activeSession = new Session.Builder(this).setApplicationId(
					MyConstants.FACEBOOK_ID).build();
			Session.setActiveSession(activeSession);
		}
		return activeSession;
	}

	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Global.FB.session.onActivityResult(this, requestCode, resultCode,
				data)
				&& Global.FB.pendingRequest
				&& Global.FB.session.getState().isOpened()) {
//			sendRequests();
		}
	  }

}