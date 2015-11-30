package com.mycom.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.mycom.lib.UserDefault;

public class MyJSON {

	public static JSONArray addJSONArray(JSONArray orgArray,  JSONArray jarray) {
		if (orgArray == null) {
			return jarray;
		}
		if (jarray == null) {
			return orgArray;
		}
		
		try{
			for(int i=0;i<jarray.length();i++){     
				orgArray.put(jarray.get(i));     
			}
		}catch (Exception e){e.printStackTrace();}
		
		return orgArray;
		
	}

	public static JSONArray copyJSONArray(JSONArray jarray) {
		
		JSONArray Njarray=new JSONArray();
		try{
		for(int i=0;i<jarray.length();i++){     
			Njarray.put(jarray.get(i));     
		}
		}catch (Exception e){e.printStackTrace();}
		
		return Njarray;
		
	}
	
	
	public static JSONArray clearJSONArray(JSONArray jarray) {
		
		JSONArray Njarray=new JSONArray();
		
		return Njarray;
	}
	
	public static JSONArray RemoveJSONArray( JSONArray jarray, int pos) {

		JSONArray Njarray=new JSONArray();
		try{
		for(int i=0;i<jarray.length();i++){     
		    if(i!=pos)
		        Njarray.put(jarray.get(i));     
		}
		}catch (Exception e){e.printStackTrace();}
		
			
		return Njarray;

	}

	public static JSONArray RemoveJSONArray( JSONArray jarray, JSONObject obj) {

		JSONArray Njarray=new JSONArray();
		try{
		for(int i=0;i<jarray.length();i++){
			String key1 = obj.toString();
			String key2 = jarray.get(i).toString();
			if (!key1.equals(key2))
		        Njarray.put(jarray.get(i));     
		}
		}catch (Exception e){e.printStackTrace();}
		
			
		return Njarray;

	}

	public static JSONArray addOneJSONArray(JSONArray orgArray,  JSONArray jarray) {
		if (orgArray == null) {
			return jarray;
		}
		if (jarray == null) {
			return orgArray;
		}
		
		try{
			for(int i=0;i<jarray.length();i++){
				Object obj = jarray.get(i);
				
				boolean bExist = false;
				for (int j = 0 ; j < orgArray.length() ; j ++) {
					if (orgArray.get(j).equals(obj)) {
						bExist = true;
						break;
					}
				}
				if (!bExist)
					orgArray.put(jarray.get(i));     
			}
		}catch (Exception e){e.printStackTrace();}
		
		return orgArray;
		
	}
	
}


