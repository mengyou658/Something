package com.moons.xst.buss;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.buss.cycle.CycleComMethod;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.bean.Z_DataCodeGroup;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;

public class DJPlan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2629015470250512246L;
	private Z_DJ_Plan djplan;

	public void setDJPlan(Z_DJ_Plan djplan) {
		this.djplan = djplan;
	}

	public Z_DJ_Plan getDJPlan() {
		return djplan;

	}

	/**
	 * 计划对应的周期
	 */
	private Cycle cycle;

	public void SetCycle(Cycle cycle) {
		this.cycle = cycle;
	}

	public Cycle GetCycle() {
		return cycle;
	}

	/**
	 * 计划对应的启停点
	 */
	private DJ_ControlPoint srpoint;

	public void SetSrPoint(DJ_ControlPoint srpoint) {
		this.srpoint = srpoint;
	}

	public DJ_ControlPoint GetSrPoint() {
		return srpoint;
	}

	/**
	 * 做计划时判断计划是否可做，不更新周期中计算后的开始时间和结束时间
	 * 
	 * @param date
	 * @param emsg
	 * @return
	 */
	public String JudgePlanTemp(Context context, Date date) {
		String emsg = "";
		if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
			AppContext.calculateCycleService.calculate(cycle, date, true);
			
			/*if (cycle.getTaskBegin() != null && cycle.getTempTaskBegin() != null
					&& !cycle.getTaskBegin().equals(cycle.getTempTaskBegin())) {
				emsg = context.getString(R.string.plan_overcycle_msg);
				return emsg;
			}*/
			if ((cycle.getTempTaskBegin() == null) || 
					(cycle.getTaskBegin() != null
					&& cycle.getTempTaskBegin() != null
					&& !cycle.getTaskBegin().equals(cycle.getTempTaskBegin()))) {
				emsg = context.getString(R.string.plan_overcycle_msg);
				return emsg;
			}
		} else if (AppContext.getCurrLineType() == AppConst.LineType.DJPC.getLineType()) {
			if (date.compareTo(DateTimeHelper.StringToDate(djplan.getDisStart_TM())) < 0
					|| date.compareTo(DateTimeHelper.StringToDate(djplan.getDisEnd_TM())) > 0) {
				emsg = context.getString(R.string.plan_overcycle_msg);
				return emsg;
			}
		}
