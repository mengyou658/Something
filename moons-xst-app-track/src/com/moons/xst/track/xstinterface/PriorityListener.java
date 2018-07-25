/**
 * 
 */
package com.moons.xst.track.xstinterface;

/**
 * 启停点回调接口
 * 
 * @author LKZ
 * 
 */
public interface PriorityListener {
	/**
	 * 回调函数，更改设备状态后通知刷新DianJianMain的UI显示
	 */
	public void refreshPriorityUI(boolean autoYn);

	/**
	 * 回调函数，设置完启停后退出启停界面时，DianjianMain界面回调的方法
	 */
	public void comeBackToDianjianMain(boolean autoYn);
}
