package com.basic.views;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.mycom.data.MyJSON;
import com.mycom.lib.CustomGridView.PullToRefreshBase;
import com.mycom.lib.CustomGridView.PullToRefreshBase.OnRefreshListener;
import com.mycom.lib.CustomGridView.PullToRefreshBase.OnRefreshListener2;
import com.mycom.lib.CustomGridView.PullToRefreshGridView;
import com.network.httpclient.JsonParser;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class PhotosView extends BaseView {

	public Context m_context = null;
	public static PhotosView g_photoView = null;
	
	ProgressDialog progress = null;
	
	PullToRefreshGridView mPullRefreshGridView;
	GridView mGridView = null;
	ImageAdapter 	adapter = null;
	
	Spinner  mSpinner = null;
	
	
	JSONArray arrAllPhotos = null;
	JSONArray arrPhotos = null;
	JSONArray arrFilter = null;
	
	int m_nPage = 0;
	
	public PhotosView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public PhotosView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotosView(Context context) {
        super(context);
        
    }
    
    public void clear() {
    	m_context = null;
    	g_photoView = null;
    	
    	progress = null;
    	
    	mGridView = null;
    	adapter = null;
    	
    	mSpinner = null;
    	
    	arrAllPhotos = null;
    	arrPhotos = null;
    	arrFilter = null;
    	
    	m_handler = null;
    }
    
    public void init(Context context) {
    	m_context = context;
    	g_photoView = this;
    	
    	
    	StarTracker.StarSendView(m_context, "Photo Gallery");
    	
    	
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.gridview);
		mGridView = mPullRefreshGridView.getRefreshableView();
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				m_nPage = 0;
				getPhotoGallery();
			}

			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				getPhotoGallery();
			}

		});
				
				
    	mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				PhotoListView subView = (PhotoListView) LayoutInflater.from(m_context).inflate(R.layout.view_photolist, null);
		    	subView.init(m_context);
		    	subView.initWithData(arrPhotos, m_nPage, position, m_EnableBanner);
		    	
		    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		    	        ViewGroup.LayoutParams.FILL_PARENT,
		    	        ViewGroup.LayoutParams.FILL_PARENT); 
		    	((HomeActivity)m_context).baselayout.addView(subView, params);
		    	
		    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
		    	subView.startAnimation(out);
		    	
/*				
				JSONObject feed = null;
				try {
					feed = arrPhotos.getJSONObject(position);
					
					feed.put("content_id", feed.getString("id"));
					feed.put("post_type", "photo");
					feed.put("img_url",  feed.getString("destination"));
					

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
		    	PhotoDetailView subView = (PhotoDetailView) LayoutInflater.from(m_context).inflate(R.layout.view_photo_detail, null);
		    	subView.init(m_context, feed);
		    	
		    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		    	        ViewGroup.LayoutParams.FILL_PARENT,
		    	        ViewGroup.LayoutParams.FILL_PARENT); 
		    	((HomeActivity)m_context).baselayout.addView(subView, params);
		    	
		    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
		    	subView.startAnimation(out);
*/				
			}
		});
    	
    	mSpinner = (Spinner) findViewById(R.id.spinner);
    	mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String filter = arg0.getItemAtPosition(arg2).toString();
				if (filter.equalsIgnoreCase("all")) {
					arrPhotos = MyJSON.copyJSONArray(arrAllPhotos);
				}
				else {
					arrPhotos = MyJSON.clearJSONArray(arrPhotos);
					try {
					for (int i = 0; i < arrAllPhotos.length(); i++) {
						JSONObject obj = arrAllPhotos.getJSONObject(i);
						
						if (filter.equalsIgnoreCase(obj.getString("tags"))) {
							arrPhotos.put(obj);
						}
					}
					}catch (Exception e) {};
				}
				
				adapter.notifyDataSetChanged();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	m_nPage = 0;
    	getPhotoGallery();
    	
    }
    
    private void getPhotoGallery () {
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

			JSONObject data = JsonParser.getPhotoGallery(m_nPage);


			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}

			JSONArray photos = data.getJSONArray("photos");
			
			if(arrAllPhotos == null || m_nPage == 0) {
				arrAllPhotos = photos;
				arrPhotos = photos;
			}
			else {
				arrAllPhotos = MyJSON.addJSONArray(arrAllPhotos, photos);
				arrPhotos = MyJSON.addJSONArray(arrPhotos, photos);
			}
			
			
			JSONArray filters = data.getJSONArray("filters");
			if (arrFilter == null || m_nPage == 0) {
				arrFilter = filters;
			} else {
				arrFilter = MyJSON.addOneJSONArray(arrFilter, filters);
			}
