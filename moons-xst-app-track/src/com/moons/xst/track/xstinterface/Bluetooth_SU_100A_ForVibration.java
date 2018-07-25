package com.moons.xst.track.xstinterface;

import java.util.Hashtable;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.sytest.app.blemulti.interfaces.MBP_CB;
import com.sytest.app.blemulti.interfaces.Scan_CB;
import com.sytest.app.blemulti.interfaces.SucFail;
import com.sytest.app.blemulti.interfaces.Value_CB;

import de.greenrobot.event.EventBus;

public class Bluetooth_SU_100A_ForVibration implements
		BluetoothVibrationInterface {
	private static final long SCANTIMEOUT = 120000;
	private static final int SCANSUCCESS=1;
	private static final int SCANFAIL=2;
	public static final class SignalType {
		// 加速度
		public static final byte ACCELERATION = (byte) 0x00;
		// 速度
		public static final byte VELOCITY = (byte) 0x01;
		// 位移
		public static final byte DISPLACEMENT = (byte) 0x02;
	}
	private Scan_CB scanCallback;
	private BleDevice bleDevice = null;
	private Handler handler=new Handler();
	private ValueForBackInterface vibrationInterface;
	private static Context mcontext;
	private boolean giveBack = true;
	private boolean needVibration = true;
	private byte mZDType = SignalType.ACCELERATION;
	private String ZDType = "";
	private boolean initilizedYN = false;
	private boolean isToConn = true;// 是否启动连接
	private boolean isError;// jar包内部错误，无回调
	private float VibrationValue = 0.0f;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANSUCCESS:
				connBuletoothDevice(vibrationInterface);
				break;
			case SCANFAIL:
				vibrationInterface.onFail("ConnFail");
				Toast.makeText(mcontext,mcontext.getString(R.string.msg_bluetooth_device_restart_tip), Toast.LENGTH_SHORT).show();
				break;
			}

		};
	};

	public Bluetooth_SU_100A_ForVibration(Context context) {
		mcontext = context;
		Rat.initilize(mcontext);
		initilizedYN = true;
	}

	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			if (isToConn) {
				stopScanBluetooth();
				mHandler.obtainMessage(SCANFAIL).sendToTarget();
			}
		}
	};
	Runnable connRunnable = new Runnable() {

		@Override
		public void run() {
			if(isError)
			vibrationInterface.onFail("ConnFail");
		}
	};
	@Override
	public void connBuletoothDevice(
			final ValueForBackInterface vibrationInterface) {
		isError=true;
		SucFail cb = new SucFail() {
			@Override
			public void onSucceed_UI(@Nullable String msg) {
				isError=false;
				handler.removeCallbacks(connRunnable);
				bleDevice = Rat.getInstance().getFirstBleDevice();
				getVibrationValueAfterConn();
			}

			@Override
			public void onFailed_UI(@Nullable String msg) {
				isError=false;
				handler.removeCallbacks(connRunnable);
				vibrationInterface.onFail("ConnFail");
			}
		};
		handler.postDelayed(connRunnable, SCANTIMEOUT);
		Rat.getInstance().connectDevice_Normal(false,
				AppContext.getBlueToothAddressforVibration(), cb);
	}

	private void ScanBefor() {
		if (StringUtils.isEmpty(AppContext.getBlueToothAddressforVibration())) {
			vibrationInterface.onFail("ConnFail");
			return;
		}
		mHandler.postDelayed(mRunnable, SCANTIMEOUT);
		stopScanBluetooth();
		if (scanCallback == null) {
			scanCallback = new Scan_CB() {

				@Override
				public void onLeScan(BluetoothDevice device) {
					if (!StringUtils.isEmpty(device.getName())
							&& AppContext.getBlueToothAddressforVibration()
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

	@Override
	public void disConnBluetoothDevice(boolean isWifiEnable) {
		if (!StringUtils.isEmpty(AppContext.getBlueToothAddressforVibration())) {
			if (initilizedYN) {
				stopGetVibrationValue();
				Rat.getInstance().disConnectDevice(
						AppContext.getBlueToothAddressforVibration());
			}
		}

	}

	// -----------------------------------------------------------------------------------------//
	Runnable vibrationRunnable = new Runnable() {

		@Override
		public void run() {
			if (needVibration) {
				refreshVibration(vibrationInterface);
				refreshVibrationValues(vibrationInterface);
				refreshBattery(vibrationInterface);
				handler.postDelayed(this, 1000);
			}
		}
	};
//	Runnable BatteryRunnable = new Runnable() {
//
//		@Override
//		public void run() {
//			if (needVibration) {
//				refreshBattery(vibrationInterface);
//				handler.postDelayed(this, 100);
//			}
//		}
//	};

	public void refreshVibration(final ValueForBackInterface vibrationInterface) {
		if (bleDevice != null) {
			Recipe.newInstance(bleDevice).getValue_Z(mZDType, false,
					new Value_CB() {
						@Override
						public void onValue_UI(float x, float y, float z) {
							// 单向小蘑菇 su100a，获得的是z向的数据 processValue(z);
							if (giveBack) {
								VibrationValue = z;
								
//								refreshBattery(vibrationInterface);
							}
						}

						@Override
						public void onFail_UI(@NonNull BleException e) {
							if (giveBack)
								vibrationInterface.onFail("ValueFail");
						}
					});
		}
	}

	@Override
	public void getVibrationValue(boolean connectYN, String ZDType,
			final ValueForBackInterface vibrationInterface) {
		this.vibrationInterface = vibrationInterface;
		isToConn = true;
		if (!connectYN) {
			this.ZDType = ZDType;
			ScanBefor();
		} else {
			getVibrationValueAfterConn();
		}
	}

	public void getVibrationValueAfterConn() {
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
//		if(vibrationRunnable!=null){
//			handler.removeCallbacks(vibrationRunnable);
//		}
		handler.postDelayed(vibrationRunnable, 100);
	}

	public void stopGetVibrationValue() {
		if (handler == null) {
			handler = new Handler();
		}
		if (mHandler == null) {
			mHandler = new Handler();
		}
		needVibration = false;
		giveBack = false;
		handler.removeCallbacks(vibrationRunnable);
//		handler.removeCallbacks(BatteryRunnable);
		handler.removeCallbacks(connRunnable);
		mHandler.removeCallbacks(mRunnable);
	}

	// -----------------------------------------------------------------------------------------//
	public void refreshVibrationValues(
			final ValueForBackInterface vibrationInterface) {
		if (bleDevice != null) {
			Recipe.newInstance(bleDevice).getBulk_MbpByRaw(mZDType, false,
					false, true, new MBP_CB() {
						@Override
						public void onBoxing_UI(float[] boxing, XYZ xyz) {
							// 接收到波形数据
							if (giveBack) {
								vibrationInterface
										.onVibrationBXVauesSuccess(boxing);
							}
						}

						@Override
						public void onPinpu_UI(float[] pinpu, XYZ xyz) {
							// 接收到频谱数据
							if (giveBack) {
								vibrationInterface
										.onVibrationPPVauesSuccess(pinpu);
							}
						}

						@Override
						public void onPinpu_Complex_UI(byte[] bs, XYZ xyz) {
							// 接收到频谱复数
							// vibrationValuesInterface.onVibrationVauesSuccess(bs);
						}

						@Override
						public void onMathValue_UI(float mathValue, XYZ xyz) {
							// 接收到一个测量值
							// vibrationValuesInterface.onVibrationVauesSuccess(boxing);
						}

						@Override
						public void onCompleted_UI() {
//							handler.post(BatteryRunnable);
							// 结束
							if (SignalType.DISPLACEMENT == mZDType) {
								vibrationInterface
										.onSuccess(VibrationValue / 1000);
							} else {
								vibrationInterface.onSuccess(VibrationValue);
							}
						}

						@Override
						public void onFail_UI(@NonNull BleException e) {
							String msg = e == null ? "" : e.getMessage();
							if (giveBack)
								vibrationInterface.onFail("ValueFail");
						}
					});
		} else {
		}
	}

	@Override
	public Hashtable<String, int[]> getVibrationValueLen() {
		Hashtable<String, int[]> htHashtable = new Hashtable<String, int[]>();
		htHashtable.put("A", new int[] { (int) (800 * 2.56), 800 + 1, 5000 });
		htHashtable.put("S", new int[] { (int) (400 * 2.56), 400 + 1, 1000 });
		htHashtable.put("V", new int[] { (int) (400 * 2.56), 400 + 1, 1000 });
		return htHashtable;
	}

	// -----------------------------------------------------------------------------------------//
	@Override
	public void enableBluetooth() {
		Rat.getInstance().enableBluetooth();
	}

	@Override
	public void BluetoothState() {
	}

	@Override
	public void disableBluetooth() {

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
					if (!TextUtils.isEmpty(device.getName())) {
						EventBus.getDefault().post(device);
					}
				}
			};
		}
		try {
			Rat.getInstance().startScan(scanCallback);
		} catch (ScanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopScanBluetooth() {
		scanCallback = null;
		Rat.getInstance().stopScan();
	}

	private com.moons.xst.track.widget.AlertDialog dialog;

	private void refreshBattery(final ValueForBackInterface vibrationInterface) {
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

//						if (dialog != null) {
//							return;
//						}
						if (mcontext == null)
							return;
						
						if (dialog == null) {
							LayoutInflater factory = LayoutInflater.from(mcontext);
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
						}else{
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
}
