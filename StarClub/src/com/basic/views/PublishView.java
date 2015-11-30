package com.basic.views;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

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

import com.custom.items.Item;
import com.custom.items.ItemImage;
import com.custom.items.RowType;
import com.mycom.data.Global;
import com.mycom.data.MyConstants;
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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;


public class PublishView extends BasePostView {

	public Context m_context;
	public static PublishView g_publishView;
	
	private boolean m_bIsPublish = false;
	
	
	ProgressDialog progress = null;
	
	ImageView ivPhoto = null;
	
	JSONObject m_feed = null;
	
    int m_bEnableFb = 0;
    int m_bEnableTw = 0;
    int m_bEnableIns = 0;
    
    
	public PublishView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public PublishView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PublishView(Context context) {
        super(context);
        
    }
    
    public void initPublish(Context context, JSONObject feed) {
    	m_bIsPublish = true;
    	init(context, feed);
    }
    public void initShare(Context context, JSONObject feed) {
    	m_bIsPublish = false;
    	init(context, feed);
    }
    
    private void init(Context context, final JSONObject feed) {
    	m_context = context;
    	g_publishView = this;
    	m_feed = feed;
    	
    	((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
    	
    	Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});

    	final EditText tvCaption = (EditText) findViewById(R.id.ivCaption);
		String caption = "";
		try {
			caption = feed.getString("caption");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tvCaption.setText(caption);
		
	    ImageView ivVideoIcon = (ImageView) findViewById(R.id.ivVideoIcon);
	    ivVideoIcon.setVisibility(View.GONE);
	    String postType = "";
	    try {
			postType = feed.getString("post_type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if (postType.equals("video")) {
	    	ivVideoIcon.setVisibility(View.VISIBLE);
	    }
	    
	    
    	Button btnPublish = (Button) findViewById(R.id.btnPublish);
    	btnPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPublish(feed);
			}
		});
    	if (!m_bIsPublish) {
    		btnPublish.setVisibility(View.GONE);
    	}
    	
    	
    	LinearLayout shareLayout = (LinearLayout) findViewById(R.id.shareLayout);
    	shareLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeCaptionText(tvCaption.getText().toString());
				onShare();
			}
		});

    	
    	
    	LinearLayout fbLayout = (LinearLayout) findViewById(R.id.facebookLayout);
    	fbLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m_bEnableFb = m_bEnableFb == 0 ? 1 : 0;
				uploadSocialShare(m_bEnableFb == 1 ? "Enable Facebook" : "Disable Facebook");
			}
		});
    	
    	LinearLayout twLayout = (LinearLayout) findViewById(R.id.twitterLayout);
    	twLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m_bEnableTw = m_bEnableTw == 0 ? 1 : 0;
				uploadSocialShare(m_bEnableTw == 1 ? "Enable Twitter" : "Disable Twitter");
				
			}
		});
    	
    	LinearLayout instLayout = (LinearLayout) findViewById(R.id.instagramLayout);
    	instLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m_bEnableIns = m_bEnableIns == 0 ? 1 : 0;
				uploadSocialShare(m_bEnableIns == 1 ? "Enable Instagrame" : "Disable Instagram");
			}
		});
    	
    	
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
    	
    	try {
        	m_bEnableFb = userInfo.getInt("enable_facebook");
        }catch (JSONException e){};

    	try {
        	m_bEnableTw = userInfo.getInt("enable_twitter");
        }catch (JSONException e){};

    	try {
        	m_bEnableIns = userInfo.getInt("enable_instagram");
        }catch (JSONException e){};

    	try {
    		((HomeActivity)m_context).m_bEnableFbCaption = userInfo.getInt("enable_facebook_custom_caption");
        }catch (JSONException e){};
 
       	try {
    		((HomeActivity)m_context).m_bEnableTWCaption = userInfo.getInt("enable_twitter_custom_caption");
        }catch (JSONException e){};
        
        refreshSocialBtn();
        
    	
		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
		ivPhoto.setScaleType(ScaleType.FIT_CENTER);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressPhoto);
		
    	String imgUrl = "";
		try {
			imgUrl = feed.getString("image_path");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		if (imgUrl.equals("")) {
			ivPhoto.setImageResource(R.drawable.starclub_logo);
			setResultDic(m_feed, null);
		}
		else {
			
		((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivPhoto, ((HomeActivity)m_context).optFull, new SimpleImageLoadingListener() {
			 @Override
			 public void onLoadingStarted(String imageUri, View view) {
				 progressBar.setProgress(0);
				 progressBar.setVisibility(View.VISIBLE);
			 }

			 @Override
			 public void onLoadingFailed(String imageUri, View view,
					 FailReason failReason) {
				 progressBar.setVisibility(View.GONE);
			 }

			 @Override
			 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				 progressBar.setVisibility(View.GONE);
				 
			        String postType = "";
				    try {
						postType = m_feed.getString("post_type");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    Bitmap image = null;
			        if (postType.equals("text"))
			 			image = null;
			 		else
			 			image = ((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
			        
			    	setResultDic(m_feed, image);

			 }
		 }, new ImageLoadingProgressListener() {
			 @Override
			 public void onProgressUpdate(String imageUri, View view, int current,
					 int total) {
				 progressBar.setProgress(Math.round(100.0f * current / total));
			 }
		 }
		);	
		}
		
		
    }
    
    
    private void onPublish (final JSONObject feed) {

		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Publishing....");
    	progress.show();
    
		new Thread(new Runnable() {
			
			public void run() {
				try {

					String postType = feed.getString("post_type");
					String contentId = feed.getString("content_id");
					
					
					JSONObject data = JsonParser.setPublish(postType, contentId);


					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					m_handler.sendEmptyMessage(1);
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
    
    private void refreshSocialBtn() {
        ImageView imgEnableFb = (ImageView) findViewById(R.id.facebookIcon);
        TextView textEnableFb = (TextView) findViewById(R.id.facebookText);
        if (m_bEnableFb == 0) {
        	imgEnableFb.setBackgroundResource(R.drawable.facebook_disable_icon);
        	textEnableFb.setTextColor(Color.BLACK);
        }
        else {
        	imgEnableFb.setBackgroundResource(R.drawable.facebook_icon);
        	textEnableFb.setTextColor(Color.parseColor("#3B5999"));
        }

        ImageView imgEnableTw = (ImageView) findViewById(R.id.twitterIcon);
        TextView textEnabletw = (TextView) findViewById(R.id.twitterText);
        if (m_bEnableTw == 0) {
        	imgEnableTw.setBackgroundResource(R.drawable.twitter_disable_icon);
        	textEnabletw.setTextColor(Color.BLACK);
        }
        else {
        	imgEnableTw.setBackgroundResource(R.drawable.twitter_icon);
        	textEnabletw.setTextColor(Color.parseColor("#1bb2e9"));
        }

        ImageView imgEnableInsta = (ImageView) findViewById(R.id.instagramIcon);
        TextView textEnableInsta = (TextView) findViewById(R.id.instagramText);
        if (m_bEnableIns == 0) {
        	imgEnableInsta.setBackgroundResource(R.drawable.instagram_disable_icon);
        	textEnableInsta.setTextColor(Color.BLACK);
        }
        else {
        	imgEnableInsta.setBackgroundResource(R.drawable.instagram_icon);
        	textEnableInsta.setTextColor(Color.parseColor("#4980a4"));
        }

        

    	final ToggleButton btnEnableFacebook = 	(ToggleButton) findViewById(R.id.btnEnableFacebook);
    	btnEnableFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomeActivity)m_context).m_bEnableFbCaption = btnEnableFacebook.isChecked() ? 1 : 0;
				uploadSocialShare(((HomeActivity)m_context).m_bEnableFbCaption == 1 ?
						"Enable Facebook Caption" : "Disable Facebook Caption");
			}
		});
    	btnEnableFacebook.setChecked(((HomeActivity)m_context).m_bEnableFbCaption == 0 ? false : true);
    	
    	final ToggleButton btnEnableTwitter = 	(ToggleButton) findViewById(R.id.btnEnableTwitter);
    	btnEnableTwitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomeActivity)m_context).m_bEnableTWCaption = btnEnableTwitter.isChecked() ? 1 : 0;
				uploadSocialShare(((HomeActivity)m_context).m_bEnableTWCaption == 1 ? 
						"Enable Twitter Caption" : "Disable Twitter Caption");
			}
		});
    	btnEnableTwitter.setChecked(((HomeActivity)m_context).m_bEnableTWCaption == 0 ? false : true);
    }
    
    private void uploadSocialShare(String msg) {
    	
    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage(msg);
    	
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
				 
				 reqEntity.addPart("enable_facebook", 		new StringBody("" + m_bEnableFb));
				 reqEntity.addPart("enable_twitter", 		new StringBody("" + m_bEnableTw));
				 reqEntity.addPart("enable_instagram", 		new StringBody("" + m_bEnableIns));

				 reqEntity.addPart("enable_facebook_custom_caption", 		new StringBody("" + ((HomeActivity)m_context).m_bEnableFbCaption));
				 reqEntity.addPart("enable_twitter_custom_caption", 		new StringBody("" + ((HomeActivity)m_context).m_bEnableTWCaption));

				 postMethod.setEntity(reqEntity);
				 responseBody = hc.execute(postMethod,res);
				 
				 System.out.println("update result = " + responseBody);
				 JSONObject result = new JSONObject(responseBody);
					if (result != null) {
						boolean status = result.optBoolean("status"); 
						if (status) {

							m_handler.sendEmptyMessage(2);
							
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
			
			progress.hide();
			
			switch (msg.what) {
			case 1:
				
				DraftReviewView.g_DraftView.onRefresh();
				
				break;
				
			case 2: 
				
				JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
				
				try {
					userInfo.put("enable_facebook", m_bEnableFb);
					userInfo.put("enable_twitter", m_bEnableTw);
					userInfo.put("enable_instagram", m_bEnableIns);
					
					userInfo.put("enable_facebook_custom_caption", ((HomeActivity)m_context).m_bEnableFbCaption);
					userInfo.put("enable_twitter_custom_caption", ((HomeActivity)m_context).m_bEnableTWCaption);

					UserDefault.setDictionaryForKey(userInfo, "user_info");
					
					refreshSocialBtn();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			case -1:
				break;
				
			case -2:
				
				m_bEnableFb = m_bEnableFb == 0 ? 1 : 0;
				m_bEnableTw = m_bEnableTw == 0 ? 1 : 0;
				m_bEnableIns = m_bEnableIns == 0 ? 1 : 0;
				
				break;
				
			}
			
		}
    };
    
    @Override
    public void onBack() {
        
    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_publishView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_publishView);    	
    	    }
    	});
    }
}
