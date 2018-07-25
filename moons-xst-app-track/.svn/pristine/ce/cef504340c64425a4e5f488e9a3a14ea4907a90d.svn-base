package com.moons.xst.track.common;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

public class HwCfgInfo {
	
	ContentResolver resolver = null; 
     
    public HwCfgInfo(Context context){ 
        resolver = context.getContentResolver(); 
    } 
    public boolean isHwSupportRfid(){ 
        if(resolver == null)return false; 
        String rfid = Settings.System.getString(resolver, "rfid"); 
        return (rfid.compareTo("Yes") == 0); 
    } 
    public boolean isHwSupportVibration(){ 
        if(resolver == null)return false; 
        String vib = Settings.System.getString(resolver, "vibration"); 
        return (vib.compareTo("Yes") == 0); 
    } 
    public boolean isSupportTemperature(){ 
        if(resolver == null)return false; 
        String temp = Settings.System.getString(resolver, "temperature"); 
        return (temp.compareTo("Yes") == 0); 
    } 
    public boolean isSupportSpeed(){ 
        if(resolver == null)return false; 
        String speed = Settings.System.getString(resolver, "speed"); 
        return (speed.compareTo("Yes") == 0); 
    } 
    public boolean isSupportWalkieTalkie(){ 
        if(resolver == null)return false; 
        String wk = Settings.System.getString(resolver, "walkietalkie"); 
        return (wk.compareTo("Yes") == 0); 
    } 
    public boolean isRotateCamera(){ 
        if(resolver == null)return false; 
        String rc = Settings.System.getString(resolver, "rotateCamera"); 
        return (rc.compareTo("Yes") == 0); 
    } 
}