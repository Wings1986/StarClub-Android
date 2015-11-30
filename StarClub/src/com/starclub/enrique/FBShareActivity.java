package com.starclub.enrique;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;







import com.custom.items.Item;
import com.custom.items.ItemBanner;
import com.custom.items.ItemHeader;
import com.custom.items.ItemImage;
import com.custom.items.ItemLogo;
import com.custom.items.ItemShare;
import com.custom.items.ItemShareHeader;
import com.custom.items.ItemText;
import com.custom.items.RowType;
import com.facebook.AppEventsLogger;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphObject;
import com.starclub.enrique.R;
import com.mycom.data.Global;
import com.mycom.data.Global.FB;
import com.mycom.data.MyConstants;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.Utils;


public class FBShareActivity<ResultListAdapter> extends Activity {
    
//    static final String PENDING_REQUEST_BUNDLE_KEY = "com.facebook.samples.graphapi:PendingRequest";
//    Session session;
//    boolean pendingRequest;
    
    JSONArray m_arrPages;
    
    public Context m_context = null;

	private ListView mListView = null;
	private MyListAdapter 	adapter = null;

	ProgressDialog progress = null;
	Button btnFBLogin = null;
	
    private Session createSession() {
        Session activeSession = Session.getActiveSession();
        if (activeSession == null || activeSession.getState().isClosed()) {
            activeSession = new Session.Builder(this).setApplicationId(MyConstants.FACEBOOK_ID).build();
            Session.setActiveSession(activeSession);
        }
        return activeSession;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        setContentView(R.layout.activity_fb_page);

        UserDefault.init(this);
        
        m_context = this;
        
        Global.FB.session = createSession();
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);


        setInterface();
        
        refreshFbButton();
        
