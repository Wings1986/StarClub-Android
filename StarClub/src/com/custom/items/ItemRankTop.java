package com.custom.items;

import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.lib.CircleImageView;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemRankTop implements Item {
 
	private final JSONObject	feed;
	
	private Context m_context = null;
    
    public ItemRankTop(Context context, JSONObject feed) {
    	this.m_context = context;
    	this.feed = feed;
    }
 
    @Override
    public int getViewType() {
        return RowType.RANK_TOP_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.list_ranking_header, null);
        }
 
        CircleImageView ivPhoto = (CircleImageView) convertView
				.findViewById(R.id.ivPhoto);

		TextView tvUserName = (TextView) convertView
				.findViewById(R.id.txtUserName);
		TextView tvPoints = (TextView) convertView
				.findViewById(R.id.txtPoints);
		
		try {
			
			String image_url = feed.getString("img_url");
			((HomeActivity)m_context).imageLoader.displayImage(image_url, ivPhoto,
					((HomeActivity)m_context).optIcon, 
					((HomeActivity)m_context).animateFirstListener);

			String userName = feed.getString("userName");
			tvUserName.setText(userName);
			
			String points = feed.getString("point");
			tvPoints.setText(points);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return convertView;
    }
 
}