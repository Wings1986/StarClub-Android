package com.starclub.enrique;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import com.mycom.data.MyConstants;
import com.mycom.lib.UserDefault;


public class GCMIntentService extends GCMBaseIntentService {

	public static final String SENDER_ID = MyConstants.GCM_Project_Number;
	
	public GCMIntentService() {
		super(SENDER_ID);
	}
	
	@Override
	protected void onError(Context arg0, String arg1) {
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			showMessage(context, intent);
		}
	}

	@Override
	protected void onRegistered(Context context, String regID) {
		if(!regID.equals("") && regID != null){
			System.out.println("onRegistered!! " + regID);
			final String strRegID = regID;
			new Thread(new Runnable() {
				
				public void run() {
					insertRegistrationID(strRegID);
				}
			}).start();
			
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		
	}

	public void insertRegistrationID(String regID) {

	 	System.out.println("deviceToken = " + regID);
	 	UserDefault.setStringForKey(regID, "deviceToken");	            
		
    }
	
	private void showMessage(Context context, Intent intent){

		String msg = intent.getStringExtra("message");
		
		if (msg == null)
			msg = "";

		System.out.println("Push message = " + msg);

		
		String pushType = intent.getStringExtra("push_type");
		
		String feedsStr = intent.getStringExtra("feeds");
		JSONArray feeds = null;
		try {
			feeds = new JSONArray(feedsStr);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		boolean bRunning = false;
		String strClassName = "";
		
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> info = am.getRunningTasks(30);
		
		int nCount = 0;
		for ( Iterator iterator = info.iterator(); iterator.hasNext();) {
			ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) iterator.next();
			
			Log.i("Running Services", runningTaskInfo.topActivity.getClassName());
			
			if ( runningTaskInfo.topActivity.getClassName().contains(".HomeActivity") && nCount == 0 ) {
				bRunning = true;
				strClassName = runningTaskInfo.topActivity.getClassName();

				break;
			}
			break;
		}
		
		if (bRunning) {

			Message mMessage = new Message();
			
			Bundle args = new Bundle();
			args.putString("msg", msg);
			args.putString("push_type", pushType);
			args.putString("feeds", feeds.toString());
			mMessage.setData(args);
    	    
    	    ((HomeActivity)(HomeActivity.g_HomeActivity)).receivedHandler.sendMessage(mMessage);

		}
		else {
			
			try {
				
				NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
				
				Intent notificationIntent = new Intent(this.getApplicationContext(), HomeActivity.class);
				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				notificationIntent.putExtra("push_type", pushType);
				notificationIntent.putExtra("feeds", feeds.toString());
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
				
				Notification notification = new Notification();

				notification.icon = R.drawable.ic_enrique_dev;
				notification.tickerText = msg;
				notification.when = System.currentTimeMillis();
				notification.defaults = Notification.DEFAULT_ALL;
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(context, context.getString(R.string.app_name), msg, pendingIntent);
				
				notificationManager.notify(0, notification);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
