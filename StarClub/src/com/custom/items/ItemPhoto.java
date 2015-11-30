package com.custom.items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.mycom.data.Global;
import com.mycom.lib.Const;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ImageView.ScaleType;

public class ItemPhoto implements Item {
 
    private final JSONObject	feed;
    private Context m_context;
    
    public TextView tvCaption = null;
    
    public TextView tvNumOfLike = null;
    
    public ToggleButton btnLike = null;
    public ImageView ivPhoto = null;
    
    public FrameLayout  lockLayout = null;

    public ImageView 	ivVideoIcon = null;
    
    
	public LinearLayout layoutComment = null;
	public LinearLayout layoutAddComment = null;
	public LinearLayout layoutShare = null;
	

    public ItemPhoto(Context context, JSONObject feed) {
    	this.m_context = context;
    	
    	this.feed = feed;
    }
 
    @Override
    public int getViewType() {
        return RowType.IMAGE_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.list_video_gallery, null);
        }
 
		ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
		ivPhoto.setScaleType(ScaleType.FIT_CENTER);
		
		int ivWidth = 0;
		try {
			ivWidth = feed.getInt("img_width");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int ivHeight = 0;
		try {
			ivHeight = feed.getInt("img_height");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}        
		FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) ivPhoto.getLayoutParams();
    	int virturlHeight = 320;
    	if (ivWidth != 0 && ivHeight != 0)
    		virturlHeight= ((HomeActivity)m_context).getScreenWidth() * ivHeight / ivWidth;
    	param.height = virturlHeight;
    	ivPhoto.setLayoutParams(param);

		final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressPhoto);
		
		tvCaption = (TextView) convertView.findViewById(R.id.txtMessage);
		
		TextView tvComment = (TextView) convertView.findViewById(R.id.txtComment);
		
		ivVideoIcon = (ImageView) convertView.findViewById(R.id.ivVideoIcon);
		
		lockLayout = (FrameLayout) convertView.findViewById(R.id.layoutLock);
		TextView txtlock = (TextView) convertView.findViewById(R.id.txtLockNum);
		
		layoutComment = (LinearLayout) convertView.findViewById(R.id.layoutComment);
		layoutAddComment = (LinearLayout) convertView.findViewById(R.id.layoutAddComment);
		layoutShare = (LinearLayout) convertView.findViewById(R.id.layoutShare);
		
		tvNumOfLike = (TextView) convertView.findViewById(R.id.txtLikeNum);
		btnLike = (ToggleButton) convertView.findViewById(R.id.btnLike);

		
		try {
			
			String imgUrl = feed.getString("image_path");
			((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivPhoto, ((HomeActivity)m_context).optFull, new SimpleImageLoadingListener() {
				 @Override
				 public void onLoadingStarted(String imageUri, View view) {
					 progressBar.setProgress(0);
					 progressBar.setVisibility(View.VISIBLE);
				 }

				 @Override
				 public void onLoadingFailed(String imageUri, View view,
						 FailReason failReason) {
					 progressBar.setVisibility(View.GONE);
				 }

				 @Override
				 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					 progressBar.setVisibility(View.GONE);
				 }
			 }, new ImageLoadingProgressListener() {
				 @Override
				 public void onProgressUpdate(String imageUri, View view, int current,
						 int total) {
					 progressBar.setProgress(Math.round(100.0f * current / total));
				 }
			 }
					);	

			int nCreditWant = 0;
			try {
				nCreditWant = Integer.parseInt(feed.getString("credit").toString());
			} catch (Exception e) {
				nCreditWant = 0;
			}
			
			txtlock.setText(nCreditWant + " Credits");
			
			boolean bUnlocked = feed.getBoolean("unlock");
			if (nCreditWant == 0 || bUnlocked) {
				lockLayout.setVisibility(View.GONE);

				btnLike.setEnabled(true);
				layoutComment.setEnabled(true);
				layoutAddComment.setEnabled(true);
				layoutShare.setEnabled(true);
				tvNumOfLike.setEnabled(true);
			}
			else {
				lockLayout.setVisibility(View.VISIBLE);
				
				btnLike.setEnabled(false);
				layoutComment.setEnabled(false);
				layoutAddComment.setEnabled(false);
				layoutShare.setEnabled(false);
				tvNumOfLike.setEnabled(false);
			}
			/// message
	        tvCaption.setText(feed.getString("description"));
	        
	        int numberofComments = feed.getInt("numbersofcomments");
	        if (numberofComments == 1) {
	        	tvComment.setText("1 Comment");
	        } else if (numberofComments > 1) {
	        	tvComment.setText(numberofComments + " Comments");
	        } else {
	        	tvComment.setText("No Comments");
	        }

	        int numberOfLike = feed.getInt("numberoflike");
	        tvNumOfLike.setText("" + numberOfLike);
	        
	       	boolean did_like = feed.getBoolean("did_like");
	       	btnLike.setChecked(did_like);
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return convertView;
    }
 
}