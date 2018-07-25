package com.moons.xst.buss;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import Decoder.BASE64Encoder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.baidu.navisdk.util.common.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.bean.DJ_TaskIDPosActive;
import com.moons.xst.track.bean.MObject_State;
import com.moons.xst.track.bean.MObject_StateHis;
import com.moons.xst.track.bean.PlanInfoCaseXJJIT;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.bean.SRLineTreeLastResult;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;
import com.moons.xst.track.bean.Z_Condition;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.ZipUtils;
import com.moons.xst.track.communication.UploadFile;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.dao.DJ_PhotoByResultDao;
import com.moons.xst.track.dao.DJ_ResultActiveDao;
import com.moons.xst.track.dao.DJ_TaskIDPosActiveDao;
import com.moons.xst.track.dao.MObject_StateDao;
import com.moons.xst.track.dao.MObject_StateHisDao;
import com.moons.xst.track.dao.SRLineTreeLastResultDao;
import com.moons.xst.track.dao.Z_ConditionDao;
import com.moons.xst.track.dao.Z_DJ_PlanDao;
import com.moons.xst.track.extendeddao.Ext_DJResultDao;
import com.moons.xst.track.ui.CommDownload;

/**
 * 巡点检业务
 * 
 * @author LKZ
 * 
 */
public class DJResultHelper {

	private static final String TAG = "DJResultHelper";

	private DJResultDAOSession djresultdaoSession;

	private DJ_ResultActiveDao djresultActiveDao;
	private DJ_PhotoByResultDao djphotobyresultDao;
	private DJ_TaskIDPosActiveDao djtaskIDPosActiveDao;
	private MObject_StateDao mobjectstateDao;

	InitDJsqlite init = new InitDJsqlite();

	static DJResultHelper _intance;

	private List<DJ_PhotoByResult> filesList;

	private List<DJ_ResultActive> djResultList;

	private String imagePath;

	private String audioPath;

	private String replace;

	private String json;

	private byte[] data8k_TXs;

	private byte[] fftData_TXs;
	
	private NotificationManager nm;

	public static DJResultHelper GetIntance() {
		if (_intance == null) {
			_intance = new DJResultHelper();
		}
		return _intance;
	}

	public DJResultHelper() {
	}

