package com.starclub.enrique;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.basic.views.AddPhotoView;
import com.basic.views.AddTextView;
import com.basic.views.AddVideoView;
import com.basic.views.CommentView;
import com.basic.views.CommunityView;
import com.basic.views.DeepLinkView;
import com.basic.views.DraftReviewView;
import com.basic.views.FollowUserView;
import com.basic.views.HelpView;
import com.basic.views.InboxView;
import com.basic.views.LikeListView;
import com.basic.views.MediaPlayerView;
import com.basic.views.MessageDetailView;
import com.basic.views.PhotoListView;
import com.basic.views.PhotosView;
import com.basic.views.PollContestView;
import com.basic.views.PollsResultView;
import com.basic.views.PollsView;
import com.basic.views.ProfileView;
import com.basic.views.PublishView;
import com.basic.views.QuizResultView;
import com.basic.views.QuizView;
import com.basic.views.RankingView;
import com.basic.views.SearchUserView;
import com.basic.views.SendMsgView;
import com.basic.views.SettingView;
import com.basic.views.ShopView;
import com.basic.views.TourDateView;
import com.basic.views.UpdateView;
import com.basic.views.UserDetailView;
import com.basic.views.VideosView;
import com.basic.views.CustomWebView;
import com.basic.views.AllAccessView;
import com.basic.views.PhotoDetailView;
import com.basic.views.YoutubeView;
import com.facebook.AppEventsLogger;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphObject;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.frame.util.MenuEventController;
import com.frame.util.MenuLazyAdapter;
import com.frame.util.OnSwipeTouchListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gcm.GCMRegistrar;
import com.mycom.data.MyConstants;
import com.mycom.data.Global;
import com.mycom.data.Global.FB;
import com.mycom.lib.Const;
import com.mycom.lib.ResizeAnimation;
import com.mycom.lib.UserDefault;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.network.httpclient.JsonParser;
import com.network.httpclient.MyParser;
import com.network.httpclient.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.share.twitter.listeners.Twitt;
import com.share.twitter.listeners.TwitterApp;
import com.starclub.enrique.login.AvatarActivity;
import com.starclub.enrique.login.MainLogInActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.provider.MediaStore.Images;
import android.text.style.UpdateAppearance;
import android.text.style.UpdateLayout;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Android activity with menu and layout to add elements of your application
 * @author
 */
public class HomeActivity extends Activity {

    
	public RelativeLayout baselayout;
	private MenuLazyAdapter menuAdapter;
	
	private boolean open = false;
	private boolean noController = false;
    
	public static Context g_HomeActivity;
    
	private ListView listMenu;
	private TextView viewTitle;
	
	public Button		btnNavRight = null;
	public LinearLayout mainLayout = null;
	public LinearLayout gestureLayout = null;
	public LinearLayout mediaLayout = null;
	
	public Button 		btnSearch = null;
	
	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions optIcon, optFull;
	public ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	public int m_prePoint = 1;
	
	
	/*****************
	 *    New Variable
	 */
	
	public AllAccessView 		m_viewAllAccess = null;
	public PhotosView		 	m_viewPhotos = null;
	public VideosView			m_viewVideos = null;
	public PollContestView		m_viewPollContest = null;
	public ProfileView			m_viewProfile = null;
	public CommunityView		m_viewCommnunity = null;
	public InboxView			m_viewInbox = null;
	public RankingView			m_viewRanking = null;
	public SettingView			m_viewSetting = null;
	public HelpView				m_viewHelp = null;
	public MediaPlayerView		m_viewMediaPlayer = null;
	public SearchUserView 		m_viewSearch = null;
	public DraftReviewView		m_viewDraftReview = null;
	
	
	/*
	 *  Facebook posting
	 */
//    static final String PENDING_REQUEST_BUNDLE_KEY = "com.facebook.samples.graphapi:PendingRequest";
//
//    public Session session;
//    boolean pendingRequest;
    boolean bclickfacebook = false;
    
    
    boolean m_bIsDraft = false;
    public boolean m_bDraftShare = false;
    public int m_bEnableFbCaption = 0;
    public int m_bEnableTWCaption = 0;
    
    ProgressDialog progress = null;
    
	@Override
	public void onStart(){
		super.onStart();
		
		StarTracker.StartQuantcast(this);
	}
	@Override
	public void onStop() {
		super.onStop();
		
		StarTracker.StopQuantcast();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		StarTracker.StarRunStatus(this, StarTracker.DID_TERMINATE, "Run Status", "Terminated", "State Change");
		
		super.onDestroy();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		AppEventsLogger.activateApp(this, MyConstants.FACEBOOK_ID);
		
		if (m_viewMediaPlayer.mediaPlayer != null) {
			m_viewMediaPlayer.mediaPlayer.start();
		}
		
		StarTracker.StarRunStatus(this, StarTracker.IS_ACTIVE, "Run Status", "Active", "State Change");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if (m_viewMediaPlayer.mediaPlayer != null 
				&& m_viewMediaPlayer.mediaPlayer.isPlaying()) {
			m_viewMediaPlayer.mediaPlayer.pause();
		}
		
		StarTracker.StarRunStatus(this, StarTracker.IS_BACKGROUND, "Run Status", "Background", "State Change");
	}
	
	 @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	        super.onRestoreInstanceState(savedInstanceState);

	        Global.FB.pendingRequest = savedInstanceState.getBoolean(Global.FB.PENDING_REQUEST_BUNDLE_KEY, Global.FB.pendingRequest);
	    }

	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);

	        outState.putBoolean(Global.FB.PENDING_REQUEST_BUNDLE_KEY, Global.FB.pendingRequest);
	    }
	    
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
        logHeap();
