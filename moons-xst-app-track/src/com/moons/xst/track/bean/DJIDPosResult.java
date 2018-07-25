package com.moons.xst.track.bean;

import java.util.Date;

/**
 * 点检路线到位结果实体类
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class DJIDPosResult extends Entity{
	
	/*
	 * ID位置键值
	 */
	private int idposID;
	public int getIDPosID() {
		return idposID;
	}
	public void setIDPosID(int IDPosID) {
		this.idposID = IDPosID;
	}

	/*
	 * ID位置名称
	 */
	private String idposName;
	public String getIDPosName() {
		return idposName;
	}
	public void setIDPosName(String IDPosName) {
		this.idposName = IDPosName;
	}
	
	/*
	 * 员工姓名
	 */
	private String appuserName;//
	public String getAppUserName() {
		return appuserName;
	}
	public void setAppUserName(String AppUserName) {
		this.appuserName = AppUserName;
	}	
	
	/*
	 * I到位时间
	 */
	private Date startTime;//
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date StartTime) {
		this.startTime = StartTime;
	}

	/*
	 * 结束时间
	 */	
	private Date endTime;//
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date EndTime) {
		this.endTime = EndTime;
	}
	
	/*
	 * 班次
	 */	
	private String shiftName;//
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String ShiftName) {
		this.shiftName = ShiftName;
	}		
	
	/*
	 * 值别
	 */	
	private String groupName;//
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String GroupName) {
		this.groupName = GroupName;
	}			
	
	/*
	 * 耗时
	 */	
	private int timeCount;//	
	public int getTimeCount() {
		return this.timeCount;
	}
	public void setTimeCount(int TimeCount) {
		this.timeCount = TimeCount;
	}	
	

	public void buildObject(int IDPosID, String IDPosName, String AppUserName, Date StartTime, Date EndTime, String ShiftName, String GroupName,int TimeCount){
		this.setIDPosID(IDPosID);
		this.setIDPosName(IDPosName);
		this.setAppUserName(AppUserName);
		this.setStartTime(StartTime);
		this.setEndTime(EndTime);
		this.setShiftName(ShiftName);
		this.setGroupName(GroupName);
		this.setTimeCount(TimeCount);
	}	
	
}
