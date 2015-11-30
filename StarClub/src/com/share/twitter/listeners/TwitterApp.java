package com.share.twitter.listeners;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

@SuppressLint("HandlerLeak")
public class TwitterApp {
	private Twitter mTwitter;
	private TwitterSession mSession;
	private twitter4j.auth.AccessToken mAccessToken;
	private CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private OAuthProvider mHttpOauthprovider;
	private String mConsumerKey;
	private String mSecretKey;
	private ProgressDialog mProgressDlg;
	private TwDialogListener mListener;
	private Activity context;

    
	public static final String CONSUMER_KEY = "SCCGMcUY47lPC1SCxlO1A";
    public static final String CONSUMER_SECRET = "tqCqXUS7qCXNIsH9klAVWMoKaDvUs4K3XshQOo";
	public static final String CALLBACK_URL = "http://www.sunitguru.com";

    public static final String TWEET_AUTH_KEY = "2241279278-lXD7GGmpOhQ2fi17pG9nbwPzsMLSbljsiDYk4wJ";
    public static final String TWEET_AUTH_SECRET_KEY = "cBiJoNel147F1FY3oeXZQFho4kwJ8COwgJwceWzeIQiqK";

    public static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_ACCESS_TOKEN_SECRET = "access_token_secret";
//    public static final String KEY_USER_NAME = "user_name";
//    public static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
//    public static final String TWITTER_LOGIN_PREFERENCES = "twitter_login_preferences";

	private static final String TWITTER_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String TWITTER_AUTHORZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String TWITTER_REQUEST_URL = "https://api.twitter.com/oauth/request_token";

	public TwitterApp(Activity context, String consumerKey, String secretKey) {
		this.context = context;

		mTwitter = new TwitterFactory().getInstance();
		mSession = new TwitterSession(context);
		mProgressDlg = new ProgressDialog(context);

		mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mConsumerKey = consumerKey;
		mSecretKey = secretKey;

		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey,
				mSecretKey);

		String request_url = TWITTER_REQUEST_URL;
		String access_token_url = TWITTER_ACCESS_TOKEN_URL;
		String authorize_url = TWITTER_AUTHORZE_URL;

		mHttpOauthprovider = new DefaultOAuthProvider(request_url,
				access_token_url, authorize_url);
		mAccessToken = mSession.getAccessToken();

		configureToken();
	}

	public void setListener(TwDialogListener listener) {
		mListener = listener;
	}

	private void configureToken() {
		if (mAccessToken != null) {
			mTwitter.setOAuthConsumer(mConsumerKey, mSecretKey);
			mTwitter.setOAuthAccessToken(mAccessToken);
		}
	}

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();

			mAccessToken = null;
		}
	}

	public String getUsername() {
		return mSession.getUsername();
	}

	public void updateStatus(String status[]) throws Exception {
		try {
			String text = status[0];
			if(text.length()>140){
				text = text.substring(0,139);
			}
			if(status.length==1){
				
				StatusUpdate st = new StatusUpdate(text);
				mTwitter.updateStatus(text);
				
			}else{
				StatusUpdate st = new StatusUpdate(text);
				st.setMedia(String_to_File(status[1]));
				mTwitter.updateStatus(st);
			}
			
		} catch (TwitterException e) {
			throw e;
		}
	}

	public void authorize() {
		mProgressDlg.setMessage("Loading ...");
		mProgressDlg.show();

		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;

				try {
					authUrl = mHttpOauthprovider.retrieveRequestToken(
							mHttpOauthConsumer, CALLBACK_URL);
					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler
						.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}

	public void processToken(String callbackUrl) {
		mProgressDlg.setMessage("Finalizing ...");
		mProgressDlg.show();

		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;

				try {
					mHttpOauthprovider.retrieveAccessToken(mHttpOauthConsumer,
							verifier);

					mAccessToken = new twitter4j.auth.AccessToken(
							mHttpOauthConsumer.getToken(),
							mHttpOauthConsumer.getTokenSecret());

					configureToken();

					User user = mTwitter.verifyCredentials();

					mSession.storeAccessToken(mAccessToken, user.getName());

					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	private String getVerifier(String callbackUrl) {
		String verifier = "";

		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");

			URL url = new URL(callbackUrl);
			String query = url.getQuery();

			String array[] = query.split("&");

			for (String parameter : array) {
				String v[] = parameter.split("=");

				if (URLDecoder.decode(v[0]).equals(
						oauth.signpost.OAuth.OAUTH_VERIFIER)) {
					verifier = URLDecoder.decode(v[1]);
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return verifier;
	}

	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {

			public void onComplete(String value) {

				processToken(value);

			}

			public void onError(String value) {
				mListener.onError("Failed opening authorization page");
			}
		};

		new TwitterDialog(context, url, listener).show();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressDlg.dismiss();

			if (msg.what == 1) {
				if (msg.arg1 == 1)
					mListener.onError("Error getting request token");
				else
					mListener.onError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					mListener.onComplete("");
			}
		}
	};

	public interface TwDialogListener {
		public void onComplete(String value);

		public void onError(String value);
	}
	
	
	 // this function will make your image to file
    public File String_to_File(String img_url) {
    	File casted_image = null;
        try {
            int index = img_url.lastIndexOf(".");
            String nameOfImageWithExtension = img_url.substring(index + 1); // file type

            String _dir = Environment.getExternalStorageDirectory().toString();
            File rootSdDirectory = new File(_dir + "/temp_img/");
            Log.i("nameOfImageWithExtension ", "> " + nameOfImageWithExtension);

            if (rootSdDirectory.getAbsoluteFile().exists()) {
                rootSdDirectory.delete();
                rootSdDirectory.mkdirs();
            } else {
                rootSdDirectory.mkdirs();
            }

            casted_image = new File(rootSdDirectory, "temp_file.jpg");
            Log.i("casted_image ", "> " + casted_image);

            if (casted_image.exists()) {
                casted_image.delete();
                casted_image.createNewFile();
            } else {
                casted_image.createNewFile();

            }

            FileOutputStream fos = new FileOutputStream(casted_image);

            InputStream is = null;

            is = OpenHttpConnection(img_url);

            Bitmap myBitmap = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
            byte[] byteArray = outstream.toByteArray();
            fos.write(byteArray);
            fos.close();

            /*
             * byte[] buffer = new byte[1024]; int size = 0; while
             * ((size = in.read(buffer)) > 0) { fos.write(buffer, 0,
             * size); } fos.close();
             */
            return casted_image;

        } catch (Exception e) {

            System.out.print(e);
            // e.printStackTrace();

        }
        return casted_image;
    }
    
    
    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection)) throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) { in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }
        return in;
    }

}