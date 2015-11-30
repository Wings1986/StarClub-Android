package com.basic.views;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
















import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.HttpConnectionUtil;
import com.network.httpclient.JsonParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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


public class ShopDetailView extends BaseView {
	
	public Context m_context;
	public ShopDetailView m_self;
	
	public JSONObject m_item = null;
	
	
	public ShopDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public ShopDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopDetailView(Context context) {
        super(context);
    }

    public void init(Context context, JSONObject shopItem) {
    	m_context = context;
		m_self = this;
		m_item = shopItem;
		
		
		((HomeActivity)m_context).imageLoader.clearMemoryCache();
		
		
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
		Button btnAddtoCart = (Button) findViewById(R.id.btnAddtoCart);
		btnAddtoCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickAddtoCart();
			}
		});

    	
    	try {
    		JSONArray arrImages = shopItem.getJSONArray("images");
    		
    		for (int i = 0 ; i < arrImages.length() ; i ++) {
    			String imgUrl = arrImages.getJSONObject(i).getString("image"); 
    			if (i == 0) {
    				ImageView ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
    				ImageView ivSubPhoto1 = (ImageView) findViewById(R.id.ivSubPhoto1);
    				
    				((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivPhoto, ((HomeActivity)m_context).optFull, new SimpleImageLoadingListener() {
						 @Override
						 public void onLoadingStarted(String imageUri, View view) {
						 }

						 @Override
						 public void onLoadingFailed(String imageUri, View view,
								 FailReason failReason) {
						 }

						 @Override
						 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						 }
					 }, new ImageLoadingProgressListener() {
						 @Override
						 public void onProgressUpdate(String imageUri, View view, int current,
								 int total) {
						 }
					 }
    				);
    				
    				((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivSubPhoto1,
    						((HomeActivity)m_context).optIcon, 
    						((HomeActivity)m_context).animateFirstListener);
    			}
    			else if (i == 1) {
    				ImageView ivSubPhoto2 = (ImageView) findViewById(R.id.ivSubPhoto2);
    				
    				((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivSubPhoto2,
    						((HomeActivity)m_context).optIcon, 
    						((HomeActivity)m_context).animateFirstListener);
    			}
    			else if (i == 2) {
    				ImageView ivSubPhoto3 = (ImageView) findViewById(R.id.ivSubPhoto3);
    				
    				((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivSubPhoto3,
    						((HomeActivity)m_context).optIcon, 
    						((HomeActivity)m_context).animateFirstListener);
    			}
    		}

    		TextView tvTitle = (TextView) findViewById(R.id.txtTitle);
    		tvTitle.setText(shopItem.getString("title"));
    		
    		TextView tvPrice = (TextView) findViewById(R.id.txtPrice);
    		tvPrice.setText("$" + shopItem.getString("price"));
    		
    		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    

    private void onBack() {
    
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	m_self.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	((HomeActivity)m_context).baselayout.removeView(m_self);    	
    	    }
    	});
    }
 
    private void onClickAddtoCart() {
    	boolean bAdded = false;
    	try {
		for (int i = 0 ; i < ShopView.g_viewCart.length() ; i ++) {
			JSONObject item = ShopView.g_viewCart.getJSONObject(i);
			
			if (item.getString("id").equalsIgnoreCase(m_item.getString("id"))) {
				bAdded = true;
				break;
			}
		}
		
		if (bAdded == false) {
			ShopView.g_viewCart.put(m_item);
			
			Const.showMessage("Added", "That item is added successfully", (HomeActivity)m_context);
		}
		else {
			Const.showMessage("Note", "That item already is added", (HomeActivity)m_context);
		}
		
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
