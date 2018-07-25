package com.moons.xst.track.xstinterface;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;
import com.sytest.app.blemulti.BleDevice;
import com.sytest.app.blemulti.Rat;
import com.sytest.app.blemulti.easy.Recipe;
import com.sytest.app.blemulti.exception.BleException;
import com.sytest.app.blemulti.exception.ScanException;
import com.sytest.app.blemulti.interfaces.Battery_CB;
import com.sytest.app.blemulti.interfaces.Scan_CB;
import com.sytest.app.blemulti.interfaces.SucFail;
import com.sytest.app.blemulti.interfaces.Tmp_CB;

import de.greenrobot.event.EventBus;

public class Bluetooth_SU_100A_ForTemperature implements
		BluetoothTemperatureInterface {
	private static final long SCANTIMEOUT = 120000;
	private static final int SCANSUCCESS = 1;
	private static final int SCANFAIL = 2;
	private BleDevice bleDevice;

	private ValueForBackInterface temperatureInterface;
	private com.moons.xst.track.widget.AlertDialog dialog = null;
	private Context mcontext;
	private boolean needTem = true;
	private boolean giveBack = true;
	private boolean initilizedYN = false;
	private boolean isToConn = true;// 是否启动连接
	private boolean isError;// jar包内部错误，无回调
	private Scan_CB scanCallback;
	private SucFail cb;
	private Handler handler = new Handler();

	public Bluetooth_SU_100A_ForTemperature(Context context) {
		mcontext = context;
		Rat.initilize(context);
		initilizedYN = true;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANSUCCESS:
				connBuletoothDevice(temperatureInterface);
				break;
			case SCANFAIL:
				temperatureInterface.onFail("ConnFail");
				Toast.makeText(mcontext,mcontext.getString(R.string.msg_bluetooth_device_restart_tip), Toast.LENGTH_SHORT).show();
				break;
			}

		};
	};

	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			if (isToConn) {
				stopScanBluetooth();
				mHandler.obtainMessage(SCANFAIL).sendToTarget();
			}
		}
	};

	private void ScanBefor() {
		if (StringUtils.isEmpty(AppContext.getBlueToothAddressforTemperature())) {
			temperatureInterface.onFail("ConnFail");
			return;
		}
		mHandler.postDelayed(mRunnable, SCANTIMEOUT);//两分钟后会执行扫描失败
		stopScanBluetooth();
		if (scanCallback == null) {
			scanCallback = new Scan_CB() {

				@Override
				public void onLeScan(BluetoothDevice device) {
					if (!TextUtils.isEmpty(device.getName())
							&& AppContext.getBlueToothAddressforTemperature()
									.equals(device.getAddress())) {
						stopScanBluetooth();
						isToConn = false;
						mHandler.removeCallbacks(mRunnable);
						mHandler.obtainMessage(SCANSUCCESS).sendToTarget();
					}
				}
			};
		}
		try {
			Rat.getInstance().startScan(scanCallback);
		} catch (ScanException e) {
			isToConn = false;
			mHandler.removeCallbacks(mRunnable);
			mHandler.obtainMessage(SCANFAIL).sendToTarget();

			e.printStackTrace();
		}

	}

	Runnable connRunnable = new Runnable() {

		@Override
		public void run() {
			if (isError) {
//				Toast.makeText(mcontext, "内部错误", 0).show();
				temperatureInterface.onFail("ConnFail");
			}
		}
	};

	@Override
	public void connBuletoothDevice(
			final ValueForBackInterface temperatureInterface) {
		isError = true;
		if (cb == null) {

			cb = new SucFail() {

				@Override
				public void onSucceed_UI(@Nullable String msg) {
					isError = false;
					handler.removeCallbacks(connRunnable);
					bleDevice = Rat.getInstance().getFirstBleDevice();
					getTemperatureAfterConn();
				}

				@Override
				public void onFailed_UI(@Nullable String msg) {
					isToConn = false;
					isError = false;
					handler.removeCallbacks(connRunnable);
					temperatureInterface.onFail("ConnFail");
				}
			};
		}
		handler.postDelayed(connRunnable, SCANTIMEOUT);
		Rat.getInstance().connectDevice_Normal(false,
				AppContext.getBlueToothAddressforTemperature(), cb);
	}

	@Override
	public void disConnBluetoothDevice(boolean isWifiEnable) {
		if (!StringUtils
				.isEmpty(AppContext.getBlueToothAddressforTemperature())) {
			if (initilizedYN) {
				stopGetTemperature();
				Rat.getInstance().disConnectDevice(
						AppContext.getBlueToothAddressforTemperature());
			}
		}
	}

	// -----------------------------------------------------------------------------------------//
	@Override
	public void stopGetTemperature() {
		if (handler == null) {
			handler = new Handler();
		}
		if (mHandler == null) {
			mHandler = new Handler();
		}
		isToConn = false;
		needTem = false;
		giveBack = false;
		handler.removeCallbacks(tempreatureRunnable);
		handler.removeCallbacks(BatteryRunnable);
		handler.removeCallbacks(connRunnable);
		mHandler.removeCallbacks(mRunnable);
	}

	@Override
	public void getTemperature(final Context context, final boolean connectYN,
			final ValueForBackInterface temperatureInterface) {
		this.temperatureInterface = temperatureInterface;
//		stopGetTemperature();
		isToConn = true;// 启用扫描倒计时
		if (!connectYN)
			ScanBefor();
		else
			getTemperatureAfterConn();
	}

	Runnable tempreatureRunnable = new Runnable() {

		@Override
		public void run() {
			if (needTem) {
				refreshTemperature(temperatureInterface);
				refreshBattery(temperatureInterface);
				handler.postDelayed(this, 1000);
			}
		}

	};
	Runnable BatteryRunnable = new Runnable() {

		@Override
		public void run() {
			if (needTem) {
				refreshBattery(temperatureInterface);
				handler.postDelayed(this, 1000);
			}
		}

	};

	private void refreshBattery(final ValueForBackInterface temperatureInterface) {
		if (bleDevice != null) {
			Recipe.newInstance(bleDevice).getBattery(new Battery_CB() {

				@Override
				public void onBattery_UI(float f) {
					if (f < 1.0 && f >= 0.80)
						EventBus.getDefault().post("BATTERY_FIVE");
					else if (f < 0.80 && f >= 0.60)
						EventBus.getDefault().post("BATTERY_FOUR");
					else if (f < 0.60 && f >= 0.40)
						EventBus.getDefault().post("BATTERY_THREE");
					else if (f < 0.40 && f >= 0.20)
						EventBus.getDefault().post("BATTERY_TWO");
					else if (f < 0.20 && f >= 0.10) {
						EventBus.getDefault().post("BATTERY_ONE");

						if (mcontext == null)
							return;

						if (dialog == null) {
							LayoutInflater factory = LayoutInflater
									.from(mcontext);
							final View view = factory.inflate(
									R.layout.textview_layout, null);
							dialog = new com.moons.xst.track.widget.AlertDialog(
									mcontext)
									.builder()
									.setTitle(
											mcontext.getString(R.string.msg_bluetooth_battery_tip))
									.setView(view)
									.setMsg(mcontext
											.getString(R.string.msg_bluetooth_battery_low))
									.setPositiveButton(
											mcontext.getString(R.string.sure),
											new android.view.View.OnClickListener() {
												@Override
												public void onClick(View v) {

												}
											});
							dialog.show();
						} else {
							return;
						}
					} else {
						EventBus.getDefault().post("BATTERY_EMPTY");
					}
				}

				@Override
				public void onFail_UI(BleException e) {
					EventBus.getDefault().post("BATTERY_ERROR");
				}
			});
		}
	}

	public void getTemperatureAfterConn() {
		needTem = true;
		giveBack = true;
		if (handler == null) {
			handler = new Handler();
		}
//		if(tempreatureRunnable!=null){
//			handler.removeCallbacks(tempreatureRunnable);
//		}
		handler.postDelayed(tempreatureRunnable, 100);
	}

	private void refreshTemperature(
			final ValueForBackInterface temperatureInterfac) {
		if (bleDevice != null) {
			Recipe.newInstance(bleDevice).getTmp(new Tmp_CB() {

				@Override
				public void onValue_UI(float f) {
					if (giveBack) {
						temperatureInterfac.onSuccess(f);
					}

				}

				@Override
				public void onFail_UI(BleException e) {
					if (giveBack)
						temperatureInterfac.onFail("ValueFail");
				}
			});
		}
	}

	@Override
	public boolean getFSLEnable() {
		return false;
	}

	// -----------------------------------------------------------------------------------------//
	@Override
	public void enableBluetooth() {
		Rat.getInstance().enableBluetooth();
	}

	@Override
	public void disableBluetooth() {
		Rat.getInstance().disableBluetooth();
	}

	@Override
	public void scanBluetooth() {
		stopScanBluetooth();
		getBluetoothDevice();
	}

	private void getBluetoothDevice() {
		if (scanCallback == null) {
			scanCallback = new Scan_CB() {

				@Override
				public void onLeScan(BluetoothDevice device) {
					if (!StringUtils.isEmpty(device.getName())) {
						EventBus.getDefault().post(device);
					}
				}
			};
		}
		try {
			Rat.getInstance().startScan(scanCallback);
		} catch (ScanException e) {
			Toast.makeText(mcontext, e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	@Override
	public void BluetoothState() {
	}

	@Override
	public void stopScanBluetooth() {
		scanCallback = null;
		Rat.getInstance().stopScan();
	}

	@Override
	public void setFSL(Double eiss) {

	}
}
