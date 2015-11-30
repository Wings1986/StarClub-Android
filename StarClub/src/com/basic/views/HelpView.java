package com.basic.views;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.MyConstants;
import com.mycom.data.Global;
import com.mycom.lib.Const;
import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class HelpView extends BaseView {
	
	public Context m_context;

	private WebView mWebView;
	private String m_url = "";
	
	private ProgressDialog progressBar;  
	private static final String TAG = "Main";
	
	private String mUrl = "";
	
	public HelpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public HelpView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HelpView(Context context) {
        super(context);
    }
    
    @SuppressLint("SetJavaScriptEnabled")
	public void init(Context context, String url) {
    	m_context = context;
    	
//    	Button btnPrev = (Button) findViewById(R.id.btnPrev);
//    	btnPrev.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mWebView.goBack();
//			}
//		});
//    	Button btnNext = (Button) findViewById(R.id.btnNext);
//    	btnNext.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mWebView.goForward();
//			}
//		});
		
    	
		String strScreenName = "";
		
		if (url.indexOf("help") > 0) {
			strScreenName = "Help";
		} else if (url.indexOf("shop") > 0) {
			strScreenName = "Shop";
		} else if (url.indexOf("tour") > 0) {
			strScreenName = MyConstants.MENU_TOUR;
		}
		
    	StarTracker.StarSendView(m_context, strScreenName);


    	mWebView = (WebView) findViewById(R.id.webView);
    	
    	
    	
    	WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(m_context).create();

        progressBar = ProgressDialog.show(m_context, "", "Loading...");

        mWebView.setWebViewClient(new WebViewClient() {
        	
        	String mUrl = "";
        
        	@Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url)
            {
        		Log.i(TAG, "Processing webview url click...");
        		
                //check if the url matched the url loaded via webview.loadUrl()
                if (checkMatchedLoadedURL(url))
                {
                    return false;
                } 
                else
                {
                	
                	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                	
                    ((HomeActivity)m_context).startActivity(intent);
                    return true;
                }
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
        
        mUrl = url;
        mWebView.loadUrl(url);
        
    }
		
    private boolean checkMatchedLoadedURL(String loadedUrl)
    {
        if (loadedUrl != null && mUrl != null)
        {
            // remove the tailing space if exisits
            int length = loadedUrl.length();
            --length;
            char buff = loadedUrl.charAt(length);
            if (buff == '/')
            {
                loadedUrl = loadedUrl.substring(0, length);
            }

            // load the url in browser if not the OTHER_APPS_URL
            return mUrl.equalsIgnoreCase(loadedUrl);
        }
        return false;
    }
}
