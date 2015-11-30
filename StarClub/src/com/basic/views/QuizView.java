package com.basic.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.data.MyJSON;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.network.httpclient.JsonParser;
import com.starclub.enrique.HomeActivity;
import com.starclub.enrique.R;

















import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;




public class QuizView extends BaseView {
	
	public Context m_context = null;
	public static QuizView g_QuizView = null;
	public QuizView m_self = null;
	
	private ViewPager mPager;

	ProgressDialog progress = null;

	private JSONObject m_quizEntry = null;
	private JSONArray m_arrQuizs = null;
	private JSONObject m_dicFeed = null;
	
    int m_nPage = 0;
    
    int m_nSelAnswer = -1;

	int m_countAnswer = 0;
	
    ArrayList<String> arrAnswered = new ArrayList<String>();
    
	public QuizView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public QuizView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuizView(Context context) {
        super(context);
    }
    
    public void init(Context context, JSONObject feed) {
    	m_context = context;
		m_self = this;
		g_QuizView = this;
		
		((HomeActivity)m_context).addHistory(getClass().getSimpleName(), this);
		
		m_quizEntry = feed;

		Button btnBack = (Button) findViewById(R.id.btnBack);
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBack();
			}
		});
    	
    	Button btnNext = (Button) findViewById(R.id.btnNext);
    	btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					onClickNext();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
    	mPager = (ViewPager) findViewById(R.id.pager);
    	mPager.setOnTouchListener(new OnTouchListener()
        {           
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
    	
    	getQuizInfo();
    }
    

	
    private void getQuizInfo () {

		progress = new ProgressDialog(m_context);
		progress.setCancelable(false);
		progress.setMessage("Loading....");

    	progress.show();

    	
		new Thread(new Runnable() {
			
			public void run() {
				
				try {

			    	String postType = m_quizEntry.getString("post_type");
			    	String contentId = m_quizEntry.getString("content_id");
			    			
					JSONObject data = JsonParser.getQuizData(postType, contentId);

					if (data == null || !data.getBoolean("status")) {
						m_handler.sendEmptyMessage(-1);
						return;
					}

					m_arrQuizs = data.getJSONArray("questions");
					m_dicFeed = data.getJSONObject("feed");
					
					m_handler.sendEmptyMessage(0);
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
     
   
    
    public Handler m_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progress.hide();
			
			switch (msg.what) {
			case 0:
				setBackground();
				refresh();
				break;
				
			case -1:
				Const.showMessage("", "Network failed.", (HomeActivity)m_context);
				break;
			}
			
		}
    };
    
    private void setBackground() {
    	String backImageUrl = "";
		try {
			backImageUrl = m_dicFeed.getString("background_image_path");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	int ivWidth = 0;
		try {
			ivWidth = m_dicFeed.getInt("background_img_width");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int ivHeight = 0;
		try {
			ivHeight = m_dicFeed.getInt("background_img_height");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		ImageView ivBackground = (ImageView)findViewById(R.id.ivBackground);
		
		FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) ivBackground.getLayoutParams();
    	int virturlHeight = 320;
    	if (ivWidth != 0 && ivHeight != 0)
    		virturlHeight= ((HomeActivity)m_context).getScreenWidth() * ivHeight / ivWidth;
    	param.height = virturlHeight;
    	ivBackground.setLayoutParams(param);
        
    	((HomeActivity)m_context).imageLoader.displayImage(backImageUrl, ivBackground, ((HomeActivity)m_context).optFull, null);
    }
    
    
    private void refresh() {

    	mPager.setAdapter(new ResultListAdapter(m_context));
    	
    }
  

    public class ResultListAdapter extends PagerAdapter{

    	private Context mContext = null;
    	private LayoutInflater inflater;
    	
		public ResultListAdapter(Context mContext) {
			this.mContext = mContext;
			inflater = ((Activity) mContext).getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		@Override
		public int getCount() {
			return m_arrQuizs.length();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View pollLayout = inflater.inflate(R.layout.item_polls, view, false);
			assert pollLayout != null;
			
			TextView tvTitle = (TextView) pollLayout.findViewById(R.id.tvTitle);
			RadioGroup group = (RadioGroup) pollLayout.findViewById(R.id.radioGroup1);
		    group.setOrientation(RadioGroup.VERTICAL);
			group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					
					View radioButton = group.findViewById(checkedId);
					int idx = group.indexOfChild(radioButton);
					
					m_nSelAnswer = idx;
				}
			});
			

			JSONObject obj = null;
			
			try {
				obj = m_arrQuizs.getJSONObject(position);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String title = "";
			try {
				title = obj.getString("question_name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tvTitle.setText(title);
			
			JSONArray arrAnswers = null;
			try {
				arrAnswers = obj.getJSONArray("answer");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int i = 0 ; i < arrAnswers.length() ; i ++) {
				String btnName = "";
				try {
					JSONObject answer = arrAnswers.getJSONObject(i);
					btnName = answer.getString("name");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        RadioButton rb  = new RadioButton(m_context);
		        rb.setText(btnName);
		        group.addView(rb); //the RadioButtons are added to the radioGroup instead of the layout
			}
			
			view.addView(pollLayout, 0);
			
			return pollLayout;
/*			
			View pollLayout = inflater.inflate(R.layout.item_quiz, view, false);
			assert pollLayout != null;
			
			TextView tvTitle = (TextView) pollLayout.findViewById(R.id.tvTitle);
			LinearLayout layoutGroup = (LinearLayout) pollLayout.findViewById(R.id.layoutGroup);

			JSONObject obj = null;
			
			try {
				obj = m_arrQuizs.getJSONObject(position);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String title = "";
			try {
				title = obj.getString("question_name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tvTitle.setText(title);
			
			JSONArray arrAnswers = null;
			try {
				arrAnswers = obj.getJSONArray("answer");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int i = 0 ; i < arrAnswers.length() ; i ++) {
				String btnName = "";
				try {
					JSONObject answer = arrAnswers.getJSONObject(i);
					btnName = answer.getString("name");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final CheckBox tb = new CheckBox(m_context);
				tb.setText(btnName);
				tb.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						try {
							onClickButton(v, tb.isChecked());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				tb.setTag(position*10 + i);
				
		        layoutGroup.addView(tb);
			}
			
			view.addView(pollLayout, 0);
			
			return pollLayout;
*/			
		}

		
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

	}

    
 	 public void clear () {
     	((HomeActivity)m_context).imageLoader.clearMemoryCache();
     	
     	m_context = null;
    	m_self = null;
    	g_QuizView = null;
    	mPager = null;

    	progress = null;

    	m_quizEntry= null;
    	m_arrQuizs = null;
     	
     }
 	
 	 public void removeQuizView() {
 		((HomeActivity)m_context).baselayout.removeView(m_self);
    	clear();
 	 }
 	 
 	 public void onBack() {
    
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
    	    	clear();
    	    }
    	});
    }
    
    private void onClickNext() throws JSONException {
//    	 if (arrAnswered.size() == 0) {
//    		 Const.showMessage("", "Please answer a question!", (HomeActivity)m_context);
//   		   	return;
//         }

   	 	if (m_nSelAnswer == -1) {
   		 	Const.showMessage("", "Please select an answer.", (HomeActivity)m_context);
   		 	return;
   	 	}

   	 
    	 String choose_answer_id = m_arrQuizs.getJSONObject(m_nPage).getJSONArray("answer").getJSONObject(m_nSelAnswer).getString("is_correct");
    	 if (choose_answer_id.equals("1")) {
    		 m_countAnswer ++;
    	 }

/*    	 
    	 boolean bBreak = false;
          boolean bRight = false;
          
         for (int j = 0 ; j < (int) m_arrQuizs.getJSONObject(m_nPage).getJSONArray("answer").length(); j ++) {
             
             JSONObject item = m_arrQuizs.getJSONObject(m_nPage).getJSONArray("answer").getJSONObject(j);
             
             String answer_id = item.getString("answer_id");
             String is_correct = item.getString("is_correct");
             
             boolean bOneRight = false;
             
             for (int i = 0; i < arrAnswered.size(); i ++) {
                 String choose_answer_id = arrAnswered.get(i);
                 
                 if (answer_id.equals(choose_answer_id)) {
                     if (is_correct.equals("1")) {
                         bOneRight = true;
                         break;
                     }
                     else {
                         bBreak = true;
                         break;
                     }
                 }
             }
             
             if (bBreak == true) {
                 bRight = false;
                 break;
             }
             
             if (bOneRight == false) {
                 if (is_correct.equals("1")) {
                     bRight = false;
                     break;
                 }
             }
             else {
                 bRight = true;
             }
             
         }
    	 
         if (bRight) {
             m_countAnswer ++;
         }
*/
    	 
         m_nPage ++;
         m_nSelAnswer = -1;
         arrAnswered.clear();
         
         if (m_nPage >= m_arrQuizs.length()) {
             
        	 QuizResultView subView = (QuizResultView) LayoutInflater.from(
     				m_context).inflate(R.layout.view_quiz_result, null);
     		subView.init(m_context, m_dicFeed, m_arrQuizs.length(), m_countAnswer);

     		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
     				ViewGroup.LayoutParams.FILL_PARENT,
     				ViewGroup.LayoutParams.FILL_PARENT);
     		((HomeActivity) m_context).baselayout.addView(subView, params);

     		Animation out = AnimationUtils.loadAnimation(m_context,
     				R.anim.slide_left);
     		subView.startAnimation(out);
         }
         else {
        	 mPager.setCurrentItem(m_nPage);
        	 
         }
    }
    
    private void onClickButton(View v, boolean bSelected) throws JSONException {
    	int tag = Integer.parseInt(v.getTag().toString());
    	
    	m_nPage = tag / 10;
    	int index = tag % 10;
    	
//        boolean bSelected = ((CheckBox) v).isSelected();

        JSONObject dicQuestion = m_arrQuizs.getJSONObject(m_nPage);
 	    String ansId = dicQuestion.getJSONArray("answer").getJSONObject(index).getString("answer_id");

        if (bSelected) {
        	arrAnswered.add(ansId);
        } else {
        	arrAnswered.remove(ansId);
        }
    }
  
   
}
