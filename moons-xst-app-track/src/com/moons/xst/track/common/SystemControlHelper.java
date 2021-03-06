/**
 * 
 */
package com.moons.xst.track.common;

import java.util.List;

import com.moons.xst.track.AppContext;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;

/**
 * 系统控制类
 * 
 * @author LKZ
 * 
 */
public class SystemControlHelper {

	/**
	 * 设置系统时间
	 * 
	 * 系统时间与标准时间差值大于1分钟时才进行校准
	 * 
	 * 前提：需要获取到系统级别操作权限
	 * 
	 * @param datetimelong
	 * @return 是否执行了校准
	 */
	public static Boolean setSystemDatatime(Long datetimelong) {
		try {
			if (datetimelong > 0) {
				long timeSpan = datetimelong - System.currentTimeMillis();
				if (Math.abs(timeSpan) > 1000 * 60) {
					SystemClock.setCurrentTimeMillis(datetimelong);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean setSystemTimeZone(String timeZone) {
		try {
			AlarmManager alarm =(AlarmManager)AppContext.baseContext.getSystemService(Context.ALARM_SERVICE);
			alarm.setTimeZone(timeZone);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 根据包名启动另外一个APP
	 * 
	 * @param context
	 * @param packagename
	 * @author lkz
	 */
	public static void doStartApplicationWithPackageName(Context context,
			String packagename) {
		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(
					packagename, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return;
		}
		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// 通过getPackageManager()的queryIntentActivities方法遍历
		List<ResolveInfo> resolveinfoList = context.getPackageManager()
				.queryIntentActivities(resolveIntent, 0);
		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			// 设置ComponentName参数1:packagename参数2:MainActivity路径
			ComponentName cn = new ComponentName(packageName, className);
			intent.setComponent(cn);
			context.startActivity(intent);
		}
	}
	
	private static long lastClickTime; 
    public static boolean isFastDoubleClick() { 
        long time = System.currentTimeMillis(); 
        long timeD = time - lastClickTime; 
        if ( 0 < timeD && timeD < 800) {    
            return true;    
        }    
        lastClickTime = time;    
        return false;    
    } 
}
