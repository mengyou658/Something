/**
 * @CheckPointInfo.java
 * @author LKZ
 * @2015-1-16
 */
package com.moons.xst.track.bean;

import java.util.Date;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;

/**
 * 考核点实体类
 * 
 * @author LKZ
 * 
 */
public class CheckPointInfo extends Entity {

	private int GPSPosition_ID;
	private String GPSPosition_CD;
	private int Sort_NR;
	private String Longitude;
	private String Latitude;
	private String UTCDateTime;
	private String Desc_TX;
	private String LineID;
	private String NextPointHour_NR;
	private String Deviation;
	private String CheckPoint_Type;
	private String Order_YN;
	private String KHDate_DT;
	private int Cycle_ID;
	private String CycleBaseDate_DT;
	private String SpeedLimit_Num;

	public CheckPointInfo() {
	}

	public CheckPointInfo(int GPSPosition_ID, String GPSPosition_CD,
			int Sort_NR, String Longitude, String Latitude, String UTCDateTime,
			String Desc_TX, String LineID, String NextPointHour_NR,
			String Deviation, String CheckPoint_Type, String Order_YN,
			String KHDate_DT, String SpeedLimit_Num,int cycid,String cyc_DT) {
		this.GPSPosition_ID = GPSPosition_ID;
		this.GPSPosition_CD = GPSPosition_CD;
		this.Sort_NR = Sort_NR;
		this.Longitude = Longitude;
		this.Latitude = Latitude;
		this.UTCDateTime = UTCDateTime;
		this.Desc_TX = Desc_TX;
		this.LineID = LineID;
		this.NextPointHour_NR = NextPointHour_NR;
		this.Deviation = Deviation;
		this.CheckPoint_Type = CheckPoint_Type;
		this.Order_YN = Order_YN;
		this.KHDate_DT = KHDate_DT;
		this.SpeedLimit_Num = SpeedLimit_Num;
		this.Cycle_ID = cycid;
		this.CycleBaseDate_DT = cyc_DT;
	}

	public int getGPSPosition_ID() {
		return GPSPosition_ID;
	}

	public void setGPSPosition_ID(int GPSPosition_ID) {
		this.GPSPosition_ID = GPSPosition_ID;
	}

	public String getGPSPosition_CD() {
		return GPSPosition_CD;
	}

	public void setGPSPosition_CD(String GPSPosition_CD) {
		this.GPSPosition_CD = GPSPosition_CD;
	}

	public int getSort_NR() {
		return Sort_NR;
	}

	public void setSort_NR(int Sort_NR) {
		this.Sort_NR = Sort_NR;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String Longitude) {
		this.Longitude = Longitude;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String Latitude) {
		this.Latitude = Latitude;
	}

	public String getUTCDateTime() {
		return UTCDateTime;
	}

	public void setUTCDateTime(String UTCDateTime) {
		this.UTCDateTime = UTCDateTime;
	}

	public String getDesc_TX() {
		return Desc_TX;
	}

	public void setDesc_TX(String Desc_TX) {
		this.Desc_TX = Desc_TX;
	}

	public String getLineID() {
		return LineID;
	}

	public void setLineID(String LineID) {
		this.LineID = LineID;
	}

	public String getNextPointHour_NR() {
		return NextPointHour_NR;
	}

	public void setNextPointHour_NR(String NextPointHour_NR) {
		this.NextPointHour_NR = NextPointHour_NR;
	}

	public String getDeviation() {
		return Deviation;
	}

	public void setDeviation(String Deviation) {
		this.Deviation = Deviation;
	}

	public String getCheckPoint_Type() {
		return CheckPoint_Type;
	}

	public void setCheckPoint_Type(String CheckPoint_Type) {
		this.CheckPoint_Type = CheckPoint_Type;
	}

	public String getOrder_YN() {
		return Order_YN;
	}

	public void setOrder_YN(String Order_YN) {
		this.Order_YN = Order_YN;
	}

	public String getSpeedLimit_Num() {
		return SpeedLimit_Num;
	}

	public void setSpeedLimit_Num(String speedLimit_Num) {
		this.SpeedLimit_Num = speedLimit_Num;
	}

