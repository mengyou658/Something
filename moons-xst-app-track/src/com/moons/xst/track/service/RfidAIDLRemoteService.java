package com.moons.xst.track.service;

import java.text.DecimalFormat;

import com.moons.xst.track.common.UIHelper;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IImeasureService;
import android.os.ServiceManager;

public class RfidAIDLRemoteService extends Service {

	private static final String TAG = "RfidAIDLRemoteService";

	private static int mDebugFlag = 0;
	boolean isStop = false;
	// 当前编码
	private String mCurRfidNo = "";
	// 读取钮扣超时时间，以秒为单位
	private int mTimeOuts = -1;
	private byte[] mSPKData = new byte[8];
	SharedPreferences preferences;
	Boolean isRFID=true;

	@Override
	public void onCreate() {
		start();
	}
	public class mBinder extends Binder{
		public void yesRFID(){
			isRFID=true;
		}
		public void noRFID(){
			isRFID=false;
		}
	}
	@Override
	public void onDestroy() {
		isStop = true;
		stop();
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void start() {
		handler.postDelayed(runnable, 0);
	}

	private void stop() {
		handler.removeCallbacks(runnable);
		//cancel();
	}

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			try {
				int rst = inquire();
				if (rst == 0) {
					int rst1 = getuid();
					if (rst1 > 0) {
						mCurRfidNo = GetRfidStr();
						SendData(mCurRfidNo);
						handler.postDelayed(runnable, 1000);
					} else {
						handler.postDelayed(runnable, 1000);
					}
				} else {
					SendData("");
					handler.postDelayed(runnable, 1000);
				}
			} catch (Exception e) {
				// TODO: handle exception
				handler.postDelayed(runnable, 1000);
			}
			/*if (isStop == false){
				handler.postDelayed(this, 1000);
			}*/
		}
	};

	private int inquire() {
		
			if (mDebugFlag == 0) {
				try {
					IImeasureService imeasureService = IImeasureService.Stub
							.asInterface(ServiceManager.getService("imeasure"));

					int rst = imeasureService.rfi_inquire(mTimeOuts);

					return rst;

				} catch (Exception e) {
					return -1;
				}
			} else {
				try {
					Thread.sleep(500);
					//Thread.currentThread().sleep(500);// 毫秒
				} catch (Exception e) {
					// TODO: handle exception
				}
				return 0;
			}
	}

	/**
	 * /** 取消请求RFID标签
	 */
	private int cancel() {
		int rst = -1;
		if (mDebugFlag == 0) {
			try {
				IImeasureService imeasureService = IImeasureService.Stub
						.asInterface(ServiceManager.getService("imeasure"));

				rst = imeasureService.rfi_cancel();

			} catch (Exception e) {
				rst = -1;
			}
		} else
			rst = 0;
		return rst;
	}

	private int getuid() {
		int rst = -1;
		if (mDebugFlag == 0) {
			try {
				IImeasureService imeasureService = IImeasureService.Stub
						.asInterface(ServiceManager.getService("imeasure"));

				rst = imeasureService.rfi_get_uid(mSPKData);

			} catch (Exception e) {
				rst = -1;
			}
		} else {
			for (int i = 0; i < mSPKData.length; i++) {
				DecimalFormat df = new DecimalFormat("#0");
				String cl0 = df.format(15 * (Math.random()));
				mSPKData[i] = Byte.valueOf(cl0);
			}
			rst = 1;
		}
		return rst;
	}

	private String GetRfidStr() {
		String rString = "";
		try {
			for (int i = mSPKData.length - 1; i >= 0; i--) {
				// if (i < mSPKData.length - 1)
				// rString = rString + "-";
				rString += addZeroForNum(
						Integer.toHexString(mSPKData[i] & 0xFF), 2);
			}
		} catch (Exception e) {
			rString = "";
		}

		return rString.toUpperCase();
	}

	private void SendData(String rfidNo) {
		Intent intent = new Intent();

		intent.putExtra("RfidNo", rfidNo);

		intent.setAction("android.intent.action.RfidActivity");// action与接收器相同
		
		sendBroadcast(intent);
	}
	private String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);// 左补0
				// sb.append(str).append("0");//右补0
				str = sb.toString();
				strLen = str.length();
			}
		}

		return str;
	}
}