package com.starclub.enrique.login;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;

 import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.data.MyConstants;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.Utils;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


public class MainSignUpActivity extends BaseActivity {
    /** Called when the activity is first created. */
	
	public static int RETURN_AVATAR = 100;
	
	public Bitmap m_bmpAvatar = null;
	public int curGender = 0;
	
	ImageView ivAvatar = null;
	EditText  editFirstName = null;
	EditText  editLastName = null;
	EditText  editEmail	 = null;
	EditText  editPassword = null;
	
	
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
        
        setContentView(R.layout.activity_signup);
    
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
				onSignUp();
			}
		});
        
        ivAvatar = (ImageView) findViewById(R.id.imagePhoto);
        ivAvatar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				openImageIntent();
				return false;
			}
		});
        ivAvatar.setScaleType(ScaleType.FIT_CENTER);
        
		
        
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editEmail	 = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        
    }
    
	
    public void onSignUp() {
    	String firstName = editFirstName.getText().toString();
    	if (firstName.length() < 1) {
			Const.showMessage("NOTE!", "Please input first name", this);
			return;
		}
    	String lastName = editLastName.getText().toString();
    	if (lastName.length() < 1) {
			Const.showMessage("NOTE!", "Please input last name", this);
			return;
    	}
    	String email = editEmail.getText().toString();
    	if (email.length() < 1) {
			Const.showMessage("NOTE!", "Please input email", this);
			return;
    	}
    	String password = editPassword.getText().toString();
    	if (password.length() < 1) {
			Const.showMessage("NOTE!", "Please input password", this);
			return;
    	}
    	
    	progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("Sign Up...");
    	
		progress.show();
    	
		new Thread(){
			public void run(){
				signUp();
			}
		}.start();
		
    }
    
    private void signUp() {
    	String firstName = editFirstName.getText().toString();
    	String lastName = editLastName.getText().toString();
    	String email = editEmail.getText().toString();
    	String password = editPassword.getText().toString();
    	String userName = firstName + " " + lastName;
    	
    	HttpParams myParams = new BasicHttpParams();

		 HttpConnectionParams.setConnectionTimeout(myParams, 30000);
		 HttpConnectionParams.setSoTimeout(myParams, 30000);
		 
		 DefaultHttpClient hc= new DefaultHttpClient(myParams);
		 ResponseHandler <String> res=new BasicResponseHandler();
		
		 String url = Utils.getSignUpUrl();
		 HttpPost postMethod = new HttpPost(url);
		

		 String responseBody = "";
		 try {
		 MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		 reqEntity.addPart("facebook", 		new StringBody("0"));
		 reqEntity.addPart("cid",	 		new StringBody("" + MyConstants.CID));
		 reqEntity.addPart("ud_token",      new StringBody(Utils.getDeviceToken()));
		 reqEntity.addPart("ud_type",      new StringBody("ANDROID"));
		 
		 if (userName.length() > 0)
			 reqEntity.addPart("username",		new StringBody(userName));

		 if (firstName.length() > 0)
			 reqEntity.addPart("firstname",		new StringBody(firstName));

		 if (lastName.length() > 0)
			 reqEntity.addPart("lastname",		new StringBody(lastName));
		    
		 if (password.length() > 0)
			 reqEntity.addPart("password", 	new StringBody(password));
		
		 if (email.length() > 0)
			 reqEntity.addPart("email", 		new StringBody(email));
		 
		 
		 if (m_bmpAvatar != null) {
			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 m_bmpAvatar.compress(CompressFormat.JPEG, 75, bos);
			 byte[] data = bos.toByteArray();
			 
			 ByteArrayBody bab = new ByteArrayBody(data, "picture.jpg");
			 
			 reqEntity.addPart("picture", bab);
		 }
		 
//		 if (filepath != null) {
//		    File file = new File(filepath);
//		    ContentBody cbFile = new FileBody(file, "image/jpeg");
//		    reqEntity.addPart("user_avatar", cbFile);
//		   }
		 
		 
		 postMethod.setEntity(reqEntity);
		 responseBody = hc.execute(postMethod,res);
		 
		 System.out.println("post review = " + responseBody);
		 JSONObject result = new JSONObject(responseBody);
			if (result != null) {
				boolean status = result.optBoolean("status"); 
				if (status) {
					
					
					JSONObject userInfo = result.optJSONObject("info");
					String token = result.optString("token");
					
					userInfo.put("password", password);
					userInfo.put("token", token);
					
					UserDefault.setDictionaryForKey(userInfo, "user_info");
					
					m_handler.sendEmptyMessage(LOGIN_OK);
					
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
		 
		 m_handler.sendEmptyMessage(LOGIN_FAIL);
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
				
			case -1:
				
				Const.showMessage("", "Sign Up failed.", MainSignUpActivity.this);

				break;
			}
			
		}
    };
    
    public void gotoExplorer() {
    	if (MainLogInActivity.g_mainLoginAcitivity != null)
    		MainLogInActivity.g_mainLoginAcitivity.finish();
    	
		 Intent intent = new Intent(MainSignUpActivity.this, HomeActivity.class);
		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intent);
		 finish();
	 }
    
    
    /*
     *  photo part
     */
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == 1) {

			
			try {
				if (data != null) {

					Uri selPhotoUri = data.getData();
					m_bmpAvatar = getThumbnail(selPhotoUri);

					ivAvatar.setImageBitmap(m_bmpAvatar);
				} else {
					m_bmpAvatar = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				m_bmpAvatar = null;
			}

		}
	}
    
    
    private void openImageIntent() {
		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = getPackageManager();
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

		startActivityForResult(chooserIntent, 1);
	}

	

	public Bitmap getThumbnail(Uri uri) throws FileNotFoundException,
			IOException {
		final int THUMBNAIL_SIZE = 100;

		InputStream input = getContentResolver().openInputStream(uri);

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
		input = this.getContentResolver().openInputStream(uri);
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