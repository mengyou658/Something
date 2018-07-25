package com.moons.xst.track.xstinterface;

import java.util.Date;

import android.content.Context;

import com.moons.xst.buss.DJIDPosHelper;
import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.DJResultHelper;
import com.moons.xst.buss.DJShiftHelper;
import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.ui.NewDianjian_Main;

public class XSTMethodByLineType_CASEXJ implements XSTMethodByLineTypeInterface {
	
	@Override
	public boolean showWWSRYN(Context context, boolean finishYN) {
		UIHelper.ToastMessage(context,
				R.string.djidpos_message_cannotsetsrforcasexj);
		return false;
	}
	
	@Override
	public String setTransInfo(String info) {
		if (!StringUtils.isEmpty(AppConst.PlanTimeIDStr)) {
			return info + "|" + AppConst.PlanTimeIDStr;
		} else
			return info;
	}
	
	@Override
	public int getCompleteNum(Context context, String curID) {
		return DJIDPosHelper.GetCompleteNum(context, curID);
	}
	
	@Override
	public String getCountStr(Context context, int completeNum, int srNotNeedDoNum, int yxCount) {
		return (completeNum + srNotNeedDoNum) + "/" + yxCount;
	}
	@Override
	public String getNoticeStr(Context context, String noticeStr, int cycTotalCount) {
		return noticeStr;
	}
	
	@Override
	public DJ_ResultActive getCurResultActive(Context context, int lineID, DJPlan plan) {
		return DJResultHelper.GetIntance().getOneDJResult(
				context,
				lineID,
				plan.getDJPlan().getDJ_Plan_ID(),
				AppConst.PlanTimeIDStr);
	}
	
	@Override
	public boolean judgePlanIsCompleted(Context context, DJPlan plan) {
		return plan.JudgePlanIsCompletedForCASEXJ(context);
	}
	
	@Override
	public String setRemainText(Context context, int yxplanCount, int completeNum, int srNotNeedDoNum) {
		return "";
	}
	
	@Override
	public String setResultTransInfo(String info) {
		if (!StringUtils.isEmpty(AppConst.PlanTimeIDStr)) {
			return info + "|" + AppConst.PlanTimeIDStr;
		} else 
			return info;
	}
	
	@Override
	public String getShiftName(Context context, DJPlan plan) {
		return "";
	}
	
	@Override
	public String getShiftGroupName(Context context, DJPlan plan) {
		return "";
	}
	
	@Override
	public String getCountStrForLK(Context context, int completeNum, int yxplanCount) {
		return context.getString(R.string.plan_casexjcount,
				yxplanCount);
	}
	
	@Override
	public String getQueryDT(DJPlan plan) {
		return DateTimeHelper.getDateTimeNow();
	}
	
	@Override
	public String setCPTransInfo(String info) {
		if (!StringUtils.isEmpty(AppConst.PlanTimeIDStr)) {
			return info + "|" + AppConst.PlanTimeIDStr;
		}
		else
			return info;
	}
	
	@Override
	public int getCompleteNum(String idpos) {
		return 0;
	}
	
	@Override
	public boolean loadYXDJPlanByIDPos(Date date, DJPlan plan) {
		return true;
	}
	
	@Override
	public String setPlanCount(Context context, int completeCount, int srNotNeedDoNum,int canDoCount) {
		String result = completeCount + "/" + canDoCount;		
		return result;
	}
	
	@Override
	public boolean srIsSetting(Cycle cycle, String str) {
		return true;
	}
}