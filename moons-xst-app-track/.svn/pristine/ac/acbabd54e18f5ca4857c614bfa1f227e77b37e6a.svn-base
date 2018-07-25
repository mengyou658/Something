package com.moons.xst.buss;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.GPSMobjectBugResult;
import com.moons.xst.track.bean.GPSMobjectBugResultForFiles;
import com.moons.xst.track.bean.GPSPosition;
import com.moons.xst.track.bean.GPSPositionForInit;
import com.moons.xst.track.bean.GPSPositionForResult;
import com.moons.xst.track.bean.GPSPositionForResultHis;
import com.moons.xst.track.bean.GPSXXPlanResult;
import com.moons.xst.track.bean.OverSpeedRecordInfo;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.communication.UploadFile;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.dao.CheckPointDAO;
import com.moons.xst.track.dao.GPSMobjectBugResultDao;
import com.moons.xst.track.dao.GPSMobjectBugResultForFilesDao;
import com.moons.xst.track.dao.GPSMobjectBugResultForFilesHisDao;
import com.moons.xst.track.dao.GPSMobjectBugResultHisDao;
import com.moons.xst.track.dao.GPSPositionDao;
import com.moons.xst.track.dao.GPSPositionForInitDao;
import com.moons.xst.track.dao.GPSPositionForResultDao;
import com.moons.xst.track.dao.GPSPositionForResultHisDao;
import com.moons.xst.track.dao.GPSXXPlanResultDao;
import com.moons.xst.track.dao.OverSpeedRecordDao;

public class GPSPositionHelper {

	private static final String TAG = "GPSPositionHelper";

	private DJResultDAOSession djResultDAOSession;
	private DJDAOSession djdaoSession;
	private GPSPositionDao gpspositiondao;
	private GPSPositionForInitDao gpsinitdao;
	private GPSPositionForResultDao gpsPositionForResultDao;
	private GPSMobjectBugResultDao gpsMobjectBugResultDao;
	private GPSMobjectBugResultForFilesDao gpsMobjectBugResultForFilesDao;
	private CheckPointDAO checkPointDAO;
	private OverSpeedRecordDao overSpeedRecordDao;
	private GPSXXPlanResultDao gpsxxPlanResultDao;

	// 历史表Dao
	private GPSPositionForResultHisDao gpsPositionForResultHisDao;
	private GPSMobjectBugResultHisDao gpsMobjectBugResultHisDao;
	private GPSMobjectBugResultForFilesHisDao gpsMobjectBugResultForFilesHisDao;

	InitDJsqlite init = new InitDJsqlite();

	static GPSPositionHelper _intance;

	public static GPSPositionHelper GetIntance() {
		if (_intance == null) {
			_intance = new GPSPositionHelper();
		}
		return _intance;
	}

