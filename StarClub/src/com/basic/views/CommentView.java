package com.basic.views;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.mycom.data.Global;
import com.mycom.data.MyJSON;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.mycom.lib.XListView;
import com.network.httpclient.JsonParser;
import com.network.httpclient.Utils;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;
import com.starclub.enrique.StarTracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;




public class CommentView extends BaseView {
	
	LayoutInflater inflater = null; 
	
	public Context m_context;
	public static CommentView g_commentView;
	public CommentView m_self;
	
	private XListView mListView;
	private ResultListAdapter 	adapter = null;

	private EditText	editComment = null;

	ProgressDialog progress = null;

	private int m_parentOpt = -1;
	
	
	private JSONObject m_dic;
	private JSONArray m_arrComment = null;
	
	boolean bChanged = false;
	int m_nIndexDel = -1;
	
	boolean bAddComment = false;
	int m_nParentIndex = 0;
	
	int m_nPage = 0;
	int m_nTotalComment = -1;
	
	public CommentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        
        initView(context);
    }

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initView(context);
    }

    public CommentView(Context context) {
        super(context);
        
        initView(context);
    }
    
    private void initView(Context context){

//    	View.inflate(context, R.layout.view_comment, this);
    	  
    }
    
    public void init(Context context, JSONObject feed, int index, boolean bAddComment, int parentOpt) {
    	m_context = context;
    	g_commentView = this;
		m_self = this;
		m_dic = feed;
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		this.m_nParentIndex = index;
		this.bAddComment = bAddComment;
		
		m_parentOpt = parentOpt;

		bChanged = false;
		
    	if (bAddComment) {
        	StarTracker.StarSendView(m_context, "Add Comment");
    	} else {
        	StarTracker.StarSendView(m_context, "Comments");
    	}
    	
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
				postComment();
			}
		});
		
    	mListView = (XListView) findViewById(R.id.listview);
    	mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
		
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int index = (int)arg2 - 1;
		 		String userName = "";
		 		try {
					JSONObject comment = m_arrComment.getJSONObject(index);
					userName = comment.getString("name");
		 		} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 		
		 		JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
		 		String owerName = "";
		 		try {
		 			owerName = userInfo.getString("name");
		 		} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 		
		 		if (userName.equals(owerName)
		 				|| Global.getUserType() != Global.FAN) {
					m_nIndexDel = index;

					AlertDialog.Builder dialog = new AlertDialog.Builder((HomeActivity) m_context);
					dialog.setTitle("NOTE!");
					dialog.setMessage("Do you want to remove this comment?");
					dialog.setIcon(android.R.drawable.ic_dialog_info);
					dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							deleteComment();
						}
					  });
					dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
					dialog.show();
				}
				
			}
		});
    	
    	
    	editComment = (EditText) findViewById(R.id.tvText);
    	editComment.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        	// Perform action on key press
		          
		        	postComment();
		        	
		          return true;
		        }
		        return false;
		    }
		});
		if (bAddComment) {
			HomeActivity.showKeyboard(m_context, editComment);
		}
		
    	
    	ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
    	JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
    	
    	String imgUrl = "";
		try {
			imgUrl = userInfo.getString("img_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((HomeActivity)m_context).imageLoader.displayImage(imgUrl, ivAvatar,
				((HomeActivity)m_context).optIcon, 
				((HomeActivity)m_context).animateFirstListener);	
    	
		getComments();
    	
    }
    

	
    private void getComments () {

		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Loading....");

    	progress.show();

    	
		new Thread(new Runnable() {
			
			public void run() {
				try {

			    	String postType = m_dic.getString("post_type");
			    	String contentId = m_dic.getString("content_id");
			    			
					JSONObject data = JsonParser.getComment(postType, contentId, m_nPage);


					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					JSONArray comments = data.getJSONArray("comments");
					
					if(m_arrComment == null || m_nPage == 0)
						m_arrComment = comments;
					else {
						m_arrComment = MyJSON.addJSONArray(m_arrComment, comments);
					}
					
					if (comments == null || comments.length() < 1) {
						if (m_nPage == 0)
							m_handler.sendEmptyMessage(5);
						else 
							m_handler.sendEmptyMessage(6);
						return;
					}
					
					m_nPage ++;
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				m_handler.sendEmptyMessage(0);

			}
		}).start();
    }
    
    
    
    private void postComment() {

    	final String comment = editComment.getText().toString().trim();
    	
    	if (comment.length() < 1) {
    		Const.showMessage("Warnings!", "Please input comment", (HomeActivity)m_context);
    		return;
    	}
    	
    	
    	HomeActivity.closeKeyboard(m_context, editComment);
        editComment.setText("");
        
        
    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Posting....");

    	progress.show();

    	new Thread(new Runnable() {
			
			public void run() {
				try {

			    	String postType = m_dic.getString("post_type");
			    	String contentId = m_dic.getString("content_id");
			    	
			    	
					JSONObject data = JsonParser.addComment(postType, contentId, comment, m_arrComment.length()+1);

					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					m_arrComment = data.getJSONArray("comments");
					m_nTotalComment = data.getInt("comments_count");
					
					bChanged = true;
					
					m_handler.sendEmptyMessage(0);
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
    
    private void deleteComment() {
    	
    	InputMethodManager imm = (InputMethodManager)m_context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((HomeActivity) m_context).getWindow().getCurrentFocus().getWindowToken(), 0);
        
        
    	progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Deleting....");

    	progress.show();

    	new Thread(new Runnable() {
			
			public void run() {
				try {

					JSONObject comment = null;
					
					comment = m_arrComment.getJSONObject(m_nIndexDel);
					
			    	String postType = comment.getString("post_type");
			    	String contentId = comment.getString("content_id");
			    	String commentId = comment.getString("id");
			    	
					JSONObject data = JsonParser.deleteComment(postType, contentId, commentId, m_arrComment.length()-1);

					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					m_arrComment = MyJSON.RemoveJSONArray(m_arrComment, m_nIndexDel);
					m_nTotalComment = data.getInt("comments_count");
					
					bChanged = true;
					
					m_handler.sendEmptyMessage(0);
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
    
    
    public void onFanDetail(View v) {
 		int index = 0;
 		index = Integer.parseInt(v.getTag().toString());
 		
 		String userId = "";
 		
 		try {
			JSONObject comment = m_arrComment.getJSONObject(index);
			
			userId = comment.getString("user_id");
 		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (userId.equals(Utils.getUserID())) {
			return;
		}
		
 		UserDetailView subView = (UserDetailView) LayoutInflater.from(
				m_context).inflate(R.layout.view_user_profile, null);
// 		UserDetailView subView = new UserDetailView(m_context);
		subView.init(m_context, userId);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		((HomeActivity) m_context).baselayout.addView(subView, params);

		Animation out = AnimationUtils.loadAnimation(m_context,
				R.anim.slide_left);
		subView.startAnimation(out);
 		
    }
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progress.hide();
			
			switch (msg.what) {
			case 0:
				mListView.setPullLoadEnable(true);
				refresh();
				break;
				
			case 5:
				refresh();
			case 6:
				mListView.setPullLoadEnable(false);
				break;
				
			case -1:
				Const.showMessage("", "Network failed.", (HomeActivity)m_context);
				break;
			}
			
		}
    };
    
    private void refresh() {
    	
//		editComment.setText(""); 

		if (adapter == null) {
    		adapter = new ResultListAdapter(m_context);
    		
    		mListView.setAdapter(adapter);
    		mListView.setCacheColorHint(Color.TRANSPARENT);
    	}
    	else {
    		adapter.notifyDataSetChanged();
    	}
    	
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getCurrentTime());
    }
  

    public class ResultListAdapter extends BaseAdapter {

    	private Context mContext = null;
    	
		public ResultListAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return m_arrComment.length();
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
			
//			convertView = null;
			
			final ViewCache holder;

			JSONObject comment = null;
			
			
			try {
				
				comment = m_arrComment.getJSONObject(position);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			if (convertView == null) {
				holder = new ViewCache();
				
				LayoutInflater inflater = LayoutInflater.from(mContext);
				
	    		convertView = inflater.inflate(R.layout.list_comment, null);
	    				 	
    			holder.ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
    			holder.tvUserName = (TextView) convertView.findViewById(R.id.txtUserName);
    			holder.tvMessage = (TextView) convertView.findViewById(R.id.txtMessage);
    			holder.tvTimeStamp = (TextView) convertView.findViewById(R.id.txtTimeStamp);
				
				holder.id = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewCache) convertView.getTag();
				
			}
			
			
			
				
			try {
				
				String image_url = comment.getString("img_url");
				if (image_url == null || image_url.length() < 1) {
					holder.ivAvatar.setImageResource(R.drawable.demo_avatar);
				}
				else {
					((HomeActivity)m_context).imageLoader.displayImage(image_url, holder.ivAvatar,
							((HomeActivity)m_context).optIcon, 
							((HomeActivity)m_context).animateFirstListener);
				}
		       	
		       	holder.ivAvatar.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onFanDetail(v);
					}
				});
		       	holder.ivAvatar.setTag(position);
		       	
		       	
		       	String userName = comment.getString("name");
		       	holder.tvUserName.setText(userName);
		       	
		       	String message = comment.getString("comment");
		       	holder.tvMessage.setText(message);
		       	
		       	String timeStamp = comment.getString("time_stamp");
		       	holder.tvTimeStamp.setText(Const.getTimeAgo(timeStamp));
	       	
		       	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	    		
			return convertView;
			
		}


	}
 	class ViewCache {
 		private  int id = -1;
 		
		private  ImageView ivAvatar;
		private  TextView tvUserName, tvMessage, tvTimeStamp;
	}
	
 	
 	 public void clear () {
     	((HomeActivity)m_context).imageLoader.clearMemoryCache();
     	
     	m_context = null;
    	m_self = null;
    	
    	mListView = null;
    	adapter = null;

    	editComment = null;

    	progress = null;

    	m_dic = null;
    	m_arrComment = null;
     	
     }
 	
    public void onBack() {
    
    	HomeActivity.closeKeyboard(m_context, editComment);

    	if (bChanged) {
    		
    		JSONArray arrComment = new JSONArray();
    		for (int i = 0 ; i < m_arrComment.length() ; i ++) {
    			if (i > 2) 
    				break;
    			
    			try {
					arrComment.put(m_arrComment.get(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		if (m_parentOpt == Global.OPT_ALLACCESS) {
    			AllAccessView.g_AllAccessView.onCommentDone(m_nParentIndex, arrComment, m_nTotalComment);
    		}
    		else if (m_parentOpt == Global.OPT_DRAFTVIEW) {
    			DraftReviewView.g_DraftView.onCommentDone(m_nParentIndex, arrComment, m_nTotalComment);
    		}
//    		else if (m_parentOpt == Global.OPT_USERDETAIL) {
//    			UserDetailView.g_UserDetailView.initData();
//    		}
    		else if (m_parentOpt == Global.OPT_PROFILE) {
    			ProfileView.g_profileView.onCommentDone(m_nParentIndex, arrComment, m_nTotalComment);
    		}
    		else if (m_parentOpt == Global.OPT_COMMUNITY) {
    			CommunityView.g_CommunityView.onCommentDone(m_nParentIndex, arrComment, m_nTotalComment);
    		}
    		else if (m_parentOpt == Global.OPT_PHOTODETAIL) {
    			PhotoDetailView.g_PhotoDetailView.onCommentDone(m_nParentIndex, arrComment, m_nTotalComment);
    		}
    		else if (m_parentOpt == Global.OPT_VIDEO_GALLERY) {
    			VideosView.g_VideosView.onCommentDone(m_nParentIndex, arrComment, m_nTotalComment);
    		}
    		else if (m_parentOpt == Global.OPT_PHOTO_GALLERY) {
    			PhotoListView.g_PhotoListView.onCommentDone(m_nParentIndex, arrComment, m_nTotalComment);
    		}
    		
    	}

    	((HomeActivity)m_context).removeHistory(getClass().getSimpleName());
    	
    	Animation in = AnimationUtils.loadAnimation(m_context, R.anim.slide_right);
    	m_self.startAnimation(in);

    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	
    	    	((HomeActivity)m_context).baselayout.removeView(m_self);
    	    }
    	});
    }
    
    private void onClickPost() {
    	
    	String comment = editComment.getText().toString().trim();
    	if (comment.length() < 1) {
    		Const.showMessage("warning", "Please input comment", (HomeActivity)m_context);
    		return;
    	}

    	postComment();
    }
  
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(m_context, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(m_context, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("Search Layout", "Handling Keyboard Window shown");

        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();

        if (actualHeight > proposedheight){
            // Keyboard is shown
        	mListView.smoothScrollToPosition(0);
        	
        } else {
            // Keyboard is hidden
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    
    }
    
    @Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		m_nPage = 0;
		getComments();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getComments();
	}
}
