package com.moons.xst.track.xstinterface;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.string;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.common.StringUtils;
import com.tocel.vibrationlib.interfaces.MBP_CB;
import com.tocel.vibrationlib.interfaces.Rat;
import com.tocel.vibrationlib.interfaces.Recipe;
import com.tocel.vibrationlib.interfaces.Scan_CB;
import com.tocel.vibrationlib.interfaces.SucFail;

import de.greenrobot.event.EventBus;

public class Bluetooth_TuoSheng_ForVibration implements
		BluetoothVibrationInterface {

	public class SignalType {
		public static final byte ACCELERATION = (byte) 0x00;// 加速度
		public static final byte VELOCITY = (byte) 0x01;// 速度
		public static final byte DISPLACEMENT = (byte) 0x02;// 位移
	}

	private Context context;
	private ValueForBackInterface valueForBackInterface;
	private byte mZDType = SignalType.ACCELERATION;
	private String ZDType = "";
	private boolean needVibration = false;
	private boolean giveBack = false;
	private Handler handler;
	private Scan_CB scan_CB;
	private SucFail sucFail;
	private MBP_CB mbp_CB;
	private int wifiState;
	private WifiManager wm;
	private ExecutorService executor;

	public Bluetooth_TuoSheng_ForVibration(Context mContext) {
		context = mContext;
		enableBluetooth();
	}

	private void wifi_close() {
		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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
		if (sucFail == null) {
			sucFail = new SucFail() {

				@Override
				public void onFailed_UI(String msg) {
					valueForBackInterface.onFail("ConnFail");
				}

				@Override
				public void onSucceed_UI(BluetoothDevice device, String msg) {
					getVibrationValueAfterConn();
				}

			};
		}
		Rat.getInstance().connectDevice_Normal(
				AppContext.getBlueToothAddressforVibration(), sucFail);
	}

	@Override
	public void disConnBluetoothDevice(boolean isWifiEnable) {
		if (isWifiEnable) {
			checkWifi();
		}
		stopGetVibrationValue();
		if (null != executor) {
			executor.shutdownNow();// 立即停止线程，终止之前正在执行的任务
		}
		Rat.getInstance().disConnectDevice();
	}

	@Override
	public void BluetoothState() {

	}

	@Override
	public void getVibrationValue(boolean connectYN, String ZDType,
			ValueForBackInterface valueForBackInterface) {
		this.valueForBackInterface = valueForBackInterface;

		if (!connectYN) {
			this.ZDType = ZDType;
			wifi_close();
			connBuletoothDevice(valueForBackInterface);
		} else {
			getVibrationValueAfterConn();
		}
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (needVibration) {
				refreshVibrationValues(valueForBackInterface);
			}
		}

	};

	private void checkWifi() {
		if (WifiManager.WIFI_STATE_ENABLED == wifiState
				|| WifiManager.WIFI_STATE_ENABLING == wifiState) {
			if (wm != null)
				wm.setWifiEnabled(true);
		}
	}

	public void getVibrationValueAfterConn() {
		executor = Executors.newSingleThreadExecutor();
		if (ZDType.equalsIgnoreCase("A")) {
			mZDType = SignalType.ACCELERATION;
		} else if (ZDType.equalsIgnoreCase("S")) {
			mZDType = SignalType.DISPLACEMENT;
		} else if (ZDType.equalsIgnoreCase("V")) {
			mZDType = SignalType.VELOCITY;
		}
		needVibration = true;
		giveBack = true;
		if (handler == null) {
			handler = new Handler();
		}
		if (runnable != null) {
			handler.removeCallbacks(runnable);
		}
		handler.postDelayed(runnable, 100);

	}

	// Runnable repateRunnable = new Runnable() {
	//
	// @Override
	// public void run() {
	// if (needVibration) {
	// Recipe.getInstance().getBulk_MbpByRaw(mZDType, mbp_CB);
	// }
	// }
	// };
	Handler valueHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case ONFAIL:
				valueForBackInterface.onFail("ValueFailForMore");
				break;
			case ONCOMPLETED:
				if (runnable != null) {
					handler.removeCallbacks(runnable);
				}
				executor.execute(new Runnable() {

					@Override
					public void run() {
						if (needVibration) {
							Recipe.getInstance().getBulk_MbpByRaw(mZDType,
									mbp_CB);
						}
					}
				});
				break;
			case ONBOXING:
				switch (mZDType) {
				case SignalType.ACCELERATION:
					valueForBackInterface.onVibrationBXVauesSuccess(Arrays
							.copyOfRange((float[]) msg.obj, 0, 6399));
					break;
				case SignalType.DISPLACEMENT:
					valueForBackInterface.onVibrationBXVauesSuccess(Arrays
							.copyOfRange((float[]) msg.obj, 0, 1279));
					break;
				case SignalType.VELOCITY:
					valueForBackInterface.onVibrationBXVauesSuccess(Arrays
							.copyOfRange((float[]) msg.obj, 0, 1279));
					break;
				}
				break;
			case ONPINGPU:
				switch (mZDType) {
				case SignalType.ACCELERATION:
					valueForBackInterface.onVibrationPPVauesSuccess(Arrays
							.copyOfRange((float[]) msg.obj, 0, 2500));
					break;
				case SignalType.DISPLACEMENT:
					valueForBackInterface.onVibrationPPVauesSuccess(Arrays
							.copyOfRange((float[]) msg.obj, 0, 500));
					break;
				case SignalType.VELOCITY:
					valueForBackInterface.onVibrationPPVauesSuccess(Arrays
							.copyOfRange((float[]) msg.obj, 0, 500));
					break;
				}
				break;
			case ONVALUE:
				valueForBackInterface.onSuccess((Float) msg.obj);
				break;
			default:
				break;
			}
		};
	};
	private static final int ONFAIL = 1;
	private static final int ONCOMPLETED = 2;
	private static final int ONBOXING = 3;
	private static final int ONPINGPU = 4;
	private static final int ONVALUE = 5;

	public void  refreshVibrationValues(
			final ValueForBackInterface valueForBackInterface) {
		if (mbp_CB == null) {
			mbp_CB = new MBP_CB() {

				@Override
				public void onFail_UI(String msg) {
					valueHandler.obtainMessage(ONFAIL).sendToTarget();
				}

				@Override
				public void onCompleted_UI() {
					if (giveBack) {
						valueHandler.obtainMessage(ONCOMPLETED).sendToTarget();
					}
				}

				@Override
				public void onBoxing_UI(float[] boxing) {
					if (giveBack) {
						valueHandler.obtainMessage(ONBOXING, boxing)
								.sendToTarget();
					}
				};

				@Override
				public void onPinpu_UI(float[] pinpu) {
					if (giveBack) {
						valueHandler.obtainMessage(ONPINGPU, pinpu)
								.sendToTarget();
					}
				}

				@Override
				public void onValue_UI(float acc, float v, float s) {

					if (giveBack) {
						switch (mZDType) {
						case SignalType.ACCELERATION:
							valueHandler.obtainMessage(ONVALUE, acc)
									.sendToTarget();
							break;
						case SignalType.DISPLACEMENT:
							// um-->mm
							valueHandler.obtainMessage(ONVALUE, s / 1000)
									.sendToTarget();
							break;
						case SignalType.VELOCITY:
							valueHandler.obtainMessage(ONVALUE, v)
									.sendToTarget();
							break;
						}
					}
				}
			};
		}
		Recipe.getInstance().getBulk_MbpByRaw(mZDType, mbp_CB);
	}

	@Override
	public void stopGetVibrationValue() {
		needVibration = false;
		giveBack = false;
		if (handler == null) {
			handler = new Handler();
		}
		handler.removeCallbacks(runnable);
	}

	@Override
	public Hashtable<String, int[]> getVibrationValueLen() {
		Hashtable<String, int[]> htHashtable = new Hashtable<String, int[]>();
		htHashtable.put("A", new int[] { (int) (2500 * 2.56), 2500 + 1, 5000 });
		htHashtable.put("S", new int[] { (int) (500 * 2.56), 500 + 1, 1000 });
		htHashtable.put("V", new int[] { (int) (500 * 2.56), 500 + 1, 1000 });
		return htHashtable;
	}

}
