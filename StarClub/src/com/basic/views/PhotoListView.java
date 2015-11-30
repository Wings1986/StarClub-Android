package com.basic.views;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.views.AllAccessView.MyListAdapter;
import com.custom.items.Item;
import com.custom.items.ItemBanner;
import com.custom.items.ItemHeader;
import com.custom.items.ItemImage;
import com.custom.items.ItemLogo;
import com.custom.items.ItemPhoto;
import com.custom.items.ItemText;
import com.custom.items.RowType;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.mycom.data.Global;
import com.mycom.data.MyConstants;
import com.mycom.data.MyJSON;
import com.mycom.lib.Const;
import com.mycom.lib.PullAndLoadListView;
import com.mycom.lib.PullToRefreshListView;
import com.mycom.lib.PullToRefreshListView.OnRefreshListener;
import com.mycom.lib.XListView;


import com.mycom.lib.UserDefault;
import com.network.httpclient.JsonParser;
import com.starclub.enrique.BuyActivity;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;






















import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


public class PhotoListView extends BaseView {

	public static PhotoListView g_PhotoListView = null;
	
	public Context m_context = null;

	private XListView mListView = null;
	private MyListAdapter 	adapter = null;

	
	ProgressDialog progress = null;

	JSONArray m_arrPhoto = null;
	JSONArray m_arrData = null;
	
	int m_nIndexDel = -1;
		
	int m_nPage = 0;
	
	
	public PhotoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public PhotoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoListView(Context context) {
        super(context);
        
    }

