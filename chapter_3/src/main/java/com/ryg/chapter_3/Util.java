package com.ryg.chapter_3;

import android.view.MotionEvent;

/**
 * createBy	 keepon
 */
public class Util {
    public static String getActioString(MotionEvent event){
        int action = event.getAction();
        if(action ==MotionEvent.ACTION_DOWN){
            return "ACTION_DOWN";
        } else if(action ==MotionEvent.ACTION_MOVE){
            return "ACTION_MOVE";
        }else if(action ==MotionEvent.ACTION_UP){
            return "ACTION_UP";
        }else if(action ==MotionEvent.ACTION_CANCEL){
            return "ACTION_CANCEL";
        }
        return "";
    }
}
