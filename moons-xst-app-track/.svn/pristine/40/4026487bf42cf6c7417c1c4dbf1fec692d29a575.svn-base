package com.moons.xst.track.bean;

import java.util.Date;

/**
 * 点检路线实体类
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class DJIDPos extends Entity{
	

	
	private Long idposID;//
	public DJIDPos(Long IDPosID, String IDPosCD, String TypeCD, String IDPosName,  String LastUserName,Date LastTime, int PlanCount, char Status){
		// TODO Auto-generated constructor stub
		this.setIDPosID(IDPosID);
		this.setIDPosCD(IDPosCD);
		this.setTypeCD(TypeCD);
		this.setIDPosName(IDPosName);
		this.setLastTime(LastTime);
		this.setLastUserName(LastUserName);
		this.setStatus(Status);
		this.setPlanCount(PlanCount);
	}
	
	public Long getIDPosID() {
		return idposID;
	}
	public void setIDPosID(Long IDPosID) {
		this.idposID = IDPosID;
	}
	
	private String idposCD;//ID位置编码
	public String getIDPosCD() {
		return idposCD;
	}
	public void setIDPosCD(String IDPosCD) {
		this.idposCD= IDPosCD;
	}
	
	private String typeCD;//ID位置类型
	public String getTypeCD() {
		return typeCD;
	}
	public void setTypeCD(String TypeCD) {
		this.typeCD= TypeCD;
	}	
	
	private String idposName;//ID位置描述
	public String getIDPosName() {
		return idposName;
	}
	public void setIDPosName(String IDPosName) {
		this.idposName = IDPosName;
	}
	
	private String lastUserName;//最近一次人员
	public String getLastUserName() {
		return lastUserName;
	}
	public void setLastUserName(String LastUserName) {
		this.lastUserName = LastUserName;
	}	
	
	private Date lastTime;//最近一次碰触时间
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date LastTime) {
		this.lastTime = LastTime;
	}

	private int planCount;//ID位置下的计划数量
	public int getPlanCount() {
		return this.planCount;
	}
	public void setPlanCount(int PlanCount) {
		this.planCount = PlanCount;
	}	
	
	private char status;//状态
	public char getStatus() {
		return status;
	}
	public void setStatus(char Status) {
		this.status = Status;
	}		
	
	
	public void buildObject(Long IDPosID, String IDPosCD, String TypeCD, String IDPosName, Date LastTime, String LastUserName, char Status, int PlanCount){
		//DJLine oneDJLine = null;
		//oneDJLine = new DJLine();
		this.setIDPosID(IDPosID);
		this.setIDPosCD(IDPosCD);
		this.setTypeCD(TypeCD);
		this.setIDPosName(IDPosName);
		this.setLastTime(LastTime);
		this.setLastUserName(LastUserName);
		this.setStatus(Status);
		this.setPlanCount(PlanCount);
		//return oneDJLine;
		
	}	
	
}
