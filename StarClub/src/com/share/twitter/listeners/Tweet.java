package com.share.twitter.listeners;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.util.HttpURLConnection;


import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Tweet {
	ProgressDialog pDialog;
	Activity ctx;
	File casted_image;
	String imggURL;
	String msg;
	 private static twitter4j.Twitter twitter;
	 private static RequestToken requestToken;
	public Tweet(Activity ctx, String imggURL, String msg){
		
		this.ctx = ctx;
		this.imggURL = imggURL;
		this.msg = msg;
	}
	 /**
     * Function to update status
     * */
	
	public void tweet(){
		new updateTwitterStatus().execute(msg);
	}
	
	
	 /**
     * Function to login twitter
     * */
    public void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TwitterApp.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TwitterApp.CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter.getOAuthRequestToken(TwitterApp.CALLBACK_URL);
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
                
                tweet();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            Toast.makeText(ctx, "Already Logged into twitter", Toast.LENGTH_LONG).show();
            tweet();

        }
    }
	
    class updateTwitterStatus extends AsyncTask < String, String, String > {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage("Updating to twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String...args) {
            Log.d("Tweet Text", "> " + args[0]);
            String status = args[0];
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TwitterApp.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TwitterApp.CONSUMER_SECRET);

            // Access Token
            String access_token = TwitterApp.KEY_ACCESS_TOKEN;
            // Access Token Secret
            String access_token_secret = TwitterApp.KEY_ACCESS_TOKEN_SECRET;

            builder.setOAuthAccessToken(access_token);
            builder.setOAuthAccessTokenSecret(access_token_secret);

            Configuration configuration = builder.build();
            twitter4j.Twitter twitter = new TwitterFactory(configuration).getInstance();
            //AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            //Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            // Update status
            //twitter4j.Status response = twitter.updateStatus(status);

            StatusUpdate st = new StatusUpdate(status);
            String_to_File(imggURL);
            //DownloadImage("http://www.imgbase.info/images/safe-wallpapers/miscellaneous/computer/39075_computer_android.jpg");
            try {

                st.setMedia(casted_image);
                twitter.updateStatus(st);

            } catch (Exception e) {
                Log.e("Twitter Update Error", "" + e);
            }

            Log.i("Status", "> " + st.getInReplyToStatusId());

            return access_token_secret;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI Always use runOnUiThread(new
         * Runnable()) to update UI from background thread, otherwise
         * you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            ((Activity)ctx).runOnUiThread(new Runnable() {@Override
                public void run() {
                    Toast.makeText(ctx, "Status tweeted successfully", Toast.LENGTH_SHORT).show();
                    // Clearing EditText field
                    
                }
            });
        }

    }

    /**
     * Function to logout from twitter It will just clear the application
     * shared preferences
     * */
    private void logoutFromTwitter() {
        // Clear the shared preferences
        
        // After this take the appropriate action
        // I am showing the hiding/showing buttons again
        // You might not needed this code
        
    }

    /**
     * Check user already logged in your application using twitter Login
     * flag is fetched from Shared Preferences
     * */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return false;
    }

    
    // this function will make your image to file
    public File String_to_File(String img_url) {

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
