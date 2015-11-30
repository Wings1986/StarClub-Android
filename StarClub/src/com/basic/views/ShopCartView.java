package com.basic.views;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.MyJSON;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ToggleButton;

public class ShopCartView extends BaseView {

	public Context m_context = null;
	public ShopCartView	m_self = null;
	
	private ListView mListView;
	private ResultListAdapter adapter = null;

	int m_indexDel = 0;
	
	public ShopCartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

	public ShopCartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShopCartView(Context context) {
		super(context);

	}

	public void init(Context context) {

		m_context = context;
		m_self = this;
		
		mListView = (ListView) findViewById(R.id.listview);

		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
		Button btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickNext();
			}
		});

		initData();
	}

	public void initData() {

		boundAdapter();
	}


	
	public void boundAdapter() {

		adapter = new ResultListAdapter(m_context);

		mListView.setAdapter(adapter);
		mListView.setCacheColorHint(Color.TRANSPARENT);

		adapter.notifyDataSetChanged();

	}

	public class ResultListAdapter extends BaseAdapter {

		private Context mContext = null;

		public ResultListAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return ShopView.g_viewCart.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			final ViewCache holder;

			JSONObject feed = null;

			try {

				feed = ShopView.g_viewCart.getJSONObject(position);

				if (convertView == null) {
					holder = new ViewCache();

					LayoutInflater inflater = LayoutInflater.from(mContext);

					
					convertView = inflater.inflate(R.layout.list_viewcart,
							null);

					holder.ivPhoto = (ImageView) convertView
							.findViewById(R.id.ivPhoto);

					holder.tvLocation = (TextView) convertView
							.findViewById(R.id.txtLocation);
					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.txtTitle);
					holder.tvPrice = (TextView) convertView
							.findViewById(R.id.txtPrice);

					holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
					holder.etQuantity = (EditText) convertView.findViewById(R.id.etQuantity);
					
					holder.id = position;
					convertView.setTag(holder);
				} else {
					holder = (ViewCache) convertView.getTag();
				}

				JSONArray arrImages = feed.getJSONArray("images");
				if (arrImages != null) {
					if (arrImages.length() > 0) {
						String image_url = arrImages.getJSONObject(0).getString("image"); 
						((HomeActivity)m_context).imageLoader.displayImage(image_url, holder.ivPhoto,
								((HomeActivity)m_context).optIcon, 
								((HomeActivity)m_context).animateFirstListener);
					}
				}
				
				holder.tvLocation.setText("Enrique lglesias");
				
				holder.tvTitle.setText(feed.getString("title"));
				holder.tvPrice.setText("$" + feed.getString("price"));
				
				holder.btnDelete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						onClickDelete(v);
					}
				});
				holder.btnDelete.setTag(position);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return convertView;

		}

	}

	class ViewCache {
		private int id = -1;

		private ImageView ivPhoto;
		private TextView tvLocation, tvTitle, tvPrice;
		private EditText etQuantity;
		private Button btnDelete;

	}

	
	 private void onBack() {
		    
		 Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
	    	m_self.startAnimation(in);

	    	in.setAnimationListener(new Animation.AnimationListener(){
	    	    @Override
	    	    public void onAnimationStart(Animation arg0) {
	    	    }           
	    	    @Override 
	    	    public void onAnimationRepeat(Animation arg0) {
	    	    }           
	    	    @Override
	    	    public void onAnimationEnd(Animation arg0) {
	    	    	((HomeActivity)m_context).baselayout.removeView(m_self);    	
	    	    }
	    	});
	    }   	
	 
	 public void onClickNext() {
		 
	 }
	 
	// ///////////////////////// Button
		public void onClickDelete(View btn) {
	 		
	 		m_indexDel = Integer.parseInt(btn.getTag().toString());
	 		
			AlertDialog.Builder dialog = new AlertDialog.Builder((HomeActivity)m_context);
			dialog.setTitle("");
			dialog.setMessage("Do you want to delete that item?");
			dialog.setIcon(android.R.drawable.ic_dialog_info);
			dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					
					ShopView.g_viewCart = MyJSON.RemoveJSONArray(ShopView.g_viewCart, m_indexDel);
					
					boundAdapter();
				}
			  });
			dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});
			dialog.show();
			
		}



}
