package com.basic.views;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;



import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;


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
import com.starclub.enrique.login.AvatarActivity;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
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



public class AddPhotoView extends BaseView {
	
	public Context m_context = null;
	public static AddPhotoView g_addPhotoView  = null;
	
	EditText editText = null;
	Bitmap m_bmpAvatar = null;
	ImageView ivPhoto = null;
	
	ProgressDialog progress = null;
	Button btnPost = null;
	
	private int m_parentOpt = -1;

    boolean m_bIsPublished = false;
    JSONObject m_shareDic;

    
	public AddPhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public AddPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddPhotoView(Context context) {
        super(context);
    }
    
    public void init(Context context, int parentOpt) {
    	
    	m_context = context;
		g_addPhotoView = this;
		m_parentOpt = parentOpt;

		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		((HomeActivity)m_context).imageLoader.clearMemoryCache();
		
    
    	StarTracker.StarSendView(m_context, "New Photo");

    	
		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
		btnPost = (Button) findViewById(R.id.btnNext);
		btnPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!m_bIsPublished)
					onPost();
				else
					onSharePage(m_context, m_shareDic, false);
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
//				Intent intent = new Intent(m_context, AvatarActivity.class);
//				Const.g_option = 2;
//				UserDefault.setIntForKey(2, "g_option");
//				((HomeActivity)m_context).startActivityForResult(intent, RETURN_AVATAR);

				((HomeActivity)m_context).setMultiOrientation(false);
				
				openImageIntent();

			}
		});
    	
    }

    
    public void onPost() {
    	
    	if (editText.getText().toString().trim().length() < 1) {
    		Const.showMessage("Warning!", "Please input caption text!", (HomeActivity)m_context);
    		return;
    	}
    	if (m_bmpAvatar == null) {
    		Const.showMessage("Warning!", "Please take photo!", (HomeActivity)m_context);
    		return;
    	}
    	
    	HomeActivity.closeKeyboard(m_context, editText);
    	
		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Uploading....");

    	progress.show();

    	
    	new Thread(new Runnable() {
			
			public void run() {
				addPhoto();
			}
		}).start();
    }
    
    
    public void addPhoto() {

    	HttpParams myParams = new BasicHttpParams();

		 HttpConnectionParams.setConnectionTimeout(myParams, 30000);
		 HttpConnectionParams.setSoTimeout(myParams, 30000);
		 
		 DefaultHttpClient hc= new DefaultHttpClient(myParams);
		 ResponseHandler <String> res=new BasicResponseHandler();
		
		 
		 String url = Utils.addImageUrl();
		 HttpPost postMethod = new HttpPost(url);

		 String responseBody = "";
		 try {
		 MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		 reqEntity.addPart("cid",	 		new StringBody("" + MyConstants.CID));
		 reqEntity.addPart("token",	 		new StringBody("" + Utils.getUserToken()));
		 reqEntity.addPart("user_id", 		new StringBody("" + Utils.getUserID()));
		 reqEntity.addPart("description", 	new StringBody(editText.getText().toString().trim()));
		 
		 if (m_bmpAvatar != null) {
			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 m_bmpAvatar.compress(CompressFormat.JPEG, 75, bos);
			 byte[] data = bos.toByteArray();
			 
			 ByteArrayBody bab = new ByteArrayBody(data, "picture.jpg");
			 
			 reqEntity.addPart("image", bab);
		 }
		 
		 
		 postMethod.setEntity(reqEntity);
		 responseBody = hc.execute(postMethod,res);
		 
		 System.out.println("update result = " + responseBody);
		 JSONObject result = new JSONObject(responseBody);
			if (result != null) {
				boolean status = result.optBoolean("status"); 
				if (status) {
					
					
					if (Global.getUserType() != Global.FAN) {
						 JSONArray arrPost = result.getJSONArray("post");
						 final JSONObject obj = arrPost.getJSONObject(0);
						 
						 m_bIsPublished = true;
						 m_shareDic = obj;
						 
//						 String imageUrl = obj.getString("image_path");
//						 AQuery androidAQuery = new AQuery(this);
//					        androidAQuery.ajax(imageUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
//					        	@Override
//					        	public void callback(String url, Bitmap object,
//					        			com.androidquery.callback.AjaxStatus status) {
//					        		super.callback(url, object, status);
//					        		
//					        		setResultDic(obj, object);
//					        		
//					        	}
//					        });
				     }
					
					
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
				
				if (m_parentOpt == Global.OPT_PROFILE) {
					ProfileView.g_profileView.onRefresh();
	    		}
				else if (m_parentOpt == Global.OPT_COMMUNITY) {
					CommunityView.g_CommunityView.onRefresh();
				}
				else if (m_parentOpt == Global.OPT_ALLACCESS) {
					AllAccessView.g_AllAccessView.onRefresh();
				}

		    	if (Global.getUserType() == Global.FAN) {
		    		onBack();
		    	}
		    	else {
		    		btnPost.setText("Share");
		    	}

		    	
				break;
				
			case -1:
				break;
			}
			
		}
    };
    
    private void clear() {
    	m_context = null;
    	g_addPhotoView  = null;
    	
    	editText = null;
    	m_bmpAvatar = null;
    	ImageView ivPhoto = null;
    	
    	progress = null;

    }
    
    
    public void onBack() {

    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	g_addPhotoView.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	((HomeActivity)m_context).baselayout.removeView(g_addPhotoView);    	
    	    	clear();
    	    }
    	});
    	
    }
    
    private void setAviaryImage(Uri mImageUri) {
    	
		((HomeActivity)m_context).setMultiOrientation(false);

		Intent newIntent = new Intent(m_context, FeatherActivity.class );
		newIntent.setData( mImageUri );
		newIntent.putExtra( Constants.EXTRA_IN_API_KEY_SECRET, MyConstants.AVIARY_SECRET_KEY);
		((HomeActivity)m_context).startActivityForResult( newIntent, 1000 );    
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			if (data != null) {
				
				if (requestCode == 1000) { //Aviary
					// output image path
	                Uri mImageUri = data.getData();
	                Bundle extra = data.getExtras();

	                if( null != extra ) {
                        // image has been changed by the user?
//                        boolean changed = extra.getBoolean( Constants.EXTRA_OUT_BITMAP_CHANGED );
                        
                        m_bmpAvatar = getThumbnail(mImageUri);

	    				ivPhoto.setImageBitmap(m_bmpAvatar);
	    				
                    }
				
				}
				else if (requestCode == 100) { // Photo
					Uri selPhotoUri = data.getData();
					setAviaryImage(selPhotoUri);
				}

			}
			else {
				m_bmpAvatar = null;
			}
                
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    
    /*
     *  photo part
     */
    
    private void openImageIntent() {
		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				"Please Choose");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));

		((HomeActivity)m_context).startActivityForResult(chooserIntent, 100);
	}


	public Bitmap getThumbnail(Uri uri) throws FileNotFoundException,
			IOException {
		final int THUMBNAIL_SIZE = 350;

		InputStream input = m_context.getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1)
				|| (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;

		double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE)
				: 1.0;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = m_context.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}
}
