package com.network.httpclient;

import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class MyParser {

	public static String getStringFromUrl(String strUrl) {
		
		HttpGet httpGet = new HttpGet(strUrl);
		HttpClient client = new DefaultHttpClient();
		String responseData = "";
		try {
			ResponseHandler<String> rh = new BasicResponseHandler();
			responseData = client.execute(httpGet, rh);
		} catch (Exception e) {
			return null;
		}
		return responseData;
	}
	
	public static boolean excuteUrl(String url) {
		boolean bSuccess = true;
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		
		try {
			ResponseHandler<String> rh = new BasicResponseHandler();
			client.execute(httpGet, rh);
		} catch (Exception e) {bSuccess = false;}
		return bSuccess;
	}
	
}
