package com.moons.xst.track.xstinterface;

import android.content.Context;

public interface BluetoothTemperatureInterface extends BluetoothBaseInterface{
	/**
	 * 获取测温值
	 */
	public void getTemperature(Context context, boolean connectYN, ValueForBackInterface valueForBackInterface);
	
	/**
	 * 停止获取温度
	 */
	public void stopGetTemperature();
	
	/**
	 * 发射率是否可以设置
	 * @return
	 */
	public boolean getFSLEnable();
	/**
	 * 发射率设置
	 */
	public void setFSL(Double eiss);
}
