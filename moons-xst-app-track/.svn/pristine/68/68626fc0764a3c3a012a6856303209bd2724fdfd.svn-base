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
import com.moons.xst.track.ui.NewDianjian_Main;

public class XSTMethodByLineType_XDJ implements XSTMethodByLineTypeInterface {
	
	@Override
	public boolean showWWSRYN(Context context, boolean finishYN) {
		if (AppContext.DJSpecCaseFlag == 1) {
			UIHelper.ToastMessage(context,
					R.string.djidpos_message_cannotsetsrforspeccase);
			return false;
		} else {
			if (finishYN) {
				return true;
			}
			else {
				UIHelper.ToastMessage(context,
						R.string.djidpos_message_idposloding);
				return false;
			}
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
		return AppContext.DJSpecCaseFlag == 1 ? context.getString(
				R.string.plan_speccasecount,
				String.valueOf(yxCount))
				: ((completeNum + srNotNeedDoNum) + "/" + yxCount);
	}
	@Override
	public String getNoticeStr(Context context, String noticeStr, int cycTotalCount) {
		noticeStr = AppContext.DJSpecCaseFlag == 1 ? context.getString(
				R.string.plan_speccase_statistic_notice,
				String.valueOf(cycTotalCount)) : noticeStr;
		return noticeStr;
	}
	
	@Override
	public DJ_ResultActive getCurResultActive(Context context, int lineID, DJPlan plan) {
		return DJResultHelper.GetIntance().getOneDJResult(
				context,
				lineID,
				plan.getDJPlan().getDJ_Plan_ID(),
				DateTimeHelper.DateToString(plan.GetCycle()
						.getTaskBegin()),
				DateTimeHelper.DateToString(plan.GetCycle()
						.getTaskEnd()));
	}
	
	@Override
	public boolean judgePlanIsCompleted(Context context, DJPlan plan) {
		return plan.JudgePlanIsCompleted(DateTimeHelper.GetDateTimeNow());
	}
	
	@Override
	public String setRemainText(Context context, int yxplanCount, int completeNum, int srNotNeedDoNum) {
		String result = "";
		if (AppContext.DJSpecCaseFlag == 1) {
		} else {
			result = context.getString(R.string.plan_undo)
					+ String.valueOf((yxplanCount
							- completeNum - srNotNeedDoNum));
		}
		return result;
	}
	
	@Override
	public String setResultTransInfo(String info) {
		return info;
	}
	
	@Override
	public String getShiftName(Context context, DJPlan plan) {
		if (plan != null) {
			return DJShiftHelper.GetIntance().getShiftNameByCD(context,
					plan.GetCycle().getCurShiftNo());
		} else {
			return DJShiftHelper.GetIntance().getShiftNameByCD(context,
					DJShiftHelper.GetIntance().getShiftCDByTime(context,
					DateTimeHelper.GetDateTimeNow()));
		}
	}
	
	@Override
	public String getShiftGroupName(Context context, DJPlan plan) {
		Date date = DateTimeHelper.GetDateTimeNow();
		if (plan != null) {
			return  DJShiftHelper.GetIntance()
					.computeNowShiftGroupName(context,
							date,
							plan.GetCycle().getCurShiftNo());
		} else {
			String shiftCD = DJShiftHelper.GetIntance().getShiftCDByTime(context,
					DateTimeHelper.GetDateTimeNow());
			return  DJShiftHelper.GetIntance()
					.computeNowShiftGroupName(context,
							date,
							shiftCD);
		}
	}
	
	@Override
	public String getCountStrForLK(Context context, int completeNum, int yxplanCount) {
		return AppContext.DJSpecCaseFlag == 1 ? context.getString(
				R.string.plan_speccasecount,
				String.valueOf(yxplanCount)) : completeNum
				+ "/" + yxplanCount;
	}
	
	@Override
	public String getQueryDT(DJPlan plan) {
		if (AppContext.DJSpecCaseFlag == 1) {
			return DateTimeHelper.getDateTimeNow();
		} else {
			return DateTimeHelper
					.DateToString(plan
							.GetQueryDate(DateTimeHelper
									.GetDateTimeNow()));
		}
	}
	
	@Override
	public String setCPTransInfo(String info) {
		return info;
	}
	
	@Override
	public int getCompleteNum(String idpos) {
		if (AppContext.DJSpecCaseFlag == 1)
			return 0;
		else {
			Integer completeCount = 0;
			Date dt = DateTimeHelper.StringToDate(DateTimeHelper
					.getDateTimeNow());
			for (DJPlan _planInfo : AppContext.YXDJPlanByIDPosBuffer.get(idpos)) {

				if (_planInfo.JudgePlanIsCompleted(dt)
						&& _planInfo.JudgePlanBySrPoint())
					completeCount = completeCount + 1;
			}
			return completeCount;
		}
	}
	
	@Override
	public boolean loadYXDJPlanByIDPos(Date date, DJPlan plan) {
		if (AppContext.DJSpecCaseFlag == 1) {
			if (plan
					.JudgePlanSpecCase(AppContext.selectedDJSpecCaseStr)) {
				plan.getDJPlan()
				   .setLastResultForDifferent_TX(plan.getDJPlan().getLastResult_TX());
				return true;
			}
		} else {
			if (plan.JudgePlanCycle(date)) {
				if (!plan.JudgePlanDisable()) {
					plan.getDJPlan()
					   .setLastResultForDifferent_TX(plan.getDJPlan().getLastResult_TX());
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public String setPlanCount(Context context, int completeCount, int srNotNeedDoNum,int canDoCount) {
		String result = "";
		if (AppContext.DJSpecCaseFlag == 1) {
			result = context.getString(R.string.plan_speccasecount, canDoCount);
		} else {
			result = (completeCount + srNotNeedDoNum) + "/" + canDoCount;
		}
		return result;
	}
	
	@Override
	public boolean srIsSetting(Cycle cycle, String str) {
		if (cycle.JudgeCyc(DateTimeHelper.StringToDate(str))) {
			return true;
		}
		return false;
	}
}