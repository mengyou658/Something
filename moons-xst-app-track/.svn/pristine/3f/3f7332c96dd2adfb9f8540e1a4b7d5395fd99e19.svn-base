package com.moons.xst.track.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.ComDBHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.GPSXXTempTask;
import com.moons.xst.track.bean.Update;
import com.moons.xst.track.common.JsonToEntityUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.ui.AboutXST;
import com.moons.xst.track.ui.CommDownload;
import com.moons.xst.track.ui.CommUSBDownload;
import com.moons.xst.track.ui.TempTask;

public class NotificationService extends Service {
	private NotificationManager nm;
	//Timer timerPullNotifactionData = new Timer();
	Handler handler = new Handler();
	private final int bgSleepMinutesForPullData = 1;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	Runnable timerPullNotifactionData = new Runnable() {
		@Override
		public void run() {
			new Thread(new Runnable() {
				public void run() {
//					notifactionForAppUpdate();
//					notifactionForLineUpdate();
					notifactionForTempTask();
					handler.postDelayed(timerPullNotifactionData,bgSleepMinutesForPullData * 1000 * 60);
				}
			}).start();
		}
	};
	@Override
	public void onCreate() {
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		handler.postDelayed(timerPullNotifactionData,1000);
	}
	
	/**
	 * 通知--程序更新
	 */
	@SuppressWarnings("deprecation")
	private void notifactionForAppUpdate() {
		try {
			if (!AppContext.getCheckUp()) {
				return;
			}
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			// if (!WebserviceFactory.checkWS())
			// return;
			int oldVsersionCode;
			if (AppContext.xstTrackPackageInfo != null) {
				oldVsersionCode = AppContext.xstTrackPackageInfo.versionCode;
			} else {
				return;
			}
			String appversionxml = WebserviceFactory
					.getAppVersionInfo(getBaseContext(),AppContext.getModel());
			Update update = Update.parse(appversionxml);
			int versionCode = update.getVersionCode();
			String versionName = update.getVersionName();
			if (oldVsersionCode < versionCode) {
				Notification n = new Notification(R.drawable.ic_logo,
						this.getText(R.string.main_menu_commu_appUpdate),
						System.currentTimeMillis());
				n.flags = Notification.FLAG_AUTO_CANCEL;
				Intent i = new Intent(getBaseContext(), AboutXST.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				// PendingIntent
				PendingIntent contentIntent = PendingIntent.getActivity(
						getBaseContext(), R.string.Notification_APPUpdate_ID,
						i, PendingIntent.FLAG_UPDATE_CURRENT);
				n.setLatestEventInfo(
						getBaseContext(),
						this.getText(R.string.main_menu_commu_appUpdate),
						this.getText(R.string.appupdate_hasnewapp)
								+ versionName + "("
								+ String.valueOf(versionCode) + ")",
						contentIntent);
				nm.notify(R.string.Notification_APPUpdate_ID, n);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 通知--路线更新
	 */
	@SuppressWarnings("deprecation")
	private void notifactionForLineUpdate() {
		try {

			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			// if (!WebserviceFactory.checkWS())
			// return;
			String lineChangeInfo = WebserviceFactory
					.getlineChangeInfo(getBaseContext());
			if ((!StringUtils.isEmpty(lineChangeInfo))
					&& lineChangeInfo.toUpperCase().equals("YES")) {
				Notification n = new Notification(R.drawable.ic_logo,
						this.getText(R.string.main_menu_commu_receive),
						System.currentTimeMillis());
				n.flags = Notification.FLAG_AUTO_CANCEL;
				Intent i;
				if (AppContext.getCommunicationType().equals(
						AppConst.CommunicationType_Wireless)) {
					i = new Intent(getBaseContext(), CommDownload.class);
				} else {
					i = new Intent(getBaseContext(), CommUSBDownload.class);
				}
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				// PendingIntent
				PendingIntent contentIntent = PendingIntent.getActivity(
						getBaseContext(), R.string.Notification_LineUpdate_ID,
						i, PendingIntent.FLAG_UPDATE_CURRENT);
				n.setLatestEventInfo(getBaseContext(),
						this.getText(R.string.main_menu_commu_receive),
						this.getText(R.string.dataupdate_hasnewline),
						contentIntent);
				nm.notify(R.string.Notification_LineUpdate_ID, n);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 通知--临时任务 正规版
	 */
	
	@SuppressWarnings("deprecation")
	private void notifactionForTempTask() {
		try {

			if (!NetWorkHelper.isNetworkAvailable(getBaseContext()))
				return;
			String tempTaskInfo = WebserviceFactory
					.getTempTaskInfo(getBaseContext());
			if (!StringUtils.isEmpty(tempTaskInfo)) {
				GPSXXTempTask[] temptask = JsonToEntityUtils.jsontoTempTask(tempTaskInfo);
				if (temptask != null && temptask.length > 0) 
				{
					Notification n = new Notification(R.drawable.ic_logo,
							this.getText(R.string.main_menu_commu_temptask),
							System.currentTimeMillis());
					n.flags = Notification.FLAG_AUTO_CANCEL;
					n.defaults |= Notification.DEFAULT_SOUND;
					n.defaults |= Notification.DEFAULT_VIBRATE;
					Intent i = new Intent(getBaseContext(), TempTask.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					// PendingIntent
					PendingIntent contentIntent = PendingIntent.getActivity(
							getBaseContext(), R.string.Notification_TempTask_ID, i,
							PendingIntent.FLAG_UPDATE_CURRENT);
					n.setLatestEventInfo(getBaseContext(),
							this.getText(R.string.main_menu_commu_temptask),
							this.getText(R.string.new_temptask),
							contentIntent);
					nm.notify(R.string.Notification_TempTask_ID, n);
					ComDBHelper.GetIntance().InsertTempTask(getBaseContext(), temptask);
					for (int j = 0; j < temptask.length; j++) {
						AppContext.nowTempTask.add(temptask[j]);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public void onDestroy() {
		try {
			handler.removeCallbacks(timerPullNotifactionData);
//			nm.cancel(R.string.Notification_APPUpdate_ID);
//			nm.cancel(R.string.Notification_LineUpdate_ID);
			nm.cancel(R.string.Notification_TempTask_ID);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
