package com.starclub.enrique.login;


import com.starclub.enrique.R;
import com.mycom.data.MyConstants;
import com.network.httpclient.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainTermsActivity extends BaseActivity {
    /** Called when the activity is first created. */
	
	private WebView mWebView;

	private ProgressDialog progressBar;  
	private static final String TAG = "Main";

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_terms);
        
	    String strTitle = getIntent().getStringExtra("title");

	    
	    TextView tvTitle = (TextView) findViewById(R.id.textTitle);
	    tvTitle.setText(strTitle);

	    String url = "";
	    if (strTitle.toLowerCase().contains("terms")) {
	    	url = MyConstants.SERVER + "/index.php/viewhelp/terms/" + MyConstants.CID;
	    } else {
	    	url = MyConstants.SERVER + "/index.php/viewhelp/pravacy/" + MyConstants.CID;
	    }
        
	    
	    Button btnBack = (Button) findViewById(R.id.btnBack);
	    btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	    
        mWebView = (WebView) findViewById(R.id.webView);
    	
    	WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(this, "", "Loading...");

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
                Toast.makeText(MainTermsActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
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

    }
  
	
}