//		super.onBackPressed();
		
        if (arrHistory.size() != 0) {
        	String viewName = arrHistory.get(arrHistory.size() -1);
        	Object obj = arrHistoryObj.get(arrHistoryObj.size() -1);
        	
        	if (viewName.equals("AddPhotoView")) {
        		((AddPhotoView)obj).onBack();
        	}
        	if (viewName.equals("AddTextView")) {
        		((AddTextView)obj).onBack();
        	}
        	if (viewName.equals("AddVideoView")) {
        		((AddVideoView)obj).onBack();
        	}
        	if (viewName.equals("CommentView")) {
        		((CommentView)obj).onBack();
        	}
        	if (viewName.equals("FollowUserView")) {
        		((FollowUserView)obj).onBack();
        	}
        	if (viewName.equals("MessageDetailView")) {
        		((MessageDetailView)obj).onBack();
        	}
        	if (viewName.equals("PhotoDetailView")) {
        		((PhotoDetailView)obj).onBack();
        	}
        	if (viewName.equals("PhotoListView")) {
        		((PhotoListView)obj).onBack();
        	}
        	if (viewName.equals("PollsResultView")) {
        		((PollsResultView)obj).onBack();
        	}
        	if (viewName.equals("PollsView")) {
        		((PollsView)obj).onBack();
        	}
        	if (viewName.equals("QuizResultView")) {
        		((QuizResultView)obj).onBack();
        	}
        	if (viewName.equals("QuizView")) {
        		((QuizView)obj).onBack();
        	}
        	if (viewName.equals("SearchUserView")) {
        		onCloseSearch();
        	}
        	if (viewName.equals("SendMsgView")) {
        		((SendMsgView)obj).onBack();
        	}
        	if (viewName.equals("UpdateView")) {
        		((UpdateView)obj).onBack();
        	}
        	if (viewName.equals("UserDetailView")) {
        		((UserDetailView)obj).onBack();
        	}
        	if (viewName.equals("YoutubeView")) {
        		((YoutubeView)obj).onBack();
        	}
        	if (viewName.equals("MediaPlayerView")) {
        		openMediaTool(false);
        	}
        	if (viewName.equals("InboxView")) {
        		((InboxView)obj).onBack();
        	}
        	if (viewName.equals("SettingView")) {
        		((SettingView)obj).onBack();
        	}
        	if (viewName.equals("LikeListView")) {
        		((LikeListView)obj).onBack();
        	}
        	if (viewName.equals("PublishView")) {
        		((PublishView)obj).onBack();
        	}
        	
        }
        else if (m_prePoint != 1) {
        	setMenuSelect(1);
        }
		else {
/*			
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Do you want to exit this app?");
			dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					dialog.cancel();

				}
			});

			dialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					dialog.cancel();
					
					Global.save();
					//System.exit(0);
					finish();
				}
			});
			
			dialog.show();
*/
			finish();
		}
		
	}
	
	ArrayList<String> arrHistory = new ArrayList<String>();
	ArrayList<Object> arrHistoryObj = new ArrayList<Object>();
	
	public void addHistory(String viewName, Object obj){
		arrHistory.add(viewName);
		arrHistoryObj.add(obj);
	}
	public void removeHistory(String viewName){
		if (arrHistory.contains(viewName)) {
			arrHistory.remove(arrHistory.size()-1);
			arrHistoryObj.remove(arrHistoryObj.size() -1);
		}
	}
	public void clearHistory() {
		arrHistory.clear();
		arrHistoryObj.clear();
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	
	public void setMultiOrientation(boolean bMulti) {
		if (bMulti) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//					| ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        System.out.println("On Create");
        
         
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
        setContentView(R.layout.activity_home);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 

        
        UserDefault.init(this);

        
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
          GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
        } else {
        	UserDefault.setStringForKey(regId, "deviceToken");
        	System.out.println("GCM : Already registered : " + regId);
        }
        
        
        g_HomeActivity = this;
        
        this.listMenu = (ListView) findViewById(R.id.listMenu);

        this.baselayout = (RelativeLayout) findViewById(R.id.layoutToMove);
        this.baselayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
        
        this.viewTitle = (TextView) findViewById(R.id.appName);
        this.mainLayout = (LinearLayout) findViewById(R.id.basicLayout);
        this.gestureLayout = (LinearLayout) findViewById(R.id.gestureLayout);
        this.gestureLayout.setOnTouchListener(gestureListener);
        this.gestureLayout.setVisibility(View.GONE);
        
        this.btnNavRight		= (Button) findViewById(R.id.btn_right);
        this.mediaLayout = (LinearLayout) findViewById(R.id.mediaLayout);
        this.mediaLayout.setBackgroundColor(Color.WHITE);
        this.btnSearch = (Button) findViewById(R.id.btnSearch);
        
        
    	optIcon = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_empty)
			.showImageForEmptyUri(R.drawable.demo_avatar)
			.showImageOnFail(R.drawable.demo_avatar)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
//			.displayer(new RoundedBitmapDisplayer(20))
			.build();
    	
    	
    	optFull = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_empty)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
    	
        initView();
        
        
//        this.menuAdapter = new MenuLazyAdapter(this, MenuEventController.menuArray.size() == 0 ? MenuEventController.getMenuDefault(this) : MenuEventController.menuArray);
        setMenuItem("");
        this.listMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if (position == 0 
						|| (!m_bIsDraft && position == 9) || (m_bIsDraft && position == 10)  ) {
					// header 
					return;
				}
				
				if (m_prePoint != position 
						|| (!m_bIsDraft && position == 8) || (m_bIsDraft && position == 9)) { // media player, any view

					clearHistory();
					
					imageLoader.clearMemoryCache();
					clearView();
					
					noController = false;
					
					if(position == 1){ // All Access
						//action
						
				    	LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
						
				    	m_viewAllAccess = (AllAccessView) inflater.inflate(R.layout.view_allaccess, null);

						mainLayout.removeAllViews();
						mainLayout.addView(m_viewAllAccess);
						
						setTitle(MyConstants.MENU_MAIN_FEED);
						setRightBtnItem(true, "Update");
						
						m_viewAllAccess.init(HomeActivity.this);
						
					} else if (m_bIsDraft && position == 2) { // Draft view
						
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
						
				    	m_viewDraftReview = (DraftReviewView) inflater.inflate(R.layout.view_draftview, null);

						mainLayout.removeAllViews();
						mainLayout.addView(m_viewDraftReview);
						
						setTitle(MyConstants.MENU_DRAFT);
						setRightBtnItem(false, "");
						
						m_viewDraftReview.init(HomeActivity.this);
						
					} else if((!m_bIsDraft && position == 2)
							|| (m_bIsDraft && position == 3)){ // Community
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewCommnunity = (CommunityView) inflater.inflate(R.layout.view_community, null);
				    	
						mainLayout.removeAllViews();
						mainLayout.addView(m_viewCommnunity);
						
						setTitle(MyConstants.MENU_COMMUNITY);
						setRightBtnItem(true, "");
						
						m_viewCommnunity.init(HomeActivity.this);
						
					} else if((!m_bIsDraft && position == 3)
							|| (m_bIsDraft && position == 4)){ // Photos
						//action
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewPhotos 	= (PhotosView) inflater.inflate(R.layout.view_photos, null);

						mainLayout.removeAllViews();
//						mainLayout.addView(m_viewPhotos);
						mainLayout.addView(m_viewPhotos, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
						
						setTitle("Photos");
						setRightBtnItem(false, "");
						
						m_viewPhotos.init(HomeActivity.this);
						
					} else if((!m_bIsDraft && position == 4)
							|| (m_bIsDraft && position == 5)){ // Videos
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewVideos 	= (VideosView) inflater.inflate(R.layout.view_videos, null);

						mainLayout.removeAllViews();
						mainLayout.addView(m_viewVideos);
						
						setTitle("Videos");
						setRightBtnItem(false, "");
						
						m_viewVideos.init(HomeActivity.this);

					} else if((!m_bIsDraft && position == 5)
							|| (m_bIsDraft && position == 6)){ // Tour Datas
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewHelp 		= (HelpView) inflater.inflate(R.layout.view_help, null);

				    	mainLayout.removeAllViews();
						mainLayout.addView(m_viewHelp);
						
						setTitle(MyConstants.MENU_TOUR);
						setRightBtnItem(false, "");
						
						String url = MyConstants.SERVER + "/index.php/viewhelp/tour/" + MyConstants.CID;
						m_viewHelp.init(HomeActivity.this, url);
						
					} else if((!m_bIsDraft && position == 6)
							|| (m_bIsDraft && position == 7)){ //Polls & contests
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewPollContest = (PollContestView) inflater.inflate(R.layout.view_pollcontest, null);
				    	
						mainLayout.removeAllViews();
						mainLayout.addView(m_viewPollContest);
						
						setTitle(MyConstants.MENU_QUIZ);
						setRightBtnItem(false, "");
						
						m_viewPollContest.init(HomeActivity.this);
						
					} else if((!m_bIsDraft && position == 7)
							|| (m_bIsDraft && position == 8)){ //Shop
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewHelp 		= (HelpView) inflater.inflate(R.layout.view_help, null);

				    	mainLayout.removeAllViews();
						mainLayout.addView(m_viewHelp);
						
						setTitle("Shop");
						setRightBtnItem(false, "");
						
						String url = MyConstants.SERVER + "/index.php/viewhelp/shop/" + MyConstants.CID;
						m_viewHelp.init(HomeActivity.this, url);
					
					} else if ((!m_bIsDraft && position == 8)
							|| (m_bIsDraft && position == 9)) { // music player
					
						openMediaTool(true);
						noController = true;
						
					} else if( ((!m_bIsDraft && position == 10) || (m_bIsDraft && position == 11)) 
							&& Global.getUserType() == Global.FAN){ // Profile
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewProfile 	= (ProfileView) inflater.inflate(R.layout.view_profile, null);
						
						mainLayout.removeAllViews();
						mainLayout.addView(m_viewProfile);
						
						JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
				        String userName = "";
						try {
							userName = userInfo.getString("name");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        userInfo = null;
				        
						setTitle(userName);
						setRightBtnItem(true, "");
						
						m_viewProfile.init(HomeActivity.this);


					} else if(((!m_bIsDraft && position == 11) || (m_bIsDraft && position == 12))
							&& Global.getUserType() == Global.FAN) { // Index
						
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewInbox		= (InboxView) inflater.inflate(R.layout.view_inbox, null);


						mainLayout.removeAllViews();
						mainLayout.addView(m_viewInbox);
						
						setTitle("Inbox");
						setRightBtnItem(false, "");
						
						m_viewInbox.init(HomeActivity.this, false);
						
					} else if((!m_bIsDraft && position == 12) || (m_bIsDraft && position == 13)) { // Ranking
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewRanking 	= (RankingView) inflater.inflate(R.layout.view_ranking, null);

						mainLayout.removeAllViews();
						mainLayout.addView(m_viewRanking);
						
						setTitle("Ranking");
						setRightBtnItem(false, "");
						
						m_viewRanking.init(HomeActivity.this);
						
//					} else if (position == 10 && Global.getUserType() != Global.FAN) { //sign out
//						noController = true;
//						signOut();
						
					} else if((((!m_bIsDraft && position == 13) || (m_bIsDraft && position == 14)) && Global.getUserType() == Global.FAN)
							|| (((!m_bIsDraft && position == 10) || (m_bIsDraft && position == 11)) && Global.getUserType() != Global.FAN)) { // Setting
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewSetting 	= (SettingView) inflater.inflate(R.layout.view_settings, null);

				    	mainLayout.removeAllViews();
						mainLayout.addView(m_viewSetting);
						
						setTitle("Settings");
						setRightBtnItem(true, "");
						
						m_viewSetting.init(HomeActivity.this, false);
						
					} else if((((!m_bIsDraft && position == 14) || (m_bIsDraft && position == 15)) && Global.getUserType() == Global.FAN)
							|| (((!m_bIsDraft && position == 11) || (m_bIsDraft && position == 12)) && Global.getUserType() != Global.FAN)) { // help
						LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
				    	m_viewHelp 		= (HelpView) inflater.inflate(R.layout.view_help, null);

				    	mainLayout.removeAllViews();
						mainLayout.addView(m_viewHelp);
						
						setTitle("Help");
						setRightBtnItem(false, "");
						
						String url = MyConstants.SERVER + "/index.php/viewhelp/help/" + MyConstants.CID;
						m_viewHelp.init(HomeActivity.this, url);
						
					}
					
				}

				m_prePoint = position;

				if(open && !noController){
					open = false;
					MenuEventController.close(g_HomeActivity, baselayout, viewTitle);
					MenuEventController.closeKeyboard(g_HomeActivity, view);
					
		    		View shadowLayout = (View) findViewById(R.id.shadowLayout);
		    		shadowLayout.setVisibility(View.GONE);
            	}
				
				menuAdapter.notifyDataSetChanged();
			
			}
		});
        
        
        btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onOpenSearch();
			}
		});
        

        /* setup facebook */
