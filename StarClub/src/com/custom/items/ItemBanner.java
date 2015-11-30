package com.custom.items;

import com.google.android.gms.ads.AdView;
import com.starclub.enrique.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ItemBanner implements Item {
 
	public AdView adView = null;
	public LinearLayout layoutBanner = null;
	
	public int idBanner = 0;
	
    public ItemBanner(int id) {
    	idBanner = id;
    }
   
 
    @Override
    public int getViewType() {
        return RowType.BANNER_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.list_feed_banner, null);
        }
 
        layoutBanner = (LinearLayout) convertView.findViewById(R.id.layoutBanner);
        
//        adView = (AdView)convertView.findViewById(R.id.adView);
//        adView.loadAd(new AdRequest());
    	  
        return convertView;
    }
 
}