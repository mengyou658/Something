/**
 * 
 */
package com.moons.xst.buss;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.camera.FileOperateUtil;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.communication.UploadFile;

/**
 * 文件上传下载处理类
 * 
 * @author LikeJo
 * 
 */
public class FilesUpDownHelper {

	static FilesUpDownHelper _intance;

	public static FilesUpDownHelper GetIntance() {
		if (_intance == null) {
			_intance = new FilesUpDownHelper();
		}
		return _intance;
	}

	/**
	 * 上传照片文件
	 * 
	 * @param context
	 * @param count
	 */
	public void uploadImageFile(Context context) {

		try {
			List<File> MyfileList = FileUtils.listPathFiles(AppConst
					.XSTTempPicPath() + "//Image//");
			if (MyfileList != null && MyfileList.size() > 0) {
				String msg = UploadFile.getIntance().post("TEMP_IMAGE", "", MyfileList);
				if (msg.substring(0, 7).equals("SUCCESS")) {
					for (File  f: MyfileList) {
						//f.delete();
						FileOperateUtil.deleteSourceFile(f.getPath(), context);
						Thread.sleep(3000);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			String mesString = e.getMessage();
			Log.e("tempImage", mesString);
		}
	}

	/**
	 * 上传录音文件
	 * 
	 * @param context
	 * @param count
	 */
	public void uploadRecordFile(Context context) {

		try {
			List<File> MyfileList = FileUtils.listPathFiles(AppConst
					.XSTTempRecordPath()+ "//audio//");
			if (MyfileList != null && MyfileList.size() > 0) {
				String msg = UploadFile.getIntance().post("TEMP_RECORD", "", MyfileList);
				if (msg.substring(0, 7).equals("SUCCESS")) {
					for (File  f: MyfileList) {
						f.delete();
						Thread.sleep(3000);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 上传录像文件
	 * 
	 * @param context
	 * @param count
	 */
	public void uploadVideoFile(Context context) {

		try {
			List<File> MyfileList = FileUtils.listPathFiles(AppConst
					.XSTTempPicPath() + "//Video//");
			if (MyfileList != null && MyfileList.size() > 0) {
				UploadFile.getIntance().post("TEMP_VEDIO", "", MyfileList);
				String msg = UploadFile.getIntance().post("TEMP_VEDIO", "", MyfileList);
				if (msg.substring(0, 7).equals("SUCCESS")) {
					for (File  f: MyfileList) {
						FileOperateUtil.deleteSourceFile(f.getPath(), context);
						//f.delete();
						Thread.sleep(3000);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
