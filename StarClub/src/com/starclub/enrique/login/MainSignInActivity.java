package com.starclub.enrique.login;


import java.util.ArrayList;

import org.json.JSONObject;


import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainSignInActivity extends BaseActivity {
    /** Called when the activity is first created. */
	
	TextView  tvEmail	 = null;
	TextView  tvPassword = null;
	
	public static MainSignInActivity g_mainSignInActivity = null;
	
	@Override
	public void onDestroy(){
	    super.onDestroy();
	    if ( progress !=null && progress.isShowing() ){
	    	progress.cancel();
	    }
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_signin);
    
        UserDefault.init(this);
        
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
        
        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSignIn();
			}
		});
        
        TextView textForgetPassword = (TextView) findViewById(R.id.textForgetPassword);
        textForgetPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onForgetPassword();
			}
		});
        
        
        
        tvEmail	 = (TextView) findViewById(R.id.editEmail);
        tvPassword = (TextView) findViewById(R.id.editPassword);
        
//        tvEmail.setText("android@hotmail.com");
//        tvPassword.setText("123456");
    }
    
    public void onSignIn() {
    	
    	progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("Sign In...");
    	
		progress.show();
		
		new Thread(){
			public void run(){
				loginProcess();
			}
		}.start();
    }
    public void loginProcess() {
    	String username = tvEmail.getText().toString();
    	String password = tvPassword.getText().toString();

		JSONObject data = JsonParser.getUserLogin(username, password);
		
		if (data == null) {
			
			m_handler.sendEmptyMessage(LOGIN_FAIL);
			return;
		}

		UserDefault.setDictionaryForKey(data, "user_info");
		
		System.out.println("user_info = " + data.toString());
		
		m_handler.sendEmptyMessage(LOGIN_OK);
	}



    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
//			progress.hide();
			progress.dismiss();
			
			switch (msg.what) {
			case 0:
				gotoExplorer();
				
				break;
				
			case 1:
				Const.showMessage("", "You sent successfully. Please check your email", MainSignInActivity.this);
				break;
				
			case -1:
				
				Const.showMessage("Sign In Error!", "Please provide correct email and password", MainSignInActivity.this);

				break;
			}
			
		}
		
		 
    };
    
    
    public void onForgetPassword() {
    	 final AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	 alert.setTitle("Please input your email!");
    	 final EditText input = new EditText(this);
 	    alert.setView(input);
 	    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
 	        public void onClick(DialogInterface dialog, int whichButton) {
 	        	dialog.cancel();
 	        	
 	            String email = input.getText().toString().trim();
 	            gotoForgetPassword(email);
 	        }
 	    });

 	    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 	        public void onClick(DialogInterface dialog, int whichButton) {
 	            dialog.cancel();
 	        }
 	    });
 	    alert.show();  
    }
    public void gotoForgetPassword(final String email){
    	    
    	progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("Sending...");
    	
		progress.show();
		
		new Thread(){
			public void run(){

				JSONObject data = JsonParser.getForgetPassword(email);
				
				if (data == null) {
					
					m_handler.sendEmptyMessage(LOGIN_FAIL);
					return;
				}

				
				m_handler.sendEmptyMessage(1);
			}
		}.start();
    }
    
    public void gotoExplorer() {
    	if (MainLogInActivity.g_mainLoginAcitivity != null)
    		MainLogInActivity.g_mainLoginAcitivity.finish();

      	 Intent intent = new Intent(MainSignInActivity.this, HomeActivity.class);
//       	 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       	intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
       	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
       	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
       	 startActivity(intent);
       	 finish();
       	 
    }
}