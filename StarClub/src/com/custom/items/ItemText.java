package com.custom.items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.lib.CircleImageView;
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

public class ItemText implements Item {
 
    private final JSONObject	feed;
    private final JSONObject	m_userInfo;
    private Context m_context;
    
    public LinearLayout layoutUser = null;
    
    public TextView tvCaption = null;
    public TextView tvNumOfLike = null;
    
    public ToggleButton btnLike = null;
    public FrameLayout lockLayout = null;
    
    public LinearLayout layoutMsg1 = null;
    public LinearLayout layoutMsg2 = null;
    public LinearLayout layoutMsg3 = null;
    
	public LinearLayout layoutComment = null;
	public LinearLayout layoutAddComment = null;
	public LinearLayout layoutShare = null;
	public LinearLayout layoutMore = null;
	
	public boolean bIsMore = false;
	public boolean bDraft = false;
	public boolean bDraftAdmin = false;
	
    public ItemText(Context context, JSONObject star, JSONObject feed, boolean bMore) {
    	this.m_context = context;
    	
    	this.m_userInfo = star;
    	this.feed = feed;
    	
    	this.bIsMore = bMore;
    }
 
    public ItemText(Context context, JSONObject star, JSONObject feed, boolean bMore, boolean draft, boolean draftAdmin) {
    	this.m_context = context;
    	
    	this.m_userInfo = star;
    	this.feed = feed;
    	
    	this.bIsMore = bMore;
    	
    	this.bDraft = true;
    	this.bDraftAdmin = draftAdmin;
    }
    
    @Override
    public int getViewType() {
        return RowType.TEXT_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
        	convertView = (View) inflater.inflate(R.layout.list_feed_text, null);
        }
 
        layoutUser = (LinearLayout) convertView.findViewById(R.id.layoutUser);
        
        CircleImageView ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
		TextView tvUserName = (TextView) convertView.findViewById(R.id.txtUserName);
		TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.txtTimeStamp);
		tvNumOfLike = (TextView) convertView.findViewById(R.id.txtLikeNum);
		btnLike = (ToggleButton) convertView.findViewById(R.id.btnLike);
		
		lockLayout = (FrameLayout) convertView.findViewById(R.id.layoutLock);
		TextView txtlock = (TextView) convertView.findViewById(R.id.txtLockNum);
		
		LinearLayout layoutMsg  = (LinearLayout) convertView.findViewById(R.id.layoutMsg);
		layoutMsg1 = (LinearLayout) convertView.findViewById(R.id.layoutMsg1);
		layoutMsg2 = (LinearLayout) convertView.findViewById(R.id.layoutMsg2);
		layoutMsg3 = (LinearLayout) convertView.findViewById(R.id.layoutMsg3);
		
		TextView tvMessage1 = (TextView) convertView.findViewById(R.id.txtMessage1);
		TextView tvMsgTimeStamp1 = (TextView) convertView.findViewById(R.id.txtMessageTimeStamp1);
		TextView tvMessage2 = (TextView) convertView.findViewById(R.id.txtMessage2);
		TextView tvMsgTimeStamp2 = (TextView) convertView.findViewById(R.id.txtMessageTimeStamp2);
		TextView tvMessage3 = (TextView) convertView.findViewById(R.id.txtMessage3);
		TextView tvMsgTimeStamp3 = (TextView) convertView.findViewById(R.id.txtMessageTimeStamp3);

		TextView tvComment = (TextView) convertView.findViewById(R.id.txtComment);
		
		layoutComment = (LinearLayout) convertView.findViewById(R.id.layoutComment);
		layoutAddComment = (LinearLayout) convertView.findViewById(R.id.layoutAddComment);
		layoutShare = (LinearLayout) convertView.findViewById(R.id.layoutShare);
		layoutMore = (LinearLayout) convertView.findViewById(R.id.layoutMore);
		if (bIsMore) {
			layoutMore.setVisibility(View.VISIBLE);
		} else {
			layoutMore.setVisibility(View.GONE);
		}
		
		TextView tvApprove = (TextView) convertView.findViewById(R.id.txtApproval);
		ImageView ivShare = (ImageView) convertView.findViewById(R.id.ivShare);
		if (bDraft) {
			ivShare.setVisibility(View.GONE);
			tvApprove.setVisibility(View.VISIBLE);
			if (bDraftAdmin) {
				tvApprove.setText("Publish");
			} else {
				tvApprove.setText("Approve");
			}
		} else {
			ivShare.setVisibility(View.VISIBLE);
			tvApprove.setVisibility(View.GONE);
		}
		
		tvCaption = (TextView) convertView.findViewById(R.id.txtText);
		
		
		try {
			
			String image_url = "";
			if (m_userInfo != null)
				image_url = m_userInfo.getString("img_url");
			else
				image_url = feed.getString("fan_img_url");
			
			((HomeActivity)m_context).imageLoader.displayImage(image_url, ivAvatar,
					((HomeActivity)m_context).optIcon, 
					((HomeActivity)m_context).animateFirstListener);
	       	
	       	
	       	String starName = "";
	       	if (m_userInfo != null)
	       		starName = m_userInfo.getString("name");
	       	else
	       		starName = feed.getString("fan_name");
	       	tvUserName.setText(starName);
	       	
	       	
	       	String timeStamp = Const.getFullTimeAgo(feed.getString("time_stamp"));
	       	tvTimeStamp.setText("" + timeStamp);
       	
	       	
	    	if (!bDraft) {
	       		int numoflike = feed.getInt("numberoflike");
	       		tvNumOfLike.setText("" + numoflike);
	       		
		       	boolean did_like = feed.getBoolean("did_like");
		       	btnLike.setChecked(did_like);
	       	}
	       	else {
	       		int approvalNum = feed.getInt("numberofapproval");
	       		int approvalAll = feed.getInt("numberofdraftuser");
	       		
	       		tvNumOfLike.setText("" + approvalNum);
	       		
	       		if (approvalNum == 0) {
	       			btnLike.setBackgroundResource(R.drawable.btn_star_red_selector);
	            } else if (approvalNum < approvalAll) {
	       			btnLike.setBackgroundResource(R.drawable.btn_star_yellow_selector);
	            } else if (approvalNum == approvalAll){
	       			btnLike.setBackgroundResource(R.drawable.btn_star_green_selector);
	            } else {
	       			btnLike.setBackgroundResource(R.drawable.btn_star_red_selector);
	            }
	       	}
	       	
	       	
	       	////////////////////////////////////////////////////
	       	
	       	
       		String txtContent = feed.getString("caption");
       		tvCaption.setText(txtContent);

			
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
				txtlock.setVisibility(View.GONE);
				
				btnLike.setEnabled(true);
				layoutComment.setEnabled(true);
				layoutAddComment.setEnabled(true);
				layoutShare.setEnabled(true);
				layoutMore.setEnabled(true);
				tvNumOfLike.setEnabled(true);
			}
			else {
				lockLayout.setVisibility(View.VISIBLE);
				txtlock.setVisibility(View.VISIBLE);
				
				btnLike.setEnabled(false);
				layoutComment.setEnabled(false);
				layoutAddComment.setEnabled(false);
				layoutShare.setEnabled(false);
				layoutMore.setEnabled(false);
				tvNumOfLike.setEnabled(false);
			}
	       	
	       	/// message
	        
	        JSONArray arrMsg = feed.getJSONArray("comments");
	        
	        String strMsgName = "No Comments";
	        String strMsgText = "";
	        String strMsgTimeStamp = "";
	        
	        int k = 0;
	        for (int i = 0; i < arrMsg.length(); i++) {
	        	if (i > 2) 
	        		break;
				JSONObject obj = arrMsg.getJSONObject(i);
				
	        	strMsgName = obj.getString("name");
	        	strMsgText = obj.getString("comment");
	        	strMsgTimeStamp = Const.getTimeAgo(obj.getString("time_stamp"));

	        	SpannableString ss1=  new SpannableString(strMsgName + " " + strMsgText);
		        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
		        ss1.setSpan(boldSpan, 0, strMsgName.length(), 0); // set size
		        
		        if (k == 0) {
		        	tvMessage1.setText(ss1);
			        tvMsgTimeStamp1.setText(strMsgTimeStamp);
		        }
		        else if (k == 1) {
		        	tvMessage2.setText(ss1);
			        tvMsgTimeStamp2.setText(strMsgTimeStamp);
		        }
		        else if (k == 2) {
		        	tvMessage3.setText(ss1);
			        tvMsgTimeStamp3.setText(strMsgTimeStamp);
		        }
		        k++;
			}	
		        
