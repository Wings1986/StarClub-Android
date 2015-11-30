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


public class SendMsgView extends BaseView {
	
	public Context m_context;
	public static SendMsgView g_sendMsgView;
	
	EditText editText = null;
	ProgressDialog progress = null;
	
	String m_receiverId = "";
	String m_receiverName = "";
	
	
	public SendMsgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public SendMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendMsgView(Context context) {
        super(context);
    }

    public void init(Context context, String user_id, String user_name, String replyMessage) {
    	m_context = context;
		g_sendMsgView = this;
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		
		m_receiverId = user_id;
		m_receiverName = user_name;
		
		((HomeActivity)m_context).imageLoader.clearMemoryCache();
		
		
		StarTracker.StarSendView(m_context, "Send Message");
		

		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
		Button btnPost = (Button) findViewById(R.id.btnNext);
		btnPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPost();
			}
		});
    	
		TextView tvTitle = (TextView) findViewById(R.id.txtTitle);
		tvTitle.setText("To " + m_receiverName);
		
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
    	
    	TextView tvReplyMsg = (TextView) findViewById(R.id.tvMessage);
    	tvReplyMsg.setText(replyMessage);
    }
    
    public void onPost() {
    	
    	if (editText.getText().toString().trim().length() < 1) {
    		
    		Const.showMessage("Warning!", "Please input message.", (HomeActivity)m_context);
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
			
			JSONObject data = JsonParser.sendMessage(m_receiverId, text);

			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
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
				gotoBack();
				break;
				
			case -1:
				break;
			}
			
		}
    };
    
    private void gotoBack() {
    	
    	onBack();
    }
    
    public void onBack() {
    
    	HomeActivity.closeKeyboard(m_context, editText);

    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_sendMsgView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	((HomeActivity)m_context).baselayout.removeView(g_sendMsgView);    	
    	    }
    	});
    }
    
}
