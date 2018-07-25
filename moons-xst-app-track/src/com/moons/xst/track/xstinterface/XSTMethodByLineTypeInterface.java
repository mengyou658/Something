package com.moons.xst.track.xstinterface;

import java.util.Date;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.track.bean.DJ_ResultActive;

import android.content.Context;

public interface XSTMethodByLineTypeInterface {
	
	/**
	 * 是否允许设置外围启停(碰钮扣界面)
	 * @param context
	 * @param finishYN
	 * @return
	 */
	public boolean showWWSRYN(Context context, boolean finishYN);
	/**
	 * 设置TRANSINFO信息(碰钮扣界面)
	 * @param context
	 * @return
	 */
	public String setTransInfo(String info);
	/**
	 * 获取已完成条数(做计划界面)
	 * @param context
	 * @param curID
	 * @return
	 */
	public int getCompleteNum(Context context, String curID);
	/**
	 * 获取提示字符串(做计划界面)
	 * @param context
	 * @param countStr
	 * @param noticeStr
	 * @param completeNum
	 * @param srNotNeedDoNum
	 * @param cycTotalCount
	 * @param yxCount
	 */
	public String getCountStr(Context context, int completeNum, int srNotNeedDoNum, int yxCount);
	public String getNoticeStr(Context context,String noticeStr, int cycTotalCount);
	/**
	 * 获取当前计划结果(做计划界面)
	 * @param context
	 * @param obj
	 * @return
	 */
	public DJ_ResultActive getCurResultActive(Context context, int lineID, DJPlan plan);
	/**
	 * 判断当前计划是否完成(做计划界面)
	 * @param context
	 * @param date
	 * @return
	 */
	public boolean judgePlanIsCompleted(Context context, DJPlan plan);
	/**
	 * 设置未检TEXT(做计划界面)
	 * @param str
	 */
	public String setRemainText(Context context, int yxplanCount, int completeNum, int srNotNeedDoNum);
	/**
	 * 设置TRANSINFO信息(做计划界面)
	 * @param context
	 * @return
	 */
	public String setResultTransInfo(String info);
	/**
	 * 计算班次值别(做计划界面)
	 * @param shiftName
	 * @param shiftGroupName
	 */
	public String getShiftName(Context context, DJPlan plan);
	public String getShiftGroupName(Context context, DJPlan plan);
	/**
	 * 设置主控点批量处理时钮扣计划完成数(做计划界面)
	 * @param context
	 * @param completeNum
	 * @param yxplanCount
	 * @return
	 */
	public String getCountStrForLK(Context context, int completeNum, int yxplanCount); 
	/**
	 * 计算查询日期
	 * @param plan
	 * @return
	 */
	public String getQueryDT(DJPlan plan);
	/**
	 * 设置TRANSINFO信息(启停设置界面)
	 * @param context
	 * @return
	 */
	public String setCPTransInfo(String info);
	/**
	 * 获取完成计划数(DJIDPosHelper.java)
	 * @param date
	 * @param idpos
	 * @return
	 */
	public int getCompleteNum(String idpos);
	/**
	 * 根据钮扣ID计算有效计划(DJIDPosHelper.java)
	 * @param date
	 * @param plan
	 * @return
	 */
	public boolean loadYXDJPlanByIDPos(Date date, DJPlan plan);
	/**
	 * 设置计划数(DJIDPosHelper.java)
	 * @param context
	 * @param completeCount
	 * @param srNotNeedDoNum
	 * @param canDoCount
	 * @return
	 */
	public String setPlanCount(Context context, int completeCount, int srNotNeedDoNum,int canDoCount);
	/**
	 * 判断是否设置过启停点(DJPlan.java)
	 * @param cycle
	 * @param str
	 * @return
	 */
	public boolean srIsSetting(Cycle cycle, String str);
}