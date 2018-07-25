package com.moons.xst.track.xstinterface;

import java.util.Hashtable;

public interface BluetoothVibrationInterface extends BluetoothBaseInterface{

	/**
	 * 获取测震值
	 */
	public void getVibrationValue(boolean connectYN,
			String ZDType,ValueForBackInterface valueForBackInterface);
	/**
	 * 停止获取震值
	 */
	public void stopGetVibrationValue();
	
	/**
	 * 获取振动分析数据长度
	 * @return
	 */
	public Hashtable<String, int[]> getVibrationValueLen();
}
