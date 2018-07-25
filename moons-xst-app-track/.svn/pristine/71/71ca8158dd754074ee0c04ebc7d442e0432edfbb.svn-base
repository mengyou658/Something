package com.moons.xst.track.common;

import com.moons.xst.track.AppContext;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class GPSManagerHelper {
	
	private static GPSManagerHelper instance = null;  
	
	private Context mContext;
	
	private GPSManagerHelper(Context context) {
		mContext = context;
	}
	
	public static GPSManagerHelper getGPSManagerHelper(Context context) {  
		   
	     synchronized (GPSManagerHelper.class) {  
	         if (instance == null) {  
	             instance = new GPSManagerHelper(context);  
	         }  
	     }  
	     return instance;  
	}
	
	private boolean getGpsState() {  
	    ContentResolver resolver = mContext.getContentResolver();  
	    boolean state = Settings.Secure.isLocationProviderEnabled(resolver,  
	            LocationManager.GPS_PROVIDER);   
	    return state;  
	}  
	  
	/** 
	 *  
	 * @param context 
	 */  
	public synchronized void openGpsPrompt() {  
	    if (!getGpsState()) {
	    	AppContext.isAskByStartApp = true;
	    	Intent intent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
	    }	    
	}
	
	public synchronized void closeGpsPrompt() {
		if (getGpsState()) {
	    	Intent intent = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
	    }
	}
}