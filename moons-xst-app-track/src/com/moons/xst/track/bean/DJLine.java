package com.moons.xst.track.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 点检路线实体类
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class DJLine extends Entity implements BaseSearch {
	private int lineID;// 路线ID

	public int getLineID() {
		return lineID;
	}

	public void setLineID(int LineID) {
		this.lineID = LineID;
	}

	private String lineName;// 路线描述

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String LineName) {
		this.lineName = LineName;
	}

	private Date buildTime;// 标准化时间

	public Date getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(Date BuildTime) {
		this.buildTime = BuildTime;
	}

	private int idCount;// ID位置数量

	public int getIdCount() {
		return this.idCount;
	}

	public void setIdCount(int IdCount) {
		this.idCount = IdCount;
	}

	private int planCount;// 计划数量

	public int getPlanCount() {
		return this.planCount;
	}

	public void setPlanCount(int PlanCount) {
		this.planCount = PlanCount;
	}

	private int lineType = -1;// 路线类型

	/**
	 * 0-巡检;1-点检排程;2-巡更;3-精密点检;4-巡视路线;5-SIS路线;6-GPS巡线;7-新GPS巡线
	 * 
	 */
	public int getlineType() {
		return this.lineType;
	}

	/**
	 * 0-巡检;1-点检排程;2-巡更;3-精密点检;4-巡视路线;5-SIS路线;6-GPS巡线
	 * 
	 */
	public void setlineType(int LineType) {
		this.lineType = LineType;
	}

	private boolean needUpdate = false;

	public boolean getneedUpdate() {
		return this.needUpdate;
	}

	public void setneedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	private String planDBPath;

	public String getPlanDBPath() {
		return planDBPath;
	}

	public void setPlanDBPath(String planDBFilePath) {
		planDBPath = planDBFilePath;
	}

	private String resultDBPath;

	public String getResultDBPath() {
		return resultDBPath;
	}

	public void setResultDBPath(String resultDBFilePath) {
		resultDBPath = resultDBFilePath;
	}

	Boolean downloadyn = false;

	public void setdownloadedYN(Boolean yn) {
		downloadyn = yn;
	}

	public Boolean getdownloadedYN() {
		return downloadyn;
	}

	Boolean Buidyn = false;

	public void setBuidYN(Boolean yn) {
		Buidyn = yn;
	}

	public Boolean getBuidYN() {
		return Buidyn;
	}

	Boolean SpecCaseYN = false;

	public void setSpecCaseYN(Boolean yn) {
		SpecCaseYN = yn;
	}

	public Boolean getSpecCaseYN() {
		return SpecCaseYN;
	}
	//路线是否选择（用于点检排程选择下载）
	public boolean isSelected=false;
	
	public void setIsSelected(boolean b){
		isSelected=b;
	}
	
	public boolean getIsSelected(){
		return isSelected;
	}

	public void buildObject(int LineID, String LineName, Date BuildTime,
			int IDCount, int PlanCount) {
		// DJLine oneDJLine = null;
		// oneDJLine = new DJLine();
		this.setLineID(LineID);
		this.setLineName(LineName);
		this.setBuildTime(BuildTime);
		this.setIdCount(IDCount);
		this.setPlanCount(PlanCount);
		// return oneDJLine;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DJLine) {
			DJLine u = (DJLine) obj;
			return this.lineID == u.lineID
					&& this.buildTime.equals(u.buildTime);
		}
		return super.equals(obj);
	}
	
	@Override
	public String getSearchCondition() {
		 StringBuilder searchStr = new StringBuilder();
	        searchStr.append(lineName);
	        return searchStr.toString();
	}
}
