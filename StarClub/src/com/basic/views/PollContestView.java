package com.basic.views;


import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.views.ProfileView.MyListAdapter;
import com.custom.items.Item;
import com.custom.items.ItemImage;
import com.custom.items.RowType;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.MyConstants;
import com.mycom.data.Global;
import com.mycom.data.MyJSON;
import com.mycom.lib.CircleImageView;
import com.mycom.lib.Const;
import com.mycom.lib.PullAndLoadListView;
import com.mycom.lib.PullToRefreshListView;
import com.mycom.lib.UserDefault;
import com.mycom.lib.PullAndLoadListView.OnLoadMoreListener;
import com.mycom.lib.PullToRefreshListView.OnRefreshListener;


import com.mycom.lib.XListView;
import com.network.httpclient.JsonParser;
import com.network.httpclient.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
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


public class PollContestView extends BaseView {

	public static PollContestView g_PollContestView = null;
	
	public Context m_context = null;

	private XListView mListView = null;
	private ResultListAdapter 	adapter = null;

	ProgressDialog progress = null;

	JSONArray m_arrData = null;
	JSONObject m_starInfo = null;

	
	int m_nPage = 1;
	
	public PollContestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public PollContestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PollContestView(Context context) {
        super(context);
        
    }
    
    public void clear () {
    	g_PollContestView = null;
    	
    	m_context = null;

    	mListView = null;
    	adapter = null;

    	progress = null;

    	m_arrData = null;
    	m_starInfo = null;
    	m_handler = null;
    }
    
    public void init(Context context) {
    	g_PollContestView = this;
    	m_context = context;
    	
    	StarTracker.StarSendView(m_context, MyConstants.MENU_QUIZ);
    	
    	
		mListView = (XListView) findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

/*		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// Your code to refresh the list contents goes here

				m_nPage = 1;
				initData();
			}
		});    	
		mListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				initData();
			}
		});    	
*/		
    	
    	initData();
    }
    
    public void initData() {

		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Loading....");

    	progress.show();

		new Thread(new Runnable() {
			
			public void run() {
				getDataServer();
			}
		}).start();
    }
    
    public void getDataServer() {

		JSONObject data = null;
		
		data = JsonParser.getMainFeed("poll_quiz", m_nPage);
		
		
		try {
			
			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}
		
			
			m_starInfo = data.getJSONObject("star");
			
			
			JSONArray feeds = data.getJSONArray("feeds");
			
			if(m_arrData == null || m_nPage == 1)
				m_arrData = feeds;
			else {
				m_arrData = MyJSON.addJSONArray(m_arrData, feeds);
			}
			
			if (feeds == null || feeds.length() < 1) {
				if (m_nPage == 1)
					m_handler.sendEmptyMessage(5);
				else 
					m_handler.sendEmptyMessage(6);
				return;
			}
			

	    	m_nPage ++;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		m_handler.sendEmptyMessage(0);
	}

    

    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progress.hide();
			
