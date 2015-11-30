package com.basic.views;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
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
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;





import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
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
import android.widget.AdapterView.OnItemClickListener;


public class AddVideoView extends BaseView {
	
	public Context m_context = null;
	public static AddVideoView g_addVideoView  = null;
	
	EditText editText = null;
	
	String selectedPath = null;

	ImageView ivPhoto = null;
	
	ProgressDialog progress = null;
	
	private int m_parentOpt = -1;
	
	public AddVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public AddVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddVideoView(Context context) {
        super(context);
    }

    public void init(Context context, int parentOpt) {
    	m_context = context;
		g_addVideoView = this;
		m_parentOpt = parentOpt;
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		((HomeActivity)m_context).imageLoader.clearMemoryCache();
		
		
    	StarTracker.StarSendView(m_context, "New Video");


    	
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
		Button btnPost = (Button) findViewById(R.id.btnNext);
		btnPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPost();
			}
		});
    	
    	editText = (EditText) findViewById(R.id.tvText);
    	
    	ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
    	
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
    	String imgUrl = null;
		try {
			imgUrl = userInfo.getString("img_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivAvatar,
				((HomeActivity)m_context).optIcon, 
				((HomeActivity)m_context).animateFirstListener);
    	
    	
    	ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
    	ivPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				((HomeActivity)m_context).setMultiOrientation(false);
				
				openCamera();
			}
		});
    	
    }
    
    private void clear () {
    	m_context = null;
    	g_addVideoView  = null;
    	
    	editText = null;
    	selectedPath = null;
    	ivPhoto = null;
    	progress = null;
    }
    
    public void openCamera() {
    	final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		final PackageManager packageManager = m_context.getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			cameraIntents.add(intent);
		}
		
		Intent galleryIntent = new Intent();
		galleryIntent.setType("video/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
		
		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				"Please Choose");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));
				
		((HomeActivity)m_context).startActivityForResult(chooserIntent, 101);
    }
    public void onPost() {
    	
    	if (editText.getText().toString().trim().length() < 1) {
    		Const.showMessage("Warning!", "Please input caption text!", (HomeActivity)m_context);
    		return;
    	}
    	if (selectedPath == null
    			|| selectedPath.length() < 1) {
    		Const.showMessage("Warning!", "Please choose video to upload!", (HomeActivity)m_context);
    		return;
    	}
    	
    	HomeActivity.closeKeyboard(m_context, editText);
    	
		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Uploading....");

    	progress.show();

    	
    	new Thread(new Runnable() {
			
			public void run() {
				addVideo();
			}
		}).start();
    }
    
    
    public void addVideo() {

    	HttpParams myParams = new BasicHttpParams();

		 HttpConnectionParams.setConnectionTimeout(myParams, 30000);
		 HttpConnectionParams.setSoTimeout(myParams, 30000);
		 
		 DefaultHttpClient hc= new DefaultHttpClient(myParams);
		 ResponseHandler <String> res=new BasicResponseHandler();
		
		 
		 String url = Utils.addVideoUrl();
		 HttpPost postMethod = new HttpPost(url);

		 String responseBody = "";
		 try {
		 MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		 reqEntity.addPart("cid",	 		new StringBody("" + MyConstants.CID));
		 reqEntity.addPart("token",	 		new StringBody("" + Utils.getUserToken()));
		 reqEntity.addPart("user_id", 		new StringBody("" + Utils.getUserID()));
		 reqEntity.addPart("description", 	new StringBody(editText.getText().toString().trim()));
		 
		 if (selectedPath != null) {
			 File input = new File(selectedPath);
			 FileBody filebodyVideo = new FileBody(input); 
			 reqEntity.addPart("video", filebodyVideo);
		 }
		 
		 
		 postMethod.setEntity(reqEntity);
		 responseBody = hc.execute(postMethod,res);
		 
		 System.out.println("update result = " + responseBody);
		 JSONObject result = new JSONObject(responseBody);
			if (result != null) {
				boolean status = result.optBoolean("status"); 
				if (status) {
					
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
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progress.hide();
			
			switch (msg.what) {
			case 0:

				if (Global.getUserType() == Global.FAN) {
					if (m_parentOpt == Global.OPT_PROFILE) {
						ProfileView.g_profileView.onRefresh();
		    		}
					else if (m_parentOpt == Global.OPT_COMMUNITY) {
						CommunityView.g_CommunityView.onRefresh();
					}
					else if (m_parentOpt == Global.OPT_ALLACCESS) {
						AllAccessView.g_AllAccessView.onRefresh();
					}
				}
				
				onBack();
				break;
				
			case -1:
				break;
			}
			
		}
    };
    
    public void onBack() {
    
    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_addVideoView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(g_addVideoView);    	
    	    	clear();
    	    }
    	});
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


		try {
			Uri selectedImageUri = data.getData();
            selectedPath = getPath(selectedImageUri);
            
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedPath,
            	    MediaStore.Images.Thumbnails.MINI_KIND);
            
            thumb = getResizedBitmap(thumb);
            ivPhoto.setImageBitmap(thumb);
            
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    
    public Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

    	int newHeight = 250;
    	int newWidth = width * newHeight  / height;
    			
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    
    
    private String getPath(Uri uri) { 
        String[] projection = { MediaStore.Video.Media.DATA, 
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION }; 
        @SuppressWarnings("deprecation") 
        Cursor cursor = ((HomeActivity)m_context).managedQuery(uri, projection, null, null, null); 
        cursor.moveToFirst(); 
        String filePath = cursor.getString(cursor 
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); 
        int fileSize = cursor.getInt(cursor 
                .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); 
        long duration = TimeUnit.MILLISECONDS.toSeconds(cursor.getInt(cursor 
                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))); 
        System.out.println("size: " + fileSize); 
        System.out.println("path: " + filePath); 
        System.out.println("duration: " + duration); 
  
        return filePath; 
    }
    
}
