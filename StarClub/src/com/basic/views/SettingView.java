package com.basic.views;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.mycom.data.MyConstants;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.Utils;
import com.starclub.enrique.BuyActivity;
import com.starclub.enrique.FBShareActivity;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;
import com.starclub.enrique.login.AvatarActivity;
import com.starclub.enrique.login.MainLogInActivity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class SettingView extends BaseView {

	public static SettingView g_SettingView = null;
	Context m_context = null;
	
	public int curGender = -1;
	
	TextView  tvFullName = null;
	TextView  tvEmail	 = null;
	TextView  tvPassword = null;
	TextView  tvBirthday = null;
	RadioGroup rgGender	 = null;
	TextView  tvCity	 = null;
	TextView  tvState	 = null;
	TextView  tvCountry	 = null;
	
	TextView  tvCredits  = null;
	TextView  tvVersion  = null;
	
	ToggleButton btnFacebook = null;
	ToggleButton btnTwitter = null;
	
	
	ProgressDialog progress = null;
	
	
	int bEnableFacebook = 0;
	int bEnableTwitter = 0;
	
	boolean m_bSubMode = false;
	
	public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingView(Context context) {
        super(context);
        
    }
    
    public void clear() {
    	g_SettingView = null;
    	m_context = null;
    	
    	tvFullName = null;
    	tvEmail	 = null;
    	tvPassword = null;
    	tvBirthday = null;
    	rgGender	 = null;
    	tvCity	 = null;
    	tvState	 = null;
    	tvCountry	 = null;
    	
    	tvCredits  = null;
    	tvVersion  = null;
    	
    	progress = null;
    	m_handler = null;
    }
    
    public void init(Context context, boolean bSubMode) {
    	
    	g_SettingView = this;
    	m_context = context;
    	
    	((HomeActivity)m_context).m_bShowSetting = true;
    	
    	m_bSubMode = bSubMode;
    	
    	StarTracker.StarSendView(m_context, "Settings");
    	

    	if (m_bSubMode == false) {
    		
        	((HomeActivity)m_context).btnNavRight.setText("Save");
        	((HomeActivity)m_context).btnNavRight.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				onSave();
    			}
    		});
        	
        	if (Global.getUserType() != Global.FAN){
        		((HomeActivity)m_context).btnNavRight.setVisibility(View.GONE);
        	}
    	}
    	else {	
    		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
    		
    		FrameLayout titleLayout = (FrameLayout)findViewById(R.id.titleLayout);
    		titleLayout.setVisibility(View.VISIBLE);
    		
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
					onSave();
				}
			});
    		
    		if (Global.getUserType() != Global.FAN){
        		btnNext.setVisibility(View.GONE);
        	}
    	}
    	
    	
    	try {
    		
    		JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
    	
            tvFullName = (TextView) findViewById(R.id.editName);
            tvFullName.setText(userInfo.getString("name"));
            
            tvEmail	 = (TextView) findViewById(R.id.editEmail);
            tvEmail.setText(userInfo.getString("email"));
            
            tvPassword = (TextView) findViewById(R.id.editPassword);
            String password = userInfo.getString("password");
            if (password.equals("0")) {
            	tvPassword.setTextColor(Color.GRAY);
            	tvPassword.setEnabled(false);
            } else {
            	tvPassword.setText(password);
            }
            
            tvBirthday = (TextView) findViewById(R.id.editBirthday);
            String birthday = userInfo.getString("birthday");
            if (birthday.indexOf("0000") < 0) {
            	tvBirthday.setText(birthday);	
            }
            
            
            rgGender   = (RadioGroup) findViewById(R.id.radioGroup1);
            rgGender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			
    			@Override
    			public void onCheckedChanged(RadioGroup group, int checkedId) {
    				
    				switch(checkedId)
                    {
                    case R.id.radio0:
                        // TODO Something
                    	curGender = 0;	
                        break;
                    case R.id.radio1:
                        // TODO Something
                    	curGender = 1;
                        break;
                    }
    			}
    		});
            curGender = userInfo.getInt("gender");
            if (curGender == 0) {
           	 	rgGender.check(R.id.radio0);
            } else if (curGender == 1){
           	 	rgGender.check(R.id.radio1);
            }
            
            tvCity	 = (TextView) findViewById(R.id.editCity);
            tvCity.setText(userInfo.getString("city"));
            
            tvState = (TextView) findViewById(R.id.editState);
            tvState.setText(userInfo.getString("state"));
            
            tvCountry = (TextView) findViewById(R.id.editCountry);
            tvCountry.setText(userInfo.getString("country"));
            
            
            tvCredits = (TextView) findViewById(R.id.txtCredits);
            tvCredits.setText("" + userInfo.getInt("credit"));

            tvVersion = (TextView) findViewById(R.id.tvVersion);
            tvVersion.setText("version : " + ((HomeActivity)m_context).getVersionName());
            
            
            Button btnBuyMore = (Button) findViewById(R.id.btnBuyMore);
            btnBuyMore.setOnClickListener(new OnClickListener() {
   			
            	@Override
       			public void onClick(View v) {
       				
       				Intent intent = new Intent(m_context, BuyActivity.class);
       				((HomeActivity) m_context).startActivityForResult(intent, 102);
       			}
       		});
            
            FrameLayout layoutFBShare = (FrameLayout) findViewById(R.id.layoutFBShare);
            layoutFBShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
       				Intent intent = new Intent(m_context, FBShareActivity.class);
       				((HomeActivity) m_context).startActivity(intent);
				}
			});
            
            btnFacebook = (ToggleButton) findViewById(R.id.btnFacebook);
            btnFacebook.setOnClickListener(new OnClickListener() {
   			
            	@Override
       			public void onClick(View v) {
       				// TODO Auto-generated method stub
            		bEnableFacebook = btnFacebook.isChecked() ? 1 : 0;
            		
            		uploadSocialShare();
       			}
            });
            
            try {
            	bEnableFacebook = userInfo.getInt("enable_facebook");
            }catch (JSONException e){};
            btnFacebook.setChecked(bEnableFacebook==1 ? true : false);
            
            
            btnTwitter = (ToggleButton) findViewById(R.id.btnTwitter);
            btnTwitter.setOnClickListener(new OnClickListener() {
       			
       			@Override
       			public void onClick(View v) {
       				// TODO Auto-generated method stub
       				
       				bEnableTwitter = btnTwitter.isChecked() == true ? 1 : 0;
       				
       				uploadSocialShare();
       			}
       		});
            try {
            	bEnableTwitter = userInfo.getInt("enable_twitter");
            }catch (JSONException e){};
            btnTwitter.setChecked(bEnableTwitter==1 ? true : false);

            
            Button btnSignOut = (Button) findViewById(R.id.btnSignOut);
            btnSignOut.setOnClickListener(new OnClickListener() {
   			
            	@Override
       			public void onClick(View v) {
       				// TODO Auto-generated method stub
            		
            		HomeActivity.signOut();
            		
       			}
       		});
         
    	}catch (JSONException e){};
         
    	if (Global.getUserType() != Global.FAN){
    		LinearLayout layoutNoAdmin = (LinearLayout) findViewById(R.id.layoutField);
    		layoutNoAdmin.setVisibility(View.GONE);
    	}
    }
    
    public void refreshCredit() {
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
        tvCredits = (TextView) findViewById(R.id.txtCredits);
        try {
			tvCredits.setText("" + userInfo.getInt("credit"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    public void onSave() {
    	
    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Updating...");
    	
		progress.show();
    	
		new Thread(){
			public void run(){
				String userName = tvFullName.getText().toString();
		    	String email = tvEmail.getText().toString();
		    	String password = tvPassword.getText().toString();
		    	String birthday = tvBirthday.getText().toString();
		    	String gender = "" + curGender;
		    	String city = tvCity.getText().toString();
		    	String state = tvState.getText().toString();
		    	String country = tvCountry.getText().toString();
		    	String credit = tvCredits.getText().toString();
		    	
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
				 
				 
				 if (userName.length() > 0)
					 reqEntity.addPart("username",		new StringBody(userName));
				 
				 if (password.length() > 0)
					 reqEntity.addPart("password", 	new StringBody(password));
				
				 if (email.length() > 0)
					 reqEntity.addPart("email", 		new StringBody(email));
				 
				 if (birthday.length() > 0)
					 reqEntity.addPart("birthday", 		new StringBody(birthday));

				 reqEntity.addPart("gender", 		new StringBody(gender));

				 if (city.length() > 0)
				 	reqEntity.addPart("city", 			new StringBody(city));
				 if (state.length() > 0)
					 reqEntity.addPart("state", 		new StringBody(state));
				 if (country.length() > 0)
					 reqEntity.addPart("country", 		new StringBody(country));
				
				 if (credit.length() > 0)
					 reqEntity.addPart("credit",	 		new StringBody(credit));
				 
//				 if (m_bmpAvatar != null) {
//					 ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					 m_bmpAvatar.compress(CompressFormat.JPEG, 75, bos);
//					 byte[] data = bos.toByteArray();
//					 
//					 ByteArrayBody bab = new ByteArrayBody(data, "picture.jpg");
//					 
//					 reqEntity.addPart("picture", bab);
//				 }
				 
				 
				 postMethod.setEntity(reqEntity);
				 responseBody = hc.execute(postMethod,res);
				 
				 System.out.println("update result = " + responseBody);
				 JSONObject result = new JSONObject(responseBody);
					if (result != null) {
						boolean status = result.optBoolean("status"); 
						if (status) {
							
							JSONObject userInfo = result.optJSONObject("info");
							
							
							userInfo.put("password", password);
							userInfo.put("token", Utils.getUserToken());
							
							UserDefault.setDictionaryForKey(userInfo, "user_info");
							
							m_handler.sendEmptyMessage(0);
							
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
				 
				 m_handler.sendEmptyMessage(-1);
			}
		}.start();
		
    }

   
    private void uploadSocialShare() {
    	
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
				 
				 String enable_facebook = btnFacebook.isChecked() ? "1" : "0";
				 String enable_twitter  = btnTwitter.isChecked() ? "1" : "0";
				 reqEntity.addPart("enable_facebook", 		new StringBody(enable_facebook));
				 reqEntity.addPart("enable_twitter", 		new StringBody(enable_twitter));

//				 if (m_bmpAvatar != null) {
//					 ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					 m_bmpAvatar.compress(CompressFormat.JPEG, 75, bos);
//					 byte[] data = bos.toByteArray();
//					 
//					 ByteArrayBody bab = new ByteArrayBody(data, "picture.jpg");
//					 
//					 reqEntity.addPart("picture", bab);
//				 }
				 
				 
				 postMethod.setEntity(reqEntity);
				 responseBody = hc.execute(postMethod,res);
				 
				 System.out.println("update result = " + responseBody);
				 JSONObject result = new JSONObject(responseBody);
					if (result != null) {
						boolean status = result.optBoolean("status"); 
						if (status) {

							m_handler.sendEmptyMessage(1);
							
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

				JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
				
				try {
					userInfo.put("enable_facebook", btnFacebook.isChecked() ? 1 : 0);
					userInfo.put("enable_twitter", btnTwitter.isChecked() ? 1 : 0);
					
					UserDefault.setDictionaryForKey(userInfo, "user_info");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				
				
			case -1:
				Const.showMessage("", "Network is failed.", (HomeActivity)m_context);
				break;
				
			case -2:
				
				btnFacebook.setChecked(bEnableFacebook == 0 ? true : false);
				btnTwitter.setChecked(bEnableTwitter == 0 ? true : false);
				
				break;
			}
			
		}
   };
    
   public void onBack() {
	    
	   	((HomeActivity)m_context).m_bShowSetting = false;
	   
	   	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
	   	
	   	Animation in = null;
   		in = AnimationUtils.loadAnimation(m_context, R.anim.scale_in);
	   	g_SettingView.startAnimation(in);

	   	in.setAnimationListener(new Animation.AnimationListener(){
	   	    @Override
	   	    public void onAnimationStart(Animation arg0) {
	   	    }           
	   	    @Override 
	   	    public void onAnimationRepeat(Animation arg0) {
	   	    }           
	   	    @Override
	   	    public void onAnimationEnd(Animation arg0) {
	   	    	
	   	    	((HomeActivity)m_context).baselayout.removeView(g_SettingView);
	   	    	clear();
	   	    }
	   	});
   }
   
}
