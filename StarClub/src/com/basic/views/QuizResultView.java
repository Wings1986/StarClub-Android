package com.basic.views;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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




public class QuizResultView extends BaseView {
	
	public Context m_context;
	public static QuizResultView g_quizResultView;
	
	
	private JSONObject m_dicFeed = null;
	
	
	int m_nTotal;
	int m_nResult;	
	
	public QuizResultView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public QuizResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuizResultView(Context context) {
        super(context);
    }
    
    public void init(Context context, JSONObject dicFeed, int total, int result) {
    	m_context = context;
		g_quizResultView = this;

		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		
		m_nTotal = total;
		m_nResult = result;

		m_dicFeed = dicFeed;
		
		StarTracker.StarSendView(m_context, "Quiz");
    	
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

    	TextView tvResult = (TextView) findViewById(R.id.tvResult);
    	tvResult.setText("" + m_nResult);
    
    	WebView mWebView = (WebView) findViewById(R.id.wvView);
    	mWebView.setBackgroundColor(0x00000000);
    	
    	String end_description = null;
		try {
			end_description = m_dicFeed.getString("end_description");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	mWebView.loadUrl(end_description);
    	
    	setBackground();

    	postResultQuiz();
    }
    
    private void postResultQuiz() {
    	
//    	if (m_nResult == m_nTotal) 
    	{
    		
    		new Thread(new Runnable() {
    			
    			public void run() {
    				
    				try {

    				    String quizId = m_dicFeed.getString("id");

    					JSONObject data = JsonParser.getQuizAnswer(quizId, m_nTotal, m_nResult);

    					return;

    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    		}).start();
        }

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
 	 public void clear () {
     	((HomeActivity)m_context).imageLoader.clearMemoryCache();
     	
     	m_context = null;
    	g_quizResultView = null;
     }
 	
    public void onBack() {

    	QuizView.g_QuizView.removeQuizView();

    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());

    	((HomeActivity)m_context).removeHistory("QuizView");
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_quizResultView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_quizResultView);
    	    	clear();
    	    	
    	    	if (m_nResult == m_nTotal) {
    	    		try {
    	    			PollContestView.g_PollContestView.initData();
    	    		} catch (Exception e) {}
    	    	}
    	    	
    	    }
    	});
    }
    
    private void onClickNext(){
//    	final String items[] = {"Facebook", "Twitter", "Instagram"};
// 		
// 		AlertDialog.Builder ab=new AlertDialog.Builder((HomeActivity)m_context);
// 		ab.setTitle("Sharing");
// 		ab.setItems(items, new DialogInterface.OnClickListener() {
//
// 			public void onClick(DialogInterface d, int choice) {
// 				
// 				String text = "You answered " + (m_nResult + " / " + m_nTotal) + "questions correctly";
// 				
//	 	 		HashMap<String, Object> postObj = new HashMap<String, Object>();
//	 		    postObj.put("TEXT", text);
//	 		    
//	 		   if (choice == 0) {
//	 		    	((HomeActivity)m_context).shareFacebook(postObj);	
//	 		   } else if (choice == 1) {
//	 			   ((HomeActivity)m_context).shareTwitter(postObj);
//	 		   } else {
//	 			   ((HomeActivity)m_context).shareInstagram(postObj);
//	 		   }
//	 		   
// 		 	}
// 		 });
// 		 ab.show();
    }
  
   
}
