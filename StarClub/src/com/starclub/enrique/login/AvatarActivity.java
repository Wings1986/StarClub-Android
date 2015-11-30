package com.starclub.enrique.login;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.starclub.enrique.R;

import com.mycom.data.Global;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class AvatarActivity extends BaseActivity {
    /** Called when the activity is first created. */
	
	public Bitmap 	m_bmp;
	ImageView ivPic = null;
	
//	public static int  g_option = -1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_avatar);
    
        initToolbar();
    }
    
    public void initToolbar() {
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
 	
 					Intent intent = new Intent(AvatarActivity.this, MainSignUpActivity.class);
 					
 					if (m_bmp != null) {
 	 					ByteArrayOutputStream stream = new ByteArrayOutputStream();
 	 					m_bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
 	 					byte[] byteArray = stream.toByteArray();
 						intent.putExtra("image", byteArray);
 					}
					startActivity(intent);
 					
/* 					
 					if (m_bmp != null) {
	 						
 						ByteArrayOutputStream stream = new ByteArrayOutputStream();
	 					m_bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
	 					byte[] byteArray = stream.toByteArray();
	 						
	 					Intent intent = new Intent(AvatarActivity.this, MainSignUpActivity.class);
 						intent.putExtra("image", byteArray);
 						startActivity(intent);
	 						
	 						
 						if (Const.g_option == 0) {
 	 						Intent intent = new Intent(AvatarActivity.this, MainSignUpActivity.class);
 	 						intent.putExtra("image", byteArray);
 	 						startActivity(intent);
 	 						Const.g_option = -1;
 						}
 						else if (Const.g_option == 1) { // setting
 	 						Intent data = getIntent();
 	 						data.putExtra("image",byteArray);
 	 						setResult(RESULT_OK, data);
 	 					    onBackPressed();
 	 					    Const.g_option = -1;
 						}
 						else if (Const.g_option == 2) { // add photo
 	 						Intent data = getIntent();
 	 						data.putExtra("image",byteArray);
 	 						setResult(RESULT_OK, data);
 	 					    onBackPressed();
 	 					    Const.g_option = -1;
 						}
 					}
*/ 		
 					
 				}
 			});
  		
  		Button btnCameraRoll = (Button) findViewById(R.id.btnTakePhoto);
  		btnCameraRoll.setOnClickListener(new OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					// TODO Auto-generated method stub
 					
 					openImageIntent();
 				}
 			});
  		
  		ivPic = (ImageView) findViewById(R.id.imageView1);
  		ivPic.setScaleType(ScaleType.FIT_CENTER);
  		
      }
    
    /*
     *  photo part
     */
    
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
					m_bmp = getThumbnail(selPhotoUri);

					ivPic.setImageBitmap(m_bmp);
				} else {
					m_bmp = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				m_bmp = null;
			}

		}
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