			switch (msg.what) {
			case 0:
				mListView.setPullLoadEnable(true);
				refresh();
				
				break;
				
			case 1:
				adapter.notifyDataSetChanged();
				break;
				
			case 5:
				refresh();
			case 6:
				mListView.setPullLoadEnable(false);
				break;
				
			case -1:
				Const.showMessage("", "Network failed.", (HomeActivity)m_context);
				break;
			}
			
		}
    };
		
    
    public void refresh()
    {
    	if (adapter == null) {
    		LayoutInflater inflater = LayoutInflater.from(m_context);
    	    adapter = new ResultListAdapter(m_context);
    	    mListView.setAdapter(adapter);
        } else {
//            adapter.setItem(items);
    		adapter.notifyDataSetChanged();
        }
    	
    	
//		mListView.onRefreshComplete();
//		mListView.onLoadMoreComplete();
     	mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getCurrentTime());
    }
    
    
    public class ResultListAdapter extends BaseAdapter {

    	private Context mContext = null;
    	
		
		public ResultListAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return m_arrData.length();
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
				
				feed = m_arrData.getJSONObject(position);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			if (convertView == null) {
				holder = new ViewCache();
				
				LayoutInflater inflater = LayoutInflater.from(mContext);
				
				convertView = inflater.inflate(R.layout.list_feed_poll, null);
    			
    			holder.layoutUser = (LinearLayout) convertView.findViewById(R.id.layoutUser);
    			
    			holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
    			holder.tvUserName = (TextView) convertView.findViewById(R.id.txtUserName);
    			holder.tvTimeStamp = (TextView) convertView.findViewById(R.id.txtTimeStamp);
    			holder.tvNumOfLike = (TextView) convertView.findViewById(R.id.txtLikeNum);
    			holder.btnLike = (ToggleButton) convertView.findViewById(R.id.btnLike);
    			
    			holder.layoutMsg1 = (LinearLayout) convertView.findViewById(R.id.layoutMsg1);
    			holder.layoutMsg2 = (LinearLayout) convertView.findViewById(R.id.layoutMsg2);
    			holder.layoutMsg3 = (LinearLayout) convertView.findViewById(R.id.layoutMsg3);
    			
    			holder.tvMessage1 = (TextView) convertView.findViewById(R.id.txtMessage1);
    			holder.tvMsgTimeStamp1 = (TextView) convertView.findViewById(R.id.txtMessageTimeStamp1);
    			holder.tvMessage2 = (TextView) convertView.findViewById(R.id.txtMessage2);
    			holder.tvMsgTimeStamp2 = (TextView) convertView.findViewById(R.id.txtMessageTimeStamp2);
    			holder.tvMessage3 = (TextView) convertView.findViewById(R.id.txtMessage3);
    			holder.tvMsgTimeStamp3 = (TextView) convertView.findViewById(R.id.txtMessageTimeStamp3);

    			holder.tvComment = (TextView) convertView.findViewById(R.id.txtComment);
    			
    			holder.layoutComment = (LinearLayout) convertView.findViewById(R.id.layoutComment);
    			holder.layoutAddComment = (LinearLayout) convertView.findViewById(R.id.layoutAddComment);
    			holder.layoutShare = (LinearLayout) convertView.findViewById(R.id.layoutShare);
    			
				holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
				holder.txtContest = (TextView) convertView.findViewById(R.id.txtContest);
				holder.btnStart = (Button) convertView.findViewById(R.id.btnStart);
				
				holder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
				holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressPhoto);
				
				holder.lockLayout = (FrameLayout) convertView.findViewById(R.id.layoutLock);
				holder.txtlock = (TextView) convertView.findViewById(R.id.txtLockNum);
    			
			
				holder.id = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewCache) convertView.getTag();
				
			}
			
			try {
				
				
				String image_url = m_starInfo.getString("img_url");
				((HomeActivity)m_context).imageLoader.displayImage(image_url, holder.ivAvatar,
						((HomeActivity)m_context).optIcon, 
						((HomeActivity)m_context).animateFirstListener);
		       	
				String starName = m_starInfo.getString("name");
		       	holder.tvUserName.setText(starName.toUpperCase());
		       	
		       	
		       	String timeStamp = Const.getFullTimeAgo(feed.getString("time_stamp"));
		       	holder.tvTimeStamp.setText("" + timeStamp);
	       	
		       	
		       	int numoflike = feed.getInt("numberoflike");
		       	holder.tvNumOfLike.setText("" + numoflike);
		       	holder.tvNumOfLike.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							onLikeList(position);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

		       	holder.btnLike.setBackgroundResource(R.drawable.btn_star_black_selector);
		       	holder.btnLike.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onLikeButton(position);
					}
				});
		       	boolean did_like = feed.getBoolean("did_like");
		       	holder.btnLike.setChecked(did_like);
		       	
		       	
		       	////////////////////////////////////////////////////
		       	
		       	String title = feed.getString("caption");
		       	holder.txtTitle.setText(title);
		       	
		       	String contest = feed.getString("text");
		       	holder.txtContest.setText(contest);
		       	
		       	holder.btnStart.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onClickStart(v);
					}
				});
		       	holder.btnStart.setTag(position);
		       	
		       	
		       	String imgUrl = feed.getString("image_path");
				((HomeActivity)m_context).imageLoader.displayImage(imgUrl, holder.ivPhoto, ((HomeActivity)m_context).optFull, new SimpleImageLoadingListener() {
					 @Override
					 public void onLoadingStarted(String imageUri, View view) {
						 holder.progressBar.setProgress(0);
						 holder.progressBar.setVisibility(View.VISIBLE);
					 }

					 @Override
					 public void onLoadingFailed(String imageUri, View view,
							 FailReason failReason) {
						 holder.progressBar.setVisibility(View.GONE);
					 }

					 @Override
					 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						 holder.progressBar.setVisibility(View.GONE);
					 }
				 }, new ImageLoadingProgressListener() {
					 @Override
					 public void onProgressUpdate(String imageUri, View view, int current,
							 int total) {
						 holder.progressBar.setProgress(Math.round(100.0f * current / total));
					 }
				 }
						);	


				
				holder.lockLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							onClickLock((FrameLayout) v);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				holder.lockLayout.setTag(position);
				
				int nCreditWant = 0;
				try {
					nCreditWant = Integer.parseInt(feed.getString("credit").toString());
				} catch (Exception e) {
					nCreditWant = 0;
				}
				
				holder.txtlock.setText("" + nCreditWant + " Credits");
				
				boolean bUnlocked = feed.getBoolean("unlock");
				if (nCreditWant == 0 || bUnlocked) {
					holder.lockLayout.setVisibility(View.GONE);
				} else {
					holder.lockLayout.setVisibility(View.VISIBLE);
				}
				
		       	
		       	/// message
		        
		        
		        JSONArray arrMsg = feed.getJSONArray("comments");
		        int nComment = arrMsg.length();
		        
		        
		        String strMsgName = "No Comments";
		        String strMsgText = "";
		        String strMsgTimeStamp = "";
		        
		        int k = 0;
		        for (int i = arrMsg.length()-1; i >= 0; i--) {
					JSONObject obj = arrMsg.getJSONObject(i);
					
		        	strMsgName = obj.getString("name");
		        	strMsgText = obj.getString("comment");
		        	strMsgTimeStamp = Const.getTimeAgo(obj.getString("time_stamp"));

		        	SpannableString ss1=  new SpannableString(strMsgName + " " + strMsgText);
			        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
			        ss1.setSpan(boldSpan, 0, strMsgName.length(), 0); // set size
			        
			        if (k == 0) {
			        	holder.tvMessage1.setText(ss1);
				        holder.tvMsgTimeStamp1.setText(strMsgTimeStamp);
			        }
			        else if (k == 1) {
			        	holder.tvMessage2.setText(ss1);
				        holder.tvMsgTimeStamp2.setText(strMsgTimeStamp);
			        }
			        else if (k == 2) {
			        	holder.tvMessage3.setText(ss1);
				        holder.tvMsgTimeStamp3.setText(strMsgTimeStamp);
			        }
			        k++;
				}	
			        
		        holder.layoutMsg1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onClickMsgUser(v);
					}
				});
		        holder.layoutMsg1.setTag(position * 3 + 0);

		        holder.layoutMsg2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onClickMsgUser(v);
					}
				});
		        holder.layoutMsg2.setTag(position * 3 + 1);

		        holder.layoutMsg3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onClickMsgUser(v);
					}
				});
		        holder.layoutMsg3.setTag(position * 3 + 2);

		        
			    if (k == 0) {
			    	holder.layoutMsg1.setVisibility(View.GONE);
			    	holder.layoutMsg2.setVisibility(View.GONE);
			    	holder.layoutMsg3.setVisibility(View.GONE);
			    } else if (k == 1) {
			    	holder.layoutMsg1.setVisibility(View.VISIBLE);
			    	holder.layoutMsg2.setVisibility(View.GONE);
			    	holder.layoutMsg3.setVisibility(View.GONE);
			    } else if (k == 2){
			    	holder.layoutMsg1.setVisibility(View.VISIBLE);
			    	holder.layoutMsg2.setVisibility(View.VISIBLE);
			    	holder.layoutMsg3.setVisibility(View.GONE);
			    } else {
			    	holder.layoutMsg1.setVisibility(View.VISIBLE);
			    	holder.layoutMsg2.setVisibility(View.VISIBLE);
			    	holder.layoutMsg3.setVisibility(View.VISIBLE);
			    }
		        
		        
		        holder.tvComment.setText("" + nComment + " Comments");
		        
		        holder.layoutComment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					onClickComment( v);
    				}
    			});
		        holder.layoutComment.setTag(position);

		        holder.layoutAddComment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					onClickAddComment( v);
    				}
    			});
		        holder.layoutAddComment.setTag(position);
    			
		        holder.layoutShare.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					try {
							onClickShare(v);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			});
		        holder.layoutShare.setTag(position);
		        
		        
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    		
			return convertView;
			
		}


	}
 	class ViewCache {
 		private  int id = -1;
 		
 		private  LinearLayout layoutUser;
		private  CircleImageView ivAvatar;
		private  Button btnAll, btnText, btnImage, btnVideo;
		private  TextView tvUserName, tvTimeStamp, tvNumOfLike;
		private  ToggleButton btnLike;
		private  LinearLayout layoutMsg1, layoutMsg2, layoutMsg3;
		private  TextView tvMessage1, tvMsgTimeStamp1, tvMessage2, tvMsgTimeStamp2, tvMessage3, tvMsgTimeStamp3;
		private  TextView tvComment;
		private  LinearLayout layoutComment, layoutAddComment, layoutShare;

		private  ImageView ivPhoto;
		private  ProgressBar progressBar;
		private  FrameLayout lockLayout;
		private  TextView txtlock;
		
		private  TextView txtTitle, txtContest;
		private  Button btnStart;
		
	}
	
 	public void onLikeList(final int position) throws JSONException {
    	
    	int index = position;
    	
    	final JSONObject feed = m_arrData.getJSONObject(index);
    	
     	final LikeListView subView = (LikeListView) LayoutInflater.from(m_context).inflate(R.layout.view_like_user, null);

    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    			ViewGroup.LayoutParams.FILL_PARENT,
    			ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);

    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);
    	out.setAnimationListener(new Animation.AnimationListener(){
     	    @Override
     	    public void onAnimationStart(Animation arg0) {
     	    }           
     	    @Override 
     	    public void onAnimationRepeat(Animation arg0) {
     	    }           
     	    @Override
     	    public void onAnimationEnd(Animation arg0) {
     	    	subView.init(m_context, feed);
     	    	
     	    }
     	});
    }

 	public void onLikeButton(final int position) {
    	
    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Liking...");
    	progress.show();
    	
    	new Thread(new Runnable() {
			
			public void run() {
				try {
					
					int index = position;
				
					JSONObject feed = m_arrData.getJSONObject(index);
				
					String postType = feed.getString("post_type");
					String contentId = feed.getString("content_id");
					boolean bSelected = !feed.getBoolean("did_like");
					
					JSONObject data = JsonParser.addLike(postType, contentId, bSelected?1:0);
					
					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					int numberOfLike = data.getInt("numbersoflike");
					feed.put("numberoflike", numberOfLike);
					feed.put("did_like", bSelected);
					
					m_arrData.put(index, feed);
					
					m_handler.sendEmptyMessage(1);
					
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				
			}
		}).start();
    }

 	public void onClickUserLayout(View btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString()) - 1;
 		
 		JSONObject feed = null;
 		String userId = "";
 		
		try {
			feed = m_arrData.getJSONObject(index);
			
			userId = feed.getString("fan_id");
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (userId.equals(Utils.getUserID())) {
			return;
		}
		
		UserDetailView subView = (UserDetailView) LayoutInflater.from(
				m_context).inflate(R.layout.view_user_profile, null);
		subView.init(m_context, userId);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		((HomeActivity) m_context).baselayout.addView(subView, params);

		Animation out = AnimationUtils.loadAnimation(m_context,
				R.anim.slide_left);
		subView.startAnimation(out);
		
 	}
 	
 	public void onClickLock(FrameLayout btn) throws JSONException {
 		
 		final int pos = Integer.parseInt(btn.getTag().toString());
 		final int selLock = pos;
 		
 		JSONObject feedLock = null;
 		
 		feedLock = m_arrData.getJSONObject(selLock);
 		String strNumOfCredit = feedLock.getString("credit");
 		int numWant = Integer.parseInt(strNumOfCredit);

 		int userNumOfCredit = UserDefault.getDictionaryForKey("user_info").getInt("credit");

 	    if (userNumOfCredit >= numWant) {
 	    	String msg = String.format("You have %d credits left. This content need %d credits to unlock. Do you want to unlock this content?",
 	    			userNumOfCredit, numWant);
 	    	
 	    	AlertDialog.Builder dialog = new AlertDialog.Builder((HomeActivity) m_context);
 			dialog.setTitle("Unlock Confirmation!");
 			dialog.setMessage(msg);
 			dialog.setIcon(android.R.drawable.ic_dialog_info);
 			dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
 				public void onClick(DialogInterface dialog,int id) {
 					// if this button is clicked, close
 					gotoLock(selLock);
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
 	    else {
 	    	String msg = String.format("You have %d credits left. This content need %d credits to unlock. You need to redeem StarCreditss on Settings",
 	    			userNumOfCredit, numWant);
 	        
 	    	AlertDialog.Builder dialog = new AlertDialog.Builder((HomeActivity) m_context);
 			dialog.setTitle("Unlock Confirmation!");
 			dialog.setMessage(msg);
 			dialog.setIcon(android.R.drawable.ic_dialog_info);
 			dialog.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
 				public void onClick(DialogInterface dialog,int id) {
 					// if this button is clicked, close
 					dialog.cancel();
 				}
 			  });
 			dialog.setNegativeButton("Buy",new DialogInterface.OnClickListener() {
 				public void onClick(DialogInterface dialog,int id) {
 					// if this button is clicked, just close
 					// the dialog box and do nothing
 					((HomeActivity)m_context).setMenuSelect(13);
 				}
 			});
 			dialog.show();
 	    }
 		
		
 	}
 	public void gotoLock(final int index) {
 		
		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Unlocking....");
    	progress.show();
    
    	new Thread(new Runnable() {
			
			public void run() {
				try {
					
					JSONObject feed = m_arrData.getJSONObject(index);
					
					String postType = feed.getString("post_type");
					String contentId = feed.getString("content_id");
					String wantCredit = feed.getString("credit").toString();
					
					JSONObject data = JsonParser.buyLock(postType, contentId);
					
					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					feed.put("unlock", true);
					m_arrData.put(index, feed);
					
					int i = 0;
					for(i = 0 ; i < m_arrData.length() ; i ++) {
						JSONObject obj = m_arrData.getJSONObject(i);
						
						if (contentId.equalsIgnoreCase(obj.getString("content_id")))
							break;
					}
					m_arrData.put(i, feed);
					
					m_handler.sendEmptyMessage(1);

					JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
					String userCredit = userInfo.getString("credit");
					int nCredit = Integer.parseInt(userCredit) - Integer.parseInt(wantCredit);
					userInfo.put("credit", nCredit);
					UserDefault.setDictionaryForKey(userInfo, "user_info");

				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
    	
 	}
 	
 	public void onClickComment(View btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString());
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
    	subView.init(m_context, feed, index, false, Global.OPT_POLLSCONTEST);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);
 	}
 	
 	public void onClickAddComment(View btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString());
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
    	subView.init(m_context, feed, index, true, Global.OPT_POLLSCONTEST);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);

 	}
 	public void onCommentDone(int index, JSONArray arrComments) {
 		try {
 			
 	 		JSONObject	feed = m_arrData.getJSONObject(index);
 			
 			feed.put("comments", arrComments);
 			m_arrData.put(index, feed);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
 	}
 	
 	
 	public void onClickShare(View btn) throws JSONException {
 		
 		final int index = Integer.parseInt(btn.getTag().toString());
 		
 		JSONObject feed = null;
			try {
				feed = m_arrData.getJSONObject(index);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
		onSharePage(m_context, feed, false);
		return;
		
/*		
		final String items[] = {"Facebook", "Twitter", "Instagram"};
 		
 		AlertDialog.Builder ab=new AlertDialog.Builder((HomeActivity)m_context);
 		ab.setTitle("Sharing");
 		ab.setItems(items, new DialogInterface.OnClickListener() {

 			public void onClick(DialogInterface d, int choice) {
 				
 				JSONObject feed = null;
				try {
					feed = m_arrData.getJSONObject(index);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
 	 		
				String text = "";
	 			String imageUrl = "";
	 			String postType = "";
	 			String contentId = "";
	 			String deeplink = "";
	 			Bitmap image = null;
	 			
	 	 		try {
	 				text = feed.getString("caption");
	 			} catch (JSONException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	 	 		
	 	 		try {
	 	 			imageUrl = feed.getString("image_path");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
	 	 		
	 	 		try {
	 	 			postType = feed.getString("post_type");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
	 	 		
	 	 		try {
	 	 			contentId = feed.getString("content_id");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
	 	 		
	 	 		try {
	 	 			deeplink = feed.getString("deep_link_web");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
				
	 	 		
	 	 		HashMap<String, Object> postObj = new HashMap<String, Object>();
		 		    postObj.put("TEXT", text);
		 		    if(image != null) {
		 		    	postObj.put("IMAGE", image);
		 		    	postObj.put("IMAGEURL", imageUrl);
		 		    }
		 		    postObj.put("POSTTYPE", postType);
	 		    postObj.put("CONTENTID", contentId);
	 		    postObj.put("DEEPLINK", deeplink);
	 		    
	 		   if (choice == 0) {
	 		    	((HomeActivity)m_context).shareFacebook(postObj);	
	 		   } else if (choice == 1) {
	 			   ((HomeActivity)m_context).shareTwitter(postObj);
	 		   } else {
	 			   ((HomeActivity)m_context).shareInstagram(postObj);
	 		   }
 		 		
 		 	}
 		 });
 		 ab.show();
*/ 		 
 	}
 	
 	public void onClickMsgUser(View v) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(v.getTag().toString());
 		
 		int feedId = index / 3;
 		int commentId = index % 3;
 		
 		int userId = -1;
 		String type = "";
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(feedId);
			
			JSONArray arrComment = feed.getJSONArray("comments");
			
			userId = arrComment.getJSONObject(commentId).getInt("user_id");
			
			type = arrComment.getJSONObject(commentId).getString("admin_type");
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if ((""+userId).equals(Utils.getUserID())) {
			return;
		}
		
		if (type == null || type.equals("") || type.equals("null")) {
			UserDetailView subView = (UserDetailView) LayoutInflater.from(
					m_context).inflate(R.layout.view_user_profile, null);
			subView.init(m_context, ""+userId);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			((HomeActivity) m_context).baselayout.addView(subView, params);

			Animation out = AnimationUtils.loadAnimation(m_context,
					R.anim.slide_left);
			subView.startAnimation(out);
			
		}

 	}
 	
 	public void onClickStart(View v) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(v.getTag().toString());
 		
 		JSONObject feed = null;
 		String postType = "";
 		boolean bAnswered = false;
 		
		try {
			feed = m_arrData.getJSONObject(index);
			postType = feed.getString("post_type");
			bAnswered = feed.getBoolean("answered");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
	    if (bAnswered) {
	    	Const.showMessage("NOTE", "You've already answered this post.", (HomeActivity)m_context);
	    	return;
	    }
	    
	    
	    if (postType.equalsIgnoreCase("poll")) {
			PollsView subView = (PollsView) LayoutInflater.from(m_context).inflate(R.layout.view_polls, null);
			subView.init(m_context, feed);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			((HomeActivity) m_context).baselayout.addView(subView, params);

			Animation out = AnimationUtils.loadAnimation(m_context,
					R.anim.slide_left);
			subView.startAnimation(out);
	    }
	    else {
	    	QuizView subView = (QuizView) LayoutInflater.from(m_context).inflate(R.layout.view_quiz, null);
			subView.init(m_context, feed);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			((HomeActivity) m_context).baselayout.addView(subView, params);

			Animation out = AnimationUtils.loadAnimation(m_context,
					R.anim.slide_left);
			subView.startAnimation(out);
	    }
	    	

 	}
 	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		m_nPage = 1;
		initData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		initData();
	}
}
