package com.moons.xst.track.common;

import android.app.Activity;

import com.moons.xst.track.AppContext;

public class PowerSaveManager {
	
	private static PowerSaveManager instance = null;
	
	private Activity mActivity;
	private AppContext appContext;
	
	private PowerSaveManager(Activity activity) {
		mActivity = activity;
		appContext = (AppContext) activity.getApplication();
	}
	
	public static PowerSaveManager getPowerSaveManager(Activity activity) {  
		   
	     synchronized (PowerSaveManager.class) {  
	         if (instance == null) {  
	             instance = new PowerSaveManager(activity);  
	         }  
	     }  
	     return instance;  
	}
	
	public synchronized void open() {
		if (appContext.isWifiClose())
			WifiManagerHelper.getWifiManagerHelper(mActivity).openWifi();
		if (appContext.isGPSClose())
			GPSManagerHelper.getGPSManagerHelper(mActivity).openGpsPrompt();
	}
	
	public synchronized void close() {
		if (appContext.isWifiClose())
			WifiManagerHelper.getWifiManagerHelper(mActivity).closeWifi();
		if (appContext.isGPSClose())
			GPSManagerHelper.getGPSManagerHelper(mActivity).closeGpsPrompt();
	}
}