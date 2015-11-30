package com.basic.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import com.custom.items.Item;
import com.mycom.lib.XListView.IXListViewListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BaseView extends LinearLayout implements IXListViewListener {

	int m_nPage = 1;

	List<Item> m_items = new ArrayList<Item>();

	int m_EnableBanner = 0;
	
	public BaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);

		isInEditMode();

	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);

		isInEditMode();

	}

	public BaseView(Context context) {
		super(context);

		isInEditMode();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
	}

	public String getCurrentTime() {

		Calendar c = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());

		return formattedDate;
	}

	public void onSharePage(Context m_context, JSONObject feed, boolean bPublish) {
		PublishView subView = (PublishView) LayoutInflater.from(m_context)
				.inflate(R.layout.view_publish, null);
		
		if (bPublish)
			subView.initPublish(m_context, feed);
		else
			subView.initShare(m_context, feed);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		((HomeActivity) m_context).baselayout.addView(subView, params);

		Animation out = AnimationUtils.loadAnimation(m_context,
				R.anim.slide_left);
		subView.startAnimation(out);

	}

}
