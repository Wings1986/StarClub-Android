package com.basic.views;


import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.lib.gif.GifMovieView;
import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class MediaPlayerView extends BaseView {
	
	public Context m_context;
	public MediaPlayerView m_self  = null;
	
	ProgressDialog progress = null;
	
	public boolean bOpened = false;
	int nSelMusic = -1;
    boolean bPlaying = false;
	
    
    public GifMovieView ivVolumn = null;
    
    public Button btnOpen = null;
    ImageView ivAvatar = null;
    TextView tvPlaying, tvTitle;
    
    Button btnPlay = null;
    
    ListView mListView = null;
	private ResultListAdapter 	adapter = null;
	
	public MediaPlayer mediaPlayer = null;
	
    JSONArray arrList = null;
    
    
	public MediaPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public MediaPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaPlayerView(Context context) {
        super(context);
    }
    
    public void init(Context context) {
    	m_context = context;
		m_self = this;

		
		FrameLayout layoutTop = (FrameLayout) findViewById(R.id.toolLayout);
		layoutTop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (bOpened) {
					((HomeActivity) m_context).openMediaTool(false);
				}
				else {
					((HomeActivity) m_context).openMediaTool(true);
				}
				
			}
		});
		
		ivVolumn = (GifMovieView) findViewById(R.id.ivVolumn);
		ivVolumn.setMovieResource(R.drawable.media_volume);
		ivVolumn.setPaused(true);
		
		
		btnOpen = (Button) findViewById(R.id.btnOpen);
		btnOpen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (bOpened) {
					((HomeActivity) m_context).openMediaTool(false);
				}
				else {
					((HomeActivity) m_context).openMediaTool(true);
				}
			}
		});
		
		
		Button btnItune = (Button) findViewById(R.id.btnItune);
		btnItune.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					onBuyItuns();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Button btnPrev = (Button) findViewById(R.id.btnPrev);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPrev();
			}
		});
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
				    bPlaying = !bPlaying;

				    if (bPlaying) {
				    
				    	StarTracker.StarSendEvent(m_context, "App Event", "Music Play", "Started");
				    
				    	onPlay();
				    }
				    else {
				    	
				    	StarTracker.StarSendEvent(m_context, "App Event", "Music Play", "Paused");
				    	
				    	onStop();
				    }
				    
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Button btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onNext();
			}
		});
    	
