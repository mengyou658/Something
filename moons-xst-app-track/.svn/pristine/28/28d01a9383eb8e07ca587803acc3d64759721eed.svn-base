package com.moons.xst.buss;

import java.io.Serializable;
import java.util.Date;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.buss.cycle.CycleComMethod;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.UnCheckDJPlanInfo;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;

public class DJPlanForUnCheck implements Serializable {
	
	private UnCheckDJPlanInfo djplan;
	
	public void setDJPlan(UnCheckDJPlanInfo djplan) {
		this.djplan = djplan;
	}

	public UnCheckDJPlanInfo getDJPlan() {
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
	 * 判断当前时间是否在周期内
	 * 
	 * @param date
	 * @return
	 */
	public Boolean JudgeCureDataIsInCycle(Date date) {
		AppContext.calculateCycleService.calculate(cycle, date, false);
		
		if (cycle.JudgeCyc()) {
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
	
	private boolean mUnCheckYN = false;
	public Boolean getUnCheckYN() {
		return mUnCheckYN;
	}
	public void setUnCheckYN(boolean b) {
		mUnCheckYN = b;
	}
}