/**
 * 
 */
package com.moons.xst.track.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.CaseXJResultHelper;
import com.moons.xst.buss.ComDBHelper;
import com.moons.xst.buss.DJResultHelper;
import com.moons.xst.buss.FilesUpDownHelper;
import com.moons.xst.buss.GPSPositionHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.dao.DJLineDAO;
import com.moons.xst.track.dao.DataTransHelper;
import com.tencent.mm.sdk.platformtools.Log;

/**
 * 实时通信服务。
 * 
 * 说明： 此为小神探客户端运行时驻留后台服务，生命周期与客户端生命周期一致， 主要负责完成与上位机web服务的实时通信。
 * 
 * @author LKZ
 * 
 */
public class CommuJITService extends Service {
	private static final String TAG = "CommuJITService";

	Intent updateSysDatetime;
	/**
	 * 一次提交GPS数据的数量
	 */
	private final int onceUploadGpscount = 20;
	/**
	 * 后台上传报缺单频率（单位：秒）
	 */
	private final int bgSleepSecondsForMobBug = 60;
	/**
	 * 后台上传点检结果频率(单位：秒)
	 */
	private int bgSleepSecondsForDJResult;

	private static int mTime;
	/**
	 * 后台上传巡点检或巡线其他结果频率(单位：秒)
	 */
	private int bgSleepSecondsForOtherResult = 60;

	Timer timerUploadFiles = new Timer();
	AppContext appContext;
	/**
	 * 0-运行 1-暂停 2-停止(总开关)
	 */
	private int Runstate = 0;
	/**
	 * 巡点检相关内容实时上传开关 0-运行 1-暂停 2-停止
	 */
	private int XDJRunstate = 0;
	/**
	 * 巡线相关内容实时上传开关 0-运行 1-暂停 2-停止
	 */
	private int XXRunstate = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	Handler handler = new Handler();

	@Override
	public void onCreate() {
		appContext = new AppContext();
		bgSleepSecondsForDJResult = AppContext.getJITUploadXDJOnceTime();
		AppConst.OnceUploadNum = AppContext.getJITUploadXDJOnceNum();
		backGroupReflashClock();
		mTime = bgSleepSecondsForDJResult;
		updateSysDatetime = new Intent();
		updateSysDatetime.setAction("com.xst.track.service.updateSysDateTime");
	}

	@Override
	public void onDestroy() {
		handler.removeCallbacks(timerUploadXDJData);
		handler.removeCallbacks(timerUploadDJResultData);
		handler.removeCallbacks(timerUploadXXData);
		handler.removeCallbacks(timerUploadMobBugData);
		//handler.removeCallbacks(taskupdatetime);

		timerUploadFiles.cancel();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Runstate = bundle.getInt("Runstate");
				XDJRunstate = bundle.getInt("XDJRunstate");
				XXRunstate = bundle.getInt("XXRunstate");
			}