//		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				try {
					setMusic(arg2);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				adapter.notifyDataSetChanged();

			}
		});
   
		ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
		tvPlaying = (TextView) findViewById(R.id.tvPlaying);
		tvPlaying.setText("Pause");
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		
    }
    
    
    public void getMusicList() {
    	
		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Loading....");

    	progress.show();

    	
    	new Thread(new Runnable() {
			
			public void run() {
				
				try {
					JSONObject data = JsonParser.getMusic();
				
					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					arrList = data.getJSONArray("musics");
				
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				m_handler.sendEmptyMessage(0);
				
			}
		}).start();
    }
    
    
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progress.hide();
			
			switch (msg.what) {
			case 0:
				try {
					refresh();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case -1:
				break;
			}
			
		}
    };
    
    private void refresh() throws JSONException {
    	  
//    	int count = arrList.length();
//    	String[] values = new String[count];
//    	for (int i = 0 ; i < count ; i ++) {
//    		values[i] = arrList.getJSONObject(i).getString("title");
//    	}
//    	
//    	adapter = new ArrayAdapter<String>(m_context,
//    		              android.R.layout.simple_list_item_activated_1, android.R.id.text1, values);

    	mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	adapter = new ResultListAdapter(m_context);
    	mListView.setAdapter(adapter);

//    	mListView.onRefreshComplete();
    	
		if (arrList.length() != 0) {
			setMusic(0);
        }

    }

    public class ResultListAdapter extends BaseAdapter {

    	private Context mContext = null;
    	
		public ResultListAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return arrList.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			TextView tvTitle = null;
			
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				
	    		convertView = inflater.inflate(R.layout.list_mediaplayer, null);
	    				 	
    			
    			tvTitle = (TextView) convertView.findViewById(R.id.txtTitle);
    			
				convertView.setTag(tvTitle);
			} else {
				tvTitle = (TextView) convertView.getTag();
				
			}
				
			
			try {
				
				String title = arrList.getJSONObject(position).getString("title");
		       	tvTitle.setText(title);
		       	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (position == nSelMusic)
				convertView.setBackgroundColor(Color.GRAY);
			else 
				convertView.setBackgroundColor(Color.TRANSPARENT);
	    		
			return convertView;
			
		}
	}
    
    private void onBuyItuns() throws JSONException {
        JSONObject music = arrList.getJSONObject(nSelMusic);
        String urlString = music.getString("url1");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
        		Uri.parse(urlString));
        m_context.startActivity(browserIntent);
    }
    private void onPrev() {
        int nMusic = nSelMusic == 0 ? 0 : nSelMusic -1;
        try {
			setMusic(nMusic);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    private void onNext() {
    	int nMusic = nSelMusic == arrList.length()-1 ? arrList.length()-1 : nSelMusic +1;
        try {
			setMusic(nMusic);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void onStop() {
    	if (mediaPlayer != null) {
    		mediaPlayer.stop();
    		mediaPlayer = null;
    	}
    	
    	if (ivVolumn != null)
    		ivVolumn.setPaused(true);
    	
    	if (tvPlaying != null)
    		tvPlaying.setText("Pause");
    	
    	if (btnPlay != null)
    		btnPlay.setBackgroundResource(R.drawable.media_btn_play);
    }
    
    private void onPlay() throws JSONException {
        JSONObject music = arrList.getJSONObject(nSelMusic);
        String url = music.getString("destination");

        System.out.println("music url = " + url);
        
        if (bPlaying == false) {
            return;
        }
        
		ivVolumn.setPaused(false);
		
    	tvPlaying.setText("Now Playing...");
    	btnPlay.setBackgroundResource(R.drawable.media_btn_pause);
    	
        if (mediaPlayer != null) {
        	mediaPlayer.stop();
        	mediaPlayer.stop();
        }
        
        StarTracker.StarSendEvent(m_context, "App Event", "Music Play", url);
        
    	mediaPlayer = new MediaPlayer(); 
    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
    	
    	try {
			mediaPlayer.setDataSource(url);
			
			mediaPlayer.prepare();
			
			mediaPlayer.setLooping(true);
			
			mediaPlayer.start();	
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    
    public void setMusic(int nIndex) throws JSONException
    {
        if (nIndex == nSelMusic || nIndex >= arrList.length()) {
            return;
        }
        
        nSelMusic = nIndex;
        
        JSONObject music = arrList.getJSONObject(nIndex);
        
        tvTitle.setText(music.getString("title"));

        String imageUrl = music.getString("image_path");
		((HomeActivity)m_context).imageLoader.displayImage(imageUrl, ivAvatar,
				((HomeActivity)m_context).optIcon, 
				((HomeActivity)m_context).animateFirstListener);

//		mListView.setItemChecked(nIndex, true);
		mListView.performItemClick(mListView, nIndex, mListView.getItemIdAtPosition(nIndex));
//		mListView.setSelection(nIndex);
//        mListView.requestFocus();
////		mListView.getAdapter().getView(nIndex, null, null).performClick();
//		mListView.performItemClick(null, 0, mListView.getListAdapter().getItemId(0));
		
//		new Handler().post(new Runnable() {
//		    @Override
//		    public void run() {
//		    	mListView.performItemClick(
//		    			mListView.getChildAt(nSelMusic),
//		    			nSelMusic,
//		                mListView.getAdapter().getItemId(nSelMusic));
//		    }
//		});
		
		onPlay();
    }
}
