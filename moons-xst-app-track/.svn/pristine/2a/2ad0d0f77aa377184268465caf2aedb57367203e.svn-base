package com.moons.xst.track.receiver;

/**
 *  开机自动启动APP
 *  以静态注册广播方式注册，不会随着APP关闭而销毁，目前暂没使用。
 *  如需使用此功能，只需放开AndroidManifest.xml文件里的如下代码块即可
 *  <!--  
        <receiver android:name="com.moons.xst.track.AppBootBroadcastReceiver">
            <intent-filter>
    			<action android:name="android.intent.action.BOOT_COMPLETED" />
   			</intent-filter>
  		</receiver>
  	-->
 *  author: wujunyi
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moons.xst.track.AppStart;
import com.moons.xst.track.ui.Tool_NFC;

public class AppBootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
 
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent appStartIntent = new Intent(context, AppStart.class);  // 要启动的Activity
            appStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appStartIntent);
        }
    }
}