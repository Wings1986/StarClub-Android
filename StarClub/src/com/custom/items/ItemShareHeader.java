package com.custom.items;


import com.starclub.enrique.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemShareHeader implements Item {
 
    
	public String  header = "";
	
    public ItemShareHeader(String header) {
    	
    	this.header = header;
    }
 
    @Override
    public int getViewType() {
        return RowType.FB_SHARE_HEADER;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.list_fb_share_header, null);
        }
 
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_back);
		TextView tvName = (TextView) convertView
				.findViewById(R.id.tvName);
		ImageView ivCheck = (ImageView) convertView.findViewById(R.id.ivImage);
		
		
		tvName.setText(header);
		
		ivCheck.setVisibility(View.GONE);

        return convertView;
    }
 
}