//        if (Global.FB.session == null) {
//        	Global.FB.session = createSession();
//        	
//        	Global.FB.pendingRequest = true;
//             
//             Session.OpenRequest openRequest = null;
//             openRequest = new Session.OpenRequest(this);
//
//             if (openRequest != null) {
//                 openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
//                 openRequest.setPermissions(Arrays.asList("publish_actions",
//                       "manage_pages", "status_update"));
//                 openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
//
//                 Global.FB.session.openForPublish(openRequest.setCallback(new StatusCallback() {
//					
//					@Override
//					public void call(Session session, SessionState state, Exception exception) {
//						// TODO Auto-generated method stub
//						
//					}
//				}));
//             }
//        }
        
//        Global.FB.session = createSession();
//        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        
    }

    
    public OnSwipeTouchListener gestureListener = new OnSwipeTouchListener() {
    	 public void onSwipeRight() {
    		 
    		 System.out.println("swipe right =============");
    		 
             if(!open){
             	open = true;
             	MenuEventController.open(g_HomeActivity, baselayout, viewTitle);
             	MenuEventController.closeKeyboard(g_HomeActivity, getCurrentFocus());
             }
         }
         public void onSwipeLeft() {
        	 
        	 System.out.println("swipe left =============");
        	 
         	if(open){
         		open = false;
         		MenuEventController.close(g_HomeActivity, baselayout, viewTitle);
         		MenuEventController.closeKeyboard(g_HomeActivity, getCurrentFocus());
         	}
         }
         
         public void onOneTouchEvent(MotionEvent e) {
         
        	 System.out.println("one touch =============");
        	 
        	 baselayout.onTouchEvent(e);
        	 
         }
         
         
    };
    
