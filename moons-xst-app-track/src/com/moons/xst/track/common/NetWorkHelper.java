/**
 * NetWorkHelper.java
 * @author LKZ
 * @created 2014-12-27
 * */
package com.moons.xst.track.common;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;

/**
 * @author LKZ
 * 
 */
public class NetWorkHelper {

	/**
	 * 判断网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (AppContext.getCheckNetworkYN()) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) {
			} else {
				NetworkInfo[] info = cm.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断GPS是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = lm.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	/**
	 * 判断WIFI是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 判断是否是3G网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为wifi
	 * 
	 * @param context
	 * @return True:WIFI;False:3G
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断APP运行所需的基本设置
	 * 
	 * @return
	 */
	public static boolean checkAppRunBaseCase(Context context) {
		if (StringUtils.isEmpty(AppContext.GetxjqCD())) {
			UIHelper.ToastMessage(context, context.getString(R.string.download_message_errorxjqcd));
			return false;
		}
		if (StringUtils.isEmpty(AppContext.GetWSAddressBasePath())) {
			UIHelper.ToastMessage(context, context.getString(R.string.download_message_errorwsaddress));
			return false;
		}
		if (StringUtils.isEmpty(AppContext.GetUniqueID())) {
			UIHelper.ToastMessage(context, context.getString(R.string.download_message_errormacaddress));
			return false;
		}
		return true;
	}
}
