package com.basic.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.data.MyConstants;
import com.mycom.data.MyJSON;
import com.mycom.lib.Const;
import com.mycom.lib.PullAndLoadListView;
import com.mycom.lib.PullAndLoadListView.OnLoadMoreListener;
import com.mycom.lib.PullToRefreshListView;
import com.mycom.lib.UserDefault;
import com.mycom.lib.PullToRefreshListView.OnRefreshListener;


import com.mycom.lib.XListView;
import com.mycom.lib.XListView.IXListViewListener;
import com.network.httpclient.JsonParser;
import com.network.httpclient.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.starclub.enrique.BuyActivity;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.PlayerActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.basic.views.VideosView.MyListAdapter;
import com.custom.items.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;


public class DraftReviewView extends BaseView{

	public static DraftReviewView g_DraftView = null;
	
	public Context m_context;

	private XListView mListView = null;
	private MyListAdapter adapter = null;
	
	ProgressDialog progress = null;

	JSONArray m_arrTotalData = null;//new JSONArray();
	JSONArray m_arrData = null; // new JSONArray();
	
	JSONObject m_starInfo = null;
	
	int m_nFeedType = Global.FEED_ALL;

	int m_nIndexDel = -1;
	
	int m_nPage = 1;
	
	
	public DraftReviewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public DraftReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraftReviewView(Context context) {
        super(context);
        
    }
    
    public void clear () {
    	
    	g_DraftView = null;
    	m_context = null;

    	mListView = null;
    	adapter = null;
    	
    	progress = null;

    	m_arrTotalData = null;
    	m_arrData = null;
    	
    	m_starInfo = null;
    	
    	m_handler = null;
    }
    
        
    
