package com.network.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mycom.lib.UserDefault;


public class JsonParser {
	
	public static JSONObject getUserLogin(String strUserName, String strPassword) {
		String strResult = MyParser.getStringFromUrl(Utils.getLoginUrl(strUserName, strPassword));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		
		try {

			JSONObject result = new JSONObject(strResult);
			
			boolean statue = result.optBoolean("status");
			if (statue) {
				JSONObject userInfo = result.optJSONObject("info");
				String token = result.optString("token");
				
				userInfo.put("password", strPassword);
				userInfo.put("token", token);

				return userInfo;
			}
			
		} catch (Exception e) {}

		return null;
	}
	
	public static JSONObject getMainFeed(String postType, int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getMainFeedUrl(postType, page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}

	public static JSONObject getCommunityFeed(String postType, int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getCommunityFeedUrl(postType, page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject getCommunityFeedFollowed(String postType, int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getCommunityFeedFollowedUrl(postType, page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}

	
	public static JSONObject getUserProfile(int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getUserProfileUrl(page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject getUserDetail(String userId, int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getUserDetailUrl(userId, page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getMessage(int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getMessageUrl(page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getRanking(int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getRankingUrl(page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject getRanking(String search) {
		String strResult = MyParser.getStringFromUrl(Utils.getRankingUrl(search));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject readMessage(String mailId) {
		String strResult = MyParser.getStringFromUrl(Utils.readMessageUrl(mailId));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject addText(String text) {
		String strResult = MyParser.getStringFromUrl(Utils.addTextUrl(text));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject addLike(String postType, String contentId, int like) {
		
		String strResult = MyParser.getStringFromUrl(Utils.getAddLikeUrl(postType, contentId, like));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}
	
	
	public static JSONObject getDeleteFeed(String postType, String contentId) {
		
		String strResult = MyParser.getStringFromUrl(Utils.getDeleteFeedUrl(postType, contentId));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}

	public static JSONObject buyLock(String postType, String contentId) {
		
		String strResult = MyParser.getStringFromUrl(Utils.getBuyLockUrl(postType, contentId));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}
	
	public static JSONObject setFollowing(String userId) {
		
		String strResult = MyParser.getStringFromUrl(Utils.setFollowingUrl(userId));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}
	public static JSONObject setUnFollowing(String userId) {
		
		String strResult = MyParser.getStringFromUrl(Utils.setUnFollowingUrl(userId));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}

	public static JSONObject setBlockFan(String userId) {
		
		String strResult = MyParser.getStringFromUrl(Utils.setBlockFanUrl(userId));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}
	public static JSONObject setUnBlockFan(String userId) {
		
		String strResult = MyParser.getStringFromUrl(Utils.setUnBlockFanUrl(userId));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}

	public static JSONObject getComment(String postType, String contentId, int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getCommentUrl(postType, contentId, page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject addComment(String postType, String contentId, String comment, int commentCount) {
		String strResult = MyParser.getStringFromUrl(Utils.addCommentUrl(postType, contentId, 
				comment, commentCount));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject deleteComment(String postType, String contentId, String commentId, int commentCount) {
		String strResult = MyParser.getStringFromUrl(Utils.delCommentUrl(postType, contentId, commentId, commentCount));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}

	public static JSONObject getPhotoGallery(int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getPhotoGalleryUrl(page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject getVideoGallery(int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getVideoGalleryUrl(page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject getTourDate() {
		String strResult = MyParser.getStringFromUrl(Utils.getTourDateUrl());
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getShop() {
		String strResult = MyParser.getStringFromUrl(Utils.getShopUrl());
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getExtraInfo(String postType, String contentId) {
		String strResult = MyParser.getStringFromUrl(Utils.getExtraInfoUrl(postType, contentId));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject sendMessage(String receiverId, String text) {
		String strResult = MyParser.getStringFromUrl(Utils.sendMessageUrl(receiverId, text));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getQuizData(String postType, String contentId) {
		String strResult = MyParser.getStringFromUrl(Utils.getQuizDataUrl(postType, contentId));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getPollAnswer(String answerId, String questionId) {
		String strResult = MyParser.getStringFromUrl(Utils.getPollAnswerUrl(answerId, questionId));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getQuizAnswer(String quizId, int quizCount, int answerCount) {
		String strResult = MyParser.getStringFromUrl(Utils.getQuizAnswerUrl(quizId, quizCount, answerCount));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	
	public static JSONObject getMusic() {
		String strResult = MyParser.getStringFromUrl(Utils.getMusicUrl());
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getPush() {
		String strResult = MyParser.getStringFromUrl(Utils.getPushUrl());
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject setPush(int enable) {
		String strResult = MyParser.getStringFromUrl(Utils.setPushUrl(enable));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	

	public static JSONObject getForgetPassword(String email) {
		String strResult = MyParser.getStringFromUrl(Utils.getForgetPasswordUrl(email));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	public static JSONObject getAdminList() {
		String strResult = MyParser.getStringFromUrl(Utils.getAdminListUrl());
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getLikeUser(String postType, String contentId, int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getLikeUserUrl(postType, contentId, page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getDeepLink(String postType, String contentId) {
		String strResult = MyParser.getStringFromUrl(Utils.getDeepLinkUrl(postType, contentId));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject getDraftFeed(String postType, int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getDraftFeedUrl(postType, page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	public static JSONObject setApprove(String postType, String contentId, int like) {
		
		String strResult = MyParser.getStringFromUrl(Utils.setApproveUrl(postType, contentId, like));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}
	public static JSONObject setPublish(String postType, String contentId) {
		
		String strResult = MyParser.getStringFromUrl(Utils.setPublishUrl(postType, contentId));
		if ( strResult == null || strResult.length() == 0 )
			return null;

		return getJSONResult(strResult);
	}
	public static JSONObject getApprovalUser(String postType, String contentId, int page) {
		String strResult = MyParser.getStringFromUrl(Utils.getApprovalUserUrl(postType, contentId, page));
		if ( strResult == null || strResult.length() == 0 )
			return null;
		
		return getJSONResult(strResult);
	}
	
	/////////////////////////
	private static JSONObject getJSONResult(String strResult) {
		try {

			JSONObject result = new JSONObject(strResult);

//			System.out.println("result = " + result.toString());
			
			boolean statue = result.optBoolean("status");
			if (statue) {

				return result;
			}
			
		} catch (Exception e) {}
		
		return null;
		
	}

}