			AppConst.OnceUploadNum = AppContext.getJITUploadXDJOnceNum();
			bgSleepSecondsForDJResult = AppContext.getJITUploadXDJOnceTime();
			if (mTime != bgSleepSecondsForDJResult) {
				handler.removeCallbacks(timerUploadDJResultData);
				mTime = bgSleepSecondsForDJResult;
				// 开启上传巡点检结果定时器
				handler.postDelayed(timerUploadDJResultData,
						bgSleepSecondsForDJResult * 1000);
			}
		}
		updateSysDatetime = new Intent();
		updateSysDatetime.setAction("com.xst.track.service.updateSysDateTime");
	}

	private void sendUpdateSysTime(long sysDateTime) {
		if (sysDateTime > 0) {
			long timeSpan = sysDateTime - System.currentTimeMillis();
			if (Math.abs(timeSpan) > 1000 * 60) {
				updateSysDatetime.putExtra("sysDateTime", sysDateTime);
				sendBroadcast(updateSysDatetime);
			}
		}
	}

	// 上传巡点检其他结果定时器
	Runnable timerUploadXDJData = new Runnable() {
		@Override
		public void run() {
			startXDJDataTimer();
		}
	};

	// 上传巡点检结果定时器
	Runnable timerUploadDJResultData = new Runnable() {
		@Override
		public void run() {
			startDJResultTimer();
		}
	};

	// 上传巡线其他结果定时器
	Runnable timerUploadXXData = new Runnable() {
		@Override
		public void run() {
			startXXDataTimer();

		}
	};

	// 上传巡线报缺结果定时器
	Runnable timerUploadMobBugData = new Runnable() {
		@Override
		public void run() {
			startXXMobBugTimer();
		}
	};

	/**
	 * 定时器
	 * 
	 * @param count
	 * @param seconds
	 */
	private void backGroupReflashClock() {

		// 开启系统校时
//		handler.postDelayed(taskupdatetime, 1000 * 30);
		// 开启上传巡点检其他结果定时器
		handler.postDelayed(timerUploadXDJData,
				bgSleepSecondsForOtherResult * 1000);
		// 开启上传巡点检结果定时器
		handler.postDelayed(timerUploadDJResultData,
				bgSleepSecondsForDJResult * 1000);
		// 开启上传巡线其他结果定时器
		handler.postDelayed(timerUploadXXData,
				bgSleepSecondsForOtherResult * 1000);
		// 开启上传巡线报缺结果定时器
		handler.postDelayed(timerUploadMobBugData,
				bgSleepSecondsForMobBug * 1000);

		TimerTask fileUploadtime = new TimerTask() {
			@Override
			public void run() {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// uploadFilesJIT();上位机无处理，无需传上去
					}
				}).start();
			}
		};
		timerUploadFiles.schedule(fileUploadtime, 1000, 1000 * 120);
	}

	/**
	 * 上传巡点检其他结果定时器
	 */
	private void startXDJDataTimer() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (Runstate == 0
							&& AppContext.resultDataFileListBuffer.size() != 0) {
						if (XDJRunstate == 0) {
							for (DJLine djLine : AppContext.resultDataFileListBuffer) {
								if (!AppContext.GetIsUpload()) {
									boolean flag = false;
									List<DJLine> lineinfos = DataTransHelper
											.ConvertLineFromXMLFile();
									for (DJLine lineinfo : lineinfos) {
										if (lineinfo.getLineID() == djLine
												.getLineID()) {
											flag = true;
											djLine.setlineType(lineinfo
													.getlineType());
											break;
										}
									}
									if (flag) {
										// 巡点检(只针对2641系统实时上传)
										if ((djLine.getlineType() == AppConst.LineType.XDJ
												.getLineType() 
												|| djLine.getlineType() == AppConst.LineType.DJPC
														.getLineType())
												&& djLine.getLineID() != 0
												&& String
														.valueOf(
																djLine.getLineID())
														.equals(String
																.valueOf(AppContext
																		.GetCurrLineIDForJIT()))) {
											// 巡点检到位结果
											uploadTaskIDPosInfoJIL(djLine);
											// 巡点检启停结果
											uploadSRInfoJIL(djLine);
											// GPS信息
											uploadGpsInfoJIT(
													onceUploadGpscount, djLine);
										}
										// 条件巡检(石化盈科)
										else if (djLine.getlineType() == AppConst.LineType.CaseXJ
												.getLineType()
												&& djLine.getLineID() != 0
												&& String
														.valueOf(
																djLine.getLineID())
														.equals(String
																.valueOf(AppContext
																		.GetCurrLineIDForJIT()))) {
											uploadCaseXJResultTogetherJIT(djLine);
											// GPS信息
											uploadGpsInfoJIT(
													onceUploadGpscount, djLine);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					DJLineDAO.loadResultDataFileList();
				}
				handler.postDelayed(timerUploadXDJData,
						bgSleepSecondsForOtherResult * 1000);
			}
		}).start();
	}

	TimerTask djresultTask;

	/**
	 * 上传巡点检结果定时器
	 */
	private void startDJResultTimer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (Runstate == 0
							&& AppContext.resultDataFileListBuffer.size() != 0) {
						if (XDJRunstate == 0) {
							for (DJLine djLine : AppContext.resultDataFileListBuffer) {
								if (!AppContext.GetIsUpload()) {
									boolean flag = false;
									List<DJLine> lineinfos = DataTransHelper
											.ConvertLineFromXMLFile();
									for (DJLine lineinfo : lineinfos) {
										if (lineinfo.getLineID() == djLine
												.getLineID()) {
											flag = true;
											djLine.setlineType(lineinfo
													.getlineType());
											break;
										}
									}
									if (flag) {
										// 实时上传点检结果，包括录音，照片文件(只针对2641系统实时上传)
										if ((djLine.getlineType() == AppConst.LineType.XDJ
												.getLineType() 
												|| djLine.getlineType() == AppConst.LineType.DJPC
														.getLineType())
												&& djLine.getLineID() != 0
												&& String
														.valueOf(
																djLine.getLineID())
														.equals(String
																.valueOf(AppContext
																		.GetCurrLineIDForJIT()))) {
											uploadDJResultJIT(djLine);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					DJLineDAO.loadResultDataFileList();
				}
				handler.postDelayed(timerUploadDJResultData,
						bgSleepSecondsForDJResult * 1000);
			}
		}).start();
	}

	/**
	 * 上传巡线其他结果定时器
	 */
	private void startXXDataTimer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (Runstate == 0
							&& AppContext.resultDataFileListBuffer.size() != 0) {
						if (XXRunstate == 0) {
							for (DJLine djLine : AppContext.resultDataFileListBuffer) {
								if (!AppContext.GetIsUpload()) {
									if (djLine.getLineID() != 0
											&& String.valueOf(
													djLine.getLineID()).equals(
													String.valueOf(AppContext
															.GetCurrLineIDForJIT()))) {
										// 巡线相关内容
										uploadGpsInfoJIT(onceUploadGpscount,
												djLine);
										uploadArriveResInfoJIT(djLine);
										uploadGPSXXPlanResultJIT(djLine);
										uploadGpsInitInfoJIT(djLine);
										uploadOverSpeedInfoJIT(djLine);
									}
								}
							}
						}
					}
				} catch (Exception e) {
					DJLineDAO.loadResultDataFileList();
				}
				handler.postDelayed(timerUploadXXData,
						bgSleepSecondsForOtherResult * 1000);
			}
		}).start();
	}

	/**
	 * 上传巡线报缺结果定时器
	 */
	private void startXXMobBugTimer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (Runstate == 0) {
						uploadTempBugResultJIT(new DJLine());
						if (AppContext.resultDataFileListBuffer.size() != 0) {
							if (XXRunstate == 0) {
								for (DJLine djLine : AppContext.resultDataFileListBuffer) {
									if (!AppContext.GetIsUpload()) {
										if (djLine.getLineID() != 0
												&& String.valueOf(
														djLine.getLineID()).equals(
														String.valueOf(AppContext
																.GetCurrLineIDForJIT()))) {
											uploadBugResultJIT(djLine);
										}
									}
								}
							}
						}
					}
