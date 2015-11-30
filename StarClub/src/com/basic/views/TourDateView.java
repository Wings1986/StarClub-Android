package com.basic.views;


import java.text.SimpleDateFormat;
import java.util.Date;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.mycom.lib.Const;
import com.mycom.lib.PullToRefreshListView;
import com.mycom.lib.PullToRefreshListView.OnRefreshListener;

import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TourDateView extends BaseView {

	
	public Context m_context = null;

	private PullToRefreshListView mListView = null;
	private ResultListAdapter 	adapter = null;

	
	ProgressDialog progress = null;

	JSONArray m_arrData = null;
	
	
	
	public TourDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public TourDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TourDateView(Context context) {
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
    	
    	
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// Your code to refresh the list contents goes here
				initData();
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

		JSONObject data = JsonParser.getTourDate();
		
		try {
			
		if (data == null || !data.getBoolean("status")) {
			m_handler.sendEmptyMessage(-1);
			return;
		}

			m_arrData = data.getJSONArray("events");
			
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
				refresh();
				break;
				
			case -1:
				
				Const.showMessage("", "Network failed.", (HomeActivity)m_context);

				break;
			}
			
		}
    };
		
    
    
    public void refresh()
    {
    	if (adapter == null) {
    		adapter = new ResultListAdapter(m_context);
    		mListView.setAdapter(adapter);
    		mListView.setCacheColorHint(Color.TRANSPARENT);
    	}
    	else {
    		adapter.notifyDataSetChanged();
    	}
    	
		mListView.onRefreshComplete();

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
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			final ViewCache holder;

			JSONObject tourDate = null;
					
			try {
				
				tourDate = m_arrData.getJSONObject(position);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (convertView == null
					|| ((ViewCache) convertView.getTag()).id != position) {
				holder = new ViewCache();
				
				LayoutInflater inflater = LayoutInflater.from(mContext);
				
				
				convertView = inflater.inflate(R.layout.list_tourdate, null);
    			
				holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
				holder.tvPhotoTime = (TextView) convertView.findViewById(R.id.txtPhotoTime);
				
				holder.tvLocation = (TextView) convertView.findViewById(R.id.txtLocation);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.txtTitle);
				holder.tvTimeStamp = (TextView) convertView.findViewById(R.id.txtTime);
				
				holder.btnDetail = (Button) convertView.findViewById(R.id.btnDetail);
				
				holder.id = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewCache) convertView.getTag();
				
			}
			
			try {
				
//				String imgUrl = tourDate.getString("image_path");
//				((HomeActivity)m_context).imageLoader.displayImage(imgUrl, holder.ivPhoto, ((HomeActivity)m_context).optFull, new SimpleImageLoadingListener() {
//					 @Override
//					 public void onLoadingStarted(String imageUri, View view) {
//						 holder.progressBar.setProgress(0);
//						 holder.progressBar.setVisibility(View.VISIBLE);
//					 }
//
//					 @Override
//					 public void onLoadingFailed(String imageUri, View view,
//							 FailReason failReason) {
//						 holder.progressBar.setVisibility(View.GONE);
//					 }
//
//					 @Override
//					 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//						 holder.progressBar.setVisibility(View.GONE);
//					 }
//				 }, new ImageLoadingProgressListener() {
//					 @Override
//					 public void onProgressUpdate(String imageUri, View view, int current,
//							 int total) {
//						 holder.progressBar.setProgress(Math.round(100.0f * current / total));
//					 }
//				 }
//						);	
				holder.ivPhoto.setImageResource(R.drawable.demo_image);

		       	
		       	/// message
		        holder.tvLocation.setText(tourDate.getString("position"));
		        holder.tvTitle.setText(tourDate.getString("title"));
		        
		        String startDate = tourDate.getString("start_date");
		        
		        holder.tvPhotoTime.setText(getShortTime(startDate));
		        holder.tvTimeStamp.setText(getLongTime(startDate));
		        
		        
		        holder.btnDetail.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onClickDetails((Button) v);
					}
				});
		        holder.btnDetail.setTag(position);
		        
		       
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
    		
		return convertView;
			
		}

	}
    
 	class ViewCache {
 		private  int id = -1;

		private  ImageView ivPhoto;
		private  ProgressBar progressBar;
		
		private  TextView tvPhotoTime, tvLocation, tvTitle, tvTimeStamp;
		private  Button btnDetail;

	}
	
 	public static String getShortTime(String dtStart) {
 		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
 		
 		Date date = null;
 		try {  
 		    date = format.parse(dtStart);  
 		} catch (Exception e) {  
 		    // TODO Auto-generated catch block  
 		    e.printStackTrace();  
 		}
 		
 		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
 		return sdf.format(date);
 	}
 	
 	public static String getLongTime(String dtStart) {
 		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
 		
 		Date date = null;
 		try {  
 		    date = format.parse(dtStart);  
 		} catch (Exception e) {  
 		    // TODO Auto-generated catch block  
 		    e.printStackTrace();  
 		}
 		
 		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
 		return sdf.format(date);
 	}
 	
 	public void onClickDetails(Button btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString());
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
			
		    feed.put("content_id", feed.getString("id"));
		    feed.put("post_type", "TourDate");
		    
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 		

		TourDateDetailView subView = (TourDateDetailView) LayoutInflater.from(m_context).inflate(R.layout.view_tourdate_detail, null);
     	subView.init(m_context, feed);
     	
     	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
     	        ViewGroup.LayoutParams.FILL_PARENT,
     	        ViewGroup.LayoutParams.FILL_PARENT); 
     	((HomeActivity)m_context).baselayout.addView(subView, params);
     	
     	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
     	subView.startAnimation(out);
 	}


}