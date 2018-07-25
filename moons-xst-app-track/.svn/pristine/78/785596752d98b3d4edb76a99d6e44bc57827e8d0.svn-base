package com.moons.xst.track.widget;

import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class TimerTextView extends TextView implements Runnable{
    private boolean isRun = false;
    private long secondCount = 60;

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setFocusable(false);
        this.setClickable(false);
    }

    @Override
    public void run() {
        if(isRun){
            this.setText(ComputeTime());
            postDelayed(this, 1000);
        }else {
            removeCallbacks(this);
        }
    }

    /**
     * 倒计时计算
     */
    private String ComputeTime() {
    	try {
	        String backTime = "";
	        secondCount--;
	        if (secondCount < 0) {           
	            this.isRun = false;
	            this.setVisibility(View.GONE);        	 
	        } else{
	        	long mins = secondCount / 60;
	        	long secs = secondCount % 60;
	        	String minsRes = "00", secsRes = "00";
	        	if (mins > 99)
	        		minsRes = "99";
	        	else if (mins < 10) 
	        		minsRes = StringUtils.leftPad(String.valueOf(mins), "0", 2);
	        	else
	        		minsRes = String.valueOf(mins);
	        	
	        	if (secs < 10)
	        		secsRes = StringUtils.leftPad(String.valueOf(secs), "0", 2);
	        	else
	        		secsRes = String.valueOf(secs);
	            backTime =  minsRes + ":" + secsRes;
	        }
	        return backTime;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "";
    	}
    }

    public void setSecondCount(long secondCount){
        this.secondCount = secondCount;
    }

    public boolean isRun() {
        return isRun;
    }

    public void beginRun() {
        this.isRun = true;
        run();
    }

    public void stopRun(){
        this.isRun = false;
    }
}