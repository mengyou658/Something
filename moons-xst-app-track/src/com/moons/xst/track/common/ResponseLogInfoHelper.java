package com.moons.xst.track.common;

import com.google.gson.Gson;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.bean.ResponseLogInfo;
import com.moons.xst.track.communication.WebserviceFactory;

public class ResponseLogInfoHelper {
	
	private static ResponseLogInfoHelper instance = null;
	
	private static String mCommType = "";
	private static boolean mState = true;
	private static String mLogInfo = "";
	private static String mConnectType = "0";
	
	public static ResponseLogInfoHelper getHelper(String commType, boolean state,
			String logInfo, String connectType) {  
		   
	     synchronized (ResponseLogInfoHelper.class) {  
	         if (instance == null) {  
	             instance = new ResponseLogInfoHelper();  
	         }  
	         mCommType = commType;
	         mState = state;
	         mLogInfo = logInfo;
	         mConnectType = connectType;
	     }  
	     return instance;  
	}
	
	public boolean ResponseLogInfoToServer() {
		/*try {
			ResponseLogInfo entity = new ResponseLogInfo();
			entity.setCommType(mCommType);
			String logState = mState ? AppConfig.LOGSTATUS_NORMAL : AppConfig.LOGSTATUS_ERROR;
			entity.setLogStatus(logState);
			entity.setLogInfo(mLogInfo);
			String connectType = mConnectType.equals("0") ? AppConfig.CONNECTIONTYPE_WIFI : AppConfig.CONNECTIONTYPE_USB;
			entity.setConnectionType(connectType);
			return WebserviceFactory
					.ResponseJIT(new Gson().toJson(entity));
		} catch (Exception ex) {
			return false;
		}*/
		return true;
	}
}