	/**
	 * 插入GPS轨迹坐标数据
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertGPSPo(Context context, GPSPosition entity) {
		try {
			// djResultDAOSession = init.InitDJResultDAOSession(context,
			// AppContext.GetCurrLineID());
			djResultDAOSession = ((AppContext) context.getApplicationContext())
					.getResultSession(AppContext.GetCurrLineID());
			gpspositiondao = djResultDAOSession.getGPSPositionDao();
			long row = gpspositiondao.InsertGPSPosition(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入考核点初始化坐标
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertGPSInit(Context context, GPSPositionForInit entity) {
		try {
			djResultDAOSession = ((AppContext) context.getApplicationContext())
					.getResultSession(AppContext.GetCurrLineID());
			gpsinitdao = djResultDAOSession.getGPSPositionForInitDao();
			long row = gpsinitdao.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入巡线到位记录
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertGPSTrackArriveResult(Context context,
			GPSPositionForResult entity) {
		try {
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getResultSession(
					AppContext.GetCurrLineID());
			gpsPositionForResultDao = djResultDAOSession
					.getGPSPositionForResultDao();
			long row = gpsPositionForResultDao.insert(entity);

			// 同时插入到历史表
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			gpsPositionForResultHisDao = djdaoSession
					.getGPSPositionForResultHisDao();
			long rowhis = gpsPositionForResultHisDao
					.insertGPSPositionForResult(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 查询巡线到位历史记录
	 * 
	 * @param context
	 * @param entity
	 */
	public List<GPSPositionForResultHis> QueryGPSTResultHis(Context context,
			int posID) {
		try {
			List<GPSPositionForResultHis> list = new ArrayList<GPSPositionForResultHis>();
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			gpsPositionForResultHisDao = djdaoSession
					.getGPSPositionForResultHisDao();
			list = gpsPositionForResultHisDao.queryRaw(
					"where GPSPosition_ID = " + String.valueOf(posID),
					new String[] {});
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 更新考核点信息
	 * 
	 * @param context
	 * @param entity
	 */
	public void updateCheckPoint(Context context, CheckPointInfo entity) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			checkPointDAO = djdaoSession.getCheckPointDAO();
			checkPointDAO.UpdateKHDate(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入缺陷单表单数据
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertMobjectBugResult(Context context,
			GPSMobjectBugResult entity) {
		try {
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getResultSession(
					AppContext.GetCurrLineID());
			gpsMobjectBugResultDao = djResultDAOSession
					.getGPSMobjectBugResultDao();
			long row = gpsMobjectBugResultDao.insert(entity);

			// 同时插入到历史表
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			gpsMobjectBugResultHisDao = djdaoSession
					.getGPSMobjectBugResultHisDao();
			long rowhis = gpsMobjectBugResultHisDao
					.insertGPSMobjectBugResult(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入缺陷单附件信息（照片、录音、录像）
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertMobjectBugResultFiles(Context context,
			GPSMobjectBugResultForFiles entity) {
		try {
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getResultSession(
					AppContext.GetCurrLineID());
			gpsMobjectBugResultForFilesDao = djResultDAOSession
					.getGPSMobjectBugResultForFilesDao();
			long row = gpsMobjectBugResultForFilesDao.insert(entity);

			// 同时插入到历史表
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			gpsMobjectBugResultForFilesHisDao = djdaoSession
					.getGPSMobjectBugResultForFilesHisDao();
			long rowhis = gpsMobjectBugResultForFilesHisDao
					.insertGPSMobjectBugResultForFiles(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	public void Operatesql(String sql) {
		gpspositiondao.Operatesql(sql);
	}

	/**
	 * 提交GPS初始化信息
	 * 
	 * @param context
	 * @param count
	 *            一次提交的条数
	 */
	public void uploadGPSInitInfoForJIT(Context context, DJLine djLine) {
		try {

			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			gpsinitdao = djResultDAOSession.getGPSPositionForInitDao();
			int onceNumber = AppContext.getJITUploadXDJOnceNum();
//			int onceNumber = Integer.parseInt(
//					((AppContext) context.getApplicationContext()).getXXOnceLoad());
			List<GPSPositionForInit> gpsPositionInitsList = gpsinitdao
					.loadforUploadJIT(onceNumber);
			if (gpsPositionInitsList == null
					|| gpsPositionInitsList.size() <= 0)
				return;
			Gson gson = new Gson();
			String jsonString = gson.toJson(gpsPositionInitsList);
			boolean brst = WebserviceFactory.insertGPSInitInfoJIT(jsonString);
			if (brst) {
				gpsinitdao.deleteUploadedData(gpsPositionInitsList);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 提交GPS轨迹数据
	 * 
	 * @param context
	 * @param count
	 *            一次提交的条数
	 */
	public void uploadGPSInfoForJIT(Context context, int count, DJLine djLine) {
		try {

			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			gpspositiondao = djResultDAOSession.getGPSPositionDao();
			List<GPSPosition> gpsPositionsList = gpspositiondao
					.loadforUploadJIT(count);
			if (gpsPositionsList == null || gpsPositionsList.size() <= 0)
				return;
			Gson gson = new Gson();
			String jsonString = gson.toJson(gpsPositionsList);
			if (WebserviceFactory.insertGPSInfoJIT(jsonString))
				gpspositiondao.deleteUploadedData(gpsPositionsList);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 提交巡线到位考核数据
	 * 
	 * @param context
	 * @param count111
	 */
	public void uploadTrackArriveResultForJIT(Context context, DJLine djLine) {
		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			gpsPositionForResultDao = djResultDAOSession
					.getGPSPositionForResultDao();
//			int onceNumber = Integer.valueOf(AppConfig.getAppConfig(context)
//					.get(AppConfig.CONF_JITUPLOADXX_ONCE_NUMBER));
//			int onceNumber = Integer.parseInt(
//					((AppContext) context.getApplicationContext()).getXXOnceLoad());
			List<GPSPositionForResult> trackArriveResultList = gpsPositionForResultDao
					.loadforUploadJIT(1);
			if (trackArriveResultList == null
					|| trackArriveResultList.size() <= 0)
				return;
			Gson gson = new Gson();
			String jsonString = gson.toJson(trackArriveResultList);
			if (WebserviceFactory.insertCPArriveResJIT(jsonString))
				gpsPositionForResultDao
						.deleteUploadedData(trackArriveResultList);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 提交报缺单数据
	 * 
	 * @param context
	 * @param count
	 */
	public void uploadMobjectBugResultForJIT(Context context, DJLine djLine) {
		// 与下面方法重复，注释掉
		/*
		 * try { if
		 * (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
		 * .getLineID()) + AppConst.ResultDBName(djLine.getLineID()))) return;
		 * Gson gson = new Gson(); djResultDAOSession =
		 * init.InitDJResultDAOSession(context, djLine.getLineID());
		 * gpsMobjectBugResultDao = djResultDAOSession
		 * .getGPSMobjectBugResultDao(); List<GPSMobjectBugResult>
		 * mobjectBugResultList = gpsMobjectBugResultDao .loadforUploadJIT(1);
		 * if (mobjectBugResultList == null || mobjectBugResultList.size() <= 0)
		 * return; gpsMobjectBugResultForFilesDao = djResultDAOSession
		 * .getGPSMobjectBugResultForFilesDao(); String bugID =
		 * mobjectBugResultList.get(0).getResult_ID();
		 * List<GPSMobjectBugResultForFiles> filesList =
		 * gpsMobjectBugResultForFilesDao .loadforUploadJIT(bugID);
		 * 
		 * String picPath = AppConst.CurrentResultPath_Pic(djResultDAOSession
		 * .getLineID()); if (filesList != null && filesList.size() > 0) { for
		 * (GPSMobjectBugResultForFiles _bugfile : filesList) { FileInputStream
		 * fis = new FileInputStream(picPath + File.separator +
		 * _bugfile.getGUID()); int length = fis.available(); byte[] buffer =
		 * new byte[length]; fis.read(buffer); String filedataString =
		 * Base64.encodeToString(buffer, Base64.DEFAULT);
		 * _bugfile.setFileData(filedataString); } }
		 * 
		 * mobjectBugResultList.get(0).setBugFiles(filesList); String jsonString
		 * = gson.toJson(mobjectBugResultList); if
		 * (WebserviceFactory.insertMobjectBugResJIT(jsonString))
		 * gpsMobjectBugResultDao.deleteUploadedData(mobjectBugResultList); }
		 * catch (Exception e) { // TODO: handle exception }
		 */
	}

	/**
	 * 提交报缺单数据
	 * 
	 * @param context
	 * @param count
	 */
	public void uploadMobjectBugResultForJIT1(Context context, DJLine djLine) {

		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;
			Thread.sleep(2000);
			Gson gson = new Gson();
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getResultSession(
					djLine.getLineID());
			gpsMobjectBugResultDao = djResultDAOSession
					.getGPSMobjectBugResultDao();
			List<GPSMobjectBugResult> mobjectBugResultList = gpsMobjectBugResultDao
					.loadforUploadJIT(1);
			if (mobjectBugResultList == null
					|| mobjectBugResultList.size() <= 0)
				return;
			gpsMobjectBugResultForFilesDao = djResultDAOSession
					.getGPSMobjectBugResultForFilesDao();
			String bugID = mobjectBugResultList.get(0).getResult_ID();
			List<GPSMobjectBugResultForFiles> filesList = gpsMobjectBugResultForFilesDao
					.loadforUploadJIT(bugID);
			mobjectBugResultList.get(0).setBugFiles(filesList);
			String jsonString = gson.toJson(mobjectBugResultList);
			String picPath = AppConst.CurrentResultPath_Pic(djResultDAOSession
					.getLineID());
			List<String> MyfileList = new ArrayList<String>();
			if (filesList != null && filesList.size() > 0) {
				for (GPSMobjectBugResultForFiles _bugfile : filesList) {
					if (_bugfile.getFile_Type().equals("JPG")) {
						MyfileList.add(picPath + File.separator
								+ _bugfile.getGUID() + ".jpg");
					}
					if (_bugfile.getFile_Type().equals("WAV")) {
						MyfileList.add(picPath + File.separator
								+ _bugfile.getGUID() + ".3gp");
					}
					if (_bugfile.getFile_Type().equals("ARM")) {
						MyfileList.add(picPath + File.separator
								+ _bugfile.getGUID() + ".amr");
					}
				}
			}
			final int size = MyfileList.size();
			String[] arr = MyfileList.toArray(new String[size]);
			String s = android.util.Base64.encodeToString(
					jsonString.getBytes(), Base64.DEFAULT);
			String msg = UploadFile.getIntance().post("XXBUG", s, arr);
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
	public void uploadMobjectBugResultForJITNEW(Context context, DJLine djLine) {

		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;
			Gson gson = new Gson();
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			gpsMobjectBugResultDao = djResultDAOSession
					.getGPSMobjectBugResultDao();
			List<GPSMobjectBugResult> mobjectBugResultList = gpsMobjectBugResultDao
					.loadforUploadJIT(1);
			if (mobjectBugResultList == null
					|| mobjectBugResultList.size() <= 0)
				return;
			gpsMobjectBugResultForFilesDao = djResultDAOSession
					.getGPSMobjectBugResultForFilesDao();
			String bugID = mobjectBugResultList.get(0).getResult_ID();
			String dir = AppConst.CurrentJITResultPath(djLine.getLineID())
					+ File.separator + bugID;
			String zippath = dir + ".zip";
			List<GPSMobjectBugResultForFiles> filesList = gpsMobjectBugResultForFilesDao
					.loadforUploadJIT(bugID);

			List<String> MyfileList = new ArrayList<String>();
			// 判断此条事件是否已打包完成
			if (!FileUtils.checkFileExists(zippath)) {
				mobjectBugResultList.get(0).setBugFiles(filesList);
				String jsonString = gson.toJson(mobjectBugResultList);
				String picPath = AppConst
						.CurrentResultPath_Pic(djResultDAOSession.getLineID());
				// List<String> MyfileList = new ArrayList<String>();
				if (filesList != null && filesList.size() > 0) {
					for (GPSMobjectBugResultForFiles _bugfile : filesList) {
						if (_bugfile.getFile_Type().equals("JPG")) {
							MyfileList.add(picPath + File.separator
									+ _bugfile.getGUID() + ".jpg");
						}
						if (_bugfile.getFile_Type().equals("WAV")) {
							MyfileList.add(picPath + File.separator
									+ _bugfile.getGUID() + ".3gp");
						}
						if (_bugfile.getFile_Type().equals("ARM")) {
							MyfileList.add(picPath + File.separator
									+ _bugfile.getGUID() + ".amr");
						}
					}
				}
				FileUtils.createDirectoryALL(dir);
				for (int i = 0; i < MyfileList.size(); i++) {
					String newpath = dir
							+ File.separator
							+ FileUtils.getFileName(MyfileList.get(i)
									.toString());
					FileUtils.copyFile(MyfileList.get(i), newpath);
				}
				String s = android.util.Base64.encodeToString(
						jsonString.getBytes(), Base64.DEFAULT);
				FileUtils.SaveToFile(dir + File.separator, bugID + ".txt", s,
						false);
				if (ZipUtils.zipFolder(dir, zippath))
					FileUtils.deleteDirectory(dir);
				else {
					// 记录日志
					OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
							AppConst.LOGSTATUS_ERROR,
							context.getString(R.string.cumm_cummdownload_iszipfileerror,
									zippath));
					return;
				}
			}
			ReturnResultInfo returnInfo = UploadFile.getIntance()
					.UploadFileByWebNew(context, null, zippath,
					bugID + ".zip", "XXBUG");
			if (returnInfo.getResult_YN()) {
				if (gpsMobjectBugResultDao
						.deleteUploadedData(mobjectBugResultList)
						&& gpsMobjectBugResultForFilesDao
								.deleteUploadedData(filesList)) {
					for (int i = 0; i < MyfileList.size(); i++) {
						File f = new File(MyfileList.get(i));
						f.delete();
					}
				}
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

	/**
	 * 插入超速报警记录
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertOverSpeedRecord(Context context,
			OverSpeedRecordInfo entity) {
		try {
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getResultSession(
					AppContext.GetCurrLineID());
			overSpeedRecordDao = djResultDAOSession.getovOverSpeedRecordDao();
			long row = overSpeedRecordDao.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 实时上传超速报警记录
	 * 
	 * @param context
	 * @param djLine
	 */
	public void uploadOverSpeedRecordForJIT(Context context, DJLine djLine) {
		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			overSpeedRecordDao = djResultDAOSession.getovOverSpeedRecordDao();
//			int onceNumber = Integer.valueOf(AppConfig.getAppConfig(context)
//					.get(AppConfig.CONF_JITUPLOADXX_ONCE_NUMBER));
//			int onceNumber = Integer.parseInt(
//					((AppContext) context.getApplicationContext()).getXXOnceLoad());
			List<OverSpeedRecordInfo> OverSpeedRecordList = overSpeedRecordDao
					.loadforUploadJIT(1);
			if (OverSpeedRecordList == null || OverSpeedRecordList.size() <= 0)
				return;
			Gson gson = new Gson();
			String jsonString = gson.toJson(OverSpeedRecordList);
			if (WebserviceFactory.insertOverSpeedInfoJIT(jsonString))
				overSpeedRecordDao.deleteUploadedData(OverSpeedRecordList);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 插入巡视计划结果
	 * 
	 * @param context
	 * @param entity
	 */
	public void insertXXPlan(Context context, GPSXXPlanResult entity) {
		try {
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getResultSession(
					AppContext.GetCurrLineID());
			gpsxxPlanResultDao = djResultDAOSession.getGPSXXPlanResultDao();
			long row = gpsxxPlanResultDao.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 实时上传巡视计划结果
	 * 
	 * @param context
	 * @param djLine
	 * @author sx
	 */
	public void uploadXXPlanResultsxForJIT(Context context, DJLine djLine) {
		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;
			djResultDAOSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			gpsxxPlanResultDao = djResultDAOSession.getGPSXXPlanResultDao();
//			int onceNumber = Integer.valueOf(AppConfig.getAppConfig(context)
//					.get(AppConfig.CONF_JITUPLOADXX_ONCE_NUMBER));
//			int onceNumber = Integer.parseInt(
//					((AppContext) context.getApplicationContext()).getXXOnceLoad());
			List<GPSXXPlanResult> planResultList = gpsxxPlanResultDao
					.loadforUploadJIT(1);
			if (planResultList == null || planResultList.size() <= 0)
				return;
			Gson gson = new Gson();
			String jsonString = gson.toJson(planResultList);
			boolean rst = WebserviceFactory.insertGPSXXPlanResJIT(jsonString);
			if (rst) {
				gpsxxPlanResultDao.deleteUploadedData(planResultList);
			}
		} catch (Exception e) {
			String tempStr = e.getMessage();
		}
	}

}
