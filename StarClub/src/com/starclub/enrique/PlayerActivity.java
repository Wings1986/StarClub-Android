package com.starclub.enrique;

import com.brightcove.player.media.Catalog;
import com.brightcove.player.media.VideoListener;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveVideoView;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class PlayerActivity extends Activity {

//	VideoView videoView = null;
	BrightcoveVideoView videoView = null;
	
	int pos = 0;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        
	  //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    
	    setContentView(R.layout.activity_player);
	    
	    
    	StarTracker.StarSendView(this, "Brightcove Player view");

    	
	    String fileName = getIntent().getStringExtra("file_name");
	    String videoId = getIntent().getStringExtra("bright_video_id");
	    
	    System.out.println("video url = " + fileName);
	    
//	    if (savedInstanceState != null) {
//	        pos = savedInstanceState.getInt("pos");
//	    }
	    
	    
	    
	    videoView = (BrightcoveVideoView) findViewById(R.id.videoView1);
	    
	    if (videoId == null || videoId.equals("")) {
	    	
	    	StarTracker.StarSendEvent(this, "App Event", "Brightcove Player Requested", fileName);
	    	
	    	videoView.setVideoURI(Uri.parse(fileName));
	    }
	    else {
	    	
	    	StarTracker.StarSendEvent(this, "App Event", "Brightcove Player Requested", videoId);
	    	
	    	
	    	Catalog catalog = new Catalog("v2_tp1lbll_k9JjauR8-RcCMzw26Tq-KD87rxfhK7KdRmlkBsrGsKQ..");
	 	    catalog.findVideoByID(videoId, new VideoListener() {
	 	    	@Override
	 	    	public void onVideo(Video video) {
	 	    		videoView.add(video);
	 	    	}
	 	    	
	 	    	@Override
	 	    	public void onError(String error) {
	 	    		// Handle error
	 	    	}
	 	    });
	    }
	    
	    videoView.setMediaController(new MediaController(this));
	    
	    
//	    videoView.setOnErrorListener(new OnErrorListener() {
//
//			@Override
//			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
//				// TODO Auto-generated method stub
//                Toast.makeText(PlayerActivity.this, "Error occured", 1000).show();
//                return false;
//			}
//	    });

	    videoView.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer arg0) {
        	    videoView.requestFocus();
        	    
        	    if (pos != 0) {
        	    	videoView.seekTo(pos);
        	    }
        	    
        	    videoView.start();
            }
	    });
	    
	    videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}	
	
//	protected void onSaveInstanceState(Bundle outState) {
//	    super.onSaveInstanceState(outState);
//	    if (videoView.isPlaying()) outState.putInt("pos", videoView.getCurrentPosition());
//	}
	
	
    @Override
    protected void onDestroy() {
    	
    	StarTracker.StarSendEvent(this, "App Event", "Brightcove Player", "Closed");
    	
    	
          try {
                  videoView.stopPlayback();
          } catch (Exception e) {
                  //
          }
          super.onDestroy();
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	super.onConfigurationChanged(newConfig);

	  // Checks the orientation of the screen
	  if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//	    Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//	    setContentView(Your Landscape layout);
	  } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//	    Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//	    setContentView(Your portrait layout);
	  }
	}
}
