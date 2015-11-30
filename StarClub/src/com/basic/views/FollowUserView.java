package com.basic.views;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.views.CommentView.ResultListAdapter;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.mycom.data.MyJSON;
import com.mycom.lib.CircleImageView;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.JsonParser;
import com.network.httpclient.Utils;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;




public class FollowUserView extends BaseView {
	
	public Context m_context;
	public static FollowUserView g_followUserView;
	
	private ListView mListView;
	private ResultListAdapter 	adapter = null;

	
	private JSONArray m_arrUsers = null;
	
	
	public FollowUserView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public FollowUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FollowUserView(Context context) {
        super(context);
    }
    
    public void init(Context context, JSONArray datas, String title) {
    	m_context = context;
		g_followUserView = this;
		m_arrUsers = datas;
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
    	StarTracker.StarSendView(m_context, title);


		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    	TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
    	tvTitle.setText(title);
		
    	mListView = (ListView) findViewById(R.id.listview);
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				onClickUser((int)arg2);
				
			}
		});
    	
    	refresh();
    	
    }
    
    private void refresh() {
    	
    	if (adapter == null) {
    		adapter = new ResultListAdapter(m_context);
    		
    		mListView.setAdapter(adapter);
    		mListView.setCacheColorHint(Color.TRANSPARENT);
    	}
    	else {
    		adapter.notifyDataSetChanged();
    	}
    }
    
    public class ResultListAdapter extends BaseAdapter {

    	private Context mContext = null;
    	
		public ResultListAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return m_arrUsers.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			final ViewCache holder;

			JSONObject user = null;
			
			
			try {
				
				user = m_arrUsers.getJSONObject(position);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			if (convertView == null) {
				holder = new ViewCache();
				
				LayoutInflater inflater = LayoutInflater.from(mContext);
				
	    		convertView = inflater.inflate(R.layout.list_follow_user, null);
	    				 	
    			holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
    			holder.tvUserName = (TextView) convertView.findViewById(R.id.txtUserName);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewCache) convertView.getTag();
				
			}
			
				
			try {
				
				String image_url = user.getString("img_url");
				((HomeActivity)m_context).imageLoader.displayImage(image_url, holder.ivAvatar,
						((HomeActivity)m_context).optIcon, 
						((HomeActivity)m_context).animateFirstListener);
		       	
		       	
		       	
		       	String userName = user.getString("name");
		       	holder.tvUserName.setText(userName);
		       	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	    		
			return convertView;
			
		}


	}
 	class ViewCache {
 		
		private  CircleImageView ivAvatar;
		private  TextView tvUserName;
	}
	
 	
 	 public void clear () {
     	((HomeActivity)m_context).imageLoader.clearMemoryCache();
     	
     	m_context = null;
    	g_followUserView = null;
    	
    	mListView = null;
    	adapter = null;

    	m_arrUsers = null;
     	
     }
 	
    public void onBack() {
    	
    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_followUserView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_followUserView);
    	    	clear();
    	    }
    	});
    }
    
  
    public void onClickUser(int index) {
 		
 		int userId = -1;
 		String type = "";
 		
 		
 		JSONObject user = null;
		try {
			user = m_arrUsers.getJSONObject(index);
			
			userId = user.getInt("id");
			
			type = user.getString("admin_type");
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if ((""+userId).equals(Utils.getUserID())) {
			return;
		}
		
		if (type == null || type.equals("") || type.equals("null")) {
			UserDetailView subView = (UserDetailView) LayoutInflater.from(
					m_context).inflate(R.layout.view_user_profile, null);
			subView.init(m_context, ""+userId);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			((HomeActivity) m_context).baselayout.addView(subView, params);

			Animation out = AnimationUtils.loadAnimation(m_context,
					R.anim.slide_left);
			subView.startAnimation(out);
			
		}

 	}
   
}
