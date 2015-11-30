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
import com.mycom.lib.XListView;
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




public class LikeListView extends BaseView {
	
	public Context m_context;
	public static LikeListView g_LikeListView;
	
	private XListView mListView;
	private ResultListAdapter 	adapter = null;

	
	private JSONArray m_arrUsers = null;
	private JSONObject m_feed = null;
	
	ProgressDialog progress = null;
	
	int m_nPage = 0;
	
	boolean m_bDraft = false;
	
	
	public LikeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public LikeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeListView(Context context) {
        super(context);
    }
    
    public void init(Context context, JSONObject feed) {
    	
    	m_context = context;
    	m_feed = feed;
    	m_bDraft = false;
    	
    	initUI();
    }
    
    public void init(Context context, JSONObject feed, boolean bdraft) {
    	
    	m_context = context;
    	m_feed = feed;
    	m_bDraft = true;
    	
    	initUI();
    }
    
    public void initUI() {
		g_LikeListView = this;
		
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		StarTracker.StarSendView(m_context, "Users");
		

		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    	TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
    	tvTitle.setText("Like Users");
		
    	mListView = (XListView) findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				onClickUser((int)arg2);
				
			}
		});
    	
    	m_nPage = 0;
    	getLikeListUser();
    }
    
    private void getLikeListUser() {
    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Loading....");

    	progress.show();
    	
		new Thread(new Runnable() {
			
			public void run() {
				
    			String postType = "";
    			String contentId = "";
				try {
					postType = m_feed.getString("post_type");
					contentId = m_feed.getString("content_id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				JSONObject data;
				
				if (!m_bDraft) {
					data = JsonParser.getLikeUser(postType, contentId, m_nPage);
				} else {
					data = JsonParser.getApprovalUser(postType, contentId, m_nPage);
				}
				
				try {
					
					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					JSONArray feeds = data.getJSONArray("users");
					
					if(m_arrUsers == null || m_nPage == 0)
						m_arrUsers = feeds;
					else {
						m_arrUsers = MyJSON.addJSONArray(m_arrUsers, feeds);
					}
					
					if (feeds == null || feeds.length() < 1) {
						if (m_nPage == 0)
							m_handler.sendEmptyMessage(5);
						else 
							m_handler.sendEmptyMessage(6);
						return;
					}
					
					m_nPage ++;
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				m_handler.sendEmptyMessage(0);
				
			}
		}).start();
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
			case 1:
				mListView.setPullLoadEnable(true);
				refresh();
				
				break;
				
			case 5:
				refresh();
			case 6:
				mListView.setPullLoadEnable(false);
				break;
				
			case -1:
				
				Const.showMessage("", "Loading failed.", (HomeActivity)m_context);

				break;
			}
			
		}
    };
    
    private void refresh() {
    	
    	if (adapter == null) {
    		adapter = new ResultListAdapter(m_context);
    		
    		mListView.setAdapter(adapter);
    		mListView.setCacheColorHint(Color.TRANSPARENT);
    	}
    	else {
    		adapter.notifyDataSetChanged();
    	}
    	
    	mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getCurrentTime());
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
				
				if (!m_bDraft) {
		    		convertView = inflater.inflate(R.layout.list_follow_user, null);
				 	
	    			holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
	    			holder.tvUserName = (TextView) convertView.findViewById(R.id.txtUserName);
				}
				else {
		    		convertView = inflater.inflate(R.layout.list_draft_user, null);
				 	
	    			holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
	    			holder.tvUserName = (TextView) convertView.findViewById(R.id.txtUserName);
	    			holder.ivApproval = (ImageView) convertView.findViewById(R.id.ivApproval);
				}
				
				convertView.setTag(holder);
			} else {
				holder = (ViewCache) convertView.getTag();
				
			}
			
				
			try {
				
				String image_url = user.getString("img_url");
				((HomeActivity)m_context).imageLoader.displayImage(image_url, holder.ivAvatar,
						((HomeActivity)m_context).optIcon, 
						((HomeActivity)m_context).animateFirstListener);
		       	
		       	
		       	
		       	String userName = user.getString("userName");
		       	holder.tvUserName.setText(userName);
		       	
		       	if (m_bDraft) {
		       		String approval = user.getString("approval");
		       		if (approval.equals("0")) {
		       			holder.ivApproval.setVisibility(View.INVISIBLE);
		       		} else {
		       			holder.ivApproval.setVisibility(View.VISIBLE);
		       		}
		       	}
		       	
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
		private  ImageView ivApproval;
		
	}
	
 	
 	 public void clear () {
     	((HomeActivity)m_context).imageLoader.clearMemoryCache();
     	
     	m_context = null;
    	g_LikeListView = null;
    	
    	mListView = null;
    	adapter = null;

    	m_arrUsers = null;
     	
     }
 	
    public void onBack() {
    	
    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_LikeListView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_LikeListView);
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
			
			userId = user.getInt("user_id");
			
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
   
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		m_nPage = 0;
		getLikeListUser();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getLikeListUser();
	}
	
}
