package com.custom.items;

import com.starclub.enrique.R;
import com.mycom.data.Global;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ItemHeader implements Item {
 
	public Button btnAll = null;
	public Button btnText = null;
	public Button btnImage = null;
	public Button btnVideo = null;
	
	
    public ItemHeader() {
    }
 
    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.list_feed_header, null);
        }
 
		btnAll = (Button) convertView.findViewById(R.id.btnAll);
		btnText = (Button) convertView.findViewById(R.id.btnText);
		btnImage = (Button) convertView.findViewById(R.id.btnImage);
		btnVideo = (Button) convertView.findViewById(R.id.btnVideo);
		

		btnAll.setTypeface(null, Typeface.NORMAL);
		btnText.setTypeface(null, Typeface.NORMAL);
		btnImage.setTypeface(null, Typeface.NORMAL);
		btnVideo.setTypeface(null, Typeface.NORMAL);
		
        return convertView;
    }
 
}