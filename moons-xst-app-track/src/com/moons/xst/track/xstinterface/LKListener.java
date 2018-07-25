/**
 * 
 */
package com.moons.xst.track.xstinterface;

import java.util.List;

import android.content.DialogInterface;

import com.moons.xst.buss.DJPlan;

/**
 * 主控点回调接口
 * 
 * @author LKZ
 * 
 */
public interface LKListener {
	/**
	 * 回调函数，刷新父UI
	 */
	public void refreshParentUI();

	/**
	 * 回调函数，返回父UI
	 */
	public void goBackToParentUI();

	/**
	 * 回调函数，确定返回
	 * 
	 * @param mDJPlan
	 */
	public void btnOK(DialogInterface dialog, List<DJPlan> mDJPlan);
	
	/**
	 * 回调函数，返回
	 * 
	 * @param mDJPlan
	 */
	public void btnCancel(DialogInterface dialog, List<DJPlan> mDJPlan);
}
