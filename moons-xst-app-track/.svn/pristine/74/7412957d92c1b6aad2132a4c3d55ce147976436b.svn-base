package com.moons.xst.buss;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.moons.xst.sqlite.ComDaoSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.ComGPSPosition;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.GPSMobjectBugResult;
import com.moons.xst.track.bean.GPSMobjectBugResultForFiles;
import com.moons.xst.track.bean.GPSXXTempTask;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.communication.UploadFile;
import com.moons.xst.track.dao.ComGPSPositionDao;
import com.moons.xst.track.dao.GPSMobjectBugResultDao;
import com.moons.xst.track.dao.GPSMobjectBugResultForFilesDao;
import com.moons.xst.track.dao.GPSXXTempTaskDao;

public class ComDBHelper {

	private static final String TAG = "ComDBHelper";
	private ComDaoSession comDaoSession;

	private ComGPSPositionDao comgpspositiondao;
	private GPSXXTempTaskDao gpstemptaskdao;
	private GPSMobjectBugResultDao gpsMobjectBugResultDao;
	private GPSMobjectBugResultForFilesDao gpsMobjectBugResultForFilesDao;

	InitDJsqlite init = new InitDJsqlite();
	static ComDBHelper _intance;

	public static ComDBHelper GetIntance() {
		if (_intance == null) {
			_intance = new ComDBHelper();
		}
		return _intance;
	}

