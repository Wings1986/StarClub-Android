package com.custom.items;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Header implements Item {
    private final String         name;
 
    public Header(String name) {
        this.name = name;
    }
 
    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            // No views to reuse, need to inflate a new view
//            convertView = (View) inflater.inflate(R.layout.header, null);
        }
 
//        TextView text = (TextView) convertView.findViewById(R.id.headerText);
//        text.setText(name);
 
        return convertView;
    }
 
}