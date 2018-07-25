package com.moons.xst.buss;

import java.util.Date;

import android.content.Context;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.xstinterface.XSTMethodByLineTypeInterface;
import com.moons.xst.track.xstinterface.XSTMethodByLineType_CASEXJ;
import com.moons.xst.track.xstinterface.XSTMethodByLineType_DJPC;
import com.moons.xst.track.xstinterface.XSTMethodByLineType_XDJ;

public class XSTMethodByLineTypeHelper {
	private static XSTMethodByLineTypeHelper _instance = null;
	
	static XSTMethodByLineTypeInterface mInterface;

	private XSTMethodByLineTypeHelper() {		
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			mInterface = new XSTMethodByLineType_XDJ();
		} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
			mInterface = new XSTMethodByLineType_CASEXJ();
		} else if (AppContext.getCurrLineType() == AppConst.LineType.DJPC.getLineType()) {
			mInterface = new XSTMethodByLineType_DJPC();
		}
	}
	
	public static XSTMethodByLineTypeHelper getInstance() {		
		if (_instance == null) {
			_instance = new XSTMethodByLineTypeHelper();
		}
		return _instance;
	}
	
	public boolean showWWSRYN(Context context, boolean finishYN) {
		return mInterface.showWWSRYN(context, finishYN);
	}
	
	public String setTransInfo(String info) {
		return mInterface.setTransInfo(info);
	}
	
	public int getCompleteNum(Context context, String curID) {
		return mInterface.getCompleteNum(context, curID);
	}
	
	public String getCountStr(Context context, int completeNum, int srNotNeedDoNum, int yxCount) {
		return mInterface.getCountStr(context, completeNum, srNotNeedDoNum, yxCount);
	}
	
	public String getNoticeStr(Context context, String noticeStr, int cycTotalCount) {
		return mInterface.getNoticeStr(context, noticeStr, cycTotalCount);
	}
	
	public DJ_ResultActive getCurResultActive(Context context, int lineID, DJPlan plan) {
		return mInterface.getCurResultActive(context, lineID, plan);
	}
	
	public boolean judgePlanIsCompleted(Context context, DJPlan plan) {
		return mInterface.judgePlanIsCompleted(context, plan);
	}
	
	public String setRemainText(Context context, int yxplanCount, int completeNum, int srNotNeedDoNum) {
		return mInterface.setRemainText(context, yxplanCount, completeNum, srNotNeedDoNum);
	}
	
	public String setResultTransInfo(String info) {
		return mInterface.setResultTransInfo(info);
	}
	
	public String getShiftName(Context context, DJPlan plan) {
		return mInterface.getShiftName(context, plan);
	}
	
	public String getShiftGroupName(Context context, DJPlan plan) {
		return mInterface.getShiftGroupName(context, plan);
	}
	
	public String getCountStrForLK(Context context, int completeNum, int yxplanCount) {
		return mInterface.getCountStrForLK(context, completeNum, yxplanCount);
	}
	
	public String getQueryDT(DJPlan plan) {
		return mInterface.getQueryDT(plan);
	}
	
	public String setCPTransInfo(String info) {
		return mInterface.setCPTransInfo(info);
	}
	
	public int getCompleteNum(String idpos) {
		return mInterface.getCompleteNum(idpos);
	}
	
	public boolean loadYXDJPlanByIDPos(Date date, DJPlan plan) {
		return mInterface.loadYXDJPlanByIDPos(date, plan);
	}
	
	public String setPlanCount(Context context, int completeCount, int srNotNeedDoNum,int canDoCount) {
		return mInterface.setPlanCount(context, completeCount, srNotNeedDoNum, canDoCount);
	}
	
	public boolean srIsSetting(Cycle cycle, String str) {
		return mInterface.srIsSetting(cycle, str);
	}
	
	// 清理HELPER
	public void disposeHelper() {
		if (_instance != null) {
			if (mInterface != null)
				mInterface = null;
			_instance = null;
		}
	}
}