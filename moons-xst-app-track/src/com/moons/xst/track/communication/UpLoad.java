/**
 * 
 */
package com.moons.xst.track.communication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.ReturnResultInfo;

/**
 * @author Administrator
 * 
 */
public class UpLoad {
	Context context;

	public UpLoad(Context context) {
		this.context = context;
	}
    public String GetNeedUploadFileInfo()
    {
    	try {
			String str = WebserviceFactory.GetNeedUploadFileInfo();
			return str;
		} catch (Exception e) {
			Message msg = Message.obtain();
			msg.what = -1;
			msg.obj = e.getMessage();
			return "";
		}
    }
	/**
	 * 上传结果文件
	 * 
	 * @param context
	 * @param xjqCD
	 * @param filePath
	 * @param newName
	 */
	public boolean UploadResultDB(String appVerString,Context context, Handler proBarHandler,
			String filePath) {
		try {
			return WebserviceFactory.UploadResultDBForWeb(appVerString,context,
					proBarHandler, AppContext.GetxjqCD(), filePath);
		} catch (Exception e) {
			Message msg = Message.obtain();
			msg.what = -1;
			msg.obj = e.getMessage();
			proBarHandler.sendMessage(msg);
			return false;
		}
	}
	
	/**
	 * 上传结果文件
	 * 返回结果信息对象
	 * @param context
	 * @param xjqCD
	 * @param filePath
	 * @param newName
	 */
	public ReturnResultInfo UploadResultDBNew(String appVerString,Context context, Handler proBarHandler,
			String filePath) {
			return WebserviceFactory.UploadResultDBForWebNew(appVerString,context,
					proBarHandler, AppContext.GetxjqCD(), filePath);
	}
}
