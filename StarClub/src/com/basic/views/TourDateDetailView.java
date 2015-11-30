package com.basic.views;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;





import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.lib.Const;
import com.network.httpclient.HttpConnectionUtil;
import com.network.httpclient.JsonParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;




public class TourDateDetailView extends BaseView {
	
	public Context m_context;
	public static TourDateDetailView g_TourDateDetailView = null;
	
	ProgressDialog progress = null;

	private TextView  tvComment;
	
	private JSONObject m_dic;
	
	
	public TourDateDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public TourDateDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TourDateDetailView(Context context) {
        super(context);
    }

    public void init(Context context, JSONObject feed) {
    	m_context = context;
		g_TourDateDetailView = this;
		m_dic = feed;
		
		
		((HomeActivity)m_context).imageLoader.clearMemoryCache();
		
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    	Button btnBuy = (Button) findViewById(R.id.btnRight);
    	btnBuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
    	
		
    	
    	TextView tvLocation = (TextView) findViewById(R.id.tvLocation);
    	String value = "";
    	
    	try {
			value = m_dic.getString("position");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	tvLocation.setText(value);
    	
    	TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
    	try {
			value = m_dic.getString("title");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	tvTitle.setText(value);
    	
    	TextView tvTime = (TextView) findViewById(R.id.tvTime);
    	try {
			value = m_dic.getString("start_date");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	tvTime.setText(TourDateView.getLongTime(value));
    	
    	TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
    	tvDescription.setText("Descriptions...");
    	
    	
    	tvComment = (TextView) findViewById(R.id.txtComment);
    	tvComment.setText("0 Comment");
    	
    	LinearLayout layoutComment = (LinearLayout) findViewById(R.id.layoutComment);
    	layoutComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				onClickComment();
			}
		});

    	LinearLayout layoutAddComment = (LinearLayout) findViewById(R.id.layoutAddComment);
    	layoutAddComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickAddComment( v);
			}
		});
		
    	LinearLayout layoutShare = (LinearLayout) findViewById(R.id.layoutShare);
        layoutShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					onClickShare(v);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    	

    	
    	getExtraInfo();
    	
    }
    
    public void getExtraInfo () {

		new Thread(new Runnable() {
			
			public void run() {
				getDataServer();
			}
		}).start();
    }
    
    public void getDataServer() {

		try {

	    	String postType = m_dic.getString("post_type");
	    	String contentId = m_dic.getString("content_id");
	    			
			JSONObject data = JsonParser.getExtraInfo(postType, contentId);


			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}

			JSONArray arrComments = data.getJSONArray("comments");
			
			Message msg = new Message();
			msg.arg1 = arrComments.length();
			msg.what = 0;
			
			m_handler.sendMessage(msg);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case 0:
				int count = msg.arg1;
				setCommentText(count);
				break;
				
			case -1:
				Const.showMessage("", "Network is failed.", (HomeActivity)m_context);
				break;
			}
			
		}
    };
    
    private void clear () {
    	m_context = null;
    	g_TourDateDetailView = null;
    	
    	progress = null;

    	tvComment = null;
    	
    	m_dic = null;
    }
    
    private void setCommentText(int count) {
    	tvComment.setText(count + " Comments");
    }
    
    private void onBack() {
    
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_TourDateDetailView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	((HomeActivity)m_context).baselayout.removeView(g_TourDateDetailView);    
    	    	clear();
    	    }
    	});
    }
    
    private void onClickComment() {
    	
//    	CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
//    	subView.init(m_context, m_dic, Global.OPT_TOURDATE_DETAIL);
//    	
//    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//    	        ViewGroup.LayoutParams.FILL_PARENT,
//    	        ViewGroup.LayoutParams.FILL_PARENT); 
//    	((HomeActivity)m_context).baselayout.addView(subView, params);
//    	
//    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
//    	subView.startAnimation(out);

    }
    
 public void onClickAddComment(View btn) {
 		
//		AddCommentView subView = (AddCommentView) LayoutInflater.from(m_context).inflate(R.layout.view_add_comment, null);
//    	subView.init(m_context, m_dic, Global.OPT_TOURDATE_DETAIL);
//    	
//    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//    	        ViewGroup.LayoutParams.FILL_PARENT,
//    	        ViewGroup.LayoutParams.FILL_PARENT); 
//    	((HomeActivity)m_context).baselayout.addView(subView, params);
//    	
//    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
//    	subView.startAnimation(out);
 	}

 	public void onClickShare(View btn) throws JSONException {
 		
		final String items[] = {"Facebook", "Twitter", "Instagram"};
 		
 		AlertDialog.Builder ab=new AlertDialog.Builder((HomeActivity)m_context);
 		ab.setTitle("Sharing");
 		ab.setItems(items, new DialogInterface.OnClickListener() {

 			public void onClick(DialogInterface d, int choice) {
 				
 			}
 		 });
 		 ab.show();
 	}
}