	/**
	 * 插入GPS轨迹坐标到标准库
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertGPSPo(Context context, ComGPSPosition entity) {
		try {
			comDaoSession = ((AppContext) context.getApplicationContext()).getComSession();
			comgpspositiondao = comDaoSession.getGPSPositionDao();
			long row = comgpspositiondao.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入临时任务
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertTempTask(Context context, GPSXXTempTask[] entity) {
		try {
			comDaoSession = ((AppContext) context.getApplicationContext()).getComSession();
			gpstemptaskdao = comDaoSession.getGPSXXTempTaskDao();
			for (int i = 0; i < entity.length; i++) {
				long row = gpstemptaskdao.insert(entity[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 更新临时任务
	 * 
	 * @param context
	 * @param entity
	 */
	public void updateTempTask(Context context, GPSXXTempTask entity) {
		try {
			comDaoSession = ((AppContext) context.getApplicationContext()).getComSession();
			gpstemptaskdao = comDaoSession.getGPSXXTempTaskDao();
			gpstemptaskdao.update(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入缺陷单表单数据到标准库
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertMobjectBugResult(Context context,
			GPSMobjectBugResult entity) {
		try {
			comDaoSession = ((AppContext) context.getApplicationContext()).getComSession();
			gpsMobjectBugResultDao = comDaoSession.getGPSMobjectBugResultDao();
			long row = gpsMobjectBugResultDao.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入缺陷单附件信息到标准库（照片、录音、录像）
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertMobjectBugResultFiles(Context context,
			GPSMobjectBugResultForFiles entity) {
		try {
			comDaoSession = ((AppContext) context.getApplicationContext()).getComSession();
			gpsMobjectBugResultForFilesDao = comDaoSession
					.getGPSMobjectBugResultForFilesDao();
			long row = gpsMobjectBugResultForFilesDao.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取临时任务
	 * 
	 * @param context
	 * @param entity
	 */
	public List<GPSXXTempTask> loadempTasks(Context context) {
		try {
			comDaoSession = ((AppContext) context.getApplicationContext()).getComSession();
			gpstemptaskdao = comDaoSession.getGPSXXTempTaskDao();
			return gpstemptaskdao.loadAll();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	public void uploadTempMobjectBugResultForJIT(Context context, DJLine djLine) {
		try {
			if (!FileUtils.checkFileExists(AppConst.XSTDBPath() + "Common.sdf"))
				return;
			Thread.sleep(2000);
			Gson gson = new Gson();
			comDaoSession = ((AppContext) context.getApplicationContext()).getComSession();
			gpsMobjectBugResultDao = comDaoSession.getGPSMobjectBugResultDao();
			List<GPSMobjectBugResult> mobjectBugResultList = gpsMobjectBugResultDao
					.loadforUploadJIT(1);
			if (mobjectBugResultList == null
					|| mobjectBugResultList.size() <= 0)
				return;
			gpsMobjectBugResultForFilesDao = comDaoSession
					.getGPSMobjectBugResultForFilesDao();
			String bugID = mobjectBugResultList.get(0).getResult_ID();
			List<GPSMobjectBugResultForFiles> filesList = gpsMobjectBugResultForFilesDao
					.loadforUploadJIT(bugID);
			mobjectBugResultList.get(0).setBugFiles(filesList);
			String jsonString = gson.toJson(mobjectBugResultList);
			List<String> MyfileList = new ArrayList<String>();
			if (filesList != null && filesList.size() > 0) {
				for (GPSMobjectBugResultForFiles _bugfile : filesList) {
					String pathString = AppConst.TempTaskPhotoPath();
					if (_bugfile.getFile_Type().equals("JPG")) {
						pathString = AppConst.TempTaskPhotoPath();
						MyfileList.add(pathString + File.separator
								+ _bugfile.getGUID()+".jpg");
					}
					if (_bugfile.getFile_Type().equals("WAV")) {
						pathString = AppConst.TempTaskVedioPath();
						MyfileList.add(pathString + File.separator
								+ _bugfile.getGUID()+".3gp");
					}
					if (_bugfile.getFile_Type().equals("ARM")) {
						pathString = AppConst.TempTaskAudioPath();
						MyfileList.add(pathString + File.separator
								+ _bugfile.getGUID()+".amr");
					}
				}
			}
			final int size = MyfileList.size();
			String[] arr = MyfileList.toArray(new String[size]);
			String s = android.util.Base64.encodeToString(jsonString.getBytes(),Base64.DEFAULT); 
			String msg =UploadFile.getIntance().post("TEMPXXBUG",s ,arr);
			if (msg.substring(0, 7).equals("SUCCESS")) {
				Thread.sleep(3000);
				for (String path : MyfileList) {
					File file = new File(path);
					file.delete();
				}
				gpsMobjectBugResultDao.deleteUploadedData(mobjectBugResultList);
				gpsMobjectBugResultForFilesDao.deleteUploadedData(filesList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**
	 * 提交报缺单数据--新方式webservice断点提交
	 * 
	 * @param context
	 * @param count
	 */
	public void uploadTempMobjectBugResultForJITNEW(Context context, DJLine djLine) {
		try {
			if (!FileUtils.checkFileExists(AppConst.XSTDBPath() + "Common.sdf"))
				return;
			Gson gson = new Gson();
			comDaoSession = ((AppContext) context.getApplicationContext()).getComSession();
			gpsMobjectBugResultDao = comDaoSession.getGPSMobjectBugResultDao();
			List<GPSMobjectBugResult> mobjectBugResultList = gpsMobjectBugResultDao
					.loadforUploadJIT(1);
			if (mobjectBugResultList == null
					|| mobjectBugResultList.size() <= 0)
				return;
			gpsMobjectBugResultForFilesDao = comDaoSession
					.getGPSMobjectBugResultForFilesDao();
			String bugID = mobjectBugResultList.get(0).getResult_ID();
			String dir = AppConst.TempTaskPath()+ File.separator+bugID;
			String zippath = dir+".zip";
			List<GPSMobjectBugResultForFiles> filesList = gpsMobjectBugResultForFilesDao
					.loadforUploadJIT(bugID);
			//判断此条事件是否已打包完成
		 if (!FileUtils.checkFileExists(zippath)) {
			mobjectBugResultList.get(0).setBugFiles(filesList);
			String jsonString = gson.toJson(mobjectBugResultList);
			List<String> MyfileList = new ArrayList<String>();
			if (filesList != null && filesList.size() > 0) {
				for (GPSMobjectBugResultForFiles _bugfile : filesList) {
					String pathString = AppConst.TempTaskPhotoPath();
					if (_bugfile.getFile_Type().equals("JPG")) {
						pathString = AppConst.TempTaskPhotoPath();
						MyfileList.add(pathString + File.separator
								+ _bugfile.getGUID()+".jpg");
					}
					if (_bugfile.getFile_Type().equals("WAV")) {
						pathString = AppConst.TempTaskVedioPath();
						MyfileList.add(pathString + File.separator
								+ _bugfile.getGUID()+".3gp");
					}
					if (_bugfile.getFile_Type().equals("ARM")) {
						pathString = AppConst.TempTaskAudioPath();
						MyfileList.add(pathString + File.separator
								+ _bugfile.getGUID()+".amr");
					}
				}
				FileUtils.createDirectoryALL(dir);
				for (int i = 0; i < MyfileList.size(); i++) {
					String newpath = dir+File.separator+FileUtils.getFileName(MyfileList.get(i).toString());
					FileUtils.copyFile(MyfileList.get(i), newpath);
					File file = new File(MyfileList.get(i));
					file.delete();
				}
			}	
			String s = android.util.Base64.encodeToString(jsonString.getBytes(),Base64.DEFAULT);
			FileUtils.SaveToFile(dir+ File.separator, bugID+".txt", s, false);
			if (ZipUtils.zipFolder(dir,zippath))
				FileUtils.deleteDirectory(dir);
			else {
				// 插入操作日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						context.getString(R.string.cumm_cummdownload_iszipfileerror,
								zippath));
				return;
			}
	 	}
		 ReturnResultInfo returnInfo = UploadFile.getIntance()
					.UploadFileByWebNew(context, null, zippath, bugID+".zip", "TEMPXXBUG");
			if (returnInfo.getResult_YN()) {
				gpsMobjectBugResultDao.deleteUploadedData(mobjectBugResultList);
				gpsMobjectBugResultForFilesDao.deleteUploadedData(filesList);
				File file = new File(zippath);
				file.delete();
			} else {
				// 插入操作日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						zippath + "\n[" + returnInfo.getError_Message_TX() + "]");
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
