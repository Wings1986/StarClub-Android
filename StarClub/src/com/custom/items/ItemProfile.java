package com.custom.items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.mycom.lib.CircleImageView;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemProfile implements Item {
 
    private final JSONObject	m_userInfo;
    private Context m_context;
    
    public Button btnAddPost = null;
    public Button btnIndex = null;
    
    public CircleImageView ivUserPic = null;
    
    public TextView tvFollower = null;
	public TextView tvFollowing = null;
	public FrameLayout layoutFollower = null;
	public FrameLayout layoutFollowing = null;
	

    public ItemProfile(Context context, JSONObject user) {
    	this.m_context = context;
    	
    	this.m_userInfo = user;
    }
 
    @Override
    public int getViewType() {
        return RowType.PROFILE_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.list_feed_user, null);
        }
 
		ivUserPic = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
		tvFollower = (TextView) convertView.findViewById(R.id.txtFollower);
		tvFollowing = (TextView) convertView.findViewById(R.id.txtFollowing);
		TextView tvPoint = (TextView) convertView.findViewById(R.id.txtPoint);
		btnAddPost = (Button) convertView.findViewById(R.id.btnAddPost);
		btnIndex = (Button) convertView.findViewById(R.id.btnIndex);
		
		layoutFollower = (FrameLayout) convertView.findViewById(R.id.layoutFollower);
		layoutFollowing = (FrameLayout) convertView.findViewById(R.id.layoutFollowing);

		try {
			
			String image_url = m_userInfo.getString("img_url");
			((HomeActivity)m_context).imageLoader.displayImage(image_url, ivUserPic,
					((HomeActivity)m_context).optIcon, 
					((HomeActivity)m_context).animateFirstListener);
			
			JSONArray arrFollower = m_userInfo.getJSONArray("followeds");
			tvFollower.setText("" + arrFollower.length());
			
			JSONArray arrFollowing = m_userInfo.getJSONArray("followings");
			tvFollowing.setText("" + arrFollowing.length());
			
			String points = m_userInfo.getString("point");
			tvPoint.setText(points);
			
			btnAddPost.setText("Settings");
			
			int numUnread = m_userInfo.getInt("numberofunreadmessage");
			btnIndex.setText(String.format("Inbox(%d)", numUnread));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return convertView;
    }
 
}