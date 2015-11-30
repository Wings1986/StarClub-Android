package com.network.httpclient;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.mycom.data.MyConstants;
import com.mycom.lib.UserDefault;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class Utils {
		
	public static final String saveFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloadDemo/";
	
	
	public static String getUrlEncoded(String strValue) {
		String strUrl = strValue;
		try {
			strUrl = URLEncoder.encode(strValue, "UTF-8");
		} catch (Exception e) {}
		return strUrl;
	}
	
	////////////////////////////////////////
	public static String getParsingString(String strOld) {

		try {
			strOld = URLEncoder.encode(strOld, "UTF-8");
			strOld = strOld.replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strOld;
		
/*
		String strResult = strOld;
		if ( strResult.contains("\\n") )
			strResult = strResult.replaceAll("\\\\n", "");
		if ( strResult.contains("\\r") )
			strResult = strResult.replaceAll("\\\\r", "");
		if ( strResult.contains("\\t") )
			strResult = strResult.replaceAll("\\\\t", "");
		strResult = getUnicodeString(strResult);
		
		if ( strResult.contains("\\u") )
			strResult = strResult.replaceAll("\\\\u", "");
		if ( strResult.contains("\\") )
			strResult = strResult.replaceAll("\\\\", "");
		
		return strResult;
*/		
	}
	
	public static String getUnicodeString(String strSource) {
		String strResult = "";
		while (strSource.contains("\\u")) {
			int nIndex = strSource.indexOf("\\u");
			String strTemp = strSource.substring(nIndex + 2, nIndex + 6);
			String strReplace = "";
			try {
				int nchar = Integer.parseInt(strTemp, 16);
				int []nArray = new int[1];
				nArray[0] = nchar;
				strReplace = new String(nArray, 0, 1);
			}
			catch( Exception e) {
				e.printStackTrace();
			}
			
			strSource = strSource.replaceAll("\\\\u" + strTemp, strReplace);
		}
		
		strResult = strSource;
		return strResult;
	}
	
	
	///////////////////////////////////////
	public static String getUserToken() {
		
		JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
		
		String token = "";
			
		try {
			token = userInfo.getString("token");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return token;
	}
	public static String getUserID() {
		
		JSONObject userInfo = UserDefault.getDictionaryForKey("user_info");
		
		String userId = "";
			
		try {
			userId = userInfo.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return userId;
		
	}
	
	public static String getDeviceToken() {
		String deviceToken = UserDefault.getStringForKey("deviceToken", "123456");
		return deviceToken;
	}

	
	
	public static String getLoginUrl(String email, String password)
	{
	    String urlString = String.format("%soption=login&email=%s&password=%s&cid=%d&ud_token=%s&ud_type=ANDROID", 
				MyConstants.SERVER_URL, email, password, MyConstants.CID, getDeviceToken());
	    
	    System.out.println("login url = " + urlString);
	    
	    return urlString;
	}
	public static String getSignUpUrl()
	{
		 String urlString = String.format("%soption=register", 
				 MyConstants.SERVER_URL);

		 System.out.println("login url = " + urlString);
		 
	    return urlString;
	}

	public static String getUpdateUrl()
	{
		 String urlString = String.format("%soption=update_user", 
				 MyConstants.SERVER_URL);

		 System.out.println("getUpdateUrl url = " + urlString);
		 
	    return urlString;
	}
	
	public static String getMainFeedUrl(String postType, int page)
	{
		 String urlString = String.format("%soption=get_main_feed&cid=%d&token=%s&post_type=%s&user_id=%s&page=%d&comment_count=3", 
				 MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, getUserID(), page);

		 System.out.println("Main Feed url = " + urlString);
		 
	    return urlString;
	}
	
	public static String getCommunityFeedUrl(String postType, int page)
	{
		 String urlString = String.format("%soption=get_community_feed&cid=%d&token=%s&post_type=%s&user_id=%s&page=%d&comment_count=3", 
				 MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, getUserID(), page);

		 System.out.println("Main Feed url = " + urlString);
		 
	    return urlString;
	}

	public static String getCommunityFeedFollowedUrl(String postType, int page)
	{
		 String urlString = String.format("%soption=get_community_of_follow&cid=%d&token=%s&post_type=%s&self_user_id=%s&page=%d&comment_count=3", 
				 MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, getUserID(), page);

		 System.out.println("Main Feed url = " + urlString);
		 
	    return urlString;
	}

	public static String getMessageUrl(int page)
	{
		 String urlString = String.format("%soption=get_message&cid=%d&token=%s&user_id=%s&count=10&page=%d", 
				 MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), page);

		 System.out.println("getMessageUrl = " + urlString);
		 
	    return urlString;
	}	
	
	public static String getRankingUrl(int page)
	{
		 String urlString = String.format("%soption=get_rating&cid=%d&token=%s&count=10&page=%d", 
				 MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), page);

		 System.out.println("getRanking url = " + urlString);
		 
	    return urlString;
	}	
	public static String getRankingUrl(String search)
	{
		 String urlString = String.format("%soption=get_rating&cid=%d&token=%s&search=%s&count=0", 
				 MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getParsingString(search));

		 System.out.println("getRanking url = " + urlString);
		 
	    return urlString;
	}	

	public static String readMessageUrl(String mailId) {
		 String urlString = String.format("%soption=update_seen&cid=%d&token=%s&mail_id=%s", 
				 MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), mailId);

		 System.out.println("readMessage url = " + urlString);
		 
	    return urlString;
	}
	
	public static String addTextUrl(String text)
	{
		 String urlString = String.format("%soption=add_text&cid=%d&token=%s&user_id=%s&description=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), getUrlEncoded(text));

		 System.out.println("addTextUrl = " + urlString);
		 
	    return urlString;
	}
	public static String addImageUrl()
	{
		 String urlString = String.format("%soption=add_photo", 
					MyConstants.SERVER_URL);

		 System.out.println("addImageUrl = " + urlString);
		 
	    return urlString;
	}
	public static String addVideoUrl()
	{
		 String urlString = String.format("%soption=add_video", 
					MyConstants.SERVER_URL);

		 System.out.println("addVideoUrl = " + urlString);
		 
	    return urlString;
	}
	
	public static String getUserProfileUrl(int page)
	{
		 String urlString = String.format("%soption=get_user&cid=%d&token=%s&self_user_id=%s&user_id=%s&page=%d&comment_count=3", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), getUserID(), page);

		 System.out.println("getUserProfile url = " + urlString);
		 
	    return urlString;
	}	
	public static String getUserDetailUrl(String userId, int page)
	{
		 String urlString = String.format("%soption=get_user&cid=%d&token=%s&self_user_id=%s&user_id=%s&page=%d&comment_count=3", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), userId, page);

		 System.out.println("getUser Detail url = " + urlString);
		 
	    return urlString;
	}	
	
	public static String setFollowingUrl(String userId)
	{
		 String urlString = String.format("%soption=set_followuser&cid=%d&token=%s&user_id=%s&followed_user_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), userId);

		 System.out.println("setFollowingUrl url = " + urlString);
		 
	    return urlString;
	}	
	public static String setUnFollowingUrl(String userId)
	{
		 String urlString = String.format("%soption=set_followuser&cid=%d&token=%s&user_id=%s&followed_user_id=%s&is_unfollow=1", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), userId);

		 System.out.println("setUnFollowingUrl = " + urlString);
		 
	    return urlString;
	}	
	
	public static String setBlockFanUrl(String userId)
	{
		 String urlString = String.format("%soption=set_block&cid=%d&token=%s&self_user_id=%s&user_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), userId);

		 System.out.println("setBlockFanUrl url = " + urlString);
		 
	    return urlString;
	}
	public static String setUnBlockFanUrl(String userId)
	{
		 String urlString = String.format("%soption=unset_block&cid=%d&token=%s&self_user_id=%s&user_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), userId);

		 System.out.println("setUnBlockFanUrl url = " + urlString);
		 
	    return urlString;
	}
	
	public static String getAddLikeUrl(String postType, String contentId, int like)
	{
		 String urlString = String.format("%soption=set_like&cid=%d&token=%s&post_type=%s&content_id=%s&user_id=%s&like=%d", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, contentId, getUserID(), like);

		 System.out.println("Add Like url = " + urlString);
		 
	    return urlString;
	}
	
	public static String getDeleteFeedUrl(String postType, String contentId)
	{
		 String urlString = String.format("%soption=delete_feed&cid=%d&token=%s&post_type=%s&content_id=%s&user_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, contentId, getUserID());

		 System.out.println("getDeleteFeed url = " + urlString);
		 
	    return urlString;
	}

	public static String getBuyLockUrl(String postType, String contentId)
	{
		 String urlString = String.format("%soption=buy_content&cid=%d&token=%s&post_type=%s&content_id=%s&user_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, contentId, getUserID());

		 System.out.println("getBuyLock url = " + urlString);
		 
	    return urlString;
	}
	

	public static String getCommentUrl(String postType, String contentId, int page)
	{
		 String urlString = String.format("%soption=get_comment&cid=%d&token=%s&&post_type=%s&content_id=%s&page=%d&comment_count=10", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, contentId, page);

		 System.out.println("getComment url = " + urlString);
		 
	    return urlString;
	}
	public static String addCommentUrl(String postType, String contentId, String comment, int commentCount)
	{
		 String urlString = String.format("%soption=set_comment&cid=%d&token=%s&user_id=%s&post_type=%s&content_id=%s&comment=%s&comment_count=%d", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), postType, contentId, getUrlEncoded(comment), commentCount);

		 System.out.println("add Comment url = " + urlString);
		 
	    return urlString;
	}
	public static String delCommentUrl(String postType, String contentId, String commentId, int commentCount)
	{
		 String urlString = String.format("%soption=delete_comment&cid=%d&token=%s&user_id=%s&post_type=%s&content_id=%s&comment_id=%s&comment_count=%d", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), postType, contentId, commentId, commentCount);

		 System.out.println("del Comment url = " + urlString);
		 
	    return urlString;
	}
	
	public static String getPhotoGalleryUrl(int page)
	{
		 String urlString = String.format("%soption=get_photos&cid=%d&token=%s&user_id=%s&count=36&page=%d", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), page);

		 System.out.println("getPhotoGalleryUrl = " + urlString);
	    return urlString;
	}
	
	public static String getVideoGalleryUrl(int page)
	{
		 String urlString = String.format("%soption=get_videos&cid=%d&token=%s&user_id=%s&page=%d", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), page);

		 System.out.println("getVideoGalleryUrl = " + urlString);
	    return urlString;
	}
	
	public static String getTourDateUrl()
	{
		 String urlString = String.format("%soption=get_events&cid=%d&token=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken());

		 System.out.println("getEventsUrl = " + urlString);
	    return urlString;
	}

	public static String getShopUrl()
	{
		 String urlString = String.format("%soption=get_shops&cid=%d&token=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken());

		 System.out.println("getEventsUrl = " + urlString);
	    return urlString;
	}
	
	public static String getExtraInfoUrl(String postType, String contentId)
	{
		 String urlString = String.format("%soption=get_extradatas&cid=%d&token=%s&user_id=%s&post_type=%s&content_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), postType, contentId);

		 System.out.println("getExtraInfoUrl url = " + urlString);
		 
	    return urlString;
	}
	
	public static String sendMessageUrl(String receiveId, String text)
	{
		 String urlString = String.format("%soption=send_message&cid=%d&token=%s&sender=%s&receiver=%s&message=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), receiveId, getUrlEncoded(text));

		 System.out.println("sendMessageUrl= " + urlString);
		 
	    return urlString;
	}
	
	public static String getQuizDataUrl(String postType, String contentId)
	{
		 String urlString = String.format("%soption=get_poll_quiz_content&cid=%d&token=%s&post_type=%s&content_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, contentId);

		 System.out.println("getQuizDataUrl = " + urlString);
		 
	    return urlString;
	}
	
	public static String getQuizAnswerUrl(String quizId, int quizCount, int answerCount)
	{
		 String urlString = String.format("%soption=quiz_answer&cid=%d&token=%s&user_id=%s&content_id=%s&correct_count=%d&question_count=%d", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), quizId, quizCount, answerCount);

		 System.out.println("getPollAnswerUrl = " + urlString);
		 
	    return urlString;
	}
	
	public static String getPollAnswerUrl(String answerId, String questionId)
	{
		 String urlString = String.format("%soption=poll_answer&cid=%d&token=%s&user_id=%s&answer_id=%s&question_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), answerId, questionId);

		 System.out.println("getPollAnswerUrl = " + urlString);
		 
	    return urlString;
	}

	public static String getMusicUrl()
	{
		 String urlString = String.format("%soption=get_musics&cid=%d&token=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken());

		 System.out.println("getMusicUrl" + urlString);
		 
	    return urlString;
	}
	
	public static String getPushUrl()
	{
		 String urlString = String.format("%soption=get_push&cid=%d&token=%s&user_id=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID());

		 System.out.println("getPushUrl = " + urlString);
		 
	    return urlString;
	}
	public static String setPushUrl(int enable)
	{
		 String urlString = String.format("%soption=push_notification&cid=%d&token=%s&user_id=%s&user_token=%s&enable=%d", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), getUserID(), getDeviceToken(), enable);

		 System.out.println("setPushUrl = " + urlString);
		 
	    return urlString;
	}
	
	public static String getForgetPasswordUrl(String email)
	{
		 String urlString = String.format("%soption=forget_password&cid=%d&email=%s", 
					MyConstants.SERVER_URL, MyConstants.CID, email);

		 System.out.println("getForgetPasswordUrl = " + urlString);
	    return urlString;
	}
	public static String getAdminListUrl()
	{
		 String urlString = String.format("%soption=get_adminlist&cid=%d", 
					MyConstants.SERVER_URL, MyConstants.CID);

		 System.out.println("getAdminListUrl = " + urlString);
	    return urlString;
	}

	public static String getLikeUserUrl(String postType, String contentId, int page)
	{
		 String urlString = String.format("%soption=get_like_users&cid=%d&post_type=%s&content_id=%s&page=%d&count=10", 
					MyConstants.SERVER_URL, MyConstants.CID, postType, contentId, page);

		 System.out.println("getLikeUserUrl url = " + urlString);
		 
	    return urlString;
	}
	
	public static String getDeepLinkUrl(String postType, String contentId)
	{
		 String urlString = String.format("%soption=get_post_detail&cid=%d&user_id=%s&post_type=%s&content_id=%s&comment_count=3", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserID(), postType, contentId);

		 System.out.println("getDeepLinkUrl url = " + urlString);
		 
	    return urlString;
	}
	
	public static String getDraftFeedUrl(String postType, int page)
	{
		 String urlString = String.format("%soption=get_main_feed&cid=%d&token=%s&post_type=%s&user_id=%s&page=%d&comment_count=3&draft=1", 
					MyConstants.SERVER_URL, MyConstants.CID, getUserToken(), postType, getUserID(), page);

		 System.out.println("Draft Feed url = " + urlString);
		 
	    return urlString;
	}
	public static String setPublishUrl(String postType, String contentId)
	{
		 String urlString = String.format("%soption=set_publish&cid=%d&post_type=%s&content_id=%s&user_id=%s&time_stamp=1", 
					MyConstants.SERVER_URL, MyConstants.CID, postType, contentId, getUserID());

		 System.out.println("setPublishUrl = " + urlString);
		 
	    return urlString;
	}
	public static String setApproveUrl(String postType, String contentId, int like)
	{
		 String urlString = String.format("%soption=set_approval&cid=%d&post_type=%s&content_id=%s&user_id=%s&approval=%d", 
					MyConstants.SERVER_URL, MyConstants.CID, postType, contentId, getUserID(), like);

		 System.out.println("setApproveUrl = " + urlString);
		 
	    return urlString;
	}
	public static String getApprovalUserUrl(String postType, String contentId, int page)
	{
		 String urlString = String.format("%soption=get_approval_users&cid=%d&post_type=%s&content_id=%s&page=%d&count=10", 
					MyConstants.SERVER_URL, MyConstants.CID, postType, contentId, page);

		 System.out.println("getApprovalUserUrl url = " + urlString);
		 
	    return urlString;
	}
}
