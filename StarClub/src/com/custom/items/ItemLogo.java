package com.custom.items;

import com.brightcove.player.media.tasks.FindVideoTask;
import com.mycom.data.MyConstants;
import com.starclub.enrique.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class ItemLogo implements Item {
    
	int m_logoResId = -1;
	
    public ItemLogo() {
    }
    public ItemLogo(int resid) {
    	m_logoResId = resid;
    }
 
    @Override
    public int getViewType() {
        return RowType.LOGO_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            // No views to reuse, need to inflate a new view
            convertView = (View) inflater.inflate(R.layout.list_feed_logo, null);
        }
 
        if (m_logoResId != -1) {
            ImageView ivLogo = (ImageView) convertView.findViewById(R.id.ivLogo);
            ivLogo.setBackgroundResource(m_logoResId);
        }
        else {
            ImageView ivLogo = (ImageView) convertView.findViewById(R.id.ivLogo);
            ivLogo.setBackgroundResource(MyConstants.RES_ALLACCESS_LOGO);
        }
        
        return convertView;
    }
 
}