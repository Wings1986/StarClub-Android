package com.basic.views;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;



public class YoutubeView extends BaseView {
	
	public Context m_context;
	public static YoutubeView g_youtubeView;

	private WebView mWebView;

	
	private ProgressDialog progressBar;  
	private static final String TAG = "Main";
	
	public YoutubeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public YoutubeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YoutubeView(Context context) {
        super(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
	public void init(Context context, String url) {
    	m_context = context;
		g_youtubeView = this;
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
	
    	StarTracker.StarSendView(m_context, "U-tube View");
    	
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    	Button btnPrev = (Button) findViewById(R.id.btnPrev);
    	btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.goBack();
			}
		});
    	Button btnNext = (Button) findViewById(R.id.btnNext);
    	btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.goForward();
			}
		});
		
    	mWebView = (WebView) findViewById(R.id.webView);
    	
    	
    	
    	WebSettings settings = mWebView.getSettings();
    	settings.setBuiltInZoomControls(true);
    	settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
    	settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);

//        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);
    	
    	
    	mWebView.setWebChromeClient(new WebChromeClient() {
    	
    	});
    	


        final AlertDialog alertDialog = new AlertDialog.Builder(m_context).create();

        progressBar = ProgressDialog.show(m_context, "", "Loading...");

              
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(m_context, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        
        mWebView.loadUrl(url);
    	
        StarTracker.StarSendEvent(m_context, "App Event", "External Video Requested", url);
    }
    

 	
    public void onBack() {
    
    	StarTracker.StarSendEvent(m_context, "App Event", "External Video Requested", "stopped");
    	
    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_youtubeView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_youtubeView);    	
    	    }
    	});
    }
    
 
   
}
