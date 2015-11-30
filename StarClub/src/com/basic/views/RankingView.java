package com.basic.views;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.views.AllAccessView.MyListAdapter;
import com.custom.items.Item;
import com.custom.items.ItemBanner;
import com.custom.items.ItemHeader;
import com.custom.items.ItemImage;
import com.custom.items.ItemLogo;
import com.custom.items.ItemRankTop;
import com.custom.items.ItemRankUser;
import com.custom.items.ItemText;
import com.custom.items.RowType;
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
import com.network.httpclient.Utils;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RankingView extends BaseView {

	public Context m_context = null;

	private XListView mListView = null;
	private MyListAdapter adapter = null;

	ProgressDialog progress = null;

	JSONArray m_arrData = null;
	
	int m_nPage = 0;
	
	public RankingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

	public RankingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RankingView(Context context) {
		super(context);

	}

	public void clear() {
		m_context = null;

		mListView = null;
		adapter = null;

		progress = null;

		m_arrData = null;
		m_handler = null;
	}
	public void init(Context context) {

		m_context = context;

		StarTracker.StarSendView(m_context, "Ranking");

    	
		mListView = (XListView) findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
//		mListView.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				// Your code to refresh the list contents goes here
//
//				initData();
//
//			}
//		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				onClickUserDetail((int)arg3);
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

		data = JsonParser.getRanking(m_nPage);

		try {

			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}

			JSONArray feeds = data.getJSONArray("rankings");
			
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

				Const.showMessage("", "Network failed.", (HomeActivity) m_context);

				break;
			}

		}
	};

	public void refresh() {

		List<Item> items = new ArrayList<Item>();
        for (int i = 0 ; i < m_arrData.length() ; i ++) {
       	 	JSONObject feed = null;
			try {
				feed = m_arrData.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (i == 0) {
				items.add(new ItemRankTop(m_context, feed));
			}
			else {
				items.add(new ItemRankUser(m_context, feed));
			}
        }
        
		if (adapter == null) {
     		LayoutInflater inflater = LayoutInflater.from(m_context);
     	    adapter = new MyListAdapter(m_context, inflater, items);
     	    mListView.setAdapter(adapter);
         } else {
             adapter.setItem(items);
     		adapter.notifyDataSetChanged();
         }

//		mListView.onRefreshComplete();
     	mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getCurrentTime());

	}


	 public class MyListAdapter extends ArrayAdapter<Item> {
	        private List<Item> items;
	        private LayoutInflater inflater;
	     

	        public MyListAdapter(Context context, LayoutInflater inflater, List<Item> items) {
	            super(context, 0, items);
	            this.items = items;
	            this.inflater = inflater;
	        }
	        public void setItem(List<Item> arrItems) {
	        	this.items = arrItems;
	        }
	        
	        @Override
	        public int getViewTypeCount() {
	            // Get the number of items in the enum
	            return RowType.getRankValues();
	     
	        }
	     
	        @Override
	        public int getItemViewType(int position) {
	            // Use getViewType from the Item interface
	            return items.get(position).getViewType();
	        }
	     
	        @Override
	        public int getCount() {
	        	// TODO Auto-generated method stub
	        	return items.size();
	        }
	        
	        @Override
	        public View getView(final int position, View convertView, ViewGroup parent) {
	            // Use getView from the Item interface
	        	
	        	Item item = items.get(position);
	        	
	        	int type = item.getViewType();
	        	
	        	View v = item.getView(inflater, convertView);
	        	
	        	if (type == RowType.RANK_USER_ITEM) {
	        		ItemRankUser itemUser = (ItemRankUser) item;
	        		
	        		itemUser.tvRanking.setText("" + (position+1));
	        	}
	        	
	        	
	            return v; //items.get(position).getView(inflater, convertView);
	        }
	 }
	
	// ///////////////////////// Button
	public void onClickUserDetail(int nIndex) {
		
		System.out.println("index = " + nIndex);
		
		
		JSONObject feed = null;
		
		String userId = "";
		
		try {
			feed = m_arrData.getJSONObject(nIndex);
		
			userId = feed.getString("user_id");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (userId.equals(Utils.getUserID())) {
			return;
		}
		
		
		UserDetailView subView = (UserDetailView) LayoutInflater.from(
				m_context).inflate(R.layout.view_user_profile, null);
		subView.init(m_context, userId);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		((HomeActivity) m_context).baselayout.addView(subView, params);

		Animation out = AnimationUtils.loadAnimation(m_context,
				R.anim.slide_left);
		subView.startAnimation(out);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		m_nPage = 0;
		initData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		initData();
	}
}
