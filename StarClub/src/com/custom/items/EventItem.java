package com.custom.items;


import android.view.LayoutInflater;
import android.view.View;

public class EventItem implements Item {
    private final String         str1;
    private final String         str2;
 
    public EventItem(String text1, String text2) {
        this.str1 = text1;
        this.str2 = text2;
    }
 
    @Override
    public int getViewType() {
        return RowType.IMAGE_ITEM;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
//        if (convertView == null) {
//            convertView = (View) inflater.inflate(R.layout.list_item, null);
//        }
// 
//        TextView text1 = (TextView) convertView.findViewById(R.id.list_content1);
//        TextView text2 = (TextView) convertView.findViewById(R.id.list_content2);
//        text1.setText(str1);
//        text2.setText(str2);
 
        return convertView;
    }
 
}