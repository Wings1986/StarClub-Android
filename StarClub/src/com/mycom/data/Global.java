package com.mycom.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.facebook.Session;
import com.mycom.lib.UserDefault;

public class Global {

	public static final class FB
	{
		
	    public static final String PENDING_REQUEST_BUNDLE_KEY = "com.facebook.samples.graphapi:PendingRequest";

	    public static Session session;
	    public static boolean pendingRequest;
	}
	
	
	public static int FEED_ALL = 0;
	public static int FEED_TEXT = 1;
	public static int FEED_IMAGE = 2;
	public static int FEED_VIDEO = 3;
	public static int FEED_POLLS = 4;
	public static int FEED_QUIZ = 5;
	public static int FEED_BANNER = 6;
	
	public static int OPT_ALLACCESS = 0;
	public static int OPT_PHOTO_GALLERY = 1;
	public static int OPT_VIDEO_GALLERY = 2;
	public static int OPT_PROFILE = 3;
	public static int OPT_COMMUNITY = 4;
	public static int OPT_USERDETAIL = 5;
	public static int OPT_PHOTODETAIL = 6;
	public static int OPT_TOURDATE_DETAIL= 7;
	public static int OPT_POLLSCONTEST= 8;
	public static int OPT_DRAFTVIEW= 9;
	
	public static int FAN = -1;
	public static int ADMIN_CMS = 0;
	public static int ADMIN_POST = 1;
	public static int ADMIN_DELETE = 2;
		    
		    
	
	public static int getUserType()
	{
		JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
		int type = FAN; 
		try {
			type = userInfo.getInt("admin_type");
		} catch (JSONException e) {
			type = FAN;
		}

		return type;
	}
	
}