//	        layoutMsg1.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					onClickMsgUser(v);
//				}
//			});
//	        layoutMsg1.setTag(position * 3 + 0);
//
//	        layoutMsg2.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					onClickMsgUser(v);
//				}
//			});
//	        layoutMsg2.setTag(position * 3 + 1);
//
//	        layoutMsg3.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					onClickMsgUser(v);
//				}
//			});
//	        layoutMsg3.setTag(position * 3 + 2);

	        
	        if (k == 0) {
		    	layoutMsg.setVisibility(View.GONE);
		    	layoutMsg1.setVisibility(View.GONE);
		    	layoutMsg2.setVisibility(View.GONE);
		    	layoutMsg3.setVisibility(View.GONE);
		    } else if (k == 1) {
		    	layoutMsg.setVisibility(View.VISIBLE);
		    	layoutMsg1.setVisibility(View.VISIBLE);
		    	layoutMsg2.setVisibility(View.GONE);
		    	layoutMsg3.setVisibility(View.GONE);
		    } else if (k == 2){
		    	layoutMsg.setVisibility(View.VISIBLE);
		    	layoutMsg1.setVisibility(View.VISIBLE);
		    	layoutMsg2.setVisibility(View.VISIBLE);
		    	layoutMsg3.setVisibility(View.GONE);
		    } else {
		    	layoutMsg.setVisibility(View.VISIBLE);
		    	layoutMsg1.setVisibility(View.VISIBLE);
		    	layoutMsg2.setVisibility(View.VISIBLE);
		    	layoutMsg3.setVisibility(View.VISIBLE);
		    }
	        
	        int nComment = feed.getInt("comments_count");
	        
	        if (nComment == 1) {
	        	tvComment.setText("1 Comment");
	        } else if (nComment > 1) {
	        	tvComment.setText("" + nComment + " Comments");	
	        } else {
	        	tvComment.setText("No Comments");
	        }
	        
	        
//	        layoutComment.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					onClickComment( v);
//				}
//			});
//	        layoutComment.setTag(position);
	       	
	        
//	        layoutShare.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//	        layoutShare.setTag(position);
	        
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return convertView;
    }
 
}