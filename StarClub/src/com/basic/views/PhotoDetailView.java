package com.basic.views;


import java.net.URL;
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
import com.mycom.lib.Const;
import com.mycom.lib.gesture.imageview.GestureImageView;
import com.mycom.lib.gesture.imageview.PanAndZoomListener;
import com.mycom.lib.gesture.imageview.PanAndZoomListener.Anchor;
import com.mycom.lib.gesture.imageview.PanZoomView;
import com.network.httpclient.JsonParser;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;




public class PhotoDetailView extends BaseView {
	
	public Context m_context;
	public static PhotoDetailView g_PhotoDetailView = null;
				
	ProgressDialog progress = null;

	private TextView  tvComment;
	
	private JSONObject m_dic;
	
	
	boolean m_isFullMode = false;
	private FrameLayout layoutTitle = null;
	private LinearLayout layoutBottom = null;
	
	
	GestureImageView ivPhoto = null;
	TextView tvTitle = null;
	

	public PhotoDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public PhotoDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoDetailView(Context context) {
        super(context);
    }
    
    
    public void init(Context context, JSONObject feed) {
    	m_context = context;
		g_PhotoDetailView = this;
		m_dic = feed;
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		
    	StarTracker.StarSendView(m_context, "Photo Deatail");


		((HomeActivity)m_context).setMultiOrientation(true);
		((HomeActivity)m_context).removeMediaTool(true);
		
		
		((HomeActivity)m_context).imageLoader.clearMemoryCache();
		
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    	layoutTitle = (FrameLayout) findViewById(R.id.titleLayout);
    	layoutBottom = (LinearLayout) findViewById(R.id.bottomLayout);
    	
    	
		
    	ivPhoto = (GestureImageView) findViewById(R.id.imagePhoto);
//    	final ImageView ivPhoto = (ImageView) findViewById(R.id.imagePhoto);
//    	ivPhoto.setScaleType(ScaleType.FIT_CENTER);
    	
    	final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressPhoto);
    	String imgUrl = "";
		try {
			imgUrl = m_dic.getString("fullimage_path");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("full image = " + imgUrl);
		
		
		((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivPhoto, ((HomeActivity)m_context).optFull, new SimpleImageLoadingListener() {
			 @Override
			 public void onLoadingStarted(String imageUri, View view) {
				 System.out.println("Loading start");
				 
				 progressBar.setProgress(0);
				 progressBar.setVisibility(View.VISIBLE);
			 }

			 @Override
			 public void onLoadingFailed(String imageUri, View view,
					 FailReason failReason) {
				 System.out.println("Loading failed");
				 
				 progressBar.setVisibility(View.GONE);
			 }

			 @Override
			 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				 	System.out.println("Loading Complete +" + loadedImage.getHeight());
					
				 	progressBar.setVisibility(View.GONE);

//				 	ivPhoto.setOnTouchListener(new PanAndZoomListener(g_PhotoDetailView, ivPhoto, Anchor.TOPLEFT));

			 }
		 }, new ImageLoadingProgressListener() {
			 @Override
			 public void onProgressUpdate(String imageUri, View view, int current,
					 int total) {
				 progressBar.setProgress(Math.round(100.0f * current / total));
				 System.out.println("image loading ... " + current);
			 }
		 }
		);	
		 
		 
		ivPhoto.setClickable(true);
		ivPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (m_isFullMode) {
					layoutTitle.setVisibility(View.VISIBLE);
					layoutBottom.setVisibility(View.VISIBLE);
					m_isFullMode = false;
				}
				else {
					layoutTitle.setVisibility(View.GONE);
					layoutBottom.setVisibility(View.GONE);
					m_isFullMode = true;
				}
			}
		});

		
		
		
    	tvTitle = (TextView) findViewById(R.id.tvTitle);
    	String title = "";
		try {
			title = m_dic.getString("caption");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	tvTitle.setText(title);
    	
    	tvComment = (TextView) findViewById(R.id.txtComment);
    	
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

    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Loading....");

    	progress.show();
    	
    	
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
			
			if (progress != null) {
				progress.hide();
				progress = null;
			}
			
			switch (msg.what) {
			case 0:
				int count = msg.arg1;
				setCommentText(count);
				break;
				
			case -1:
				Const.showMessage("", "Network failed.", (HomeActivity)m_context);
				break;
			}
			
		}
    };

    private void setCommentText(int count){
    	
    	if (count == 0) {
    		tvComment.setText("No Comments");
    	} else if (count == 1) {
    		tvComment.setText("1 Comment");
    	} else {
    		tvComment.setText(count + " Comments");
    	}

    }

    
    private void clear() {
    	m_context = null;
    	g_PhotoDetailView = null;
    	
    	progress = null;

    	tvComment = null;
    	
    	m_dic = null;
    }
    public void onBack() {
    
		((HomeActivity)m_context).removeMediaTool(false);
		((HomeActivity)m_context).setMultiOrientation(false);

		((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
		
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_PhotoDetailView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_PhotoDetailView);
    	    	clear();
    	    }
    	});
    }
    
    private void onClickComment() {
    	
    	CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
    	subView.init(m_context, m_dic, -1, false, Global.OPT_PHOTODETAIL);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);

    }
  
    public void onClickAddComment(View btn) {
 		
    	CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
    	subView.init(m_context, m_dic, -1, true, Global.OPT_PHOTODETAIL);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);
 	}

 	public void onCommentDone(int index, JSONArray arrComments, int totalcomment) {
 		setCommentText(totalcomment);
 	}

 	
 	
 	public void onClickShare(View btn) throws JSONException {
 		String contentId = "";
 		try {
	 			contentId = m_dic.getString("id");
	 		} catch (JSONException e) {
	 			e.printStackTrace();
	 		}
 		m_dic.put("content_id", contentId);
 		m_dic.put("post_type", "photo");
 		
		onSharePage(m_context, m_dic, false);
		return;
		
/*		
		final String items[] = {"Facebook", "Twitter", "Instagram"};
 		
 		AlertDialog.Builder ab=new AlertDialog.Builder((HomeActivity)m_context);
 		ab.setTitle("Sharing");
 		ab.setItems(items, new DialogInterface.OnClickListener() {

 			public void onClick(DialogInterface d, int choice) {
 				
	 			String text = tvTitle.getText().toString();
	 			String imageUrl = "";
	 			String postType = "photo";
	 			String contentId = "";
	 			String deeplink = "";
	 			Bitmap image = ((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
	 			
	 	 		try {
	 	 			imageUrl =  m_dic.getString("img_url");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
	 	 		
	 	 		try {
	 	 			contentId = m_dic.getString("id");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
	 	 		
	 	 		try {
	 	 			deeplink = m_dic.getString("deep_link_web");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
				
	 	 		
	 	 		HashMap<String, Object> postObj = new HashMap<String, Object>();
		 		postObj.put("TEXT", text);
		 		if(image != null) {
		 		    	postObj.put("IMAGE", image);
		 		    	postObj.put("IMAGEURL", imageUrl);
		 		}
		 		postObj.put("POSTTYPE", postType);
	 		    postObj.put("CONTENTID", contentId);
	 		    postObj.put("DEEPLINK", deeplink);
	 		    
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
*/ 		 
 	}
}
