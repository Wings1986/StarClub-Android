package com.basic.views;


import com.starclub.enrique.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class CustomWebView extends RelativeLayout {

	public Context m_context;

	public WebView mWebView = null;
	private ProgressBar progressBar;
	
	
	String TAG = "error";
	
	public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
//        init(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init(context);
    }

    public CustomWebView(Context context) {
        super(context);
//        init(context);
    }
    
    public void init(Context context) {
    	m_context = context;
    	
    	mWebView = (WebView) findViewById(R.id.webView1);
    	progressBar=(ProgressBar)findViewById(R.id.webloadProgressBar);
    	
    }
    
    public void loadUrl(String url) {
    	
    	mWebView.setVisibility(View.INVISIBLE);
    	
    	 WebSettings settings = mWebView.getSettings();
         settings.setJavaScriptEnabled(true);
         mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

         progressBar.setVisibility(View.VISIBLE);
         
         mWebView.setWebViewClient(new WebViewClient() {
//        	 public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//        		 Log.i(TAG, "Started loading URL: " +url);
//        		 progressBar.setVisibility(View.VISIBLE);
//        		 
//             }
        	 
             public boolean shouldOverrideUrlLoading(WebView view, String url) {
                 Log.i(TAG, "Processing webview url click...");
                 view.loadUrl(url);
                 return true;
             }

             public void onPageFinished(WebView view, String url) {
                 Log.i(TAG, "Finished loading URL: " +url);
                 progressBar.setVisibility(View.GONE);
                 mWebView.setVisibility(View.VISIBLE);
             }

             public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                 Log.e(TAG, "Error: " + description);
                 Toast.makeText(m_context, "Oh no! " + description, Toast.LENGTH_SHORT).show();
             }
         });
         mWebView.loadUrl(url);
     
    }

    public void loadHtml(String url) {
    	
    }

}
