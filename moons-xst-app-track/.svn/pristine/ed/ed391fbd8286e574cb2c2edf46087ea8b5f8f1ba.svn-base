package com.moons.xst.track.common;

import android.content.Context;

/**
 * 单位转换类
 * 
 * @author LKZ
 * 
 */
public class UnitHelper {

	/**
	 * Dip To Pixel
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static float dip2px(Context context, float dipValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (dipValue * m + 0.5f);
	}

	/**
	 * Pixel To Dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static float px2dip(Context context, float pxValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (pxValue / m + 0.5f);
	}

}