    public void init(Context context) {
    	g_DraftView = this;
    	m_context = context;

    	
    	StarTracker.StarSendView(m_context, "Draft Review");

    	
        ((HomeActivity)context).setMultiOrientation(false);

		mListView = (XListView) findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

    	
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

		JSONObject data = JsonParser.getDraftFeed("all", m_nPage);
		
		try {
			
			if (data == null || !data.getBoolean("status")) {
				m_handler.sendEmptyMessage(-1);
				return;
			}

			m_starInfo = data.getJSONObject("star");

			
			JSONArray feeds = data.getJSONArray("feeds");
			
			if(m_arrTotalData == null || m_nPage == 1)
				m_arrTotalData = feeds;
			else {
				m_arrTotalData = MyJSON.addJSONArray(m_arrTotalData, feeds);
			}
			
			getFilter();
			
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
				
				Const.showMessage("", "Loading failed.", (HomeActivity)m_context);

				break;
			}
			
		}
    };
		
    
    
    private void getFilter() {
    	
    	JSONArray arrTemp = null;
    	
    	if (m_nFeedType == Global.FEED_ALL)  {
    		arrTemp = m_arrTotalData;
        }
    	else {
    		arrTemp = new JSONArray();
    		for(int i=0;i<m_arrTotalData.length();i++){
    	        int feedType = Global.FEED_TEXT;
    	        
    			JSONObject obj = null;
    			String postType = "";
    			
				try {
					obj = m_arrTotalData.getJSONObject(i);
					postType = obj.getString("post_type").toLowerCase();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			
    			if (postType.equalsIgnoreCase("text")) {
    				feedType = Global.FEED_TEXT;
    			} else if (postType.equalsIgnoreCase("photo")) {
    				feedType = Global.FEED_IMAGE;
    			} else if (postType.equalsIgnoreCase("video")) {
    				feedType = Global.FEED_VIDEO;
    			} 
    			
    			if (m_nFeedType == feedType) {
    				arrTemp.put(obj);
    			}
    			
    		}

    	}
    	
    	
    	try {
    		m_arrData = new JSONArray();
    		int i = 0;
    		int nCount = 0;
        	for (i = 0 ; i < arrTemp.length() ; i ++) {
        		JSONObject data = arrTemp.getJSONObject(i);
        		m_arrData.put(data);
//        		if (i % 2 == 0) {
//        			if (nCount < 3){
//                    	JSONObject obj = new JSONObject();
//                    	obj.put("post_type", "banner");
//                    	obj.put("id", nCount);
//                    	m_arrData.put(obj);
//                    	
//                    	nCount ++;
//        			}
//        		}
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
    	 
//    	 m_items.add(new ItemLogo(R.drawable.draftview_logo));
         m_items.add(new ItemHeader());
         
         for (int i = 0 ; i < m_arrData.length() ; i ++) {
        	 JSONObject feed = null;
        	 String postType = "";
			try {
				feed = m_arrData.getJSONObject(i);
				
				postType = feed.getString("post_type");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 
			if (postType.equalsIgnoreCase("photo")
					|| postType.equalsIgnoreCase("video")) {
				if (Global.getUserType() == Global.FAN)
					m_items.add(new ItemImage(m_context, m_starInfo, feed, false, true, false));
				else 
					m_items.add(new ItemImage(m_context, m_starInfo, feed, true, true, true));
			}
			else if (postType.equalsIgnoreCase("text")) {
				if (Global.getUserType() == Global.FAN)
					m_items.add(new ItemText(m_context, m_starInfo, feed, false, true, false));
				else
					m_items.add(new ItemText(m_context, m_starInfo, feed, true, true, true));
			}
			else if (postType.equalsIgnoreCase("banner")) {
				int banner_id = 0;
				try {
					banner_id = feed.getInt("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m_items.add(new ItemBanner(banner_id));
			}
			else { // quiz & poll
				if (Global.getUserType() == Global.FAN)
					m_items.add(new ItemQuizPoll(m_context, m_starInfo, feed, false, true, false));
				else 
					m_items.add(new ItemQuizPoll(m_context, m_starInfo, feed, true, true, true));
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
        			
        			if (MyConstants.ADMOB_MODE == MyConstants.GOOGLE_ADMOB_NOR) {
                		AdView adView = null;
                		
                		String unitId = "";
                		if (itemBanner.idBanner == 0) {
        					unitId = MyConstants.ADMOB_HOME_ID_1;
        				} else if (itemBanner.idBanner == 1) {
        					unitId = MyConstants.ADMOB_HOME_ID_2;
        				} else {
        					unitId = MyConstants.ADMOB_HOME_ID_3;
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
        					unitId = MyConstants.ADMOB_HOME_ID_1;
        				} else if (itemBanner.idBanner == 1) {
        					unitId = MyConstants.ADMOB_HOME_ID_2;
        				} else {
        					unitId = MyConstants.ADMOB_HOME_ID_3;
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
        	if (type == RowType.HEADER_ITEM) {
        		
        	}
        	if (type == RowType.IMAGE_ITEM) {
        		final ItemImage itemImage = (ItemImage) item;
        		
        		itemImage.btnLike.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							onDraftUser(position);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
        		
    			itemImage.ivPhoto.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View arg0) {
    					onClickPhoto((ImageView)arg0);
					}
    			});
    			itemImage.ivPhoto.setTag(position);

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

    			itemImage.layoutMsg1.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
    			itemImage.layoutMsg1.setTag(position * 3 + 0);

    			itemImage.layoutMsg2.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
    			itemImage.layoutMsg2.setTag(position * 3 + 1);

    			itemImage.layoutMsg3.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
    			itemImage.layoutMsg3.setTag(position * 3 + 2);
    			
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
							onApprovePublish(v);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			});
    	        itemImage.layoutShare.setTag(position);
    	        
        	}
        	if (type == RowType.TEXT_ITEM) {
        		final ItemText itemText = (ItemText) item;
        		

        		itemText.btnLike.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							onDraftUser(position);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});


        		itemText.lockLayout.setOnClickListener(new OnClickListener() {
    				
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
        		itemText.lockLayout.setTag(position);

        		itemText.layoutMsg1.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
        		itemText.layoutMsg1.setTag(position * 3 + 0);

        		itemText.layoutMsg2.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
        		itemText.layoutMsg2.setTag(position * 3 + 1);

        		itemText.layoutMsg3.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
        		itemText.layoutMsg3.setTag(position * 3 + 2);
    			
        		itemText.layoutComment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					onClickComment( v);
    				}
    			});
        		itemText.layoutComment.setTag(position);

        		itemText.layoutAddComment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					onClickAddComment( v);
    				}
    			});
        		itemText.layoutAddComment.setTag(position);
    			
        		itemText.layoutShare.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					try {
							onApprovePublish(v);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			});
        		itemText.layoutShare.setTag(position);
        	}
        	
        	if (type == RowType.QUIZ_POLL_ITEM) {

        		final ItemQuizPoll itemQuizPoll = (ItemQuizPoll) item;
        		
        		itemQuizPoll.btnLike.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							onDraftUser(position);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
        		
    			itemQuizPoll.btnStart.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View arg0) {
    					onClickStart(arg0);
					}
    			});
    			itemQuizPoll.btnStart.setTag(position);

    			itemQuizPoll.lockLayout.setOnClickListener(new OnClickListener() {
    				
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
    			itemQuizPoll.lockLayout.setTag(position);

    			itemQuizPoll.layoutMsg1.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
    			itemQuizPoll.layoutMsg1.setTag(position * 3 + 0);

    			itemQuizPoll.layoutMsg2.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
    			itemQuizPoll.layoutMsg2.setTag(position * 3 + 1);

    			itemQuizPoll.layoutMsg3.setOnClickListener(new OnClickListener() {
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						onClickMsgUser(v);
    					}
    				});
    			itemQuizPoll.layoutMsg3.setTag(position * 3 + 2);
    			
    			itemQuizPoll.layoutComment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					onClickComment( v);
    				}
    			});
    			itemQuizPoll.layoutComment.setTag(position);

    			itemQuizPoll.layoutAddComment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					onClickAddComment( v);
    				}
    			});
    			itemQuizPoll.layoutAddComment.setTag(position);
    			
    	        itemQuizPoll.layoutShare.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					try {
							onApprovePublish(v);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			});
    	        itemQuizPoll.layoutShare.setTag(position);
    	        
        	
        	}
        	
            return v; //items.get(position).getView(inflater, convertView);
        }
    }
    
    
    public void onDraftUser(final int position) throws JSONException {

    	int index = position - 1;
    	
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
     	    	subView.init(m_context, feed, true);
     	    	
     	    }
     	});
    	
    }
 
    /////////////////////////// Button
    public void gotoPhotoDetail(final JSONObject feed) throws JSONException {

        String url_link = feed.getString("url_link");
        if (url_link != null && url_link.length() > 0) {
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_link));
        	((HomeActivity)m_context).startActivity(browserIntent);
        	return;
        }

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
    @SuppressLint("DefaultLocale")
    public void gotoVideoDetail(JSONObject feed) throws JSONException {
    	
    	String videoUrl = "";
    	try {
    		videoUrl = feed.getString("destination");
    		if (videoUrl == null || videoUrl.length() < 1) {
    			videoUrl = feed.getString("video_url");
    		}
    	} catch (JSONException e) {
    		
    		videoUrl = feed.getString("video_url");
    	}

    	String brightVideoId = "";
     	try {
     		brightVideoId = feed.getString("brightcove_media_id");
     	} catch (JSONException e) {
     	}
     	
    	if (videoUrl.toLowerCase().contains("youtube")
    			|| videoUrl.toLowerCase().contains("watch?")
    			|| videoUrl.toLowerCase().contains("youtu.be")) {
    		YoutubeView subView = (YoutubeView) LayoutInflater.from(m_context).inflate(R.layout.view_youtube, null);
    		subView.init(m_context, videoUrl);
    		
    		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    				ViewGroup.LayoutParams.FILL_PARENT,
    				ViewGroup.LayoutParams.FILL_PARENT); 
    		((HomeActivity)m_context).baselayout.addView(subView, params);
    		
    		Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    		subView.startAnimation(out);
    	}
    	else {
//    		Intent intent = new Intent(Intent.ACTION_VIEW);
//    		intent.setDataAndType(Uri.parse(videoUrl), "video/*");
//    		m_context.startActivity(intent);
    		Intent intent = new Intent((HomeActivity)m_context, PlayerActivity.class);
    		intent.putExtra("file_name", videoUrl);
    		intent.putExtra("bright_video_id", brightVideoId);
    		((HomeActivity)m_context).startActivity(intent);
    		
    	}
        	
    }    
 	
    public void onClickStart(View v) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(v.getTag().toString()) - 1;
 		
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
	    	Const.showMessage("NOTE", "You already in this post", (HomeActivity)m_context);
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

 	public void onClickPhoto(ImageView btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString()) - 1;
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 		
 		String postType = "";
		
		try {
			postType = feed.getString("post_type").toLowerCase();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int feedType = 0;
		if (postType.equalsIgnoreCase("text")) {
			feedType = Global.FEED_TEXT;
		} else if (postType.equalsIgnoreCase("photo")) {
			feedType = Global.FEED_IMAGE;
		} else if (postType.equalsIgnoreCase("video")) {
			feedType = Global.FEED_VIDEO;
		} 
		
 		if (feedType == Global.FEED_IMAGE)
			try {
				gotoPhotoDetail(feed);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else {
			try {
				gotoVideoDetail(feed);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 	}
 	
 	
 	public void onClickLock(FrameLayout btn) throws JSONException {
 		
 		final int pos = Integer.parseInt(btn.getTag().toString());
 		final int selLock = pos - 1;
 		
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
 		
 		index = Integer.parseInt(btn.getTag().toString()) - 1;
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
//		CommentView subView = new CommentView(m_context);
		
    	subView.init(m_context, feed, index, false, Global.OPT_DRAFTVIEW);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);

 	}
 	
 	public void onClickAddComment(View btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString()) - 1;
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
    	subView.init(m_context, feed, index, true, Global.OPT_DRAFTVIEW);
    	
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
 			
 			feed.put("comments", arrComments);
 			feed.put("comments_count", totalcomment);
 			
 			m_arrData.put(index, feed);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		refresh();
 	}
 	
 	public void onClickMsgUser(View v) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(v.getTag().toString());
 		
 		int feedId = index / 3 - 1;
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

		
 	public void onApprovePublish(View btn) throws JSONException {

 		final int index = Integer.parseInt(btn.getTag().toString()) - 1;
 		
 		final JSONObject feed = m_arrData.getJSONObject(index);
 		
 		if (Global.getUserType() == Global.FAN) {
 			
 			String msg = "Do you want to approve this feed?";
 	        
 	    	AlertDialog.Builder dialog = new AlertDialog.Builder((HomeActivity) m_context);
 			dialog.setMessage(msg);
 			dialog.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
 				public void onClick(DialogInterface dialog,int id) {
 					// if this button is clicked, close
 					dialog.cancel();
 				}
 			  });
 			dialog.setNegativeButton("Approve",new DialogInterface.OnClickListener() {
 				public void onClick(DialogInterface dialog,int id) {
 					// if this button is clicked, just close
 					// the dialog box and do nothing
 					
 					try {
						approveFeed(feed, index);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 				}
 			});
 			dialog.show();
 			
 		}
 		else { // publish page
 			
 			onSharePage(m_context, feed, true);
 			
 		}
 	}

 	public void approveFeed(final JSONObject feed, final int index) throws JSONException {
 		
 		boolean did_approve = feed.getBoolean("did_approve");
 		if (did_approve == true) {
 			Const.showToastMessage("Already approved!", m_context);
 			return;
 		}
 		
 		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Sending Approval...");
    	progress.show();
    
    	new Thread(new Runnable() {
			
			public void run() {
				try {
					
					String postType = feed.getString("post_type");
					String contentId = feed.getString("content_id");
					
					JSONObject data = JsonParser.setApprove(postType, contentId, 1);
					
					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					int numbersofapproval = data.getInt("numbersofapproval");
					
					feed.put("numberofapproval", numbersofapproval);
					feed.put("did_approve", true);

					
					m_arrData.put(index, feed);
					
					m_handler.sendEmptyMessage(1);

				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
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
