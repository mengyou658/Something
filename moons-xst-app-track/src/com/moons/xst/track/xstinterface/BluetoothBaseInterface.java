package com.moons.xst.track.xstinterface;

import android.content.Context;

public interface BluetoothBaseInterface {
	/**
	 * 开启蓝牙
	 */
	public void enableBluetooth();
	/**
	 * 关闭蓝牙
	 */
	public void disableBluetooth();

	/**
	 * 扫描蓝牙
	 */
	public void scanBluetooth();

	/**
	 * 停止扫描
	 */
	public void stopScanBluetooth();

	/**
	 * 建立蓝牙连接
	 */
	public void connBuletoothDevice(ValueForBackInterface valueForBackInterface);

	/**
	 * 断开连接
	 */
	public void disConnBluetoothDevice(boolean isWifiEnable);
	/**
	 * 提示蓝牙是否开启
	 */
	public void BluetoothState();


}
