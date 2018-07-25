package com.moons.xst.track.bean;

import java.util.Date;

/**
 * 点检路线实体类
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class DJPlan extends Entity{
	private int planID; //ID
	private String planDesc;//描述
	private Date lastTime;//上次巡检时间
	private String lastResult;//上次结果
	private String dataCode;//数据类型
	private String planUnit;//单位
    /** Not-null value. */
    private String Cycle_ID;
    /** Not-null value. */
    private String CycleBaseDate_DT;
    /** Not-null value. */
    private String IDPos_ID;

	public DJPlan(int PlanID, String PlanDesc, Date LastTime, String LastResult,String DataCode,String PlanUnit,String Cycle_ID, String CycleBaseDate_DT, String IDPos_ID){
		this.setPlanID(PlanID);
		this.setPlanDesc(PlanDesc);
		this.setLastTime(LastTime);
		this.setLastResult(LastResult);
		this.setPlanUnit(PlanUnit);
		this.setDataCode(DataCode);
        this.Cycle_ID = Cycle_ID;
        this.CycleBaseDate_DT = CycleBaseDate_DT;
        this.IDPos_ID = IDPos_ID;
	}
	
	public DJPlan(){
		
	}
	public int getPlanID() {
		return planID;
	}
	public void setPlanID(int PlanID) {
		this.planID = PlanID;
	}
	public String getPlanDesc() {
		return planDesc;
	}
	public void setPlanDesc(String PlanDesc) {
		this.planDesc = PlanDesc;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date LastTime) {
		this.lastTime = LastTime;
	}
	
	public String getLastResult() {
		return lastResult;
	}
	public void setLastResult(String LastResult) {
		this.lastResult = LastResult;
	}	
	
	public String getPlanUnit() {
		return planUnit;
	}
	public void setPlanUnit(String PlanUnit) {
		this.planUnit = PlanUnit;
	}		
	
	public String getDataCode() {
		return this.dataCode;
	}
	public void setDataCode(String DataCode) {
		this.dataCode = DataCode;
	}
	
    /** Not-null value. */
    public String getCycle_ID() {
        return Cycle_ID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCycle_ID(String Cycle_ID) {
        this.Cycle_ID = Cycle_ID;
    }

    /** Not-null value. */
    public String getCycleBaseDate_DT() {
        return CycleBaseDate_DT;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCycleBaseDate_DT(String CycleBaseDate_DT) {
        this.CycleBaseDate_DT = CycleBaseDate_DT;
    }

    /** Not-null value. */
    public String getIDPos_ID() {
        return IDPos_ID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setIDPos_ID(String IDPos_ID) {
        this.IDPos_ID = IDPos_ID;
    }

	public void buildObject(int PlanID, String PlanDesc, Date LastTime, String LastResult,String PlanUnit,String DataCode,String Cycle_ID, String CycleBaseDate_DT, String IDPos_ID){
		//DJLine oneDJLine = null;
		//oneDJLine = new DJLine();
		this.setPlanID(PlanID);
		this.setPlanDesc(PlanDesc);
		this.setLastTime(LastTime);
		this.setLastResult(LastResult);
		this.setPlanUnit(PlanUnit);
		this.setDataCode(DataCode);
	    this.Cycle_ID = Cycle_ID;
	    this.CycleBaseDate_DT = CycleBaseDate_DT;
	    this.IDPos_ID = IDPos_ID;
		//return oneDJLine;
		
	}	
	
}