//    public OnTouchListener touchListener = new OnTouchListener() {
//		
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//
//			System.out.println("touch event =============");
//			
//			return false;
//		}
//	};
	
    boolean bSet_AdminName = false;
    public void setMenuItem(String adminName) {
    	if (bSet_AdminName) {
    		return;
    	}
    	
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
        String userName = "";
        
		try {
			userName = userInfo.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (adminName.length() < 1) {
        	bSet_AdminName = false;
        } else {
        	bSet_AdminName = true;
        }
        
        try {
			m_bIsDraft = userInfo.getInt("is_draft") == 0 ? false : true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        userInfo = null;
        
        
        this.menuAdapter = new MenuLazyAdapter(this);
        
        this.menuAdapter.addSeparatorItem(adminName.toUpperCase());
        this.menuAdapter.addItem(MyConstants.MENU_MAIN_FEED);
        if (m_bIsDraft) {
        	this.menuAdapter.addItem(MyConstants.MENU_DRAFT);
		}
        this.menuAdapter.addItem(MyConstants.MENU_COMMUNITY);
        this.menuAdapter.addItem("Photos");
        this.menuAdapter.addItem("Videos");
        this.menuAdapter.addItem(MyConstants.MENU_TOUR);
        this.menuAdapter.addItem(MyConstants.MENU_QUIZ);
        this.menuAdapter.addItem("Shop");
        this.menuAdapter.addItem(MyConstants.MENU_MUSIC);
        this.menuAdapter.addSeparatorItem(userName.toUpperCase());
        if (Global.getUserType() == Global.FAN) {
            this.menuAdapter.addItem("Profile");
            this.menuAdapter.addItem("Inbox");
            this.menuAdapter.addItem("Ranking");
            this.menuAdapter.addItem("Setting");
            this.menuAdapter.addItem("Help");
        } else {
            this.menuAdapter.addItem("Setting");
            this.menuAdapter.addItem("Help");
        }
        
        this.listMenu.setAdapter(menuAdapter);
    }
    /**********************************************************************
     * 
     * 	Button Event 
     */
    
    public void openCloseMenu(View view){
    	if(!this.open){

    		this.open = true;
    		MenuEventController.open(g_HomeActivity, this.baselayout, this.viewTitle);
    		MenuEventController.closeKeyboard(g_HomeActivity, view);
    		
    		View shadowLayout = (View) findViewById(R.id.shadowLayout);
    		shadowLayout.setVisibility(View.VISIBLE);
    		
    	} else {
    		this.open = false;
    		MenuEventController.close(g_HomeActivity, this.baselayout, this.viewTitle);
    		MenuEventController.closeKeyboard(g_HomeActivity, view);
    		
    		View shadowLayout = (View) findViewById(R.id.shadowLayout);
    		shadowLayout.setVisibility(View.GONE);
    	}
    }
    
    public void setRightBtnItem(boolean bShow, String title) {
    
    	if (bShow) {
    		btnNavRight.setVisibility(View.VISIBLE);
    		btnNavRight.setText(title);
    		btnNavRight.setBackgroundResource(R.drawable.btn_next);
    	}
    	else {
    		btnNavRight.setVisibility(View.GONE);
    	}
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

//		openCloseMenu(null);
		
		getMenuInflater().inflate(R.menu.menu, menu);
	    return true;
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.settings:
	        // Setting option clicked.
	    	
	    	if(!this.open){
	    		
	    	} else {
	    		this.open = false;
	    		MenuEventController.close(g_HomeActivity, this.baselayout, this.viewTitle);
	    		MenuEventController.closeKeyboard(g_HomeActivity, null);
	    		
	    		View shadowLayout = (View) findViewById(R.id.shadowLayout);
	    		shadowLayout.setVisibility(View.GONE);
	    	}
	    	
	    	if (!m_bShowSetting)
	    		onSettings();
	    	
	        return true;
	    case R.id.signout:
	        // Sign out option clicked.
	    	signOut();
	        return true;

	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
	public boolean m_bShowSetting = false;
	
	public void onSettings() {
// 		((HomeActivity)m_context).setMenuSelect(13);
		
		SettingView subView = (SettingView) LayoutInflater.from(
				this).inflate(R.layout.view_settings, null);
		subView.init(this, true);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		baselayout.addView(subView, params);

		Animation out = AnimationUtils.loadAnimation(this,
				R.anim.scale_out);
		subView.startAnimation(out);
 	}
	
	
    /******************************************************************************
     *    initialize
     */
    
    public void initView()
    {

		/*
		 *  Deep Link
		 */
		
    	String scheme = "", host = "", posttype = "", contentid = "";
    	
		Uri data = getIntent().getData();
		if (data != null) {
			scheme = data.getScheme(); // http
			host = data.getHost();	  // host
			
			List<String> params = data.getPathSegments();
			if (params.size() > 1) {
				posttype = params.get(0);
				contentid = params.get(1);
			}
		}
		
		String push_type = getIntent().getStringExtra("push_type");
		
	
    	LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);

		mainLayout.removeAllViews();
		
		if (scheme.equals(MyConstants.DEEPLINK_URL)
				&& host.equals(""+MyConstants.CID)) {
	    	DeepLinkView view = (DeepLinkView) inflater.inflate(R.layout.view_deeplink, null);
			mainLayout.addView(view);

			setTitle("Deep Link");
			setRightBtnItem(false, "");

			view.init(this, posttype, contentid);
		}
		else if (push_type != null && push_type.length() > 0) {
			DeepLinkView view = (DeepLinkView) inflater.inflate(R.layout.view_deeplink, null);
			mainLayout.addView(view);

			setTitle("Deep Link");
			setRightBtnItem(false, "");

			String jsonStr = getIntent().getStringExtra("feeds");
			try {
				JSONArray feeds = new JSONArray(jsonStr);
				
				int count = feeds.length();
				if (count != 0) {
					posttype = feeds.getJSONObject(count-1).getString("post_type");
					contentid= feeds.getJSONObject(count-1).getString("content_id");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			view.init(this, posttype, contentid);
		}
		else {
	    	m_viewAllAccess = (AllAccessView) inflater.inflate(R.layout.view_allaccess, null);
			mainLayout.addView(m_viewAllAccess);
			setTitle(MyConstants.MENU_MAIN_FEED);
			m_viewAllAccess.init(this);
		}
		
		m_viewMediaPlayer = (MediaPlayerView) inflater.inflate(R.layout.view_mediaplayer, null);
		mediaLayout.addView(m_viewMediaPlayer);
		m_viewMediaPlayer.init(this);
		
		m_viewMediaPlayer.getMusicList();
    }
    
    
    public void openMediaTool(boolean bOpen) {
    	FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) mediaLayout.getLayoutParams();
    	
    	int virturlHeight;
    	if (!bOpen) {
    		virturlHeight = 35;
    	} else {
    		virturlHeight = 350;
    	}
    		
    	int new_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, virturlHeight, getResources().getDisplayMetrics());
    	int width = param.width;
    	
//    	param.height = new_height;
//    	mediaLayout.setLayoutParams(param);
    	
    	ResizeAnimation anim = new ResizeAnimation(mediaLayout, param.width, param.height, param.width, new_height);
    	mediaLayout.startAnimation(anim);
    	
		m_viewMediaPlayer.bOpened = !m_viewMediaPlayer.bOpened;
		
		if (m_viewMediaPlayer.bOpened) {
			
			StarTracker.StarSendView(this, "Music Play Close");
			
			m_viewMediaPlayer.btnOpen.setBackgroundResource(R.drawable.media_btn_close);
			
			addHistory("MediaPlayerView", this);
		}
		else {
			
			StarTracker.StarSendView(this, "Music Play Open");
			
			m_viewMediaPlayer.btnOpen.setBackgroundResource(R.drawable.media_btn_open);
			
			removeHistory("MediaPlayerView");
			
			m_prePoint = -1;
		}
    }
    public void removeMediaTool(boolean bRemove) {

		RelativeLayout layoutHome = (RelativeLayout) findViewById(R.id.layoutSubHome);
    	FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) layoutHome.getLayoutParams();

    	
    	if (bRemove) {
    		mediaLayout.setVisibility(View.GONE);
    		
        	param.bottomMargin = 0;
        	layoutHome.setLayoutParams(param);
    	}
    	else {
    		mediaLayout.setVisibility(View.VISIBLE);
        	
    		int bottomMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        	param.bottomMargin = bottomMargin;
        	layoutHome.setLayoutParams(param);
    	}
    }
    
    
    public void clearView() {
    	
    	View shadowLayout = (View) findViewById(R.id.shadowLayout);
    	LinearLayout slideLayout = (LinearLayout) findViewById(R.id.slideLayout);
    	
    	
 	   int childCount = baselayout.getChildCount();
       int i;
       for(i=0; i<childCount; i++) {
           View currentChild = baselayout.getChildAt(i);

           if (currentChild == shadowLayout) {
        	   continue;
           }
           if (currentChild == slideLayout) {
        	   continue;
           }
           
           baselayout.removeView(currentChild);
       }
       
       m_bShowSetting = false;
       
       
        		   
    	if (m_viewAllAccess != null) {
    		m_viewAllAccess.clear();
    		m_viewAllAccess = null;
    	}
    	if (m_viewPhotos != null) {
    		m_viewPhotos.clear();
    		m_viewPhotos = null;
    	}
    	if (m_viewVideos != null) {
    		m_viewVideos.clear();
    		m_viewVideos = null;
    	}
    	
    	if (m_viewPollContest != null) {
    		m_viewPollContest.clear();
    		m_viewPollContest = null;
    	}
    	if (m_viewProfile != null) {
    		m_viewProfile.clear();
    		m_viewProfile = null;
    	}
    	if (m_viewCommnunity != null) {
    		m_viewCommnunity.clear();
    		m_viewCommnunity = null;
    	}   
    	if (m_viewInbox != null) {
    		m_viewInbox.clear();
    		m_viewInbox = null;
    	}
    	if (m_viewRanking != null) {
    		m_viewRanking.clear();
    		m_viewRanking = null;
    	}
    	if (m_viewSetting != null) {
    		m_viewSetting.clear();
    		m_viewSetting = null;
    	}    	
    	if (m_viewHelp != null) {
    		m_viewHelp = null;
    	}    	
    	
    }
    
    public void setTitle(String title) {
    	viewTitle.setText(title);
    }
   
    public void setMenuSelect(int position) {
    	listMenu.performItemClick(listMenu,position,listMenu.getItemIdAtPosition(position));
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    	System.out.println("onActivityResult *******************");
    	
    	setMultiOrientation(false);

    	if (bclickfacebook
    			&& Global.FB.session.onActivityResult(this, requestCode, resultCode, data) &&
    			Global.FB.pendingRequest &&
    			Global.FB.session.getState().isOpened()) {
    		
    		System.out.println("facebook sendREquest ==============");
    		bclickfacebook = false;
    		
//            sendRequests();
            
            return;
        }
    	  
    	if (resultCode == RESULT_OK) {

    		if (requestCode == 101) { // video
        		if (AddVideoView.g_addVideoView != null) {
        			AddVideoView.g_addVideoView.onActivityResult(requestCode, resultCode, data);
        			return;
        		}
        	}
        	else if (requestCode == 100
        			|| requestCode == 1000) { // photo & Aviary
        		if (AddPhotoView.g_addPhotoView != null) {
        			AddPhotoView.g_addPhotoView.onActivityResult(requestCode, resultCode, data);
        			return;
        		}
            	if (ProfileView.g_profileView != null) {
            		ProfileView.g_profileView.onActivityResult(requestCode, resultCode, data);
            		return;
            	}
            	
        	} 
        	else if (requestCode == 102) { // buy activity
        		if (SettingView.g_SettingView != null) {
        			SettingView.g_SettingView.refreshCredit();
        			return;
        		}
        	}
//        	else if (requestCode == 1000) {
//    			shareFacebook(m_fb_post);
//    			return;
//    		}
        	else if (requestCode == 1001) {
        		inviteFriends();
        		return;
        	}

    	}
	}
    
    
	public void onOpenSearch() {
		
		LayoutInflater inflater = LayoutInflater.from(g_HomeActivity);
		m_viewSearch = (SearchUserView) inflater.inflate(R.layout.view_search_user, null);

		FrameLayout layoutHome = (FrameLayout) findViewById(R.id.layoutHome);
		
		layoutHome.addView(m_viewSearch);
		m_viewSearch.init(HomeActivity.this);
    	
    }
	public void onCloseSearch() {
		if (m_viewSearch == null)
			return;
		
		closeKeyboard(g_HomeActivity, m_viewSearch);
		
		FrameLayout layoutHome = (FrameLayout) findViewById(R.id.layoutHome);
		layoutHome.removeView(m_viewSearch);
		
    	m_viewSearch.clear();
    	m_viewSearch = null;
    	
    	removeHistory("SearchUserView");
	}
	
	public void onChooseUser(String userId) {
		
		onCloseSearch();
		
		imageLoader.clearMemoryCache();
		clearView();

		
		mainLayout.removeAllViews();
		
		UserDetailView subView = (UserDetailView) LayoutInflater.from(
				g_HomeActivity).inflate(R.layout.view_user_profile, null);
		mainLayout.addView(subView);
		
		setTitle("");
		setRightBtnItem(false, "");
		
		subView.setFullMode();
		subView.init(g_HomeActivity, userId);
		
		if (this.open) {
			open = false;
			MenuEventController.close(g_HomeActivity, baselayout, viewTitle);
			MenuEventController.closeKeyboard(g_HomeActivity, subView);
		}

		
//		if(!this.open){
//    		this.open = true;
//    		MenuEventController.open(context, this.baselayout, this.viewTitle);
//    		MenuEventController.closeKeyboard(context, subView);
//    	} 
	}


    /*
     *  Push Notification
     */
    
    public Handler receivedHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		super.handleMessage(msg);

    		Bundle mBundle = new Bundle();
    		mBundle = msg.getData();
    		
    		String title = mBundle.getString("msg");
    	    final String pushType = mBundle.getString("push_type");
    		
    	    String messsage = title;
    	    
    		AlertDialog.Builder dialog = new AlertDialog.Builder(g_HomeActivity);
    		
			dialog.setTitle("Notification");
			dialog.setMessage(messsage);
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

//			dialog.setNegativeButton("SHOW", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					
//					dialog.dismiss();
//					
//					if (pushType.equalsIgnoreCase("main_feed")) {
//						baselayout.removeAllViews();
//						((HomeActivity)context).m_prePoint = -1;
//						((HomeActivity)context).setMenuSelect(1);
//					}
//		    	    
//				}
//			});
			dialog.show();
    		
    	}
    };

    
    /*
     *  SignOut
     */
    public static void signOut() {

    	StarTracker.StarRunStatus(g_HomeActivity, StarTracker.DID_SIGNOUT, "Run Status", "Sign Out", "State Change");
    	
    	
    	UserDefault.removeDictionaryForKey("user_info");
		logOutFB();
			
		Intent intent = new Intent(g_HomeActivity, MainLogInActivity.class);
		g_HomeActivity.startActivity(intent);
		((HomeActivity) g_HomeActivity).finish();

    }
    /*
     *  Facebook Login
     */
    public String getApplicationName(){
    	Resources appR = this.getResources(); 
    	CharSequence txt = appR.getText(appR.getIdentifier("app_name", 
    	"string", this.getPackageName()));
    	
    	return txt.toString();
    }
    
    private boolean checkFacebookLogin(){
        Session session = Session.getActiveSession();
        if(session!=null && session.isOpened() ){
            return true;
        }
        return false;
    }
    
    
    boolean m_bInviteFriends = false;
    
    public void inviteFriends() {
    	
    	if (Global.FB.session != null && Global.FB.session.isOpened()) {
    		openDialogFriends();
        }
    	else {
    		
    		StatusCallback callback = new StatusCallback() {
                public void call(Session session, SessionState state, Exception exception) {
                    
                }
            };
            Global.FB.pendingRequest = true;
            Global.FB.session.openForRead(new Session.OpenRequest(this).setCallback(new StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					// TODO Auto-generated method stub
					if (exception != null) {
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("Login failed")
                                .setMessage(exception.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                        Global.FB.session = createSession();
                    } else if (session.isOpened()) {
                    	openDialogFriends();
                    }
				}
			}));
    	}
    	
    }
    
    private void openDialogFriends() {
    	
    	m_bInviteFriends = false;
    	
    	Bundle params = new Bundle();       
        params.putString("title", "Invite Friend");
        params.putString("message", "Please install Enrique App");
        
        // comma seperated list of facebook IDs to preset the recipients. If left out, it will show a Friend Picker.
//        params.putString("to",  userId);  // your friend id

        WebDialog requestsDialog = ( new WebDialog.RequestsDialogBuilder(HomeActivity.this,
                Session.getActiveSession(), params)).setOnCompleteListener(new OnCompleteListener() {
           @Override
           public void onComplete(Bundle values, FacebookException error) {
               //   Auto-generated method stub                     
               if (error != null) {
                   if (error instanceof FacebookOperationCanceledException) {
                       Toast.makeText(HomeActivity.this.getApplicationContext(), 
                               "Request cancelled", Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(HomeActivity.this.getApplicationContext(), 
                               "Network Error",  Toast.LENGTH_SHORT).show();
                   }
               } else {
                   final String requestId = values.getString("request");
                   if (requestId != null) {
                       Toast.makeText(HomeActivity.this.getApplicationContext(), 
                               "Request sent",  Toast.LENGTH_SHORT).show();
                       Log.i("TAG", " onComplete req dia ");                                   
                   } else {
                       Toast.makeText(HomeActivity.this.getApplicationContext(), 
                               "Request cancelled", Toast.LENGTH_SHORT).show();
                   }
               }                   
           }
        }).build();
        requestsDialog.show();
/*        
        Bundle params = new Bundle();
        params.putString("message", "Learn how to make your Android apps social");
        WebDialog requestsDialog = (
            new WebDialog.RequestsDialogBuilder(this,
                Session.getActiveSession(),
                params))
                .setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						// TODO Auto-generated method stub
						 final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            Toast.makeText(getApplicationContext(), 
	                                "Request sent",  
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(getApplicationContext(), 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        }						
					}

                })
                .build();
        requestsDialog.show();
*/
    }
    
    HashMap<String , Object> m_fb_post = null;
    
    
    public void shareFacebook(HashMap<String, Object> postObj) {
    	
    	if (postObj == null) {
    		Const.showMessage("ERR", "PostObj is null", this);
            return;
    	}
    	
//        if (Global.getUserType() != Global.FAN) {
//        	Const.showMessage("", "Mobile admin can not use this function", this);
//            return;
//        }
        
        JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
        int bFacebook = 0;
		try {
			bFacebook = userInfo.getInt("enable_facebook");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (bFacebook == 0) {
        	Const.showMessage("", "Please Enable Social Sharing in Settings", this);
            return;
        }

        showLoading("syndicating...");
        
        m_fb_post = postObj;

        Bitmap image = (Bitmap)postObj.get("IMAGE");
        if (image == null) {
	        Drawable myDrawable = getResources().getDrawable(R.drawable.starclub_logo);
	        image = ((BitmapDrawable) myDrawable).getBitmap();
	        
	        postObj.put("IMAGE", image);
	        postObj.put("IMAGEURL", "");
	        
	        m_fb_post = postObj;

	        publishPhoto();
        }
        else {
            
        	String postType = postObj.get("POSTTYPE").toString();
        	if (postType.equals("video")) {
        		String imageUrl = postObj.get("IMAGEURL").toString();
        		imageUrl += "&icon=1";
        		m_fb_post.put("IMAGEURL", imageUrl);
        		
                AQuery androidAQuery = new AQuery(this);
                androidAQuery.ajax(imageUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                	@Override
                	public void callback(String url, Bitmap object,
                			com.androidquery.callback.AjaxStatus status) {
                		super.callback(url, object, status);
                		Bitmap image = object;
                		
                		m_fb_post.put("IMAGE", image);
                		
                		publishPhoto();
                	}
                });
        	}
        	else {
        		publishPhoto();
        	}
        }
        
        
    	
/*    	
    	if (!FBMainActivity.isLogIn) {
    		m_fb_title = title;
    		m_fb_bmp = image;
    		
			Intent intent = new Intent(HomeActivity.this, FBMainActivity.class);
			startActivityForResult(intent, 1000);
			
		}
		else {
			
	        title += " http://starsite.com/eiapp";

	        if (image == null) {
		        Drawable myDrawable = getResources().getDrawable(R.drawable.starclub_logo);
		        image = ((BitmapDrawable) myDrawable).getBitmap();
	        }
	        
	        if (image != null)
	        	publishPhoto(title, image);
	        else
	        	publishText(title);
	        
	        
	    	if (Global.ENABLE_GOOGLE_ANALYSIS) {
	    		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(Global.GOOGLE_ANALYSIS_KEY);
	   	
	        	tracker.send(MapBuilder.createAppView()
	        			.set(Fields.SCREEN_NAME, "Facebook Share Sent")
	        			.build()
	        			);
	    	}
		}
//    	commentFB = new String[2];
//    	
//    	if (title != null && title.length() > 0)
//    		commentFB[0] = title;
//    	else 
//    		commentFB[0] = getApplicationName();
//    	
//    	if (imgUrl != null && imgUrl.length() > 0)
//    		commentFB[1] = imgUrl;
//
//
//		fbLoginManager = new FBLoginManager(this,
//				R.layout.activity_home, FB.FACEBOOK_ID, FB.PERMISSIONS);
//
//		if (fbLoginManager.existsSavedFacebook()) {
//			fbLoginManager.loadFacebook();
//		} else {
//			fbLoginManager.login();
//			bFBLogin = true;
//		}
 */
        
    }
    
    
    
    private void sendRequests() {
        if (Global.FB.session != null) {
        	
        	m_fb_post.put("PAGEID", "");
        	m_fb_post.put("PAGETOKEN", "");
        	m_fb_post.put("PAGENAME", "");
        	
            Log.d("FB", "groupToken request!");
            Request myreq = Request.newGraphPathRequest(Global.FB.session, "me/accounts",
                    new Request.Callback() {

                        @Override
                        public void onCompleted(Response response) {
                            Log.d("FB", "received token");
                            GraphObject obj = response.getGraphObject();
                            JSONObject json = obj.getInnerJSONObject();
                            
                            try {
                                
                                JSONArray data = json.getJSONArray("data");
                                
                                if (data == null || data.length() == 0) {
                                	previewPostDialog(true);
                                }
                                else {
                            		JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
                            		
                            		String save_pageId = "";
                            		try {
                            			save_pageId = userInfo.getString("facebook_page_id");
                            		} catch (JSONException e) {
                            			// TODO Auto-generated catch block
                            			e.printStackTrace();
                            		}

                            		
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject acc = (JSONObject) data.get(i);
                                        
                                        String pageId = acc.getString("id");
                                        String pageName = acc.getString("name");
                                        String token = acc.getString("access_token");
                                        
                                        if (pageId.equals(save_pageId)) {
                                        	
                                        	m_fb_post.put("PAGEID", pageId);
                                        	m_fb_post.put("PAGETOKEN", token);
                                        	m_fb_post.put("PAGENAME", pageName);

                                            previewPostDialog(false);
                                            return;
                                        }
                                    }
                                    
                                    previewPostDialog(true);
                                    
                                }
                                
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

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
            activeSession = new Session.Builder(this).setApplicationId(MyConstants.FACEBOOK_ID).build();
            Session.setActiveSession(activeSession);
        }
        return activeSession;
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    
    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

    private void previewPostDialog(final boolean bPostWall) {

    	 List<String> permissions = Global.FB.session.getPermissions();
         if (!isSubsetOf(PERMISSIONS, permissions)) {
             Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
             Global.FB.session.requestNewPublishPermissions(newPermissionsRequest);
             return;
         }
    	
    	
    	if (m_bEnableFbCaption == 0) {
    		if (bPostWall) {
				postToWall();
			} else {
				postToPage();
			}
    		return;
    	}
    	
    	
    	View layout = View.inflate(this, R.layout.layout_dialog, null);
    	final EditText tvText = (EditText) layout.findViewById(R.id.etText);
    	ImageView ivImage = (ImageView) layout.findViewById(R.id.ivImage);

    	tvText.setText((String)m_fb_post.get("TEXT"));

    	Bitmap image = (Bitmap)m_fb_post.get("IMAGE");
    	if (image == null) {
        	Drawable myDrawable = getResources().getDrawable(R.drawable.starclub_logo);
            image = ((BitmapDrawable) myDrawable).getBitmap();
    	}
    	ivImage.setImageBitmap(image);
    	
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Facebook Post");
    	alert.setView(layout);
    	alert.setCancelable(false);
    	alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				try {
					facebookPostDone("Facebook share cancelled!");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    	alert.setNegativeButton("Post", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				
				
				String newTitle = tvText.getText().toString();
				m_fb_post.put("TEXT", newTitle);
				
				if (bPostWall) {
					postToWall();
				} else {
					postToPage();
				}
				
				
			}
		});
    	alert.show();
    }
    
    private void postToWall() {

//        Bundle postParams = new Bundle();
//        postParams.putString("message", m_fb_post.get("TEXT").toString());
//        postParams.putParcelable("picture", (Bitmap)m_fb_post.get("IMAGE"));
//        
//        Request request = new Request(session, "me/photos", postParams, HttpMethod.POST,
//                callback);

    	
    	StarTracker.StarSendEvent(this, "App Event", "Facebook Timeline Post", "Fan Action");
    	
    	
        String imageUrl = m_fb_post.get("IMAGEURL").toString();
        if (imageUrl == null || imageUrl.length() < 1) {
            imageUrl = MyConstants.SHARE_APP_LOGO;
        }
        else {
        	imageUrl = MyParser.getStringFromUrl(
        			String.format("http://api.bit.ly/v3/shorten?login=scsocial&apikey=R_ecc44a0059c842039e65b4594f0599c6&longUrl=%s&format=txt", Utils.getUrlEncoded(imageUrl) ));
        	
        	if (imageUrl.indexOf("invalid") > 0) {
        		imageUrl = m_fb_post.get("IMAGEURL").toString();
        	}
        }
        
        String postText = m_fb_post.get("TEXT").toString();
        
        String  appName = getApplicationName();
        
        String link = m_fb_post.get("DEEPLINK").toString();
//        if (link == null || link.length() < 1) {
//        	String postType = m_fb_post.get("POSTTYPE").toString();
//        	String contentId = m_fb_post.get("CONTENTID").toString();
//        	
//        	link = String.format("%s/%s/%s", "http://dev.cms.enrique.starsite.com/enrique/#homefeed", postType, contentId);
//        }
//        String link = String.format("starclub.enrique://%d/%s/%s", Utils.CID, 
//        		m_fb_post.get("POSTTYPE").toString(), m_fb_post.get("CONTENTID").toString());

        Bundle postParams = new Bundle();
        postParams.putString("name", appName);
        postParams.putString("caption", link);
        postParams.putString("description", postText);
        postParams.putString("link", link);
        postParams.putString("picture", imageUrl);
        
        Request request = new Request(Global.FB.session, "me/feed", postParams, HttpMethod.POST,
                callback);

        request.executeAsync();
    }

    private void postToPage() {

    	StarTracker.StarSendEvent(this, "App Event", "Facebook Page Post", "Fan Action");
    	
    	String pageId = m_fb_post.get("PAGEID").toString();
    	String token = m_fb_post.get("PAGETOKEN").toString();
    	String title = m_fb_post.get("TEXT").toString();
    	Bitmap image = (Bitmap)m_fb_post.get("IMAGE");

    	String imageUrl = m_fb_post.get("IMAGEURL").toString();
        if (imageUrl == null || imageUrl.length() < 1) {
            imageUrl = MyConstants.SHARE_APP_LOGO;
        }
        else {
        	imageUrl = MyParser.getStringFromUrl(
        			String.format("http://api.bit.ly/v3/shorten?login=scsocial&apikey=R_ecc44a0059c842039e65b4594f0599c6&longUrl=%s&format=txt", Utils.getUrlEncoded(imageUrl) ));
        	
        	if (imageUrl.indexOf("invalid") > 0) {
        		imageUrl = m_fb_post.get("IMAGEURL").toString();
        	}
        }
        
        String  appName = getApplicationName();
        
        String link = m_fb_post.get("DEEPLINK").toString();
//        if (link == null || link.length() < 1) {
//        	String postType = m_fb_post.get("POSTTYPE").toString();
//        	String contentId = m_fb_post.get("CONTENTID").toString();
//        	
//        	link = String.format("%s/%s/%s", "http://dev.cms.enrique.starsite.com/enrique/#homefeed", postType, contentId);
//        }
//        String link = String.format("starclub.enrique://%d/%s/%s", Utils.CID, 
//        		m_fb_post.get("POSTTYPE").toString(), m_fb_post.get("CONTENTID").toString());
        
        
        Bundle postParams = new Bundle();
        postParams.putString("access_token", token);
        postParams.putString("message", title);
        postParams.putString("name", appName);
        postParams.putString("caption", link);
//        postParams.putString("description", title);
        postParams.putString("link", link);
        postParams.putString("picture", imageUrl);
//        postParams.putParcelable("source", image);
        
        Request request = new Request(Global.FB.session, 
        		pageId + "/feed", postParams, HttpMethod.POST,
                callback);

        
/*    	
        Bundle postParams = new Bundle();
        postParams.putString("message", title);
        postParams.putParcelable("source", image);
        postParams.putString("access_token", token);
        
        Request request = new Request(session, 
        		pageId + "/photos", postParams, HttpMethod.POST,
                callback);
*/
        request.executeAsync();
    }
    
    private Request.Callback callback = new Request.Callback() {
        public void onCompleted(Response response) {
            Log.d("FB", "get response!");
            FacebookRequestError error = response.getError();
            if (error == null) {
                JSONObject graphResponse = response.getGraphObject()
                        .getInnerJSONObject();
                String postId = null;
                try {
                    postId = graphResponse.getString("id");
                    Log.d("FB", postId);
                    
                    String pageName = m_fb_post.get("PAGENAME").toString();
                    String msg = "Syndicated to Facebook!";
                    if (pageName.equals("")) {
                    	msg = "Syndicated to Facebook!";
                    }
                    else {
                    	msg = "Syndicated to " + pageName + " page";
                    }
                    
                    facebookPostDone(msg);
                    
                } catch (JSONException e) {
                    Log.d("FB error", "JSON error " + e.getMessage());
                    
                    try {
    					facebookPostDone("Facebook share error");
    				} catch (JSONException er) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }

            } else {
                Log.d("FB error", error.getErrorMessage()); //HERE I'M RECEIVING AN ERROR
                try {
					facebookPostDone("Facebook share failed");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

    };
    
//    public void shareFacebook(String title, Bitmap image) {
//    	
//    	if (!FBMainActivity.isLogIn) {
//			Intent intent = new Intent(HomeActivity.this, FBMainActivity.class);
//			startActivity(intent);
//		}
//		else {
//			
//	        title += " http://starsite.com/eiapp";
//	        		
//	        if (image != null)
//	        	publishPhoto(title, image);
//	        else
//	        	publishText(title);
//	        
//		}
//    }
    
    public void shareTwitter(HashMap<String, Object> postObj) {
        
//    	if (Global.getUserType() != Global.FAN) {
//        	Const.showMessage("", "Mobile admin can not use this function", this);
//            return;
//        }
        
        JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
        int nTwitter = 0;
		try {
			nTwitter = userInfo.getInt("enable_twitter");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (nTwitter == 0) {
        	Const.showMessage("", "Please Enable Social Sharing in Settings", this);
            return;
        }

        m_fb_post = postObj;
        
        showLoading("syndicating...");
        
        Bitmap image = (Bitmap)postObj.get("IMAGE");
        if (image == null) {
	        Drawable myDrawable = getResources().getDrawable(R.drawable.starclub_logo);
	        image = ((BitmapDrawable) myDrawable).getBitmap();
	        
	        postObj.put("IMAGE", image);
	        postObj.put("IMAGEURL", "");
	        
	        m_fb_post = postObj;

	        previewPostTWDialog();
        }
        else {
            
        	String postType = postObj.get("POSTTYPE").toString();
        	if (postType.equals("video")) {
        		String imageUrl = postObj.get("IMAGEURL").toString();
        		imageUrl += "&icon=1";
        		m_fb_post.put("IMAGEURL", imageUrl);
        		
                AQuery androidAQuery = new AQuery(this);
                androidAQuery.ajax(imageUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                	@Override
                	public void callback(String url, Bitmap object,
                			com.androidquery.callback.AjaxStatus status) {
                		super.callback(url, object, status);
                		Bitmap image = object;
                		
                		m_fb_post.put("IMAGE", image);
                		
                		previewPostTWDialog();
                	}
                });
        	}
        	else {
        		previewPostTWDialog();
        	}
        }
    	
    }
    
    private void previewPostTWDialog() {

    	if (m_bEnableTWCaption == 0) {
    		postToTW();
    		return;
    	}
    	
    	
    	View layout = View.inflate(this, R.layout.layout_dialog, null);
    	final EditText tvText = (EditText) layout.findViewById(R.id.etText);
    	ImageView ivImage = (ImageView) layout.findViewById(R.id.ivImage);

    	tvText.setText((String)m_fb_post.get("TEXT"));
    	
    	Bitmap image = (Bitmap)m_fb_post.get("IMAGE");
    	if (image == null) {
        	Drawable myDrawable = getResources().getDrawable(R.drawable.starclub_logo);
            image = ((BitmapDrawable) myDrawable).getBitmap();
    	}
    	ivImage.setImageBitmap(image);
    	
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Twitter Post");
    	alert.setView(layout);
    	alert.setCancelable(false);
    	alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				try {
					twitterPostDone("Twitter share cancelled!");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    	alert.setNegativeButton("Post", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				String newTitle = tvText.getText().toString();
				m_fb_post.put("TEXT", newTitle);
				
				postToTW();
				
				dialog.dismiss();
			}
		});
    	alert.show();
    }
    
    private void postToTW() {
    	String comment[] = new String[2];
    	
    	String title = m_fb_post.get("TEXT").toString();
		String imgUrl = m_fb_post.get("IMAGEURL").toString();
		
		int maxLength = 68;
		if (title.length() > maxLength) {
			title = title.substring(0, maxLength);
		}
		
        String postType = m_fb_post.get("POSTTYPE").toString();
        String deeplink = m_fb_post.get("DEEPLINK").toString();
        
        if (postType.equalsIgnoreCase("photo")) {
        	String urllink = "";
        	try {
        		 urllink = m_fb_post.get("url_link").toString();
        	} catch (Exception e) {
        		
        	}
        	
        	if (urllink.length() > 0) {
        		deeplink = urllink;
        	}
        }
        
        title = title + "\n\n" + deeplink;
        
		
        if (imgUrl == null || imgUrl.length() < 1) {
        	imgUrl = MyConstants.SHARE_APP_LOGO;
        }
        else {
//        	String postType = m_fb_post.get("POSTTYPE").toString();
//        	if (postType.equals("video")) {
//        		imgUrl += "&icon=1";
//        	}
        	
        	imgUrl = MyParser.getStringFromUrl(
        			String.format("http://api.bit.ly/v3/shorten?login=scsocial&apikey=R_ecc44a0059c842039e65b4594f0599c6&longUrl=%s&format=txt", 
        					Utils.getUrlEncoded(imgUrl)));
        }
		
    	
    	if (title != null && title.length() > 0)
    		comment[0] = title;
    	else 
    		comment[0] = getApplicationName();
    	
    	if (imgUrl != null && imgUrl.length() > 0)
    		comment[1] = imgUrl;
    	
    	Twitt twitt = new Twitt(this, TwitterApp.CONSUMER_KEY, TwitterApp.CONSUMER_SECRET);
    	twitt.shareToTwitter(comment);
    	
    	StarTracker.StarSendEvent(this, "App Event", "Twitter Post", "Fan Action");
    }
    
    
    
    public void shareInstagram(HashMap<String, Object> postObj) {
    	
    	boolean instalado = false;

        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.instagram.android", 0);
            instalado = true;
        } catch (NameNotFoundException e) {
            instalado = false;
        }

        if (instalado == false) {
        	Const.showMessage("", "You have to install Instagram app", this);
        	return;
        }
        
        showLoading("syndicating...");
        
//        Bitmap bitmap = (Bitmap) postObj.get("IMAGE");
        
		String imageUrl = postObj.get("IMAGEURL").toString();
		String postType = postObj.get("POSTTYPE").toString();
		if (postType.equals("video"))
			imageUrl += "&icon=1";
		
		
        AQuery androidAQuery = new AQuery(this);
        androidAQuery.ajax(imageUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
        	@Override
        	public void callback(String url, Bitmap object,
        			com.androidquery.callback.AjaxStatus status) {
        		super.callback(url, object, status);
        		
        		Bitmap bitmap = object;
        		
        		
        		Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.instagram.android");
                if (bitmap != null) {
                	shareIntent.setType("image/png");
        	    	String path = Images.Media.insertImage(getContentResolver(), bitmap, "image", null);
        	        Uri uri = Uri.parse(path);
        	        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        		}

                hideLoading();
                
                startActivity(shareIntent);
                
                StarTracker.StarSendEvent(g_HomeActivity, "App Event", "Instagram Post", "Fan Action");
        	}
        });

    }
    
    public void showLoading(String msg) {
    	if (progress == null) {
    		progress = new ProgressDialog(this);
    		progress.setCancelable(true);
    		progress.setMessage(msg);

        	progress.show();
    	}
    }
    public void hideLoading() {
    	if (progress != null) {
    		progress.hide();
    		progress = null;
    	}
    }
    
    public void facebookPostDone(String msg) throws JSONException {
    	
    	hideLoading();
    	
    	Const.showCustomToastMessage(msg, g_HomeActivity);
    	
    	if (!m_bDraftShare) return;
    	
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
        
		final int	bTwitter = userInfo.getInt("enable_twitter");
		final int	bInstagram = userInfo.getInt("enable_instagram");
		
        if (bTwitter == 1) {
			shareTwitter(m_fb_post);
		}
		else if (bInstagram == 1) {
			shareInstagram(m_fb_post);
		}
        
//		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//		dialog.setTitle("Do you want to share to Twitter?");
//		dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//
//				dialog.cancel();
//				
//				gotoInstagram();
//			}
//		});
//
//		dialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//				dialog.cancel();
//				
//				shareTwitter(m_fb_post);
//				
//			}
//		});
//		
//		dialog.show();

    }
    public void twitterPostDone(String msg) throws JSONException {
    	
    	hideLoading();
    	
    	
    	Const.showCustomToastMessage(msg, g_HomeActivity);
    	
    	if (!m_bDraftShare) return;
    	
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
        
		final int	bInstagram = userInfo.getInt("enable_instagram");
		
		if (bInstagram == 1) {
			shareInstagram(m_fb_post);
		}
		
//    	Thread thread=  new Thread(){
//            @Override
//            public void run(){
//                try {
//                    synchronized(this){
//                        wait(1000);
//                    }
//                }
//                catch(InterruptedException ex){                    
//                }
//
//        		
//            }
//        };
//        thread.start();  
        
    }
   
	public static void logOutFB() {
		
		Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    } else {

//	        session = new Session(g_HomeActivity);
//	        Session.setActiveSession(session);
//
//	        session.closeAndClearTokenInformation();
	            //clear your preferences if saved

	    }

	}
	
	
	
	public void publishPhoto() {
		
		bclickfacebook = true;
		
		if (Global.FB.session != null && Global.FB.session.isOpened()) {
            sendRequests();
        } 
        else
        {

        	if (Global.FB.session == null)
        		Global.FB.session = createSession();
        	
        	Global.FB.pendingRequest = true;
             
             Session.OpenRequest openRequest = null;
             openRequest = new Session.OpenRequest(this);

             if (openRequest != null) {
                 openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
                 openRequest.setPermissions(Arrays.asList("publish_actions",
                       "manage_pages", "status_update"));
                 openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);

                 Global.FB.session.openForPublish(openRequest.setCallback(new StatusCallback() {
					
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						// TODO Auto-generated method stub
						if (exception != null) {
	                         new AlertDialog.Builder(HomeActivity.this)
	                                 .setTitle("Login failed")
	                                 .setMessage(exception.getMessage())
	                                 .setPositiveButton("OK", null)
	                                 .show();
	                         Global.FB.session = createSession();
	                         
	                         hideLoading();
	                         
	                     } else if (session.isOpened()) {
	                         sendRequests();
	                     }
					}
				}));
             }	
        
        }
	}
	
	/*
	 *  Email Send
	 */
	public void sendEmail(String[] emailaddress, String message, Bitmap bitmap) {
		
		String subject = "Report";
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		
		if (bitmap != null) {
			emailIntent.setType("image/png");
	    	String path = Images.Media.insertImage(getContentResolver(), bitmap, "image", null);
	        Uri uri = Uri.parse(path);
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		}
		
		startActivity(Intent.createChooser(emailIntent, "Send email..."));
		
    	StarTracker.StarSendEvent(this, "App Event", "Report Email", "Fan Action");
	}
	
	public int getScreenWidth() {
		DisplayMetrics dimension = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimension);
        int screen_width = dimension.widthPixels;
        int screen_height = dimension.heightPixels;

        return screen_width;
	}
	/*
	 *  Memory Management
	 */
	public static void logHeap() {
        Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
        Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
        Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        System.out.println("debug. =================================");
        System.out.println("debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
        System.out.println("debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
    }

	public int getVersion() {
        try {
            	PackageInfo pInfo = g_HomeActivity.getPackageManager().getPackageInfo(g_HomeActivity.getPackageName(), PackageManager.GET_META_DATA);
            	return pInfo.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
	}
	
	public String getVersionName() {
        try {
            	PackageInfo pInfo = g_HomeActivity.getPackageManager().getPackageInfo(g_HomeActivity.getPackageName(), PackageManager.GET_META_DATA);
            	return pInfo.versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
	}

	
	public static void closeKeyboard(final Context context, final View view){
    	InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
	public static void showKeyboard(final Context context, final View view) {

		view.requestFocusFromTouch();
		view.requestFocus();
		
		view.post(new Runnable() {
		    public void run() {
		        InputMethodManager lManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		        lManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		    }
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		
		System.out.println("Configuration changed ****************************");
	}
	
}