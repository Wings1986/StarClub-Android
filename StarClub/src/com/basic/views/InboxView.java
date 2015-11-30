package com.basic.views;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.views.RankingView.MyListAdapter;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.mycom.data.MyJSON;
import com.mycom.lib.Const;
import com.mycom.lib.PullToRefreshListView;
import com.mycom.lib.PullToRefreshListView.OnRefreshListener;
import com.mycom.lib.XListView;
import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class InboxView extends BaseView {

	public Context m_context = null;

	private XListView mListView = null;
	private ResultListAdapter adapter = null;

	public static InboxView g_InboxView = null;
	
	ProgressDialog progress = null;

	JSONArray m_arrData = null;

	int m_nSelect = -1;
	
	boolean m_bSubMode = false;
	
	int m_nPage = 0;
	
	public InboxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

	public InboxView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InboxView(Context context) {
		super(context);

	}

	public void clear() {
		m_context = null;

		mListView = null;
		adapter = null;

		g_InboxView = null;
		
		progress = null;

		m_arrData = null;
		
		m_handler = null;
	}
	public void init(Context context, boolean bSubMode) {
		g_InboxView = this;
		
		m_context = context;
		m_bSubMode = bSubMode;
		
    	StarTracker.StarSendView(m_context, "Inbox");


    	
    	if (m_bSubMode == true) {
    		
    		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
    		
    		FrameLayout titleLayout = (FrameLayout)findViewById(R.id.titleLayout);
    		titleLayout.setVisibility(View.VISIBLE);
    		
    		Button btnBack = (Button) findViewById(R.id.btnBack);
    		btnBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBack();
				}
			});
    	}
    	
		mListView = (XListView) findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				onMessageDetail(arg2 - 1);
			}
		});
		initData();
	}

	public void initData() {

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

		JSONObject data = null;

		data = JsonParser.getMessage(m_nPage);

		try {

			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}

			JSONArray feeds = data.getJSONObject("messages").getJSONArray("receiver_messages");
			
			if(m_arrData == null || m_nPage == 0)
				m_arrData = feeds;
			else {
				m_arrData = MyJSON.addJSONArray(m_arrData, feeds);
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

	public Handler m_handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			progress.hide();

			switch (msg.what) {
			case 0:
				mListView.setPullLoadEnable(true);
				refresh();

				break;

			case 1:
				adapter.notifyDataSetChanged();
				break;

			case 5:
				refresh();
			case 6:
				mListView.setPullLoadEnable(false);
				break;
				
			case -1:

				Const.showMessage("", "Network is failed.", (HomeActivity) m_context);

				break;
			}

		}
	};

	public void refresh() {

		boundAdapter();

     	mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getCurrentTime());

	}

	
	public void boundAdapter() {

		if (adapter == null) {
			adapter = new ResultListAdapter(m_context);
			mListView.setAdapter(adapter);
			mListView.setCacheColorHint(Color.TRANSPARENT);
         } else {
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
			return m_arrData.length();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			final ViewCache holder;

			JSONObject feed = null;

			try {

				feed = m_arrData.getJSONObject(position);

				if (convertView == null) {
					holder = new ViewCache();

					LayoutInflater inflater = LayoutInflater.from(mContext);

					
					convertView = inflater.inflate(R.layout.list_index,
							null);

					holder.ivPhoto = (ImageView) convertView
							.findViewById(R.id.ivPhoto);

					holder.tvUserName = (TextView) convertView
							.findViewById(R.id.txtUserName);

					holder.tvTimeStamp = (TextView) convertView
							.findViewById(R.id.txtTimeStamp);
					
					holder.tvMessage = (TextView) convertView
							.findViewById(R.id.txtMessage);

					holder.id = position;
					convertView.setTag(holder);
				} else {
					holder = (ViewCache) convertView.getTag();
				}

				
				int is_seen = feed.getInt("is_seen");
				if (is_seen == 0) {
					holder.ivPhoto.setVisibility(View.VISIBLE);
				} else {
					holder.ivPhoto.setVisibility(View.INVISIBLE);
				}

				String userName = feed.getString("name");
				holder.tvUserName.setText(userName);
				
		       	String timeStamp = Const.getTimeAgo(feed.getString("time_stamp"));
		       	holder.tvTimeStamp.setText("" + timeStamp);

		       	String message = feed.getString("message");
		       	holder.tvMessage.setText(message);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return convertView;

		}

	}

	class ViewCache {
		private int id = -1;

		private ImageView ivPhoto;

		private TextView tvUserName, tvTimeStamp, tvMessage;

		
	}

	
	// ///////////////////////// Button
	public void onMessageDetail(int nIndex) {
		
		System.out.println("INBOX = " + nIndex);
		
		
		JSONObject feed = null;
		
		int is_seen = 0;
		
		try {
			feed = m_arrData.getJSONObject(nIndex);
		
			is_seen = feed.getInt("is_seen");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (is_seen == 0) {
			m_nSelect = nIndex;
		} else {
			m_nSelect = -1;
		}
		
		
		MessageDetailView subView = (MessageDetailView) LayoutInflater.from(
				m_context).inflate(R.layout.view_message_detail, null);
		subView.init(m_context, feed);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		((HomeActivity) m_context).baselayout.addView(subView, params);

		Animation out = AnimationUtils.loadAnimation(m_context,
				R.anim.slide_left);
		subView.startAnimation(out);
	}


	public void msgDetailDone() {
		
		if (m_nSelect != -1) {
			JSONObject feed = null;
			try {
				feed = m_arrData.getJSONObject(m_nSelect);
				
				feed.put("is_seen", "1");
				
				m_arrData.put(m_nSelect, feed);
			} catch (JSONException e) {}
			
			refresh();
		}
		
	}
	
	 public void onBack() {
		    
	    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
	    	
	    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
	    	g_InboxView.startAnimation(in);

	    	in.setAnimationListener(new Animation.AnimationListener(){
	    	    @Override
	    	    public void onAnimationStart(Animation arg0) {
	    	    }           
	    	    @Override 
	    	    public void onAnimationRepeat(Animation arg0) {
	    	    }           
	    	    @Override
	    	    public void onAnimationEnd(Animation arg0) {
	    	    	
	    	    	((HomeActivity)m_context).baselayout.removeView(g_InboxView);
	    	    	clear();
	    	    }
	    	});
	    }
	 
	 
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		m_nPage = 0;
		initData();
	}

	@Override
	public void onLoadMore() {
		initData();
	}
}
