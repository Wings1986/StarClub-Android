package com.custom.items;

import org.json.JSONException;
import org.json.JSONObject;

import com.starclub.enrique.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemShare implements Item {
 
	private final JSONObject	feed;
    
	public boolean bChecked = false; 
	
    public ItemShare(JSONObject feed, boolean checked) {
    	
    	this.feed = feed;
    	bChecked = checked;
    }
 
    @Override
    public int getViewType() {
        return RowType.FB_SHARE_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.list_fb_share, null);
        }
 
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_back);
		TextView tvName = (TextView) convertView
				.findViewById(R.id.tvName);
		ImageView ivCheck = (ImageView) convertView.findViewById(R.id.ivImage);
		
		
		if (feed == null) {
			tvName.setText("Timeline");
		} 
		else {
			try {
				tvName.setText(feed.getString("name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (bChecked) {
			ivCheck.setBackgroundResource(R.drawable.checked);
		} else {
			ivCheck.setBackgroundResource(R.drawable.unchecked);
		}
		
        return convertView;
    }
 
}