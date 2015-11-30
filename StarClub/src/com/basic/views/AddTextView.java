package com.basic.views;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.HttpConnectionUtil;
import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellLocation;
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
import android.view.inputmethod.InputMethodManager;
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


public class AddTextView extends BaseView {
	
	public Context m_context = null;
	public static AddTextView g_addTextView = null;
	
	EditText editText = null;
	
	ProgressDialog progress = null;
	Button btnPost = null;
	
	
	int m_parentOpt = -1;
	
    boolean m_bIsPublished = false;
    JSONObject m_shareDic;
    
    
	public AddTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public AddTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddTextView(Context context) {
        super(context);
    }

    public void init(Context context, int parentOpt) {
    	
    	m_context = context;
		g_addTextView = this;
		m_parentOpt = parentOpt;

    	((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		((HomeActivity)m_context).imageLoader.clearMemoryCache();
		
		
    	StarTracker.StarSendView(m_context, "New Text");


    	
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
		btnPost = (Button) findViewById(R.id.btnNext);
		btnPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (!m_bIsPublished)
					onPost();
				else
					onSharePage(m_context, m_shareDic, false);
			}
		});
    	
    	editText = (EditText) findViewById(R.id.tvText);
    	
    	ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
    	
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
    	String imgUrl = null;
		try {
			imgUrl = userInfo.getString("img_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivAvatar,
				((HomeActivity)m_context).optIcon, 
				((HomeActivity)m_context).animateFirstListener);
    	
    	
    }
    
    public void onPost() {
    	
    	if (editText.getText().toString().trim().length() < 1) {
    		Const.showMessage("Warning!", "Please input caption text!", (HomeActivity)m_context);
    		return;
    	}
    	
    	HomeActivity.closeKeyboard(m_context, editText);
    	
    	
		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Uploading....");

    	progress.show();

    	
    	new Thread(new Runnable() {
			
			public void run() {
				getDataServer();
			}
		}).start();
    }
    
    
    public void getDataServer() {

		try {

			String text = editText.getText().toString().trim();
			
			JSONObject data = JsonParser.addText(text);

			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}
			
			 if (Global.getUserType() != Global.FAN) {
				 JSONArray arrPost = data.getJSONArray("post");
				 JSONObject obj = arrPost.getJSONObject(0);
				 
				 m_bIsPublished = true;
				 m_shareDic = obj;
		     }
			 
			m_handler.sendEmptyMessage(0);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progress.hide();
			
			switch (msg.what) {
			case 0:
				
    	    	if (m_parentOpt == Global.OPT_PROFILE) {
					ProfileView.g_profileView.onRefresh();
	    		}
				else if (m_parentOpt == Global.OPT_COMMUNITY) {
					CommunityView.g_CommunityView.onRefresh();
				}
				else if (m_parentOpt == Global.OPT_ALLACCESS) {
					AllAccessView.g_AllAccessView.onRefresh();
				}
    	    	
    	    	if (Global.getUserType() == Global.FAN)
    	    		onBack();
    	    	else {
    	    		btnPost.setText("Share");
    	    	}
    	    	
				break;
				
			case -1:
				break;
			}
			
		}
    };
    
    
    private void clear() {
    	m_context = null;
    	g_addTextView = null;
    	
    	editText = null;
    	
    	progress = null;
    }
    
    
    public void onBack() {
    
    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_addTextView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_addTextView);
    	    	clear();
    	    }
    	});
    }
    
}
