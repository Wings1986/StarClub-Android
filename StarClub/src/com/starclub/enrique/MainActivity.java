package com.starclub.enrique;



import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import com.mycom.data.Global;
import com.mycom.lib.UserDefault;
import com.starclub.enrique.login.MainLogInActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	@Override
	public void onStart(){
		super.onStart();
		
		StarTracker.StartQuantcast(this);
	}
	@Override
	public void onStop() {
		super.onStop();
		
		StarTracker.StopQuantcast();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		
        setContentView(R.layout.activity_main);
    
        
        initData();
        
        UserDefault.init(this);

        StarTracker.StarRunStatus(this, StarTracker.DID_LAUNCH, "App Status", "StarStats Init", "State Change");
        
    }
    
    
	private static Handler handler = new Handler(); 
	
    public void initData() {
    	
    	Thread thread = new Thread(new Runnable(){

			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				handler.post(new Runnable() {
					public void run() {
						gotoNextScreen();
					}
				});
				
			}
			
		});
		thread.start();
    	
    	return;
	}
    
    
    private void gotoNextScreen() {

    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
    	
    	String token = null;
    	try {
			token = userInfo.getString("token");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	StarTracker.StarRunStatus(this, StarTracker.DID_SIGNIN, "Run Status", "Sign-In", "State Change");
    	
    	
    	if (token != null && token.length() > 1) {
        	Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        	startActivity(intent);
    	}
    	else  
    	{
        	Intent intent = new Intent(MainActivity.this, MainLogInActivity.class);
        	startActivity(intent);
    	}
    	
    	overridePendingTransition(R.anim.fadein, R.anim.fade_hold);
		finish();

    }
}