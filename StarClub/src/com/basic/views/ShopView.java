package com.basic.views;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.MyJSON;
import com.mycom.lib.Const;


import com.network.httpclient.JsonParser;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ToggleButton;


public class ShopView extends BaseView {

	public Context m_context = null;

	private GridView mGridView = null;
	private ResultListAdapter 	adapter = null;

	private ToggleButton btnList=null, btnGrid = null;
	
	Spinner  mSpinner = null;
	
	ProgressDialog progress = null;

	JSONArray m_arrAllData = null;
	JSONArray m_arrData = null;
	
	JSONArray arrFilter = null;
	
	
	public static int FILTER_LIST = 0;
	public static int FILTER_GRID = 1;
	public int m_nFilter = FILTER_LIST;
	
	public static JSONArray g_viewCart = null;
	
	
	public ShopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public ShopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopView(Context context) {
        super(context);
        
    }
    
    public void clear() {
    	m_context = null;

    	mGridView = null;
    	adapter = null;

    	btnList=null;
    	btnGrid = null;
    	
    	mSpinner = null;
    	
    	progress = null;

    	m_arrAllData = null;
    	m_arrData = null;
    	
    	arrFilter = null;
    	
    	g_viewCart = null;
    	
    	m_handler = null;
    }
    
    public void init(Context context) {

    	m_context = context;
    	
    	g_viewCart = new JSONArray();
    	
    	
    	mGridView = (GridView) findViewById(R.id.gridview);
		
		if (m_nFilter == FILTER_GRID)
			mGridView.setNumColumns(2);
		else
			mGridView.setNumColumns(1);
		
		
		mSpinner = (Spinner) findViewById(R.id.spinner);
    	mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String filter = arg0.getItemAtPosition(arg2).toString();
				if (filter.equalsIgnoreCase("all")) {
					m_arrData = MyJSON.copyJSONArray(m_arrAllData);
				}
				else {
					m_arrData = MyJSON.clearJSONArray(m_arrData);
					try {
					for (int i = 0; i < m_arrAllData.length(); i++) {
						JSONObject obj = m_arrAllData.getJSONObject(i);
						
						if (filter.equalsIgnoreCase(obj.getString("tag"))) {
							m_arrData.put(i, obj);
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
		
    	btnList = (ToggleButton) findViewById(R.id.btnList);
    	btnList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnGrid.setChecked(false);
				m_nFilter = FILTER_LIST;
				
				mGridView.setNumColumns(1);

				refreshGridView();
			}
		});
    	
    	btnGrid = (ToggleButton) findViewById(R.id.btnGrid);
    	btnGrid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnList.setChecked(false);
				m_nFilter = FILTER_GRID;
				mGridView.setNumColumns(2);
				
				refreshGridView();
			}
		});

    	Button btnViewCart = (Button) findViewById(R.id.btnViewCart);
    	btnViewCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				onClickViewCart();
			}
		});
    	
    	
    	if (m_nFilter == FILTER_LIST) {
    		btnList.setChecked(true);
    		btnGrid.setChecked(false);
    	} else {
    		btnList.setChecked(false);
    		btnGrid.setChecked(true);
    	}
    	
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

		JSONObject data = JsonParser.getShop();
		
		try {
			
		if (data == null || !data.getBoolean("status")) {
			m_handler.sendEmptyMessage(-1);
			return;
		}

			m_arrAllData = data.getJSONArray("shops");
			m_arrData = MyJSON.copyJSONArray(m_arrAllData);
			
			arrFilter = data.getJSONArray("filters");
			
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
				
				initAdapter();
				
				break;
				
			case 1:
				adapter.notifyDataSetChanged();
				break;
				
			case -1:
				
				Const.showMessage("", "Network failed.", (HomeActivity)m_context);

				break;
			}
			
		}
    };
		
    
    
    public void initAdapter()
    {
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
    	
    	refreshGridView();
    }
    
    public void refreshGridView() {
    	
		adapter = new ResultListAdapter(m_context);
		mGridView.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    	
    }
    
    
    public class ResultListAdapter extends BaseAdapter {

    	private Context mContext = null;
    	
		
		public ResultListAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			if (m_arrData == null) 
				return 0;
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

			JSONObject shop = null;
					
			try {
				
				shop = m_arrData.getJSONObject(position);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (convertView == null) {
				holder = new ViewCache();
				
				LayoutInflater inflater = LayoutInflater.from(mContext);
				
				if (m_nFilter == FILTER_LIST) {
					convertView = inflater.inflate(R.layout.list_shop, null);
					
					holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
					holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressPhoto);
					
					holder.tvLocation = (TextView) convertView.findViewById(R.id.txtLocation);
					holder.tvTitle = (TextView) convertView.findViewById(R.id.txtTitle);
					holder.tvPrice = (TextView) convertView.findViewById(R.id.txtPrice);
					
					holder.btnAddtoCart = (Button) convertView.findViewById(R.id.btnAddCart);
				}
				else { 
					convertView = inflater.inflate(R.layout.list_shop_grid, null);

					holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
					holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
					
				}
				
				holder.id = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewCache) convertView.getTag();
				
			}
			
			try {
				
				JSONArray arrImages = shop.getJSONArray("images");
				if (arrImages != null) {
					if (arrImages.length() != 0) {
						String imgUrl = arrImages.getJSONObject(0).getString("image"); 
						((HomeActivity)m_context).imageLoader.displayImage(imgUrl, holder.ivPhoto, ((HomeActivity)m_context).optFull, new SimpleImageLoadingListener() {
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

					}
				}
				
				if (m_nFilter == FILTER_GRID) {
					holder.ivPhoto.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							onClickShopDetails(v);
						}
					});
					holder.ivPhoto.setTag(position);
				}
		       	
				else {
		       	/// message
//			        holder.tvLocation.setText(shop.getString("position"));
			        holder.tvTitle.setText(shop.getString("title"));
			        
			        String price = shop.getString("price");
			        holder.tvPrice.setText("$" + price);
			        
			        
			        holder.btnAddtoCart.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							onClickAddCart( v);
						}
					});
			        holder.btnAddtoCart.setTag(position);
		        
				}
		        
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
		
		private  TextView tvLocation, tvTitle, tvPrice;
		private  Button btnAddtoCart;

	}
	
 	
 	
 	public void onClickAddCart(View btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString());
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		
 		
		boolean bAdded = false;
		for (int i = 0 ; i < g_viewCart.length() ; i ++) {
			JSONObject item = g_viewCart.getJSONObject(i);
			
			if (item.getString("id").equalsIgnoreCase(feed.getString("id"))) {
				bAdded = true;
				break;
			}
		}
		

		if (bAdded == false) {
			g_viewCart.put(feed);
			
			Const.showMessage("Added", "That item is added successfully", (HomeActivity)m_context);
		}
		else {
			Const.showMessage("Note", "That item already is added", (HomeActivity)m_context);
		}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
 	}
 	
 	public void onClickShopDetails(View btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString());
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

     	ShopDetailView subView = (ShopDetailView) LayoutInflater.from(m_context).inflate(R.layout.view_shop_detail, null);
     	subView.init(m_context, feed);
     	
     	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
     	        ViewGroup.LayoutParams.FILL_PARENT,
     	        ViewGroup.LayoutParams.FILL_PARENT); 
     	((HomeActivity)m_context).baselayout.addView(subView, params);
     	
     	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
     	subView.startAnimation(out);

 	}
    
 	public void onClickViewCart() {
     	ShopCartView subView = (ShopCartView) LayoutInflater.from(m_context).inflate(R.layout.view_shop_cart, null);
     	subView.init(m_context);
     	
     	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
     	        ViewGroup.LayoutParams.FILL_PARENT,
     	        ViewGroup.LayoutParams.FILL_PARENT); 
     	((HomeActivity)m_context).baselayout.addView(subView, params);
     	
     	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
     	subView.startAnimation(out);
 		
 	}
 	
 	
}