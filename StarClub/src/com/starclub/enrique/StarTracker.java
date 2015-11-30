package com.starclub.enrique;

import android.content.Context;

import com.facebook.FacebookRequestError.Category;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.mycom.data.MyConstants;
import com.quantcast.measurement.service.QuantcastClient;

public class StarTracker {

	
    public static final int DID_LAUNCH = 0;
    public static final int DID_TERMINATE = 1;
    public static final int DID_SIGNOUT = 2;
    public static final int DID_SIGNIN = 3;
    public static final int IS_ACTIVE = 4;
    public static final int IS_BACKGROUND = 5;
	
    
	public static void StarRunStatus(Context mContext, int status, String mCatagory, String mAction, String mLabel) {
		
		if (status == DID_LAUNCH) {
			Tracker tracker = GoogleAnalytics.getInstance(mContext).getTracker(MyConstants.GA_PROPERTY_ID);
		   	
			GoogleAnalytics.getInstance(mContext)
		    	.getLogger()
		    	.setLogLevel(LogLevel.VERBOSE); 
			
			tracker.send(MapBuilder
					  .createEvent(mCatagory, mAction, mLabel, null)
					  .set(Fields.SESSION_CONTROL, "start")
					  .build()
					);
		} else {
			Tracker tracker = GoogleAnalytics.getInstance(mContext).getTracker(MyConstants.GA_PROPERTY_ID);
		   	
			tracker.send(MapBuilder
					  .createEvent(mCatagory, mAction, mLabel, null)
					  .build()
					);
		}
		
	}
	

	public static void StarSendView(Context mContext, String mView) {
		
		Tracker tracker = GoogleAnalytics.getInstance(mContext).getTracker(MyConstants.GA_PROPERTY_ID);
		
		tracker.send(MapBuilder.createAppView()
    			.set(Fields.SCREEN_NAME, mView)
    			.build()
    			);
	}

	public static void StarSendEvent(Context mContext, String mCatagory, String mAction, String mLabel) {
		
		Tracker tracker = GoogleAnalytics.getInstance(mContext).getTracker(MyConstants.GA_PROPERTY_ID);
	   	
		tracker.send(MapBuilder
				  .createEvent(mCatagory, mAction, mLabel, null)
				  .build()
				);
	}
	
	/*
	 *  QuantCast SDK 
	 */
	public static void StartQuantcast(Context mContext) {
		
		QuantcastClient.activityStart(mContext, MyConstants.QUANTCAST_KEY, MyConstants.QUANTCAST_ID, null);
		
	}
	public static void StopQuantcast() {
		QuantcastClient.activityStop();
	}
	
}
