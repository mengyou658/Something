package com.moons.xst.track.common;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiManagerHelper {
	
	private static WifiManagerHelper instance = null;  
	
	private Context mContext;
	private WifiManager wifimanager;
	
	private WifiManagerHelper(Context context) {
		mContext = context;
		wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public static WifiManagerHelper getWifiManagerHelper(Context context) {  
		   
	     synchronized (WifiManagerHelper.class) {  
	         if (instance == null) {  
	             instance = new WifiManagerHelper(context);  
	         }  
	     }  
	     return instance;  
	}
	
	/**
	* WiFi Operate
	* */
	/**Open WiFi**/
	public synchronized void openWifi() {
		
		if (!wifimanager.isWifiEnabled()) {
			wifimanager.setWifiEnabled(true);		
		}
	}
	
	/**Close WiFi**/
	public synchronized void closeWifi() {
		
		if (wifimanager.isWifiEnabled()) {
			wifimanager.setWifiEnabled(false);			
		}
	}
	
	/**WiFi State**/
	public int checkState() {
		return wifimanager.getWifiState();
	}
}