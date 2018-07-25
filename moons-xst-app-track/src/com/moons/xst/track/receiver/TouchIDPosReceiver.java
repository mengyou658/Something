package com.moons.xst.track.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.ui.DianjianTouchIDPos;
import com.moons.xst.track.ui.LoginDialog;
import com.moons.xst.track.ui.Map_main;
import com.moons.xst.track.ui.RFIDLogin;
import com.moons.xst.track.ui.Tool_NFC;


public class TouchIDPosReceiver extends BroadcastReceiver {
	private DianjianTouchIDPos mDianjianTouchIDPosActivity;
	private Tool_NFC mTool_NFC;
	private Map_main map_main;
	private String mOpera = "";
	private Activity mActivity;
	
	MediaPlayer player;
	
	public TouchIDPosReceiver(Activity activity, String Oper) {
		mOpera = Oper;
		mActivity = activity;
		if (mOpera.equals("DJIDPos"))
			mDianjianTouchIDPosActivity = (DianjianTouchIDPos)activity;
		else if (mOpera.equals("Tool_NFC"))
			mTool_NFC = (Tool_NFC)activity;
		else if(mOpera.equals("MAP_NFC"))
			map_main=(Map_main) activity;
		
		player = MediaPlayer.create(mActivity, R.raw.notificationsound);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		
    	String curRfidNo = bundle.getString("RfidNo");
    	//处理接收到的内容
    	if (!StringUtils.isEmpty(curRfidNo)) {
    		
    		//调用声音
//    	    player.start();
    		// 调用振动
//    		Vibrator vibrator = (Vibrator)mActivity.getSystemService(Context.VIBRATOR_SERVICE);
//    	    vibrator.vibrate(500);
    	       	    
    		if (mOpera.equals("DJIDPos")) {
        	    player.start();
    			mDianjianTouchIDPosActivity.touchIDPos(curRfidNo,true);	
    		}
    		else if (mOpera.equals("Tool_NFC")) { 
    			mTool_NFC.showIDPosInfo(curRfidNo);
    		}else if(mOpera.equals("MAP_NFC")){
    			map_main.mapReceive(curRfidNo);
    		}
    	}
	}
}