//		if (!cycle.JudgeCyc()) // 判断周期
//		{
//			return "";
//		}

		return emsg;
	}

	/**
	 * 判断计划是否可做，更新周期中计算后的开始时间和结束时间
	 * 
	 * @param date
	 * @return
	 */
	public Boolean JudgePlanCycle(Date date) {
		AppContext.calculateCycleService.calculate(cycle, date, false);
		return cycle.JudgeCyc();

	}
	
	/**
	 * 判断计划是否可做(点检排程)
	 * @param data
	 * @return
	 */
	public boolean JudgePlanCycleForDJPC(Date date) {
		if (date.compareTo(DateTimeHelper.StringToDate(djplan.getDisStart_TM())) >= 0
				&& date.compareTo(DateTimeHelper.StringToDate(djplan.getDisEnd_TM())) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断在周期时段内是否已经做过
	 * 
	 * @param date
	 * @return
	 */
	public Boolean JudgePlanIsCompleted(Date date) {
		// 如果是特巡，不做判断，直接返回未做状态
		if (AppContext.DJSpecCaseFlag == 1) {
			return false;
		}
		AppContext.calculateCycleService.calculate(cycle, date, false);
		// 判断是否点检过
		String lastCmdtStr = djplan.getLastComplete_DT();
		
		if (!StringUtils.isEmpty(lastCmdtStr)) {
			if (cycle.JudgeCyc(DateTimeHelper.StringToDate(lastCmdtStr))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断在周期时段内是否已经做过
	 * 
	 * @param date
	 * @return
	 */
	public Boolean JudgePlanIsCompletedForDJPC() {
		// 判断是否点检过
		String lastCmdtStr = djplan.getLastComplete_DT();
		
		if (!StringUtils.isEmpty(lastCmdtStr)) {
			if (DateTimeHelper.StringToDate(lastCmdtStr)
					.compareTo(DateTimeHelper.StringToDate(djplan.getDisStart_TM())) >= 0
					&& DateTimeHelper.StringToDate(lastCmdtStr)
					.compareTo(DateTimeHelper.StringToDate(djplan.getDisEnd_TM())) <= 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断条件巡检计划是否已经做过
	 * @param context
	 * @return
	 */
	public Boolean JudgePlanIsCompletedForCASEXJ(Context context) {
		// 判断是否点检过
		String lastCmdtStr = djplan.getLastComplete_DT();
		if (!StringUtils.isEmpty(lastCmdtStr)) {
			List<Z_DJ_Plan> res = DJPlanHelper.GetIntance().getDJPlanIsCompleteForCASEXJ(context, djplan);
			if (res.size() > 0)
				return true;
	    }
		return false;
	}

	/**
	 * 判断计划是否受启停点控制
	 * 
	 * @return
	 */
	public Boolean JudgePlanIsSrControl() {
		if (AppContext.DJSpecCaseFlag == 1)
			return false;
		if (srpoint == null)
			return false;
		else
			return true;
	}

	/**
	 * 判断是否设置过启停点
	 * 0-巡点检：判断周期内是否设置过
	 * 8-条件巡检：判断getLastSRResult_TM是否有值，有值，则认为设置过
	 * @return
	 */
	public Boolean SrIsSetting() {
		String lastCmdtStr = srpoint.getLastSRResult_TM();
		if (!StringUtils.isEmpty(lastCmdtStr)) {
			if (XSTMethodByLineTypeHelper.getInstance().srIsSetting(cycle, lastCmdtStr))
				return true;
			/*if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
				if (cycle.JudgeCyc(DateTimeHelper.StringToDate(lastCmdtStr))) {
					return true;
				}
			}
			else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					return true;
			}*/
		}
		return false;

	}

	/**
	 * 判断启停点当前状态下计划是否可做
	 * 
	 * @return
	 */
	public Boolean JudgePlanBySrPoint() {
		try {
			// 标准上设置的启停控制信息，数据库中以16进账字符串保存
			String planMobjState = djplan.getMObjectState_TX();
			if (StringUtils.isEmpty(planMobjState))
				return true;
			Integer msInt = Integer.parseInt(planMobjState, 16);
			// 启停点设置结果，以二进账字符串保存
			String lastCmdSrResult = CycleComMethod.TransSrRst(srpoint
					.getLastSRResult_TX());

			Integer srInt = Integer.parseInt(lastCmdSrResult, 2);

			Integer result = msInt & srInt;
			if (result > 0)
				return true;
			else
				return false;

		} catch (Exception e) {
			return true;
		}

	}

	/**
	 * 判断计划是否主控点控制
	 * 
	 * @return
	 */
	public Boolean JudgePlanIsLKControl() {
		if (AppContext.DJSpecCaseFlag == 1)
			return false;
		if (StringUtils.isEmpty(djplan.getLKPoint_ID()))
			return false;
		return Integer.parseInt(djplan.getLKPoint_ID()) > 0;
	}

	private boolean hasSetLK = false;

	/**
	 * 设置是否已弹出过主控点
	 * 
	 * @param yn
	 */
	public void setLKDoYn(boolean yn) {
		hasSetLK = yn;
	}

	/**
	 * 获取是否已弹出过主控点
	 * 
	 * @param yn
	 */
	public boolean getLKDoYn() {
		return hasSetLK;
	}

	private Z_DataCodeGroup dataCodeGroup;

	/**
	 * 设置结果选项
	 * 
	 * @param mdataCodeGroup
	 */
	public void setDataCodeGroup(Z_DataCodeGroup mdataCodeGroup) {
		this.dataCodeGroup = mdataCodeGroup;
	}

	/**
	 * 获取结果选项
	 * 
	 * @return
	 */
	public Z_DataCodeGroup getDataCodeGroup() {
		return dataCodeGroup;
	}

	/**
	 * 在特巡模式下判断计划是否满足特巡条件
	 * 
	 * @param specCaseString
	 * @return
	 */
	public Boolean JudgePlanSpecCase(String specCaseString) {
		String aString = djplan.getSpecCase_TX();
		if ((!StringUtils.isEmpty(djplan.getSpecCase_YN()))
				&& djplan.getSpecCase_YN().equals("1")
				&& (!StringUtils.isEmpty(specCaseString))
				&& (!StringUtils.isEmpty(aString))) {
			char[] _aChars = aString.toCharArray();
			char[] _bChars = specCaseString.toCharArray();
			for (int i = 0; i < aString.length(); i++) {
				if (_bChars.length > i) {
					if (_aChars[i] == '1' && _bChars[i] == '1') {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 根据特巡条件编码获取特巡名称
	 * 
	 * @param specCaseStr
	 * @return
	 */
	public String getSpecCaseNames() {
		if (AppContext.DJSpecCaseFlag == 0)
			return "";
		String specCaseStr = AppContext.selectedDJSpecCaseStr;
		String specCaseNamesString = "";
		if (specCaseStr.length() > 0 && AppContext.DJSpecCaseBuffer != null
				&& AppContext.DJSpecCaseBuffer.size() > 0) {
			char[] _aChars = specCaseStr.toCharArray();
			for (int i = 0; i < specCaseStr.length(); i++) {
				if (_aChars.length > i) {
					if (_aChars[i] == '1') {
						specCaseNamesString += "/"
								+ AppContext.DJSpecCaseBuffer
										.get(String.valueOf(AppContext
												.GetCurrLineID())).get(i)
										.getName_TX();
					}
				}
			}
		}
		return specCaseNamesString;
	}

	/***
	 * 计算查询日期
	 * 
	 * @param cmpleteDate
	 * @return
	 */
	public Date GetQueryDate(Date cmpleteDate) {
		Date queryDate = cmpleteDate;
		String overDay = cycle.getCurSpanFlag();
		Date startDate = DateTimeHelper.StringToDate(DateTimeHelper
				.DateToString(cycle.getTaskBegin(), "yyyy-MM-dd  00:00:00"));
		Date endDate = DateTimeHelper.StringToDate(DateTimeHelper.DateToString(
				cycle.getTaskEnd(), "yyyy-MM-dd  00:00:00"));
		Date comDtDate = DateTimeHelper.StringToDate(DateTimeHelper
				.DateToString(cmpleteDate, "yyyy-MM-dd  00:00:00"));
		if (startDate.compareTo(endDate) == 0) // 时段不跨天
		{
			if (overDay.equals("0")) // 向前跨
			{
				queryDate = DateTimeHelper.AddDays(cmpleteDate, -1);
			} else if (overDay.equals("2")) // 向后跨
			{
				queryDate = DateTimeHelper.AddDays(cmpleteDate, 1);
			}
		} else // 时段跨天
		{
			if (overDay.equals("0")) // 向前跨
			{
				if (endDate.compareTo(comDtDate) == 0) // 0点后做
				{
					queryDate = DateTimeHelper.AddDays(cmpleteDate, -1);
				}
			} else if (overDay.equals("2")) // 向后跨
			{
				if (startDate.compareTo(comDtDate) == 0) // 0点前做
				{
					queryDate = DateTimeHelper.AddDays(cmpleteDate, 1);
				}
			}
		}
		return queryDate;
	}

	/**
	 * 判断计划是否被禁用
	 * 
	 * @param currDate
	 * @return
	 */
	public boolean JudgePlanDisable() {
		try {
			boolean bYN = false, eYN = false;
			Date bDate, eDate, nowDate;
			nowDate = DateTimeHelper.GetDateTimeNow();
			if (!djplan.getDisStart_TM().equals("")) {
				bDate = DateTimeHelper.StringToDate(djplan.getDisStart_TM());
				bYN = nowDate.compareTo(bDate) >= 0;
			}
			if (!djplan.getDisEnd_TM().equals("")) {
				eDate = DateTimeHelper.StringToDate(djplan.getDisEnd_TM());
				eYN = nowDate.compareTo(eDate) <= 0;
			}
			return bYN && eYN;
		} catch (Exception e) {
			return false;
		}

	}
}
