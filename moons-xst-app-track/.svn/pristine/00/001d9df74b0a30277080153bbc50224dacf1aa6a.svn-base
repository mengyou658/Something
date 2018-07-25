package com.moons.xst.buss.cycle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.Z_DJ_Cycle;
import com.moons.xst.track.bean.Z_DJ_CycleDTL;
import com.moons.xst.track.bean.Z_WorkDate;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;

public class Cycle {

	private Z_DJ_Cycle djcycle;

	public void setDJCycle(Z_DJ_Cycle djcycle) {
		this.djcycle = djcycle;
	}

	public Z_DJ_Cycle getDJCycle() {
		return djcycle;

	}

	/**
	 * 周期中有效期信息
	 */
	private List<Z_DJ_CycleDTL> _CycleDTLList = new ArrayList<Z_DJ_CycleDTL>();

	public List<Z_DJ_CycleDTL> GetCycleDTL() {
		return _CycleDTLList;
	}

	public void SetCycleDTL(List<Z_DJ_CycleDTL> cycleDTLlist) {
		this._CycleDTLList = cycleDTLlist;
	}

	/**
	 * 当前符合要求的有效期
	 */
	private Z_DJ_CycleDTL _CurCycleDTL = null;

	public Z_DJ_CycleDTL GetCurCycleDTL() {
		return _CurCycleDTL;
	}

	public void SetCurCycleDTL(Z_DJ_CycleDTL dtl) {
		_CurCycleDTL = dtl;
	}

	/**
	 * 路线ID
	 */
	private String LineID;

	public void setLineID(String lineid) {
		this.LineID = lineid;
	}

	public String getLineID() {
		return LineID;
	}

	/**
	 * 当前计算时间
	 */
	private Date curCalDate = null;

	public void setCurCalTM(Date curDT) {
		this.curCalDate = curDT;
	}

	public Date getCurCalDTM() {
		return curCalDate;
	}

	/**
	 * 计算后当前任务开始时间
	 */
	private Date curTaskBegin = null;

	public void setTaskBegin(Date taskBegin) {
		this.curTaskBegin = taskBegin;
	}

	public Date getTaskBegin() {
		return curTaskBegin;
	}

	/**
	 * 计算后当前任务结束时间
	 */
	private Date curTaskEnd = null;

	public void setTaskEnd(Date taskEnd) {
		this.curTaskEnd = taskEnd;
	}

	public Date getTaskEnd() {
		return curTaskEnd;
	}

	/**
	 * 计算后长周期中的开始时段
	 */
	private String startTime = "00:00:00";

	public void setStartTime(String time) {
		startTime = time;
	}

	public String getStartTime() {
		return startTime;
	}

	/**
	 * 计算后长周期中的结束时段
	 */
	private String endTime = "23:59:59";

	public void setEndTime(String time) {
		endTime = time;
	}

	public String getEndTime() {
		return endTime;
	}

	/**
	 * 计算后当前班次
	 */
	private String curShiftNo;

	public void setCurShiftNo(String CurShiftNo) {
		this.curShiftNo = CurShiftNo;
	}

	public String getCurShiftNo() {
		return curShiftNo;
	}

	/**
	 * 计算后当前跨天属性
	 */
	private String curSpanFlag;

	public void setCurSpanFlag(String CurShiftNo) {
		this.curSpanFlag = CurShiftNo;
	}

	public String getCurSpanFlag() {
		return curSpanFlag;
	}

	/**
	 * 计算后当前节假日期信息
	 */
	private String curHolidayTX;

	public void setCurHolidayTX(String CurHolidayTX) {
		this.curHolidayTX = CurHolidayTX;
	}

	public String getCurHolidayTX() {
		return curHolidayTX;
	}

	/**
	 * 临时计算周期保存任务开始时间
	 */
	private Date tempTaskBegin = null;

	public void setTempTaskBegin(Date tempTaskBegin) {
		this.tempTaskBegin = tempTaskBegin;
	}

	public Date getTempTaskBegin() {
		return tempTaskBegin;
	}

	/**
	 * 临时计算周期保存任务结束时间
	 */
	private Date tepmTaskEnd = null;

