package com.moons.xst.track.xstinterface;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.Toast;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.WifiManagerHelper;
import com.tocel.vibrationlib.interfaces.Eiss_CB;
import com.tocel.vibrationlib.interfaces.Eiss_Value_CB;
import com.tocel.vibrationlib.interfaces.Rat;
import com.tocel.vibrationlib.interfaces.Recipe;
import com.tocel.vibrationlib.interfaces.Scan_CB;
import com.tocel.vibrationlib.interfaces.SucFail;
import com.tocel.vibrationlib.interfaces.Tmp_CB;

import de.greenrobot.event.EventBus;

public class Bluetooth_TuoSheng_ForTemperature implements
		BluetoothTemperatureInterface {
	private Context context;
	private boolean needTem = false;
	private boolean giveBack = false;
	private ValueForBackInterface valueForBackInterface;
	private Handler handler;
	private Scan_CB scan_CB;
	private String fsl;
	private Double mfsl;
	private Tmp_CB tmp_CB;
	private int wifiState;

	public Bluetooth_TuoSheng_ForTemperature(Context mContext) {
		context = mContext;
		enableBluetooth();
	}
	private void wifi_close() {
		wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wifiState = wm.getWifiState();
		if (WifiManager.WIFI_STATE_ENABLED == wifiState
				|| WifiManager.WIFI_STATE_ENABLING == wifiState) {
			wm.setWifiEnabled(false);
		}
	}

	@Override
	public void enableBluetooth() {
		Rat.getInstance().enableBuletooth(context);
	}

	@Override
	public void disableBluetooth() {
		Rat.getInstance().disableBuletooth();
	}

	@Override
	public void scanBluetooth() {
		if (scan_CB == null) {
			scan_CB = new Scan_CB() {

				@Override
				public void onLeScan(final BluetoothDevice device) {
					if (!StringUtils.isEmpty(device.getName())) {
						EventBus.getDefault().post(device);
					}
				}
			};
		}
		Rat.getInstance().startScan(scan_CB);
	}

	@Override
	public void stopScanBluetooth() {
		Rat.getInstance().stopScan();
	}

	@Override
	public void connBuletoothDevice(
			final ValueForBackInterface valueForBackInterface) {
		String string = AppContext.getBlueToothAddressforTemperature();
		Rat.getInstance().connectDevice_Normal(string, new SucFail() {

			@Override
			public void onFailed_UI(String msg) {
				valueForBackInterface.onFail("ConnFail");
			}

			@Override
			public void onSucceed_UI(BluetoothDevice device, String msg) {
					getTemAfterConn();
			}

		});
	}

	@Override
	public void disConnBluetoothDevice(boolean isWifiEnable) {
		if(isWifiEnable){
			checkWifi();
		}
		stopGetTemperature();
		Rat.getInstance().disConnectDevice();
	}

	private void checkWifi() {
		if (WifiManager.WIFI_STATE_ENABLED == wifiState
				|| WifiManager.WIFI_STATE_ENABLING == wifiState) {
			if(wm!=null)
				wm.setWifiEnabled(true);
		}
	}

	@Override
	public void BluetoothState() {

	}

	@Override
	public void getTemperature(Context context, boolean connectYN,
			ValueForBackInterface valueForBackInterface) {
		this.valueForBackInterface = valueForBackInterface;
		fsl = AppContext.getBTFSLforTemputer();
		mfsl = Double.valueOf(fsl);
		if (!connectYN) {
			wifi_close();
			connBuletoothDevice(valueForBackInterface);
		} else {
			getTemAfterConn();
		}
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (needTem) {
				setFSL(mfsl);
				refreshTemputer(valueForBackInterface);
			}
		}

	};
	Runnable repateRunnable = new Runnable() {

		@Override
		public void run() {
			if (needTem) {
				Recipe.getInstance().getTmp(tmp_CB);
			}
		}

	};
	private WifiManager wm;
	

	private void refreshTemputer(
			final ValueForBackInterface valueForBackInterface) {
		if(tmp_CB==null){
			tmp_CB = new Tmp_CB() {
				
				@Override
				public void onValue_UI(double f) {
					if (giveBack) {
						valueForBackInterface.onSuccess(Float.valueOf(String
								.valueOf(f)));
						handler.post(repateRunnable);
					}
				}
				
				@Override
				public void onFail_UI(String msg) {
					valueForBackInterface.onFail("ValueFail");
				}
			};
		}

		Recipe.getInstance().getTmp(tmp_CB);
	}

	private void getTemAfterConn() {

		needTem = true;
		giveBack = true;
		if (handler == null) {
			handler = new Handler();
		}
		if(runnable!=null){
			handler.removeCallbacks(runnable);
		}
		handler.postDelayed(runnable, 100);
	}

	@Override
	public void stopGetTemperature() {
		if (handler == null) {
			handler = new Handler();
		}
		needTem = false;
		giveBack = false;
		handler.removeCallbacks(runnable);
		handler.removeCallbacks(repateRunnable);

	}

	@Override
	public boolean getFSLEnable() {
		return true;
	}

	// 设置发射率
	@Override
	public void setFSL(Double eiss) {
		Recipe.getInstance().setEiss(eiss, new Eiss_CB() {
			@Override
			public void onSucceed_UI(String msg) {
				// LogUtil.loge("发射率设置成功");
			}

			@Override
			public void onFailed_UI(String msg) {
				// LogUtil.loge("发射率设置失败");
			}
		});
	}

	// 获取发射率
	public void getFSL() {
		Recipe.getInstance().getEiss(new Eiss_Value_CB() {
			@Override
			public void onEissValue_UI(double eiss) {
				// LogUtil.loge("eiss:"+eiss);
				Toast.makeText(context, eiss + "", 0).show();
			}

			@Override
			public void onEissFailed_UI(String msg) {
				// LogUtil.loge("获取发射率失败");
			}
		});
	}
}