    public void init(Context context) {
    	g_PhotoListView = this;
    	m_context = context;
    	
    	((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
    	
    	
    	StarTracker.StarSendView(m_context, "Photos");
    	
    	
		mListView = (XListView) findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
//		mListView.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				// Your code to refresh the list contents goes here
//				
//				bPullDonw = true;
//				initData();
//			}
//		});    	

		
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    }
    
    public void initWithData(JSONArray arrData, int page, int position, int nEnableBanner) {
    	m_nPage = page;
    	m_arrPhoto = MyJSON.copyJSONArray(arrData);
    	m_EnableBanner = nEnableBanner;
    	
    	
    	int realPos = 0;
    	
    	try {
    		m_arrData = new JSONArray();
    		int i = 0;
    		int nCount = 0;
        	for (i = 0 ; i < m_arrPhoto.length() ; i ++) {
        		JSONObject data = m_arrPhoto.getJSONObject(i);
        		m_arrData.put(data);
        		if (i == position) {
        			realPos = m_arrData.length() - 1;
        		}
        		
        		if (m_EnableBanner == 1) {
            		if (i % 2 == 0) {
            			if (nCount < 3){
                        	JSONObject obj = new JSONObject();
                        	obj.put("post_type", "banner");
                        	m_arrData.put(obj);
                        	
                        	nCount ++;
            			}
            		}
        		}
        	}
        	
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	refresh();
    	
//    	mListView.smoothScrollToPosition(position);
    	mListView.setSelection(realPos+1);
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

		JSONObject data = JsonParser.getPhotoGallery(m_nPage);
		
		try {
			
			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}

			JSONArray feeds = data.getJSONArray("photos");
			
			if(m_arrPhoto == null || m_nPage == 0)
				m_arrPhoto = feeds;
			else {
				m_arrPhoto = MyJSON.addJSONArray(m_arrPhoto, feeds);
			}
			
			try {
				JSONObject channelInfo = data.getJSONObject("channel_info");
				m_EnableBanner = channelInfo.getInt("enable_banner_ads_android");
			} catch(Exception e) {}
			
			getFilter();
			
			if (feeds == null || feeds.length() < 1) {
				if (m_nPage == 0)
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
			
			if (progress != null) {
				progress.hide();
				progress = null;
			}
			
			switch (msg.what) {
			case 0:
			case 1:
				mListView.setPullLoadEnable(true);
				refresh();
				
				break;
				
//			case 1:
//				adapter.notifyDataSetChanged();
//				break;
				
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
    
    private void getFilter() {
    	
    	JSONArray arrTemp = m_arrPhoto;
    	
    	try {
    		m_arrData = new JSONArray();
    		int i = 0;
    		int nCount = 0;
        	for (i = 0 ; i < arrTemp.length() ; i ++) {
        		JSONObject data = arrTemp.getJSONObject(i);
        		m_arrData.put(data);
        		
        		if (m_EnableBanner == 1) {
            		if (i % 2 == 0) {
            			if (nCount < 3){
                        	JSONObject obj = new JSONObject();
                        	obj.put("post_type", "banner");
                        	obj.put("id", nCount);
                        	m_arrData.put(obj);
                        	
                        	nCount ++;
            			}
            		}
        		}
        	}
        	
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	arrTemp = null;
    	
    	return;
    }

    public void refresh()
    {
    	
    	m_items.clear();
    	
        for (int i = 0 ; i < m_arrData.length() ; i ++) {
       	 JSONObject feed = null;
       	 String postType = "";
			try {
				feed = m_arrData.getJSONObject(i);
				postType = feed.getString("post_type");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				postType = "";
			}
       	 
			if (postType.equalsIgnoreCase("banner")) {
				int banner_id = 0;
				try {
					banner_id = feed.getInt("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m_items.add(new ItemBanner(banner_id));
			}
			else {
				m_items.add(new ItemPhoto(m_context, feed));
			}
        }
        
        if (adapter == null) {
    		LayoutInflater inflater = LayoutInflater.from(m_context);
    	    adapter = new MyListAdapter(m_context, inflater, m_items);
    	    mListView.setAdapter(adapter);
        } else {
            adapter.setItem(m_items);
    		adapter.notifyDataSetChanged();
        }
        
//    	if (bPullDonw)
//    		mListView.onRefreshComplete();
//    	bPullDonw = false;
     	mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getCurrentTime());
    }
    
    public class MyListAdapter extends ArrayAdapter<Item> {
        private List<Item> items;
        private LayoutInflater inflater;
     

        public MyListAdapter(Context context, LayoutInflater inflater, List<Item> items) {
            super(context, 0, items);
            this.items = items;
            this.inflater = inflater;
        }
     
        public void setItem(List<Item> arrItems) {
        	this.items = arrItems;
        }
        
        @Override
        public int getViewTypeCount() {
            // Get the number of items in the enum
            return RowType.getValues();
     
        }
     
        @Override
        public int getItemViewType(int position) {
            // Use getViewType from the Item interface
            return items.get(position).getViewType();
        }
     
        @Override
        public int getCount() {
        	// TODO Auto-generated method stub
        	return items.size();
        }
        
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Use getView from the Item interface
        	
        	Item item = items.get(position);
        	
        	int type = item.getViewType();
        	
        	View v = item.getView(inflater, convertView);
        	
        	if (type == RowType.BANNER_ITEM) {
        		
        		if (m_context != null && convertView == null) {
        			
        			ItemBanner itemBanner = (ItemBanner) item;
        			
        			if (MyConstants.ADMOB_MODE == MyConstants.GOOGLE_ADMOB_NOR)
        			{
                		AdView adView = null;
                		
                		String unitId = "";
                		if (itemBanner.idBanner == 0) {
        					unitId = MyConstants.ADMOB_VIDEO_GALLARY_ID_1;
        				} else if (itemBanner.idBanner == 1) {
        					unitId = MyConstants.ADMOB_VIDEO_GALLARY_ID_2;
        				} else if (itemBanner.idBanner == 2) {
        					unitId = MyConstants.ADMOB_VIDEO_GALLARY_ID_3;
        				}
                		
                		adView = new AdView((HomeActivity)m_context);
                		adView.setAdSize(AdSize.BANNER);
                		adView.setAdUnitId(unitId);
            			if (adView != null) {
                    		itemBanner.layoutBanner.addView(adView);
                    		
                    		AdRequest request = new AdRequest.Builder()
                    								.build();
                    		
                    		adView.loadAd(request);
                		}
        				
        			}
        			else {
        				PublisherAdView adView = null;
                		
                		String unitId = "";
                		if (itemBanner.idBanner == 0) {
        					unitId = MyConstants.ADMOB_VIDEO_GALLARY_ID_1;
        				} else if (itemBanner.idBanner == 1) {
        					unitId = MyConstants.ADMOB_VIDEO_GALLARY_ID_2;
        				} else if (itemBanner.idBanner == 2) {
        					unitId = MyConstants.ADMOB_VIDEO_GALLARY_ID_3;
        				}
            		
                		adView = new PublisherAdView((HomeActivity)m_context);
                		adView.setAdSizes(AdSize.BANNER);
                		adView.setAdUnitId(unitId);
            			if (adView != null) {
                    		itemBanner.layoutBanner.addView(adView);
                    		
                    		PublisherAdRequest request = new PublisherAdRequest.Builder()
                    								.build();
                    		adView.loadAd(request);
                		}
        			}
        		
        		}

        	}
        	if (type == RowType.IMAGE_ITEM) {
        		final ItemPhoto itemImage = (ItemPhoto) item;
        		
        		itemImage.tvNumOfLike.setOnClickListener(new OnClickListener() {
					
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

        		itemImage.btnLike.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						onLikeButton(position);
					}
				});
        		
    			itemImage.ivPhoto.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View arg0) {
    					try {
							onClickPhoto((ImageView)arg0);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
    			});
    			itemImage.ivPhoto.setTag(position);

    			itemImage.ivVideoIcon.setVisibility(View.GONE);
    			
    			itemImage.lockLayout.setOnClickListener(new OnClickListener() {
    				
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
    			itemImage.lockLayout.setTag(position);
    			
    			
    			
    			itemImage.layoutComment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					onClickComment( v);
    				}
    			});
    			itemImage.layoutComment.setTag(position);

    			itemImage.layoutAddComment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					onClickAddComment( v);
    				}
    			});
    			itemImage.layoutAddComment.setTag(position);
    			
    	        itemImage.layoutShare.setOnClickListener(new OnClickListener() {
    				
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
    	        itemImage.layoutShare.setTag(position);


        	}
        	
            return v; //items.get(position).getView(inflater, convertView);
        }
    }
    
    
 	public void clear () {
 		g_PhotoListView = null;
 		
 		m_context = null;

 		mListView = null;
// 		adapter = null;

 		
 		progress = null;

 		m_arrData = null;

 	}
 	 public void onBack() {
 	    
 		((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
 		
 		PhotosView.g_photoView.setBackImage(m_arrPhoto, m_nPage);
 		
     	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
     	g_PhotoListView.startAnimation(in);

     	in.setAnimationListener(new Animation.AnimationListener(){
     	    @Override
     	    public void onAnimationStart(Animation arg0) {
     	    }           
     	    @Override 
     	    public void onAnimationRepeat(Animation arg0) {
     	    }           
     	    @Override
     	    public void onAnimationEnd(Animation arg0) {
     	    	
     	    	((HomeActivity)m_context).baselayout.removeView(g_PhotoListView);
     	    	clear();
     	    }
     	});
     }
 	 
 	 
 	public void onClickPhoto(ImageView btn) throws JSONException {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString());
 		
 		final JSONObject feed = m_arrData.getJSONObject(index);
			
 		feed.put("content_id", feed.getString("id"));
		feed.put("post_type", "photo");
			

 		
		final PhotoDetailView subView = (PhotoDetailView) LayoutInflater.from(m_context).inflate(R.layout.view_photo_detail, null);
    	
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
 	
 	
 	public void onLikeList(final int position) throws JSONException {
    	
    	int index = position;
    	
    	final JSONObject feed = m_arrData.getJSONObject(index);
    	
     	feed.put("post_type", "photo");
     	feed.put("content_id", feed.getString("id"));

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
 				
 				String postType = "photo";
 				String contentId = feed.getString("id");
 				boolean bSelected = !feed.getBoolean("did_like");
 				
 				JSONObject data = JsonParser.addLike(postType, contentId, bSelected?1:0);
 				
 				if (data == null || !data.getBoolean("status")) {
 					m_handler.sendEmptyMessage(-1);
 					return;
 				}

 				System.out.println("result = " + data.toString());
 				
 				
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
 	    	String msg = String.format("You have %d credits left. This content need %d credits to unlock. You need to redeem StarCreditss",
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
 					Intent intent = new Intent((HomeActivity)m_context, BuyActivity.class);
 					((HomeActivity)m_context).startActivity(intent);
// 					((HomeActivity)m_context).setMenuSelect(13);
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
					
					String postType = "photo"; // feed.getString("post_type");
					String contentId = feed.getString("id"); //feed.getString("content_id");
					String wantCredit = feed.getString("credit").toString();
					
					JSONObject data = JsonParser.buyLock(postType, contentId);
					
					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					feed.put("unlock", true);
					m_arrData.put(index, feed);
					
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
			
			feed.put("content_id", feed.getString("id"));
			feed.put("post_type", "photo");
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
    	subView.init(m_context, feed, index, false, Global.OPT_PHOTO_GALLERY);
    	
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
			
			feed.put("content_id", feed.getString("id"));
			feed.put("post_type", "photo");
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
    	subView.init(m_context, feed, index, true, Global.OPT_PHOTO_GALLERY);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);
 	}

	public void onCommentDone(int index, JSONArray arrComments, int totalcomment) {
 		try {
 			
 	 		JSONObject	feed = m_arrData.getJSONObject(index);
 			
 			feed.put("numbersofcomments", totalcomment);

 			m_arrData.put(index, feed);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		refresh();
 	}
	
 	public void onClickShare(View btn) throws JSONException {
 		
 		final int index = Integer.parseInt(btn.getTag().toString());
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		String postType = "photo";
		String contentId = feed.getString("id");
		String text = feed.getString("description");
		
		feed.put("post_type", postType);
		feed.put("content_id", contentId);
		feed.put("caption", text);
		
		onSharePage(m_context, feed, false);
		
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
	 			String postType = "photo";
	 			String contentId = "";
	 			String deeplink = "";
	 			Bitmap image = null;
	 			
	 	 		try {
	 				text = feed.getString("description");
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
	 	 			contentId = feed.getString("id");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
	 	 		
	 	 		try {
	 	 			deeplink = feed.getString("deep_link_web");
	 	 		} catch (JSONException e) {
	 	 			e.printStackTrace();
	 	 		}
	 	 		
	 	 		Item item = m_items.get(index);
		 		ItemPhoto itemImage = (ItemPhoto) item;
		 		image = ((BitmapDrawable)itemImage.ivPhoto.getDrawable()).getBitmap();

		 		    
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
 	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		m_nPage = 0;
		initData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		initData();
	}
	
}
