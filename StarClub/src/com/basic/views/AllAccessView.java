package com.basic.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.MyConstants;
import com.mycom.data.Global;
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

import com.custom.items.*;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;


public class AllAccessView extends BaseView{

	public static AllAccessView g_AllAccessView = null;
	
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
	
	
	public AllAccessView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public AllAccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllAccessView(Context context) {
        super(context);
        
    }
    
    public void clear () {
    	
    	g_AllAccessView = null;
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
    	g_AllAccessView = this;
    	m_context = context;

    	
    	StarTracker.StarSendView(m_context, MyConstants.MENU_MAIN_FEED);

    	
    	
        ((HomeActivity)context).setMultiOrientation(false);

//    	mListView = (ListView) findViewById(R.id.listview);
    	
		mListView = (XListView) findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
//		mListView.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				m_nRefreshListView = 1;
//				m_nPage = 1;
//				initData();
//
//			}
//		});    	
//		mListView.setOnLoadMoreListener(new OnLoadMoreListener() {
//			
//			@Override
//			public void onLoadMore() {
//				// TODO Auto-generated method stub
//				m_nRefreshListView = 2;
//				initData();
//			}
//		});
		

		if (Global.getUserType() == Global.ADMIN_CMS || 
				Global.getUserType() == Global.ADMIN_POST) {
			((HomeActivity)m_context).btnNavRight.setText("");
			((HomeActivity)m_context).btnNavRight.setBackgroundResource(R.drawable.btn_plus);
			
			((HomeActivity)m_context).btnNavRight.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final String items[] = {"Add Post", "Update"};
			 		
			 		AlertDialog.Builder ab=new AlertDialog.Builder((HomeActivity)m_context);
			 		ab.setTitle("Create Post");
			 		ab.setItems(items, new DialogInterface.OnClickListener() {

			 			public void onClick(DialogInterface d, int choice) {
			 		 		if(choice == 0) {
			 		 			onAddPost();
			 		 		}
			 		 		else if(choice == 1) {
			 		 			onUpdate();
			 		 		}
			 		 		
			 		 	}
			 		 });
			 		 ab.show();
				}
			});
		} else {
	    	((HomeActivity)m_context).setRightBtnItem(true, "Update");
	    	((HomeActivity)m_context).btnNavRight.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onUpdate();
				}
			});
		}
    	
    	
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

		JSONObject data = JsonParser.getMainFeed("all", m_nPage);
		
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
			
			try {
				JSONObject channelInfo = data.getJSONObject("channel_info");
				m_EnableBanner = channelInfo.getInt("enable_banner_ads_android");
			} catch(Exception e) {}
			
			
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
		
    private void cellRefresh(int position, int nSelected, JSONObject obj) {
    	int numberOfLike = 0;
		try {
			numberOfLike = obj.getInt("numbersoflike");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println("position = " + position + " numberoflike = " + numberOfLike);
        
        int visiblePosition = mListView.getFirstVisiblePosition();
        View convertView = mListView.getChildAt(position-visiblePosition);
        mListView.getAdapter().getView(position, convertView, mListView);
//		View convertView = mListView.getAdapter().getView(position, null, mListView);
		
//		ViewCache holder = (ViewCache) convertView.getTag();
//		System.out.println("view id = " + holder.tvTimeStamp.getText().toString());
//		
//		holder.tvNumOfLike.setText("" + numberOfLike);
		
		TextView tvNumOfLike = (TextView) convertView.findViewById(R.id.txtLikeNum);
		tvNumOfLike.setText("" + numberOfLike);
		
		ToggleButton btnLike = (ToggleButton) convertView.findViewById(R.id.btnLike);
		btnLike.setChecked(nSelected==0 ? false : true);
		
    }
    
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
    	if (m_nPage == 2) {
        	String adminName = "";
        	try {
    			adminName = m_starInfo.getString("name");
    		} catch (JSONException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        	((HomeActivity)m_context).setMenuItem(adminName);
    	}
    	
    	System.gc();
    	
    	System.out.println("arrData = " + m_arrData.length());
    	
    	
    	 m_items.clear();
    	 
         m_items.add(new ItemLogo());
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
					m_items.add(new ItemImage(m_context, m_starInfo, feed, false));
				else 
					m_items.add(new ItemImage(m_context, m_starInfo, feed, true));
			}
			else if (postType.equalsIgnoreCase("text")) {
				if (Global.getUserType() == Global.FAN)
					m_items.add(new ItemText(m_context, m_starInfo, feed, false));
				else
					m_items.add(new ItemText(m_context, m_starInfo, feed, true));
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
         }
         
         if (adapter == null) {
     		LayoutInflater inflater = LayoutInflater.from(m_context);
     	    adapter = new MyListAdapter(m_context, inflater, m_items);
     	    mListView.setAdapter(adapter);
         } else {
             	adapter.setItem(m_items);
     			adapter.notifyDataSetChanged();
         }

         System.out.println("count = " + m_items.size());
         
//         if (m_nRefreshListView == 1)
//        	 mListView.onRefreshComplete();
//         else if (m_nRefreshListView == 2)
//        	 mListView.onLoadMoreComplete();
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
        		ItemHeader itemHeader = (ItemHeader) item;
        		
        		itemHeader.btnAll.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						m_nFeedType = Global.FEED_ALL;
						refresh();
					}
				});
		       	if (m_nFeedType == Global.FEED_ALL) {
		       		itemHeader.btnAll.setTypeface(null, Typeface.BOLD);
		       	}
		       	
		       	itemHeader.btnText.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						m_nFeedType = Global.FEED_TEXT;
						refresh();
					}
				});
		       	if (m_nFeedType == Global.FEED_TEXT) {
		       		itemHeader.btnText.setTypeface(null, Typeface.BOLD);
		       	}

		       	itemHeader.btnImage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						m_nFeedType = Global.FEED_IMAGE;
						refresh();
					}
				});
		       	if (m_nFeedType == Global.FEED_IMAGE) {
		       		itemHeader.btnImage.setTypeface(null, Typeface.BOLD);
		       	}

		       	
		       	itemHeader.btnVideo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						m_nFeedType = Global.FEED_VIDEO;
						refresh();
					}
				});
		       	if (m_nFeedType == Global.FEED_VIDEO) {
		       		itemHeader.btnVideo.setTypeface(null, Typeface.BOLD);
		       	}
        		
        	}
        	if (type == RowType.IMAGE_ITEM) {
        		final ItemImage itemImage = (ItemImage) item;
        		
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
							onClickShare(v);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			});
    	        itemImage.layoutShare.setTag(position);

    	        if (itemImage.bIsMore) {
    	        	itemImage.layoutMore.setOnClickListener(new OnClickListener() {
        				
        				@Override
        				public void onClick(View v) {
        					// TODO Auto-generated method stub
        					try {
    							onClickMore(v);
    						} catch (JSONException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
        				}
        			});
    	        	itemImage.layoutMore.setTag(position);
    	        }
    	        
    	        
        	}
        	if (type == RowType.TEXT_ITEM) {
        		final ItemText itemText = (ItemText) item;
        		
        		itemText.tvNumOfLike.setOnClickListener(new OnClickListener() {
					
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

        		itemText.btnLike.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						onLikeButton(position);
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
							onClickShare(v);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			});
        		itemText.layoutShare.setTag(position);

        		if (itemText.bIsMore) {
        			itemText.layoutMore.setOnClickListener(new OnClickListener() {
        				
        				@Override
        				public void onClick(View v) {
        					// TODO Auto-generated method stub
        					try {
    							onClickMore(v);
    						} catch (JSONException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
        				}
        			});
        			itemText.layoutMore.setTag(position);
    	        }
        	}
        	
            return v; //items.get(position).getView(inflater, convertView);
        }
    }
    
    public void onUpdate() {
    	UpdateView subView = (UpdateView) LayoutInflater.from(m_context).inflate(R.layout.view_update, null);
    	subView.init(m_context);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);
    }
    
    public void onLikeList(final int position) throws JSONException {
    	
    	int index = position - 2;
    	
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
		progress.setMessage("Liking....");
    	progress.show();
    	
    	new Thread(new Runnable() {
			
			public void run() {
				try {
					
					int index = position - 2;
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
 	
 	public void onClickPhoto(ImageView btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString()) - 2;
 		
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
 		final int selLock = pos - 2;
 		
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
 		
 		index = Integer.parseInt(btn.getTag().toString()) - 2;
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
//		CommentView subView = new CommentView(m_context);
		
    	subView.init(m_context, feed, index, false, Global.OPT_ALLACCESS);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);

 	}
 	
 	public void onClickAddComment(View btn) {
 		
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString()) - 2;
 		
 		JSONObject feed = null;
		try {
			feed = m_arrData.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CommentView subView = (CommentView) LayoutInflater.from(m_context).inflate(R.layout.view_comment, null);
    	subView.init(m_context, feed, index, true, Global.OPT_ALLACCESS);
    	
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
 		
 		int feedId = index / 3 - 2;
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

		
 	public void onClickShare(View btn) throws JSONException {
 		
 		final int index = Integer.parseInt(btn.getTag().toString()) - 2;
 		
 		System.out.println("index = " + index);
 		
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
 				
		 	 		Item item = m_items.get(index + 2);
		 	 		int type = item.getViewType();
		 	 		if (type == RowType.TEXT_ITEM) {
 		 		    	image = null;
 		 		    } else {
 		 		    	ItemImage itemImage = (ItemImage) item;
 		 		    	image = ((BitmapDrawable)itemImage.ivPhoto.getDrawable()).getBitmap();
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

 	public void onClickMore(View btn) throws JSONException {
 		int index = 0;
 		
 		index = Integer.parseInt(btn.getTag().toString()) - 2;
 		m_nIndexDel = index;
 		
 		AlertDialog.Builder dialog = new AlertDialog.Builder((HomeActivity) m_context);
			dialog.setTitle("NOTE!");
			dialog.setMessage("Do you want to remove this post?");
			dialog.setIcon(android.R.drawable.ic_dialog_info);
			dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					deleteFeed();
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
 	private void deleteFeed() {
    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Deleting...");
    	progress.show();
    	
    	new Thread(new Runnable() {
			
			public void run() {
				try {
					
					int index = m_nIndexDel;
					JSONObject feed = m_arrData.getJSONObject(index);
					
					String postType = feed.getString("post_type");
					String contentId = feed.getString("content_id");
				
					JSONObject data = JsonParser.getDeleteFeed(postType, contentId);
				
					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					m_arrData = MyJSON.RemoveJSONArray(m_arrData, feed);
					
					m_handler.sendEmptyMessage(1);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				
			}
		}).start();
    }
 	
 	/// Add post
 	public void onAddPost() {
 		final String items[] = {"Add Text", "Add Photo", "Add Video"};
 		
 		AlertDialog.Builder ab=new AlertDialog.Builder((HomeActivity)m_context);
 		ab.setTitle("Create Post");
 		ab.setItems(items, new DialogInterface.OnClickListener() {

 			public void onClick(DialogInterface d, int choice) {
 		 		if(choice == 0) {
 		 			gotoAddText();
 		 		}
 		 		else if(choice == 1) {
 		 			gotoAddImage();
 		 		}
 		 		else if(choice == 2) {
 		 			gotoAddVideo();
 		 		}
 		 	}
 		 });
 		 ab.show();
 	}
 	
 	private void gotoAddText() {
 		AddTextView subView = (AddTextView) LayoutInflater.from(m_context).inflate(R.layout.view_add_text, null);
    	subView.init(m_context, Global.OPT_ALLACCESS);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);
 	}
 	private void gotoAddImage() {
 		AddPhotoView subView = (AddPhotoView) LayoutInflater.from(m_context).inflate(R.layout.view_add_image, null);
    	subView.init(m_context, Global.OPT_ALLACCESS);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);
 	}
 	private void gotoAddVideo() {
 		AddVideoView subView = (AddVideoView) LayoutInflater.from(m_context).inflate(R.layout.view_add_video, null);
    	subView.init(m_context, Global.OPT_ALLACCESS);
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
    	        ViewGroup.LayoutParams.FILL_PARENT,
    	        ViewGroup.LayoutParams.FILL_PARENT); 
    	((HomeActivity)m_context).baselayout.addView(subView, params);
    	
    	Animation out = AnimationUtils.loadAnimation(m_context, R.anim.slide_left);
    	subView.startAnimation(out);
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