	public String getKHDate_DT() {
		return KHDate_DT;
	}

	public void setKHDate_DT(String KHDate_DT) {
		this.KHDate_DT = KHDate_DT;
	}

	public String getCycleBaseDate_DT() {
		return CycleBaseDate_DT;
	}

	public void setCycleBaseDate_DT(String CycleBaseDate_DT) {
		this.CycleBaseDate_DT = CycleBaseDate_DT;
	}

	public int getCycle_ID() {
		return Cycle_ID;
	}

	public void setCycle_ID(int id) {
		this.id = Cycle_ID;
	}

	public CheckPointInfo Copy() {
		CheckPointInfo _info = new CheckPointInfo();
		_info.setGPSPosition_ID(getGPSPosition_ID());
		_info.setGPSPosition_CD(getGPSPosition_CD());
		_info.setSort_NR(getSort_NR());
		_info.setLongitude(getLongitude());
		_info.setLatitude(getLatitude());
		_info.setUTCDateTime(getUTCDateTime());
		_info.setDesc_TX(getDesc_TX());
		_info.setLineID(getLineID());
		_info.setNextPointHour_NR(getNextPointHour_NR());
		_info.setDeviation(getDeviation());
		_info.setDeviation(getDeviation());
		_info.setOrder_YN(getOrder_YN());
		_info.setKHDate_DT(getKHDate_DT());
		_info.setCycle_ID(getCycle_ID());
		_info.setCheckPoint_Type(getCheckPoint_Type());
		_info.SetCycle(GetCycle());
		return _info;
	}

	String _nearDistance = "";

	public String getNearDistance() {
		return _nearDistance;
	}

	public void setNearDistance(String distance) {
		_nearDistance = distance;
	}

	int _shakeNum = 0;

	public Integer getShakeNum() {
		return _shakeNum;
	}

	public void setShakeNum(int shakeNum) {
		_shakeNum = shakeNum;
	}

	public boolean equals(CheckPointInfo cpInfo) {
		return this.getGPSPosition_ID() == cpInfo.getGPSPosition_ID();
	}
	
	/**
	 * 考核点对应的周期
	 */
	private Cycle cycle;

	public void SetCycle(Cycle cycle) {
		this.cycle = cycle;
	}

	public Cycle GetCycle() {
		return cycle;
	}
	
	/**
	 * 判断考核点是否到位，更新周期中计算后的开始时间和结束时间
	 * 
	 * @param date
	 * @return
	 */
	public Boolean JudgeCycle(Date date) {
		AppContext.calculateCycleService.calculate(cycle, date, false);
		return cycle.JudgeCyc();

	}
	
	/**
	 * 判断在周期时段内是否已经做过
	 * 
	 * @param date
	 * @return
	 */
	public Boolean JudgeIsCompleted(Date date) {
		AppContext.calculateCycleService.calculate(cycle, date, false);
		// 判断是否点检过
		String lastCmdtStr = KHDate_DT;
		if (!StringUtils.isEmpty(lastCmdtStr)) {
			if (cycle.JudgeCyc(DateTimeHelper.StringToDate(lastCmdtStr))) {

				return true;
			}

		}
		return false;
	}
	
	private String remsg = "";
	public String getremsg() {
		return remsg;
	}

	public void setremsg(String msg) {
		remsg =msg;
	}
	/**
	 * 做计划时判断计划是否可做，不更新周期中计算后的开始时间和结束时间
	 * 
	 * @param date
	 * @param emsg
	 * @return
	 */
	public Boolean JudgePointTemp(Date date, String emsg) {
		AppContext.calculateCycleService.calculate(cycle, date, true);

		if (cycle.getTaskBegin() != null && cycle.getTempTaskBegin() != null
				&& !cycle.getTaskBegin().equals(cycle.getTempTaskBegin())) {
			emsg = "检查到已经跨时段，请重新碰钮扣";
			remsg = "检查到已经跨时段，请重新碰钮扣";
			return false;
		}
		if (!cycle.JudgeCyc()) // 判断周期
		{
			remsg = "";
			return false;
		}

		return true;
	}
}