	/**
	 * 插入到位结果
	 * 
	 * @param context
	 * @param entity
	 */
	public long InsertDJIDPosResult(Context context,
			DJ_TaskIDPosActive entity,
			XJ_TaskIDPosHis hisEntity) {
		try {
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			long row = _Extdao.InsertDJTaskIDResult(entity, hisEntity);
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新到位结果
	 * 
	 * @param context
	 * @param entity
	 */
	public void updateDJIDPosResult(Context context, DJ_TaskIDPosActive entity) {
		try {
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			_Extdao.djIDTaskActiveDao().update(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入巡点检结果
	 * 
	 * @param context
	 * @param entity
	 */
	public long InsertDJPlanResult(Context context, DJ_ResultActive entity,XJ_ResultHis xj_ResultHis,
			Hashtable<Integer, ArrayList<DJ_PhotoByResult>> djFileList,
			DJPlan mDjPlan) {
		try {
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			long row = _Extdao.InsertDJResult(entity, djFileList, xj_ResultHis,mDjPlan);
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新巡点检结果
	 * 
	 * @param context
	 * @param entity
	 */
	public void updateDJPlanResult(Context context, DJ_ResultActive entity,
			DJPlan mDjPlan) {
		try {
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			_Extdao.djResultActiveDao().update(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 插入结果附件信息（照片、录音）
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertMobjectBugResultFiles(Context context,
			DJ_PhotoByResult entity) {
		try {
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			long row = _Extdao.djPhotoByResultDao().insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 删除一条附件关联信息
	 * 
	 * @param context
	 * @param entity
	 */
	public void deleteOneFile(Context context, DJ_PhotoByResult entity) {
		try {
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			_Extdao.djPhotoByResultDao().deleteOneDJResultFile(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 提取一条结果数据
	 * 
	 * @param planID
	 * @param startTime
	 * @return
	 */
	public DJ_ResultActive getOneDJResult(Context context, int lineID,
			String planID, String startTime, String endTime) {
		try {

			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(lineID)
					+ AppConst.ResultDBName(lineID)))
				return null;
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			List<DJ_ResultActive> djResultList = _Extdao.loadOneDJResult(planID, startTime, endTime);
			if (djResultList == null || djResultList.size() <= 0)
				return null;
			else {
				return djResultList.get(0);
			}
		} finally {
		}
	}

	public DJ_ResultActive getOneDJResult(Context context, int lineID,
			String planID, String conditionStr) {
		try {

			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(lineID)
					+ AppConst.ResultDBName(lineID)))
				return null;
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			List<DJ_ResultActive> djResultList = _Extdao.loadOneDJResultForCaseXJ(planID, conditionStr);
			if (djResultList == null || djResultList.size() <= 0)
				return null;
			else {
				return djResultList.get(0);
			}
		} finally {
		}
	}

	public Hashtable<Integer, ArrayList<DJ_PhotoByResult>> getResultFileListByDJRes(
			Context context, int lineID, String planID,
			DJ_ResultActive djResultActive) {
		try {
			Hashtable<Integer, ArrayList<DJ_PhotoByResult>> _temHashtable = new Hashtable<Integer, ArrayList<DJ_PhotoByResult>>();
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(lineID)
					+ AppConst.ResultDBName(lineID)))
				return _temHashtable;
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			String _temCompleteTime = DateTimeHelper.DateToString(
					DateTimeHelper.StringToDate(djResultActive.getQuery_DT()),
					"yyyyMMdd")
					+ DateTimeHelper.DateToString(DateTimeHelper
							.StringToDate(djResultActive.getCompleteTime_DT()),
							"HHmmss");
			List<DJ_PhotoByResult> djResultFileList =_Extdao.loadDJResultFile(planID, _temCompleteTime);
			if (djResultFileList == null || djResultFileList.size() <= 0)
				return _temHashtable;
			else {
				for (DJ_PhotoByResult dj_PhotoByResult : djResultFileList) {
					if (dj_PhotoByResult.getLCType().equals("ZP")) {
						dj_PhotoByResult.setFilePath(AppConst
								.CurrentResultPath_Pic(AppContext
										.GetCurrLineID())
								+ dj_PhotoByResult.getGUID()
								+ "_"
								+ DateTimeHelper.TransDateTime(
										dj_PhotoByResult.getPhoto_DT(),
										"yyyyMMddHHmmss") + ".jpg");
						dj_PhotoByResult.setFileTitle(dj_PhotoByResult
								.getGUID() + ".jpg");
						if (!_temHashtable.containsKey(0)) {
							_temHashtable.put(0,
									new ArrayList<DJ_PhotoByResult>());
							_temHashtable.get(0).add(dj_PhotoByResult);
						} else {
							_temHashtable.get(0).add(dj_PhotoByResult);
						}
					}
					if (dj_PhotoByResult.getLCType().equals("LY")) {
						dj_PhotoByResult.setFilePath(AppConst
								.CurrentResultPath_Pic(AppContext
										.GetCurrLineID())
								+ dj_PhotoByResult.getGUID() + ".arm");
						dj_PhotoByResult.setFileTitle(dj_PhotoByResult
								.getGUID() + ".arm");
						if (!_temHashtable.containsKey(1)) {
							_temHashtable.put(1,
									new ArrayList<DJ_PhotoByResult>());
							_temHashtable.get(1).add(dj_PhotoByResult);
						} else {
							_temHashtable.get(1).add(dj_PhotoByResult);
						}
					}
				}
				return _temHashtable;
			}
		} finally {

		}
	}

	/**
	 * 插入一条启停状态结果
	 * 
	 * @param context
	 * @param entity
	 */
	public void InsertMObjectStateResult(Context context, MObject_State entity, String pointDesc_TX) {
		try {
			Ext_DJResultDao _Extdao = new Ext_DJResultDao(context);
			long row = _Extdao.InsertMObjectStateResult(entity, pointDesc_TX);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 实时上传点检结果
	 * @param context
	 * @param djLine
	 */
	public void uploadDJResultForJIT(Context context, DJLine djLine) {
		BASE64Encoder encode = new BASE64Encoder(); 
		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;
			djresultdaoSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			djresultActiveDao = djresultdaoSession.getDJ_ResultActiveDao();
			Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
			djResultList = djresultActiveDao.loadOneDJResult(AppConst.OnceUploadNum);
			if (djResultList == null || djResultList.size() <= 0)
				return;
			djphotobyresultDao = djresultdaoSession.getDJ_PhotoByResultDao();

			String picPath = AppConst.CurrentResultPath_Pic(djLine.getLineID());
			List<String> MyfileList = new ArrayList<String>();
			List<DJ_ResultActive> listWithoutFiles = new ArrayList<DJ_ResultActive>();			
			boolean result = false;
			
			for (DJ_ResultActive dj_ResultActive : djResultList) {
				data8k_TXs = dj_ResultActive.getData8K_TX();
				fftData_TXs = dj_ResultActive.getFFTData_TX();
				if (data8k_TXs != null) {
					/*String data8k_TXEncode = android.util.Base64
							.encodeToString(data8k_TXs, Base64.NO_WRAP);*/
					String data8k_TXEncode=encode.encode(data8k_TXs);
					dj_ResultActive.setData8K_STR(data8k_TXEncode);
					data8k_TXs = null;
					dj_ResultActive.setData8K_TX(data8k_TXs);
				} else {
					dj_ResultActive.setData8K_STR(null);
				}
				if (fftData_TXs != null) {
					/*String fftData_TXEncode = android.util.Base64
							.encodeToString(fftData_TXs, Base64.NO_WRAP);*/
					String fftData_TXEncode=encode.encode(fftData_TXs);
					dj_ResultActive.setFFTData_STR(fftData_TXEncode);
					fftData_TXs = null;
					dj_ResultActive.setFFTData_TX(fftData_TXs);
				} else {
					dj_ResultActive.setFFTData_STR(null);
				}

				Integer dataLen = dj_ResultActive.getDataLen();
				if (dataLen == null) {
					dj_ResultActive.setDataLen(0);
				}
				Integer rate = dj_ResultActive.getRate();
				if (rate == null) {
					dj_ResultActive.setRate(0);
				}
				Boolean ratio_NR = dj_ResultActive.getRatio_NR();
				if (ratio_NR == null) {
					dj_ResultActive.setRatio_NR(false);
				}
				Boolean ratio1_NR = dj_ResultActive.getRatio1_NR();
				if (ratio1_NR == null) {
					dj_ResultActive.setRatio1_NR(false);
				}
				String dj_Plan_ID = dj_ResultActive.getDJ_Plan_ID();
				String completeDT = DateTimeHelper.DateToString(DateTimeHelper
						.StringToDate(dj_ResultActive.getCompleteTime_DT()),
						"yyyyMMddHHmmss");
				filesList = djphotobyresultDao.loadDJResultFiles(dj_Plan_ID,
						completeDT);

				for (DJ_PhotoByResult dj_PhotoByResult : filesList) {
					if (dj_PhotoByResult.getLCType().equals("ZP")) {
						imagePath = picPath
								+ File.separator
								+ dj_PhotoByResult.getGUID()
								+ "_"
								+ DateTimeHelper.DateToString(DateTimeHelper
										.StringToDate(dj_PhotoByResult
												.getPhoto_DT()),
										"yyyyMMddHHmmss") + ".jpg";

//						String ZPEncodeToString = FileUtils.encodeBase64File(imagePath);
//						dj_PhotoByResult.setFile_Bytes(ZPEncodeToString);
						MyfileList.add(imagePath);
//						fileSizes += FileUtils.getFileSize(imagePath);
//						byte[] picBytes = FileUtils.getBytes(imagePath);
//						if (picBytes != null) {
//							/*String ZPEncodeToString = android.util.Base64
//									.encodeToString(picBytes, Base64.NO_PADDING);*/
//							for (int i = 0; i < picBytes.length; i++) {
//								picBytes[i] = (byte)(picBytes[i] & 0xff);
//							}
//							String ZPEncodeToString=encode.encode(picBytes);
//							dj_PhotoByResult.setFile_Bytes(ZPEncodeToString);
//							MyfileList.add(imagePath);
//							fileSizes += FileUtils.getFileSize(imagePath);
//						}						
					} else if (dj_PhotoByResult.getLCType().equals("LY")) {						
						audioPath = picPath + File.separator
								+ dj_PhotoByResult.getGUID() + ".arm";
						
//						String LYEncodeToString = FileUtils.encodeBase64File(audioPath);
//						dj_PhotoByResult.setFile_Bytes(LYEncodeToString);
						MyfileList.add(audioPath);
//						fileSizes += FileUtils.getFileSize(audioPath);
//						byte[] audioBytes = FileUtils.getBytes(audioPath);
//						if (audioBytes != null) {
//							/*String LYEncodeToString = android.util.Base64
//									.encodeToString(audioBytes, Base64.NO_PADDING);*/
//							String LYEncodeToString=encode.encode(audioBytes);
//							dj_PhotoByResult.setFile_Bytes(LYEncodeToString);	
//							MyfileList.add(audioPath);
//							fileSizes += FileUtils.getFileSize(audioPath);
//						}						
					}
				}
				dj_ResultActive.setResultFiles(filesList);
				
				// 循环单条结果，如果有附件，就上传这一条结果及附件，通过压缩文件的方式上传
				if (filesList.size() > 0) {
					List<DJ_ResultActive> bigFileResult = new ArrayList<DJ_ResultActive>();
					bigFileResult.add(dj_ResultActive);
					
					String dir = AppConst.CurrentJITResultPath(djLine.getLineID())
							+ File.separator + dj_Plan_ID + "_" + completeDT;
					String zippath = dir + ".zip";
					if (!FileUtils.checkFileExists(zippath)) {
						String jsonString = gson.toJson(dj_ResultActive);
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
						FileUtils.SaveToFile(dir + File.separator, dj_Plan_ID + "_" + completeDT + ".txt", s,
								false);
						if (ZipUtils.zipFolder(dir, zippath))
							FileUtils.deleteDirectory(dir);
						else {
							// 插入操作日志
							OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
									AppConst.LOGSTATUS_ERROR,
									context.getString(R.string.cumm_cummdownload_iszipfileerror,
											zippath));
							continue;
						}
					}
					ReturnResultInfo returnInfo = UploadFile.getIntance()
							.UploadFileByWebNew(context, null, zippath,
							dj_Plan_ID + "_" + completeDT + ".zip", "DJRESULTFORJIT");
					if (returnInfo.getResult_YN()) {
						djresultActiveDao
							.deleteDJResultUploadedData(bigFileResult);
						djphotobyresultDao
							.deleteDJResultForFilesUploadedData(filesList);
						for (int n = 0; n < MyfileList.size(); n++) {
							File f = new File(MyfileList.get(n));
							f.delete();
						}
						File file = new File(zippath);
						file.delete();
					} else {
						// 插入操作日志
						OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
								AppConst.LOGSTATUS_ERROR,
								zippath + "\n[" + returnInfo.getError_Message_TX() + "]");
						continue;
					}
				} else {
					listWithoutFiles.add(dj_ResultActive);
				}
			}
			
			// 将没附件的结果选出，一次上传。
			if (listWithoutFiles.size() > 0) {
				json = gson.toJson(listWithoutFiles);
				json = json.replace("false", "0").replace("true", "1");
	
				if (WebserviceFactory
						.InsertMultiDJResultJITForAndroid(json)) {
					djresultActiveDao
							.deleteDJResultUploadedData(listWithoutFiles);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}	

	/**
	 * 实时上传到位结果
	 * 
	 * @param context
	 * @param djLine
	 */
	public void uploadTaskIDPosInfoForJIL(Context context, DJLine djLine) {
		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;

			djresultdaoSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			djtaskIDPosActiveDao = djresultdaoSession
					.getDJ_TaskIDPosActiveDao();
			List<DJ_TaskIDPosActive> resultList = djtaskIDPosActiveDao
					.loadforUploadJIT(1);
			if (resultList == null || resultList.size() <= 0)
				return;
			Gson gson = new Gson();
			String jsonString = gson.toJson(resultList);
			if (WebserviceFactory.insertTaskIDPosActiveJIT(String.valueOf(djLine.getLineID()), jsonString))
				djtaskIDPosActiveDao.deleteUploadedData(resultList);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 实时上传启停结果
	 * 
	 * @param context
	 * @param djLine
	 */
	public void uploadSRInfoForJIL(Context context, DJLine djLine) {
		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;

			djresultdaoSession = ((AppContext) context.getApplicationContext()).getUploadingSession(
					djLine.getLineID());
			mobjectstateDao = djresultdaoSession.getMObject_StateDao();
			List<MObject_State> resultList = mobjectstateDao
					.loadforUploadJIT(1);
			if (resultList == null || resultList.size() <= 0)
				return;
			Gson gson = new Gson();
			String jsonString = gson.toJson(resultList);
			if (WebserviceFactory.insertSRInfoJIT(jsonString))
				mobjectstateDao.deleteUploadedData(resultList);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/*@SuppressWarnings("deprecation")
	private void notifactionForFilesOverStepMaxSize(Context context) {
		try {
			nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			if (!NetWorkHelper.isNetworkAvailable(context))
				return;
			
				Notification n = new Notification(R.drawable.ic_logo,
						context.getText(R.string.commu_send_btn_jitupload),
						System.currentTimeMillis());
				n.flags = Notification.FLAG_AUTO_CANCEL;
				Intent i = new Intent(context, CommDownload.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				// PendingIntent
				PendingIntent contentIntent = PendingIntent.getActivity(
						context, R.string.Notification_JITUpload_ID,
						i, PendingIntent.FLAG_UPDATE_CURRENT);
				n.setLatestEventInfo(context,
						context.getText(R.string.commu_send_btn_jitupload),
						context.getText(R.string.jitupload_overstepmaxsize),
						contentIntent);
				nm.notify(R.string.Notification_JITUpload_ID, n);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@SuppressWarnings("deprecation")
	private void notifactionForFilesError(Context context) {
		try {
			nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			if (!NetWorkHelper.isNetworkAvailable(context))
				return;
			
				Notification n = new Notification(R.drawable.ic_logo,
						context.getText(R.string.commu_send_btn_jitupload),
						System.currentTimeMillis());
				n.flags = Notification.FLAG_AUTO_CANCEL;
				Intent i = new Intent(context, CommDownload.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				// PendingIntent
				PendingIntent contentIntent = PendingIntent.getActivity(
						context, R.string.Notification_JITUpload_ID,
						i, PendingIntent.FLAG_UPDATE_CURRENT);
				n.setLatestEventInfo(context,
						context.getText(R.string.commu_send_btn_jitupload),
						context.getText(R.string.jitupload_errordata),
						contentIntent);
				nm.notify(R.string.Notification_JITUpload_ID, n);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}*/
}