        if (Global.FB.session != null && Global.FB.session.isOpened()) {
        	getFBPageInfo();
        }
        else {
        }
        
    }

    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Global.FB.session.onActivityResult(this, requestCode, resultCode, data) &&
        		Global.FB.pendingRequest &&
        		Global.FB.session.getState().isOpened()) {
            sendRequests();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this, MyConstants.FACEBOOK_ID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Global.FB.pendingRequest = savedInstanceState.getBoolean(Global.FB.PENDING_REQUEST_BUNDLE_KEY, Global.FB.pendingRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(Global.FB.PENDING_REQUEST_BUNDLE_KEY, Global.FB.pendingRequest);
    }
    
    public void refreshFbButton() {
    	if (Global.FB.session != null && Global.FB.session.isOpened()) {
    		btnFBLogin.setBackgroundResource(R.drawable.btn_fb_logout);
    	}
    	else {
    		btnFBLogin.setBackgroundResource(R.drawable.btn_fb_login);
    	}
    }
    public void onFacebookLogin() {
    	
    	if (Global.FB.session != null && Global.FB.session.isOpened()) {
    		// log out
    		
    		JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
    		
    		String fb_user = "";
    		try {
    			fb_user = userInfo.getString("facebook");
    		} catch (Exception e) {}
    		
    		if (fb_user.equalsIgnoreCase("1")) {
    			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    			dialog.setTitle("Logging our of Facebook will also log you out of the app");
    			dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {

    					dialog.cancel();
    					
    				}
    			});

    			dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
    				
    				@Override
    				public void onClick(DialogInterface dialog, int which) {
    					// TODO Auto-generated method stub
    					
    					dialog.cancel();
    					
    					HomeActivity.signOut();
    					
    				}
    			});
    			
    			dialog.show();
    		}
    		else {
    			HomeActivity.logOutFB();
    			
    			refreshFbButton();
    			
    			if (adapter != null) {
                    adapter.setItem(new ArrayList<Item>());
             		adapter.notifyDataSetChanged();
    			}
    		}
    		
    	}
    	else {
    		
    		Global.FB.session = createSession();

    		getFBPageInfo();
    	}
    	
    	
    }
    
    
    public void setInterface() {
    	Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	    		onBackPressed();
			}
		});
    	
    	btnFBLogin = (Button) findViewById(R.id.btnFBLogin);
    	btnFBLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onFacebookLogin();
			}
		});
    	
    	mListView = (ListView) findViewById(R.id.listview);
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 1) { // timeline
					
					uploadSocialShare("");
				}
				else if (arg2 >= 3) {// page

					try {
						
						JSONObject pageInfo = null;
		        		 String pageId = "";
						
							pageInfo = m_arrPages.getJSONObject(arg2-3);
							
							pageId = pageInfo.getString("id");
							
							uploadSocialShare(pageId);
							
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
    	
    }
    
    private void uploadSocialShare(final String pageId) {
    	
    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Setting...");
    	
		progress.show();
		
		
    	new Thread(){
			public void run(){
				HttpParams myParams = new BasicHttpParams();

				 HttpConnectionParams.setConnectionTimeout(myParams, 30000);
				 HttpConnectionParams.setSoTimeout(myParams, 30000);
				 
				 DefaultHttpClient hc= new DefaultHttpClient(myParams);
				 ResponseHandler <String> res=new BasicResponseHandler();
				
				 String url = Utils.getUpdateUrl();
				 HttpPost postMethod = new HttpPost(url);
				

				 String responseBody = "";
				 try {
				 MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				 reqEntity.addPart("cid",	 		new StringBody("" + MyConstants.CID));
				 reqEntity.addPart("token",	 		new StringBody("" + Utils.getUserToken()));
				 reqEntity.addPart("user_id", 		new StringBody("" + Utils.getUserID()));
				 reqEntity.addPart("ud_token", 		new StringBody("" + Utils.getDeviceToken()));
				 
				 reqEntity.addPart("facebook_page_id", 		new StringBody(pageId));
				 
				 postMethod.setEntity(reqEntity);
				 responseBody = hc.execute(postMethod,res);
				 
				 System.out.println("update result = " + responseBody);
				 JSONObject result = new JSONObject(responseBody);
					if (result != null) {
						boolean status = result.optBoolean("status"); 
						if (status) {

							Message msg = new Message();
							msg.obj = result.getJSONObject("info");
							msg.what = 1;
							m_handler.sendMessage(msg);
							
							return;
						}
					}
				 } catch (UnsupportedEncodingException e) {
					 e.printStackTrace();
				 } catch (ClientProtocolException e) {
					 e.printStackTrace();
				 } catch (IOException e) {
					 e.printStackTrace();
				 } catch (JSONException e) {
				 }
				 
				 m_handler.sendEmptyMessage(-2);
			}
    	}.start();
    	
    	
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
				
				break;
				
			case 1: 

				JSONObject userInfo = (JSONObject) msg.obj;
				
				UserDefault.setDictionaryForKey(userInfo, "user_info");
				
				refresh();
				
				break;
				
				
			case -1:
				Const.showMessage("", "Network is failed.", (FBShareActivity<?>)m_context);
				break;
				
			case -2:
				
				break;
			}
			
		}
   };
   
	public void getFBPageInfo() {
    	if (Global.FB.session != null && Global.FB.session.isOpened()) {
            sendRequests();
        } 
        else {
        	StatusCallback callback = new StatusCallback() {
                public void call(Session session, SessionState state, Exception exception) {
                    if (exception != null) {
                        new AlertDialog.Builder(FBShareActivity.this)
                                .setTitle("Login failed")
                                .setMessage(exception.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                        Global.FB.session = createSession();
                    } else if (session.isOpened()) {
                        sendRequests();
                    }
                }
            };
            Global.FB.pendingRequest = true;
            
            Session.OpenRequest openRequest = null;
            openRequest = new Session.OpenRequest(this);

            if (openRequest != null) {
                openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
                openRequest.setPermissions(Arrays.asList("publish_actions",
                      "manage_pages", "status_update"));
                openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);

                Global.FB.session.openForPublish(openRequest.setCallback(callback));
                
            }	
        }

    }
	
    private void sendRequests() {
        if (Global.FB.session != null) {
            Log.d("FB", "groupToken request!");
            Request myreq = Request.newGraphPathRequest(Global.FB.session, "me/accounts",
                    new Request.Callback() {

                        @Override
                        public void onCompleted(Response response) {
                            Log.d("FB", "received token");
                            GraphObject obj = response.getGraphObject();
                            JSONObject json = obj.getInnerJSONObject();
                            try {
                                
                                m_arrPages = json.getJSONArray("data");
                                
                                refresh();
                                
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }

                        }

                    });
            myreq.executeAsync();
        } else {
            Log.d("FB", "Session is closed");
        }
    }
    
    public void refresh() {
    	
    	refreshFbButton();
    	
    	
		JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
		
		String save_pagename = "";
		try {
			save_pagename = userInfo.getString("facebook_page_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    	
    	List<Item> m_items = new ArrayList<Item>();

    	m_items.clear();
    	 
         m_items.add(new ItemShareHeader(""));
         
         if (save_pagename.equals("") || save_pagename.equals("Timeline")) {
             m_items.add(new ItemShare(null, true));
         } else {
        	 m_items.add(new ItemShare(null, false)); 
         }
         
         if (m_arrPages != null && m_arrPages.length() > 0) {
        	 m_items.add(new ItemShareHeader("CHOOSE A PAGE"));
             
        	 for (int i = 0 ; i < m_arrPages.length() ; i ++) {
        		 JSONObject pageInfo = null;
        		 String page_name = "";
				
        		 try {
					
					pageInfo = m_arrPages.getJSONObject(i);
					
					page_name = pageInfo.getString("id");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		 
        		 
        		 if (page_name.equals(save_pagename)) {
        			 m_items.add(new ItemShare(pageInfo, true));
        		 }
        		 else {
        			 m_items.add(new ItemShare(pageInfo, false));
        		 }
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
        	    
        	}
        	if (type == RowType.HEADER_ITEM) {
        		
        	}
        	if (type == RowType.IMAGE_ITEM) {

        	}
        	if (type == RowType.TEXT_ITEM) {
        		
        	}
        	
            return v; //items.get(position).getView(inflater, convertView);
        }
    }
    
}
