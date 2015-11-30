package com.basic.views;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.custom.items.Item;
import com.custom.items.ItemImage;
import com.custom.items.RowType;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.mycom.data.MyJSON;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;





import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;




public class PollsResultView extends BaseView {
	
	public Context m_context;
	public static PollsResultView g_pollsResultView;
	
	ProgressDialog progress = null;

	String m_strAnswerId = "";
	String m_strQuestionId = "";
	JSONObject m_dicFeed = null;
	boolean m_bFinish;
	
	JSONArray m_arrRating = null;
	
	LinearLayout layoutResult = null;
	
	
	public PollsResultView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public PollsResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PollsResultView(Context context) {
        super(context);
    }
    
    public void init(Context context, JSONObject dicFeed, String answerId, String questionId, boolean finish) {
    	m_context = context;
		g_pollsResultView = this;

		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		m_strAnswerId = answerId;
		m_strQuestionId = questionId;

		m_dicFeed = dicFeed;
		m_bFinish = finish;

		
    	StarTracker.StarSendView(m_context, "Poll");
		
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    	Button btnNext = (Button) findViewById(R.id.btnNext);
    	btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				onClickNext();
				
			}
		});
		
    	layoutResult = (LinearLayout) findViewById(R.id.layoutResult);
    	
    	setBackground();
    	
    	
    	getResult();
    }
    
    private void setBackground() {
    	String backImageUrl = "";
		try {
			backImageUrl = m_dicFeed.getString("background_image_path");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	int ivWidth = 0;
		try {
			ivWidth = m_dicFeed.getInt("background_img_width");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int ivHeight = 0;
		try {
			ivHeight = m_dicFeed.getInt("background_img_height");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		ImageView ivBackground = (ImageView)findViewById(R.id.ivBackground);
		
		FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) ivBackground.getLayoutParams();
    	int virturlHeight = 320;
    	if (ivWidth != 0 && ivHeight != 0)
    		virturlHeight= ((HomeActivity)m_context).getScreenWidth() * ivHeight / ivWidth;
    	param.height = virturlHeight;
    	ivBackground.setLayoutParams(param);
        
    	((HomeActivity)m_context).imageLoader.displayImage(backImageUrl, ivBackground, ((HomeActivity)m_context).optFull, null);
    }
	
    private void getResult () {

		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Loading....");

    	progress.show();

    	
		new Thread(new Runnable() {
			
			public void run() {
				
				try {

					JSONObject data = JsonParser.getPollAnswer(m_strAnswerId, m_strQuestionId);

					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					m_arrRating = data.getJSONArray("ratings");
					
					m_handler.sendEmptyMessage(0);
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
     
   
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progress.hide();
			
			switch (msg.what) {
			case 0:
				
				try {
					refresh();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case -1:
				Const.showMessage("", "Network failed.", (HomeActivity)m_context);
				break;
			}
			
		}
    };
    
    private void refresh() throws JSONException {

    	for (int i = 0 ; i < m_arrRating.length() ; i ++) {
    		JSONObject rating = m_arrRating.getJSONObject(i);
    		
    		String name = rating.getString("name");
    		double percent = rating.getDouble("percent");
    		
    		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
    	            ViewGroup.LayoutParams.MATCH_PARENT,
    	            ViewGroup.LayoutParams.WRAP_CONTENT);
    		layoutParams.leftMargin = 30;
    		layoutParams.rightMargin = 30;
    		layoutParams.topMargin = 20;
    		layoutParams.bottomMargin = 20;
    		LinearLayout layoutOne = new LinearLayout(m_context);
    		layoutOne.setLayoutParams(layoutParams);
    		layoutOne.setOrientation(LinearLayout.HORIZONTAL);
    		layoutOne.setGravity(Gravity.CENTER_VERTICAL);
    		
    		
    		TextView textName = new TextView(m_context);
    		LinearLayout.LayoutParams paramText = new LinearLayout.LayoutParams(
    				150, ViewGroup.LayoutParams.WRAP_CONTENT);
    		textName.setLayoutParams(paramText);
    		textName.setText(name);
    		textName.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
    		layoutOne.addView(textName);
    		
    		
    		ProgressBar progressBar = new ProgressBar(m_context, null, android.R.attr.progressBarStyleHorizontal);
    		LinearLayout.LayoutParams paramProgress = new LinearLayout.LayoutParams(
    				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    		paramProgress.weight = 1;
    		paramProgress.leftMargin = 10;
    		paramProgress.rightMargin = 10;
    		progressBar.setLayoutParams(paramProgress);
    		progressBar.setProgress((int) percent);
    		
    		Resources res = getResources();
    	    Rect bounds = progressBar.getProgressDrawable().getBounds();
    	    progressBar.setBackgroundColor(Color.TRANSPARENT);
    	    progressBar.setProgressDrawable(res.getDrawable(R.drawable.greenpregressbar));
    	    progressBar.getProgressDrawable().setBounds(bounds);
//    		progressBar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.SRC_IN);
//    		progressBar.getProgressDrawable().setLevel((int) percent);
//    		progressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
    		layoutOne.addView(progressBar);
    		
    		
    		TextView textValue = new TextView(m_context);
    		LinearLayout.LayoutParams paramValue = new LinearLayout.LayoutParams(
    				80, ViewGroup.LayoutParams.WRAP_CONTENT);
    		textValue.setLayoutParams(paramValue);
    		textValue.setText((int)percent + "%");
    		textValue.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
    		layoutOne.addView(textValue);
    		
    		layoutResult.addView(layoutOne);
    	            
    	}
    	
    }
  


    
 	 public void clear () {
     	((HomeActivity)m_context).imageLoader.clearMemoryCache();
     	
     	m_context = null;
    	g_pollsResultView = null;
    	
    	progress = null;
     	
     }
 	
    public void onBack() {

    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());

    	if (m_bFinish) {
        	PollsView.g_PollsView.removePollsView();
        	((HomeActivity)m_context).removeHistory("PollsView");
		} else {
			PollsView.g_PollsView.refresh();
		}
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_pollsResultView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_pollsResultView);
    	    	clear();
    	    }
    	});
    }
    
    private void onClickNext(){
    	
    	final String items[] = {"Facebook", "Twitter", "Instagram"};
 		
 		AlertDialog.Builder ab=new AlertDialog.Builder((HomeActivity)m_context);
 		ab.setTitle("Sharing");
 		ab.setItems(items, new DialogInterface.OnClickListener() {

 			public void onClick(DialogInterface d, int choice) {
 				
	 			String text = "";

	 			try {
	 			for (int i = 0 ; i < m_arrRating.length() ; i ++) {
	 				JSONObject rating = m_arrRating.getJSONObject(i);
	 	    		
	 	    		String name = rating.getString("name");
	 	    		double percent = rating.getDouble("percent");
	 	    		
	 	    		text += "name = " + name + " percent = " + percent;
	 			}
	 			} catch (Exception e) {}
	 	 		
	 	 		HashMap<String, Object> postObj = new HashMap<String, Object>();
	 		    postObj.put("TEXT", text);
	 		    
	 		   if (choice == 0) {
	 		    	((HomeActivity)m_context).shareFacebook(postObj);	
	 		   } else if (choice == 1) {
	 			   ((HomeActivity)m_context).shareTwitter(postObj);
	 		   } else {
	 			   ((HomeActivity)m_context).shareInstagram(postObj);
	 		   }
	 		   
 		 	}
 		 });
 		 ab.show();
    }
  
   
}
