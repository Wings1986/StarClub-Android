package com.basic.views;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.Global;
import com.mycom.lib.Const;
import com.mycom.lib.UserDefault;
import com.starclub.enrique.HomeActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

public class BasePostView extends BaseView {

	Context m_context = null;
	public static BasePostView g_basePostView = null;
	
	
	HashMap<String, Object> m_postObj = null;
    boolean m_bIsPublished = false;
    
    int bFacebook = 0;
    int bTwitter = 0;
    int bInstagram = 0;
    
    
	public BasePostView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        
        initValue(context);
       
    }

    public BasePostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initValue(context);
    }

    public BasePostView(Context context) {
        super(context);
        
        initValue(context);
    }
    
    private void initValue(Context context) {
        m_context = context;
        g_basePostView = this;
        
//        if (Global.getUserType() != Global.FAN)
        	((HomeActivity)m_context).m_bDraftShare = true;   	
    }
    
    public void setResultDic(JSONObject feed, Bitmap image) {
    	m_bIsPublished = true;
    	
    	String text = "";
		String imageUrl = "";
		String postType = "";
		String contentId = "";
		String deeplink = "";
		
 		try {
			text = feed.getString("caption");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
 		try {
 			imageUrl = feed.getString("image_path");
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
 		
 		try {
 			postType = feed.getString("post_type");
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
 		
 		try {
 			contentId = feed.getString("content_id");
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
 		
 		try {
 			deeplink = feed.getString("deep_link_web");
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
	
 		
 		m_postObj = new HashMap<String, Object>();
 		m_postObj.put("TEXT", text);
	    if(image != null) {
	    	m_postObj.put("IMAGE", image);
	    	m_postObj.put("IMAGEURL", imageUrl);
	    }
	    m_postObj.put("POSTTYPE", postType);
	    m_postObj.put("CONTENTID", contentId);
	    m_postObj.put("DEEPLINK", deeplink);
	    
    }

    public void changeCaptionText(String textCaption)
    {
    	m_postObj.put("TEXT", textCaption);
    }
    
    public void onShare() {
    	
        JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");

		try {
			bFacebook = userInfo.getInt("enable_facebook");
			bTwitter = userInfo.getInt("enable_twitter");
			bInstagram = userInfo.getInt("enable_instagram");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (bFacebook == 1) {
			((HomeActivity)m_context).shareFacebook(m_postObj);
		}
		else if (bTwitter == 1) {
			((HomeActivity)m_context).shareTwitter(m_postObj);
		}
		else if (bInstagram == 1) {
			((HomeActivity)m_context).shareInstagram(m_postObj);
		}
		else {
        	Const.showMessage("", "Please Enable Social Sharing in Settings", (HomeActivity)m_context);
            return;
        }
    }
    
    public void onBack() {
    	((HomeActivity)m_context).m_bDraftShare = false;
    }
}
