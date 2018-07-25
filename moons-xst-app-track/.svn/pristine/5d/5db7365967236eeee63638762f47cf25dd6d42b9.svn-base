package com.moons.xst.track.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.ui.DianjianTouchIDPos;
import com.moons.xst.track.ui.Tool_NFC;

public class StaticIDPosReceiver extends BroadcastReceiver {
	
	static final String ACTION = "android.intent.action.RfidActivity";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle bundle = intent.getExtras();	
    	String curRfidNo = bundle.getString("RfidNo");
    	
    	//处理接收到的内容
    	if (!StringUtils.isEmpty(curRfidNo)) {
    		
    		if (AppContext.mStartRFID.equalsIgnoreCase(
    				AppContext.StartRFID.DJ_IDPOS.toString()))
    			DianjianTouchIDPos.getInstance().touchIDPos(curRfidNo,true);	
    		else if (AppContext.mStartRFID.equalsIgnoreCase(
    				AppContext.StartRFID.Tool_NFC.toString())) {
    			Tool_NFC.getInstance().showIDPosInfo(curRfidNo);
    		}
    	}
	}
}