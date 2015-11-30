package com.frame.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.frame.model.Menu;
import com.starclub.enrique.R;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Class to control event of menu, open and close using a static methods 
 * @author Leonardo Salles
 */
public class MenuEventController {

    public static ArrayList<HashMap<String, Object>> menuArray = new ArrayList<HashMap<String,Object>>();
    public static Display display;
    
    public static void open(final Context context, final RelativeLayout layout, final TextView name){
    	Animation in = AnimationUtils.loadAnimation(context, R.anim.push_right_out_80);
    	layout.startAnimation(in);
    	
    	in.setAnimationListener(new Animation.AnimationListener(){
    	    @Override
    	    public void onAnimationStart(Animation arg0) {
    	    }           
    	    @Override 
    	    public void onAnimationRepeat(Animation arg0) {
    	    }           
    	    @Override
    	    public void onAnimationEnd(Animation arg0) {
    	    	name.setVisibility(View.INVISIBLE);
    	    	TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                anim.setDuration(1);
                layout.startAnimation(anim);
                
                display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int left = (int) (display.getWidth() * 0.8);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layout.getLayoutParams());
                
//                int right = display.getWidth() - left;
                params.setMargins(left, 0, -left, 0);
                layout.setLayoutParams(params);
    	    }
    	});
    }
    
    public static void close(final Context context, final RelativeLayout layout, final TextView name){
    	name.setVisibility(View.VISIBLE);
    	Animation out = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
    	layout.startAnimation(out);
    	
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
    	params.setMargins(0, 0, 0, 0);
    	layout.setLayoutParams(params);
    }
    
    public static void closeKeyboard(final Context context, final View view){
    	if (view == null) 
    		return;
    	
    	InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    
}
