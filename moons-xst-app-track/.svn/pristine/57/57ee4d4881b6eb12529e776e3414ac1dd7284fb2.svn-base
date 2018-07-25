package com.moons.xst.track.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IImeasureService;
import android.os.ServiceManager;
import android.util.Log;

import com.moons.xst.track.receiver.TouchIDPosReceiver;
import com.moons.xst.track.service.RfidAIDLRemoteService;

public class RFIDManager {
	private static final String TAG = "RFIDManager";

	private static RFIDManager instance = null;
	private TouchIDPosReceiver mTouchIDPosReceiver;
	private Activity mActivity;
	private String mType = "";
	private RfidAIDLRemoteService.mBinder mBinder;

	private static boolean isConnetcted = false;
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.e(TAG, "onServiceConnected");
			// mBinder=(RfidAIDLRemoteService.mBinder) service;
			isConnetcted = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.e(TAG, "onServiceDisconnected");
			isConnetcted = false;
		}
	};

	private RFIDManager(Activity activity, String type) {
		mActivity = activity;
		mType = type;
	}

	public static RFIDManager getRFIDManager(Activity activity, String type) {

		synchronized (RFIDManager.class) {
			if (instance == null) {
				instance = new RFIDManager(activity, type);
			}
		}
		return instance;
	}

	public void bindService() {
		bindRemoteService();
	}

	public void unbindService() {
		unbindRemoteService();
		instance = null;
	}

	public void regReceiver_and_bindService() {
		regReceiver();
		bindRemoteService();
	}

	public void unRegReceiver_and_unbindRemoteService() {
		unRegReceiver();
		unbindRemoteService();
		instance = null;
	}

	/**
	 * 注册监听器
	 */
	private void regReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.RfidActivity");
		if (mTouchIDPosReceiver == null)
			mTouchIDPosReceiver = new TouchIDPosReceiver(mActivity, mType);
		/* 注册接收器 */
		mActivity.registerReceiver(mTouchIDPosReceiver, filter);
	}

	/**
	 * 解除注册接收器
	 */
	private void unRegReceiver() {
		if (mTouchIDPosReceiver != null) {
			mActivity.unregisterReceiver(mTouchIDPosReceiver);
		}
	}

	/**
	 * 绑定远程Service
	 */
	private void bindRemoteService() {
		if (!isConnetcted) {
			Intent intent = new Intent(mActivity, RfidAIDLRemoteService.class);
			isConnetcted = mActivity.bindService(intent, mServiceConnection,
					Context.BIND_AUTO_CREATE);
		}
	}

	/**
	 * 解除绑定远程Service
	 */
	private void unbindRemoteService() {
		try {
			if (isConnetcted) {
				isConnetcted = false;
				if (mServiceConnection != null) {
					mActivity.unbindService(mServiceConnection);
				}
				try {
					IImeasureService imeasureService = IImeasureService.Stub
							.asInterface(ServiceManager.getService("imeasure"));
					imeasureService.rfi_cancel();
					// rst = imeasureService.rfi_cancel();
				} catch (Exception e) {
					// rst = -1;
				}
			}
		} catch (Exception e) {}
	}
}