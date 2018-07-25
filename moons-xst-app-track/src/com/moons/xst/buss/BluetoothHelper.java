package com.moons.xst.buss;

import java.util.Hashtable;

import android.content.Context;

import com.moons.xst.track.AppConst.FromTypeForOuter;
import com.moons.xst.track.R;
import com.moons.xst.track.xstinterface.BluetoothBaseInterface;
import com.moons.xst.track.xstinterface.BluetoothTemperatureInterface;
import com.moons.xst.track.xstinterface.BluetoothVibrationInterface;
import com.moons.xst.track.xstinterface.Bluetooth_SU_100A_ForTemperature;
import com.moons.xst.track.xstinterface.Bluetooth_SU_100A_ForVibration;
import com.moons.xst.track.xstinterface.Bluetooth_TuoSheng_ForTemperature;
import com.moons.xst.track.xstinterface.Bluetooth_TuoSheng_ForVibration;
import com.moons.xst.track.xstinterface.ValueForBackInterface;

public class BluetoothHelper {
	private static BluetoothHelper _intance = null;
	private static Context mContext;
	private static String mtype;
	private static String mfrom;

	static BluetoothBaseInterface mInterface;

	private BluetoothHelper() {		
		if (mtype.equalsIgnoreCase(mContext
				.getString(R.string.outer_measuretypes_soeasytest))) {
			if (mfrom.equalsIgnoreCase(FromTypeForOuter.Temperature.toString()))
				mInterface = new Bluetooth_SU_100A_ForTemperature(mContext);
			else if (mfrom.equalsIgnoreCase(FromTypeForOuter.Vibration.toString()))
				mInterface = new Bluetooth_SU_100A_ForVibration(mContext);
		}else if(mtype.equalsIgnoreCase(mContext
				.getString(R.string.outer_measuretypes_tuosheng))){
			if (mfrom.equalsIgnoreCase(FromTypeForOuter.Temperature.toString()))
				mInterface = new Bluetooth_TuoSheng_ForTemperature(mContext);
			else if (mfrom.equalsIgnoreCase(FromTypeForOuter.Vibration.toString()))
				mInterface = new Bluetooth_TuoSheng_ForVibration(mContext);
		}
	}

	public static BluetoothHelper getIntance(Context context, String type,
			String from) {		
		mtype = type;
		mfrom = from;
		mContext = context;
		if (_intance == null) {
			_intance = new BluetoothHelper();
		}
		return _intance;
	}
	
	public void enableBluetooth(){
		mInterface.enableBluetooth();
	} 

	public void disableBluetooth() {
		mInterface.disableBluetooth();
	}

	public void scanBluetooth() {
		mInterface.scanBluetooth();
	}

	public void stopScanBluetooth() {
		mInterface.stopScanBluetooth();
	}

	// -----------------------------------------------------------------------------------------//
    // 获取温度
	public void getTemperature(boolean isConnect,ValueForBackInterface temperatureInterface) {
		((BluetoothTemperatureInterface) mInterface).getTemperature(mContext,
				isConnect,
				temperatureInterface);
	}

	// 停止获取温度
	public void stopTemperature() {
		((BluetoothTemperatureInterface) mInterface).stopGetTemperature();
	}
	
	// 发射率是否可以设置
	public boolean getFSLEnable() {
		return ((BluetoothTemperatureInterface) mInterface).getFSLEnable();
	} 
	// 发射率设置
	public void setFSL(Double eiss) {
		 ((BluetoothTemperatureInterface) mInterface).setFSL(eiss);
	} 

	// -----------------------------------------------------------------------------------------//
	// 获取震值
	public void getVibrationValue(boolean isConnect,String ZDType,ValueForBackInterface vibrationInterface) {
		((BluetoothVibrationInterface) mInterface).getVibrationValue( 
				isConnect, 
				ZDType, 
				vibrationInterface);
	}

	// 停止获取震值
	public void stopGetVibrationValue() {
		((BluetoothVibrationInterface) mInterface).stopGetVibrationValue();
	}
	
	// 获取振动分析数据长度
	public Hashtable<String, int[]> getVibrationValueLen() {
		
		return ((BluetoothVibrationInterface) mInterface).getVibrationValueLen();
	} 
	
	// -----------------------------------------------------------------------------------------//
	// 断开连接
	public void disConnBluetoothDevice(boolean isWifiEnable) {
		mInterface.disConnBluetoothDevice(isWifiEnable);
	}

	// 清理HELPER
	public void disposeHelper() {
		if (_intance != null) {
			_intance = null;
		}
	}
}