//					if (Runstate == 0
//							&& AppContext.resultDataFileListBuffer.size() != 0) {
////						uploadTempBugResultJIT(new DJLine());
//						if (XXRunstate == 0) {
//							for (DJLine djLine : AppContext.resultDataFileListBuffer) {
//								if (!AppContext.GetIsUpload()) {
//									if (djLine.getLineID() != 0
//											&& String.valueOf(
//													djLine.getLineID()).equals(
//													String.valueOf(AppContext
//															.GetCurrLineIDForJIT()))) {
//										uploadBugResultJIT(djLine);
//									}
//								}
//							}
//						}
//					}
				} catch (Exception e) {
					DJLineDAO.loadResultDataFileList();
				}
				handler.postDelayed(timerUploadMobBugData,
						bgSleepSecondsForMobBug * 1000);
			}
		}).start();
	}

	/**
	 * 实时上传gps初始化信息
	 * 
	 * @param count
	 */
	private synchronized void uploadGpsInitInfoJIT(DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			// if (!WebserviceFactory.checkWS())
			// return;
			Context context = getBaseContext();
			GPSPositionHelper.GetIntance().uploadGPSInitInfoForJIT(context,
					djLine);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 实时上传gps轨迹坐标方法
	 * 
	 * @param count
	 */
	private synchronized void uploadGpsInfoJIT(final int count, DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			// if (!WebserviceFactory.checkWS())
			// return;
			Context context = getBaseContext();
			GPSPositionHelper.GetIntance().uploadGPSInfoForJIT(context, count,
					djLine);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 实时上传巡线到位考核结果方法
	 * 
	 * @param count
	 */
	private synchronized void uploadArriveResInfoJIT(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			// if (!WebserviceFactory.checkWS())
			// return;
			Context context = getBaseContext();
			GPSPositionHelper.GetIntance().uploadTrackArriveResultForJIT(
					context, djLine);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 实时上传报缺单方法
	 * 
	 * @param count
	 */
	private synchronized void uploadBugResultJIT(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			Context context = getBaseContext();
			// GPSPositionHelper.GetIntance().uploadMobjectBugResultForJIT1(
			// context, djLine);
			GPSPositionHelper.GetIntance().uploadMobjectBugResultForJITNEW(
					context, djLine);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void uploadTempBugResultJIT(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			Context context = getBaseContext();
			// ComDBHelper.GetIntance().uploadTempMobjectBugResultForJIT(context,
			// djLine);
			ComDBHelper.GetIntance().uploadTempMobjectBugResultForJITNEW(
					context, djLine);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 实时上传巡线任务结果数据
	 * 
	 * @param count
	 */
	private synchronized void uploadGPSXXPlanResultJIT(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			// if (!WebserviceFactory.checkWS())
			// return;
			Context context = getBaseContext();
			GPSPositionHelper.GetIntance().uploadXXPlanResultsxForJIT(context,
					djLine);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 根据网络校准系统时间
	 * 
	 * @param count
	 */
	/*private void uploadSysDatetime() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
						return;
					// if (!WebserviceFactory.checkWS())
					// return;
					if ((!StringUtils.isEmpty(AppContext.getUpdateSysDateType()))
							&& AppContext.getUpdateSysDateType()
									.equals(AppConst.UpdateSysDate_WEB)
							&& AppContext.getUpdateSysDateYN()) {
						String timeString = WebserviceFactory
								.getServiceDateTimeOfStr();
						if (!StringUtils.isEmpty(timeString)) {
							sendUpdateSysTime(DateTimeHelper.StringToDate(
									timeString).getTime());
						}
					}
				} finally {
					handler.postDelayed(taskupdatetime, 1000 * 30);
				}
			}
		}).start();
	}*/

	/**
	 * 后台上传文件
	 */
	private void uploadFilesJIT() {
		try {
			if (!AppContext.getUploadFile()) {
				return;
			}
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			Context context = getBaseContext();
			FilesUpDownHelper.GetIntance().uploadImageFile(context);
			FilesUpDownHelper.GetIntance().uploadRecordFile(context);
			FilesUpDownHelper.GetIntance().uploadVideoFile(context);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 实时上传超速报警记录
	 * 
	 * @param djLine
	 */
	private synchronized void uploadOverSpeedInfoJIT(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			// if (!WebserviceFactory.checkWS())
			// return;
			Context context = getBaseContext();
			GPSPositionHelper.GetIntance().uploadOverSpeedRecordForJIT(context,
					djLine);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 实时上传巡点检结果(包括附件信息)
	 * 
	 * @param djLine
	 */
	private synchronized void uploadDJResultJIT(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
				return;
			}
			Context context = getBaseContext();
			DJResultHelper.GetIntance().uploadDJResultForJIT(context, djLine);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private synchronized void uploadCaseXJResultTogetherJIT(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
				return;
			}
			Context context = getBaseContext();
			CaseXJResultHelper.GetIntance().uploadCaseXJResultTogetherJIT(
					context, djLine);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 实时上传巡点检到位结果
	 * 
	 * @param djLine
	 */
	private synchronized void uploadTaskIDPosInfoJIL(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
				return;
			}
			Context context = getBaseContext();
			DJResultHelper.GetIntance().uploadTaskIDPosInfoForJIL(context,
					djLine);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 实时上传启停结果
	 * 
	 * @param djLine
	 */
	private synchronized void uploadSRInfoJIL(final DJLine djLine) {
		try {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			Context context = getBaseContext();
			DJResultHelper.GetIntance().uploadSRInfoForJIL(context, djLine);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
