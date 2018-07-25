/**
 * 
 */
package com.moons.xst.track.common;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * 打开文件（word、excel、ppt、pdf）
 * 
 * @author LKZ
 * 
 */
public class OfficeHelper {

	public static boolean openWPSFile(Context context, String path) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("OpenMode", "ReadOnly");// 只读模式
		// bundle.putBoolean("SEND_CLOSE_BROAD", true);
		// bundle.putString("THIRD_PACKAGE", "");// 传入的第三方包名
		// bundle.putBoolean("CLEAR_BUFFER", true);
		// bundle.putBoolean("CLEAR_TRACE", true);
		// bundle.putBoolean(CLEAR_FILE, true);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		// className为“cn.wps.moffice.documentmanager.PreStartActivity2”，packageName根据版本分别为:
		// ”cn.wps.moffice_eng”(普通版)，”cn.wps.moffice_eng”(英文版)
		intent.setClassName("cn.wps.moffice_eng",
				"cn.wps.moffice.documentmanager.PreStartActivity2");
		File file = new File(path);
		if (file == null || !file.exists()) {
			return false;
		}
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		intent.putExtras(bundle);
		try {
			context.getApplicationContext().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
