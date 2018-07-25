package com.moons.xst.track.xstinterface;

import java.util.Date;

import android.content.Context;

import com.moons.xst.buss.DJIDPosHelper;
import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.DJResultHelper;
import com.moons.xst.buss.DJShiftHelper;
import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.UIHelper;

public class XSTMethodByLineType_DJPC implements XSTMethodByLineTypeInterface {
	
	@Override
	public boolean showWWSRYN(Context context, boolean finishYN) {
		if (finishYN) {
			return true;
		}
		else {
			UIHelper.ToastMessage(context,
					R.string.djidpos_message_idposloding);
			return false;
		}
	}
	
	@Override
	public String setTransInfo(String info) {
		return info;
	}
	
	@Override
	public int getCompleteNum(Context context, String curID) {
		return DJIDPosHelper.GetCompleteNum(curID);
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
				plan.getDJPlan().getDisStart_TM(),
				plan.getDJPlan().getDisEnd_TM());
	}
	
	@Override
	public boolean judgePlanIsCompleted(Context context, DJPlan plan) {
		return plan.JudgePlanIsCompletedForDJPC();
	}
	
	@Override
	public String setRemainText(Context context, int yxplanCount, int completeNum, int srNotNeedDoNum) {
		return context.getString(R.string.plan_undo)
				+ String.valueOf((yxplanCount
						- completeNum - srNotNeedDoNum));
	}
	
	@Override
	public String setResultTransInfo(String info) {
		return info;
	}
	
	@Override
	public String getShiftName(Context context, DJPlan plan) {
		return DJShiftHelper.GetIntance().getShiftNameByCD(context,
				DJShiftHelper.GetIntance().getShiftCDByTime(context,
				DateTimeHelper.GetDateTimeNow()));
	}
	
	@Override
	public String getShiftGroupName(Context context, DJPlan plan) {
		Date date = DateTimeHelper.GetDateTimeNow();
		String shiftCD = DJShiftHelper.GetIntance().getShiftCDByTime(context,
				DateTimeHelper.GetDateTimeNow());
		return  DJShiftHelper.GetIntance()
				.computeNowShiftGroupName(context,
						date,
						shiftCD);
	}
	
	@Override
	public String getCountStrForLK(Context context, int completeNum, int yxplanCount) {
		return completeNum + "/" + yxplanCount;
	}
	
	@Override
	public String getQueryDT(DJPlan plan) {
		return DateTimeHelper.getDateTimeNow();
	}
	
	@Override
	public String setCPTransInfo(String info) {
		return info;
	}
	
	@Override
	public int getCompleteNum(String idpos) {
		Integer completeCount = 0;
		for (DJPlan _planInfo : AppContext.YXDJPlanByIDPosBuffer.get(idpos)) {

			if (_planInfo.JudgePlanIsCompletedForDJPC()
					&& _planInfo.JudgePlanBySrPoint())
				completeCount = completeCount + 1;
		}
		return completeCount;
	}
	
	@Override
	public boolean loadYXDJPlanByIDPos(Date date, DJPlan plan) {
		if (plan.JudgePlanCycleForDJPC(date)) {
			plan.getDJPlan()
			   .setLastResultForDifferent_TX(plan.getDJPlan().getLastResult_TX());
			return true;
		}
		return false;
	}
	
	@Override
	public String setPlanCount(Context context, int completeCount, int srNotNeedDoNum,int canDoCount) {
		String result = "";
		result = (completeCount + srNotNeedDoNum) + "/" + canDoCount;	
		return result;
	}
	
	@Override
	public boolean srIsSetting(Cycle cycle, String str) {
		return true;
	}
}