	public void setTempTaskEnd(Date tempTaskEnd) {
		this.tepmTaskEnd = tempTaskEnd;
	}

	public Date getTempTaskEnd() {
		return tepmTaskEnd;
	}

	private String getWorkDataString(Date now) {
		String dayX = "0000000000000000000000000000000";
		int month = DateTimeHelper.getMonth(now);
		int year = DateTimeHelper.getYear(now);
		if (AppContext.workDateBuffer != null
				&& AppContext.workDateBuffer.size() > 0) {
			for (Z_WorkDate _item : AppContext.workDateBuffer) {
				if (_item.getYear_NR() == year && _item.getMonth_NR() == month) {
					dayX = _item.getDay_TX();
					return dayX;
				}
			}
		} else {
			return "";
		}
		return dayX;
	}

	/**
	 * 根据日期获取节假日标识
	 * 
	 * @param dt
	 * @return
	 */
	private String GetWorkFlagByDate(Date dt) {
		String workListString = getWorkDataString(dt);
		int dayNo = DateTimeHelper.GetDayOfMonth(dt);
		return StringUtils.isEmpty(workListString) ? "" : workListString.substring(dayNo-1, dayNo);
	}

	/**
	 * 返回当前计算日期节假日标识，1表示节假日，0表示工作日
	 * 
	 * @return
	 */
	private String GetWorkFlag() {
		Date dt = curCalDate;
		if (_CurCycleDTL != null && _CurCycleDTL.getTimeMode_CD().equals("1")) // 短周期
		{
			dt = curTaskBegin;
		}

		return GetWorkFlagByDate(dt);
	}

	/**
	 * 判断节假日是否做
	 * 
	 * @return
	 */
	private Boolean JudgeWorkDateByWorkFlag() {
		String workFlag = GetWorkFlag();
		if (curHolidayTX.equals("3")) // 节假日工作日都做
		{
			return true;
		} else if (curHolidayTX.equals("2")) // 节假日做
		{
			return workFlag.equals("1");
		} else if (curHolidayTX.equals("1")) // 工作日做
		{
			return workFlag.equals("0");
		}
		return false;
	}

	/**
	 * 判断计算时间是否在周期内
	 * 
	 * @return
	 */
	public Boolean JudgeCyc() {
		Boolean result = JudgeCyc(curCalDate);
		if (!result)
			return result;
		result = result && JudgeWorkDateByWorkFlag();
		return result;
	}

	/**
	 * 判断指定时间是否在周期内
	 * 
	 * @param dt
	 * @return
	 */
	public Boolean JudgeCyc(Date dt) {

		Boolean result = false;
		if (_CurCycleDTL == null
				|| StringUtils.isEmpty(_CurCycleDTL.getTimeMode_CD()))
			return false;
		if (_CurCycleDTL.getTimeMode_CD().equals("1")) // 短周期
		{
			result = dt.compareTo(curTaskBegin) >= 0
					&& dt.compareTo(curTaskEnd) < 0;
		} else // 长周期,长周期也支持时段限制
		{
			if (curTaskBegin != null && curTaskEnd != null
					&& dt.compareTo(curTaskBegin) >= 0
					&& dt.compareTo(curTaskEnd) < 0) {
				Date startTM = DateTimeHelper.StringToDate(DateTimeHelper
						.DateToString(dt).substring(0, 10) + " " + startTime);
				Date endTM = DateTimeHelper.StringToDate(DateTimeHelper
						.DateToString(dt).substring(0, 10) + " " + endTime);

				if (dt.compareTo(startTM) >= 0 && dt.compareTo(endTM) < 0)
					result = true;
			}

		}
		return result;
	}
	
	/**
	 * 
	 * 当前时刻是否在有效范围内
	 */
	 public Boolean isActiveCyc() {
		 Date date = DateTimeHelper.GetDateTimeNow();
		 if (curTaskBegin==null&&curTaskEnd==null) {
			 return false;
		 }
		else {
			 if (date.compareTo(curTaskBegin)>0 && date.compareTo(curTaskEnd)<0) {
				 return true;
			 }
			 else {
				return false;
			}
		}
		
	}

}
