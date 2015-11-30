package com.frame.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Class to control of elements in the list menu
 * @author Leonardo Salles
 */
public class MenuLazyAdapter extends BaseAdapter {
	  
	private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
      
    private Activity activity;
    private ArrayList<Object> data = new ArrayList<Object>();
    private static LayoutInflater mInflater = null;

    private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();
    
    
    public MenuLazyAdapter(Activity a) {
        activity = a;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Object item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final Object item) {
        data.add(item);
        // save separator position
        mSeparatorsSet.add(data.size() - 1);
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	ViewHolder holder = null;
    	 
        int type = getItemViewType(position);
        
        if(convertView == null){
        	
        	holder = new ViewHolder();
        	
        	switch (type) {
            case TYPE_SEPARATOR:
                convertView = mInflater.inflate(R.layout.menu_list_header, null);

                holder.textView = (TextView)convertView.findViewById(R.id.list_header_title);
                holder.textView.setBackgroundColor(Color.TRANSPARENT);
                holder.imageView = (ImageView) convertView.findViewById(R.id.ivAvatar);
                break;
                
            case TYPE_ITEM:
            	convertView = mInflater.inflate(R.layout.menu_list_row, null);
            	
            	holder.backlayout = (RelativeLayout) convertView.findViewById(R.id.backlayout);
            	holder.textView = (TextView)convertView.findViewById(R.id.menuTitle);
            	
            	break;
        	}
        	convertView.setTag(holder);
        }
        else {
        	 holder = (ViewHolder)convertView.getTag();
        }

//        HashMap<String, Object> menuConfig = new HashMap<String, Object>();
//        menuConfig = data.get(position);

        String title = data.get(position).toString();
        
        if (type == TYPE_SEPARATOR) {
        	if (position == 0) {
        		holder.imageView.setImageResource(R.drawable.menu_logo);
        	} else {
        		holder.imageView.setImageResource(R.drawable.menu_avatar);
        	}
            holder.textView.setText(title);
            
        }
        else if (type == TYPE_ITEM) {
        	
        	if (((HomeActivity)activity).m_prePoint == position) {
        		holder.backlayout.setBackgroundColor(Color.parseColor("#e6e6e6"));
        	} else {
        		holder.backlayout.setBackgroundColor(Color.WHITE);
        	}
     	   	holder.textView.setText(title);
        }
        
        return convertView;
    }
        
      public  class ViewHolder {
    	  public RelativeLayout backlayout;
            public TextView textView;
            public ImageView imageView;
//            public ImageView checkView;
//            public SwitchView switchView;
//            public TextView	textContent;
        }
}