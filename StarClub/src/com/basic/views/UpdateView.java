package com.basic.views;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.data.MyConstants;
import com.mycom.lib.PullToRefreshListView;
import com.mycom.lib.PullToRefreshListView.OnRefreshListener;




import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;


public class UpdateView extends BaseView {

	public Context m_context;
	public static UpdateView g_updateView;
	
	
	private ToggleButton btnOption = null;
	
	public UpdateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public UpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UpdateView(Context context) {
        super(context);
        
    }
    
    public void init(Context context) {
    	m_context = context;
    	g_updateView = this;
    	
    	((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
    	
    	Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    	btnOption = (ToggleButton) findViewById(R.id.btnOption);
    	btnOption.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setPush();
			}
		});
    	
    	ImageView ivLogo = (ImageView) findViewById(R.id.ivLogo);
    	ivLogo.setBackgroundResource(MyConstants.RES_ALLACCESS_LOGO);
    	
    	
    	getPush();
    	
    }
    
    private void getPush () {

		new Thread(new Runnable() {
			
			public void run() {
				getDataServer();
			}
		}).start();
    }
    
    public void getDataServer() {

		try {

			JSONObject data = JsonParser.getPush();


			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}

			boolean enable = data.getBoolean("enable");
			
			Message msg = new Message();
			msg.arg1 = enable ? 1 : 0;
			msg.what = 0;
			
			m_handler.sendMessage(msg);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
    
    private void setPush () {

		new Thread(new Runnable() {
			
			public void run() {
				setDataServer();
			}
		}).start();
    }
    
    public void setDataServer() {

		try {

			boolean bSelected = btnOption.isChecked();
			
			JSONObject data = JsonParser.setPush(bSelected ? 1 : 0);


			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}

			m_handler.sendEmptyMessage(1);
		
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
				int enable = msg.arg1;
				btnOption.setChecked(enable == 1 ? true : false);
				
				break;
				
			case 1:
				break;
				
			case -1:
				break;
			}
			
		}
    };
    
    public void onBack() {
        
    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_updateView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_updateView);    	
    	    }
    	});
    }
    
}
