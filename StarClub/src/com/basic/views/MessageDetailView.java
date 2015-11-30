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
import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;



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




public class MessageDetailView extends BaseView {
	
	public Context m_context;
	public static MessageDetailView g_messageDetailView;
	
	
	private JSONObject m_dic;
	
	
	public MessageDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public MessageDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageDetailView(Context context) {
        super(context);
    }

    public void init(Context context, JSONObject feed) {
    	m_context = context;
		g_messageDetailView = this;
		m_dic = feed;
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		StarTracker.StarSendView(m_context, "Message");
    	
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				InboxView.g_InboxView.msgDetailDone();
				onBack();
			}
		});
    	
		Button btnReply = (Button) findViewById(R.id.btnNext);
		btnReply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				gotoReplyMessage();
			}
		});
    	
		TextView tvFromuser = (TextView) findViewById(R.id.txtFromUser);
		TextView tvTouser = (TextView) findViewById(R.id.txtToUser);
		TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
		
		int is_seen = 0;
		try {
			JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
			tvTouser.setText(userInfo.getString("name"));
			
			tvFromuser.setText(m_dic.getString("name"));
			tvMessage.setText(m_dic.getString("message"));
		
			is_seen = m_dic.getInt("is_seen");
		} catch (JSONException e) {}
		
		
		if (is_seen == 0)
			readMessage();
    	
    }
    
    private void readMessage () {

		new Thread(new Runnable() {
			
			public void run() {
				getDataServer();
			}
		}).start();
    }
    
    public void getDataServer() {

		try {

	    	String mailId = m_dic.getString("id");
	    			
			JSONObject data = JsonParser.readMessage(mailId);

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
			
			switch (msg.what) {
			case 0:
				break;
				
			case -1:
				break;
			}
			
		}
    };
    
    public void gotoReplyMessage() {
		SendMsgView subView = (SendMsgView) LayoutInflater.from(
				m_context).inflate(R.layout.view_send_msg, null);
		
		String receiverId = "";
		String receiverName = "";
		String replyMessage = "";
		
		try {
			receiverId = m_dic.getString("sender_id");
			receiverName = m_dic.getString("name");
			replyMessage = m_dic.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		subView.init(m_context, receiverId, receiverName, replyMessage);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		((HomeActivity) m_context).baselayout.addView(subView, params);

		Animation out = AnimationUtils.loadAnimation(m_context,
				R.anim.slide_left);
		subView.startAnimation(out);

    }
    
    public void onBack() {
    
    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_messageDetailView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_messageDetailView);    	
    	    }
    	});
    }
    
}
