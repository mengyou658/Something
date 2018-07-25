/**
 * 
 */
package com.moons.xst.track.common;

import com.moons.xst.track.AppConst;

/**
 * Math计算辅助类
 * 
 * @author LKZ
 * 
 */
public class MathHelper {

	/**
	 * 将一个整数转化为启停点字符串
	 * 
	 * @param srInt
	 * @return
	 */
	public static String SRTransToString(int srInt) {
		int max = (int) (Math.pow(2, AppConst.SR_STRINGLENGTH) - 1);
		if (srInt > max || srInt < 0) {
			return "";
		}
		String tmpStr = Integer.toString(srInt, 2);
		while (tmpStr.length() < AppConst.SR_STRINGLENGTH) {
			tmpStr = "0" + tmpStr;
		}
		tmpStr = tmpStr.replace('0', '_');
		tmpStr = tmpStr.replace('1', '0');
		return tmpStr;
	}

	/**
	 * 将一个启停点字符串转化为整数
	 * 
	 * @param srInt
	 * @return
	 */
	public static Integer SRTransToInt(String srStr) {

		if (StringUtils.isEmpty(srStr))
			return -1;
		String tmpStr = srStr;
		tmpStr = tmpStr.replace('0', '1');
		tmpStr = tmpStr.replace('_', '0');
		return AppConst.SR_STRINGLENGTH - tmpStr.indexOf("1") - 1;
	}
}
