package com.mycom.lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Const {

	
	
	public static String DATE_FORMAT = "yyyyMMdd";
	
	public static String FILE_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/StarClub/";
	
	
	
	@SuppressLint("DefaultLocale")
	public static String getFullTimeAgo(String str) {
		
		double time = 0;
		try {
			time = Double.parseDouble(str);
		} catch (Exception e) {
			time = 0;
		}
		
		double deltaSeconds = System.currentTimeMillis()/1000 - time;
		
//		System.out.println("delta = " + deltaSeconds);
		
		int deltaMinutes = (int) (deltaSeconds / 60.0f);
	    
	    int minutes;
	    
	    if(deltaSeconds < 120 || deltaMinutes < 5)
	    {
	        return "Just now";
	    }
	    else if (deltaMinutes < 60)
	    {
	        return String.format("%dm ago", deltaMinutes);
	    }
	    else if (deltaMinutes < 120)
	    {
	        return "1h ago";
	    }
	    else if (deltaMinutes < (24 * 60))
	    {
	        minutes = (int)Math.floor(deltaMinutes/60);
	        return String.format("%dh ago", minutes);
	    }
	    else if (deltaMinutes < (24 * 60 * 2))
	    {
	        return "Yesterday";
	    }
	    else if (deltaMinutes < (24 * 60 * 7))
	    {
	        minutes = (int)Math.floor(deltaMinutes/(60 * 24));
	        return String.format("%dd ago", minutes);
	    }
	    else if (deltaMinutes < (24 * 60 * 14))
	    {
	        return "1w ago";
	    }
	    else if (deltaMinutes < (24 * 60 * 31))
	    {
	        minutes = (int)Math.floor(deltaMinutes/(60 * 24 * 7));
	        return String.format("%dw ago", minutes);
	    }
	    else if (deltaMinutes < (24 * 60 * 61))
	    {
	        return "1M";
	    }
	    else if (deltaMinutes < (24 * 60 * 365.25))
	    {
	        minutes = (int)Math.floor(deltaMinutes/(60 * 24 * 30));
	        return String.format("%dM ago", minutes);
	    }
	    else if (deltaMinutes < (24 * 60 * 731))
	    {
	        return "1y";
	    }
	    
	    minutes = (int)Math.floor(deltaMinutes/(60 * 24 * 365));
	    return String.format("%dy ago", minutes);
	}
	
	@SuppressLint("DefaultLocale")
	public static String getTimeAgo(String str) {
		
		double time = 0;
		try {
			time = Double.parseDouble(str);
		} catch (Exception e) {
			time = 0;
		}
		
		double deltaSeconds = System.currentTimeMillis()/1000 - time;
		
//		System.out.println("delta = " + deltaSeconds);
		
		int deltaMinutes = (int) (deltaSeconds / 60.0f);
	    
	    int minutes;
	    
	    if(deltaSeconds < 120 || deltaMinutes < 5)
	    {
//	        return "Just now";
	    	return "1m";
	    }
	    else if (deltaMinutes < 60)
	    {
	        return String.format("%dm", deltaMinutes);
	    }
	    else if (deltaMinutes < 120)
	    {
	        return "1h";
	    }
	    else if (deltaMinutes < (24 * 60))
	    {
	        minutes = (int)Math.floor(deltaMinutes/60);
	        return String.format("%dh", minutes);
	    }
	    else if (deltaMinutes < (24 * 60 * 2))
	    {
	        return "1d";
	    }
	    else if (deltaMinutes < (24 * 60 * 7))
	    {
	        minutes = (int)Math.floor(deltaMinutes/(60 * 24));
	        return String.format("%dd", minutes);
	    }
	    else if (deltaMinutes < (24 * 60 * 14))
	    {
	        return "1w";
	    }
	    else if (deltaMinutes < (24 * 60 * 31))
	    {
	        minutes = (int)Math.floor(deltaMinutes/(60 * 24 * 7));
	        return String.format("%dw", minutes);
	    }
	    else if (deltaMinutes < (24 * 60 * 61))
	    {
	        return "1M";
	    }
	    else if (deltaMinutes < (24 * 60 * 365.25))
	    {
	        minutes = (int)Math.floor(deltaMinutes/(60 * 24 * 30));
	        return String.format("%dM", minutes);
	    }
	    else if (deltaMinutes < (24 * 60 * 731))
	    {
	        return "1Y";
	    }
	    
	    minutes = (int)Math.floor(deltaMinutes/(60 * 24 * 365));
	    return String.format("%dy", minutes);
	}
	
	public static void showMessage(String strTitle, String strMessage, Activity parent) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(parent);
		dialog.setTitle(strTitle);
		dialog.setMessage(strMessage);
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setNegativeButton("Ok", null);
		dialog.show();
	}
	public static void showMessage(Activity parent) {
		String strTitle = "Oops!";
		String strMessage = "We seem to be experiencing a system overload. Please try again in a few minutes.";
		AlertDialog.Builder dialog = new AlertDialog.Builder(parent);
		dialog.setTitle(strTitle);
		dialog.setMessage(strMessage);
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setNegativeButton("Ok", null);
		dialog.show();
	}
	
	public static void showToastMessage(String toasttext, Context context) {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, toasttext, duration);
		toast.show();
	}
	
	public static void showCustomToastMessage(String toasttext, Context context) {
		int duration = Toast.LENGTH_LONG;
		
		Toast toast = Toast.makeText(context, toasttext, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		
//		LinearLayout toastLayout = (LinearLayout) toast.getView();
//		TextView toastTV = (TextView) toastLayout.getChildAt(0);
//		toastTV.setTextSize(toastTV.getTextSize() + 5);
		
		toast.show();
	}
	
	
	public static int toInt(String str) {
		int result = -1;

		try {
			float fRet = Float.parseFloat(str);
			
			result = (int) fRet;

		} catch (Exception e) {
			// TODO: handle exception
			result = 0;
		}

		return result;

	}
	
	public static float toFloat(String str) {
		float result = 0.0f;

		// result = Integer.parseInt(str);

		try {
			result = Float.parseFloat(str);
		} catch (Exception e) {
			// TODO: handle exception
			result = 0.0f;
		}

		return result;

	}
	
	public static double toDouble(String str) {
		double result = 0.0f;

		try {
			result = Double.parseDouble(str);
		} catch (Exception e) {
			// TODO: handle exception
			result = 0.0f;
		}

		return result;

	}

	public static Date stringToDate(String dateString) {

		String fmt = DATE_FORMAT;
		try {
			return (new SimpleDateFormat(fmt)).parse(dateString);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	public static String convertString(String str) {
		if (str == null) {
			return "";
		}
		if (str.indexOf("null") > -1) {
			return "";
		}
		return str;
	}
	public static String dateToString(Date date) {
		String fmt = DATE_FORMAT;

		try {
			return (new SimpleDateFormat(fmt)).format(date);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	

	
	public static void Debug(String _debug) {
		System.out.println(_debug);

	}
}