//			arrFilter = data.getJSONArray("filters");
			
			try {
				JSONObject channelInfo = data.getJSONObject("channel_info");
				m_EnableBanner = channelInfo.getInt("enable_banner_ads_android");
			} catch(Exception e) {}
			
			m_nPage ++;
			
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
				boundAdapter();
				
				break;
				
			case 1:
				break;
				
			case -1:
				break;
			}
			
		}
    };
    
    public void boundAdapter() {
		
    	List<String> list = new ArrayList<String>();
    	for (int i = 0 ; i < arrFilter.length() ; i ++) {
    		String item = "";
			try {
				item = arrFilter.getString(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		list.add(item);
    	}
    	
    	ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(m_context, android.R.layout.simple_spinner_item, list);
    	spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(spinAdapter);
    	
    	if (adapter == null) {
    		adapter = new ImageAdapter();
    		
    		mGridView.setAdapter(adapter);
    		mGridView.setCacheColorHint(Color.TRANSPARENT);
    	}
    	else {
    		adapter.notifyDataSetChanged();
    	}

    	
    	mPullRefreshGridView.onRefreshComplete();
    }
    
    
    public void setBackImage(JSONArray array, int page) {
    	if (m_nPage == page)
    		return;
    	
    	arrPhotos = array;
    	m_nPage = page;
    	
    	if (adapter != null) {
    		adapter.notifyDataSetChanged();
    	}
    	
    	mPullRefreshGridView.onRefreshComplete();
    }
    /////////////////////////
    
    public class ImageAdapter extends BaseAdapter {
    	
    	
		@Override
		public int getCount() {
			return arrPhotos.length();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = ((HomeActivity) m_context).getLayoutInflater().inflate(R.layout.list_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				holder.imgLock = (ImageView) view.findViewById(R.id.imgLock);
				
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			JSONObject item = null;
			try {
				item = arrPhotos.getJSONObject(position);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String imgUrl = null;
			
	        int nCreditWant = 0;
	        boolean bUnlocked = false;

			try {
				imgUrl = item.getString("img_small_thumb");
				nCreditWant = item.getInt("credit");
				bUnlocked = item.getBoolean("unlock");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			((HomeActivity)m_context).imageLoader.displayImage(imgUrl, holder.imageView, ((HomeActivity)m_context).optFull, new SimpleImageLoadingListener() {
										 @Override
										 public void onLoadingStarted(String imageUri, View view) {
											 holder.progressBar.setProgress(0);
											 holder.progressBar.setVisibility(View.VISIBLE);
										 }

										 @Override
										 public void onLoadingFailed(String imageUri, View view,
												 FailReason failReason) {
											 holder.progressBar.setVisibility(View.GONE);
										 }

										 @Override
										 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
											 holder.progressBar.setVisibility(View.GONE);
										 }
									 }, new ImageLoadingProgressListener() {
										 @Override
										 public void onProgressUpdate(String imageUri, View view, int current,
												 int total) {
											 holder.progressBar.setProgress(Math.round(100.0f * current / total));
										 }
									 }
			);

	        
	        if (nCreditWant == 0 || bUnlocked) {
	        	holder.imgLock.setVisibility(View.GONE);
	        } else {
	        	holder.imgLock.setVisibility(View.VISIBLE);
	        }
	        
	        
			return view;
		}

		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
			
			ImageView imgLock;
		}
	}
    
}
