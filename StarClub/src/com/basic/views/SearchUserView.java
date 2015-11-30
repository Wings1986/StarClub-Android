package com.basic.views;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.data.MyJSON;
import com.mycom.lib.CircleImageView;
import com.mycom.lib.Const;
import com.mycom.lib.PullToRefreshListView;
import com.mycom.lib.UserDefault;
import com.mycom.lib.PullToRefreshListView.OnRefreshListener;


import com.network.httpclient.JsonParser;
import com.network.httpclient.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnCloseListener;
import android.widget.ToggleButton;


public class SearchUserView extends BaseView {

	public Context m_context = null;

	private ListView mListView = null;
	private ResultListAdapter 	adapter = null;

	JSONArray m_arrUsers = null;
	JSONArray m_arrSearchedUsers = null;
	

	public SearchUserView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public SearchUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchUserView(Context context) {
        super(context);
        
    }
    
    public void clear () {
    	
    	m_context = null;

    	mListView = null;
    	adapter = null;

    	m_arrSearchedUsers = null;
    	m_arrUsers = null;
    	
    	m_handler = null;
    }
    
    public void init(Context context) {
    	m_context = context;
    	
    	
    	((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
    	
    	
    	final SearchView searchView = (SearchView) findViewById(R.id.search);

    	searchView.setIconified(false);
    	
    	int closeButtonId = getResources().getIdentifier("android:id/search_close_btn", null, null);  
    	ImageView closeButtonImage = (ImageView) searchView.findViewById(closeButtonId);
    	closeButtonImage.setImageResource(R.drawable.close_icon);
    	
//    	searchView.onActionViewExpanded();
    	
    	searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText) {
            	
//            	m_arrSearchedUsers = new JSONArray();
//            	
//            	if (m_arrUsers != null) {
//            		for (int i = 0 ; i < m_arrUsers.length() ; i ++) {
//                		JSONObject dic = null;
//                		String userName = "";
//    					try {
//    						dic = m_arrUsers.getJSONObject(i);
//    						userName = dic.getString("userName").toLowerCase();
//
//    						if (userName.contains(newText.toLowerCase())) {
//    	            			m_arrSearchedUsers.put(dic);
//    	            		}
//
//    					} catch (JSONException e) {
//    						// TODO Auto-generated catch block
//    						e.printStackTrace();
//    					}
//                	}
//            	}
//            	
//            	refresh();
            	
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
            	
            	getSearchData(query);
            	
            	return false;
            }

         });
    	searchView.setOnCloseListener(new OnCloseListener() {
            @Override
            public boolean onClose() {
                
            	((HomeActivity)m_context).onCloseSearch();
            	
                return false;
            }
        });
    	
		mListView = (ListView) findViewById(R.id.listview);
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				onClickUser(arg2);
			}
		});
    	
    }
    

    public void getSearchData(final String search) {


    	new Thread(new Runnable() {

			public void run() {
				JSONObject data = null;

				data = JsonParser.getRanking(search);

				try {

					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					m_arrSearchedUsers = data.getJSONArray("rankings");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				m_handler.sendEmptyMessage(0);
			}
		}).start();
    }
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case 0:
				
				refresh();
				
				break;
				
			case 1:
				adapter.notifyDataSetChanged();
				break;
				
			case -1:
				
				Const.showMessage("", "Network failed.", (HomeActivity)m_context);

				break;
			}
			
		}
    };
		
    
    public void refresh()
    {
    	if (m_arrSearchedUsers == null) 
    		return;
    	
    	if (adapter == null) {
    		adapter = new ResultListAdapter(m_context);
		
    		mListView.setAdapter(adapter);
    	} 
    	else {
    		adapter.notifyDataSetChanged();
    	}
    	
    }

    
    public class ResultListAdapter extends BaseAdapter {

    	private Context mContext = null;
    	
		
		public ResultListAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return m_arrSearchedUsers.length();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			final ViewCache holder;

			JSONObject feed = null;
			
			try {
				
				feed = m_arrSearchedUsers.getJSONObject(position);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			if (convertView == null) {
				holder = new ViewCache();
				
				LayoutInflater inflater = LayoutInflater.from(mContext);
				
	    		convertView = inflater.inflate(R.layout.list_search_user, null);
			 	
    			holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
    			holder.tvUserName = (TextView) convertView.findViewById(R.id.txtUserName);

    			convertView.setTag(holder);
			} else {
				holder = (ViewCache) convertView.getTag();
			}
			
			try {
				
				String image_url = feed.getString("img_url");
				((HomeActivity)m_context).imageLoader.displayImage(image_url, holder.ivAvatar,
						((HomeActivity)m_context).optIcon, 
						((HomeActivity)m_context).animateFirstListener);

				String userName = feed.getString("userName");
				holder.tvUserName.setText(userName);
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    		
			return convertView;
			
		}


	}
 	class ViewCache {
		private  CircleImageView ivAvatar;
		private  TextView tvUserName;		
	}
	
 	public void onClickUser(int index) {
 		
 		JSONObject feed = null;
 		String userId = "";
		try {
			feed = m_arrSearchedUsers.getJSONObject(index);
			userId = feed.getString("user_id");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (userId.equals(Utils.getUserID())) {
			return;
		}
		
	    ((HomeActivity)m_context).onChooseUser(userId);	

 	}
}
