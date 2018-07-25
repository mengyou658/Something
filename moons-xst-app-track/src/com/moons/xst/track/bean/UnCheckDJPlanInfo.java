package com.moons.xst.track.bean;

import java.io.Serializable;

public class UnCheckDJPlanInfo implements Serializable {
	
	/** Not-null value. */
	private String DJ_Plan_ID;
	/** Not-null value. */
	private String PlanDesc_TX;
	private int SortNo_NR;
	private String ESTStandard_TX;
	private String DataType_CD;
	private Integer Cycle_ID;
	private String IDPos_ID;
	private String LastComplete_DT;
	private String MustCheck_YN;
	private String SpecCase_YN;
	private String SpecCase_TX;
	private int CheckMethod;
	private String SRPoint_ID;
	private String MObjectState_TX;
	private String Place_TX;
	private String Name_TX;

	public UnCheckDJPlanInfo() {
	}

	public UnCheckDJPlanInfo(String DJ_Plan_ID, String PlanDesc_TX, int SortNo_NR,
			String ESTStandard_TX, String DataType_CD, Integer Cycle_ID,
			String IDPos_ID, String LastComplete_DT, String MustCheck_YN, 
			String SpecCase_YN, String SpecCase_TX,int CheckMethod, 
			String SRPoint_ID, String MObjectState_TX,
			String Place_TX, String Name_TX) {
		this.DJ_Plan_ID = DJ_Plan_ID;
		this.PlanDesc_TX = PlanDesc_TX;
		this.SortNo_NR = SortNo_NR;
		this.ESTStandard_TX = ESTStandard_TX;
		this.DataType_CD = DataType_CD;
		this.Cycle_ID = Cycle_ID;
		this.IDPos_ID = IDPos_ID;
		this.LastComplete_DT = LastComplete_DT;
		this.MustCheck_YN = MustCheck_YN;
		this.SpecCase_YN = SpecCase_YN;
		this.SpecCase_TX = SpecCase_TX;
		this.CheckMethod = CheckMethod;
		this.SRPoint_ID = SRPoint_ID;
		this.MObjectState_TX = MObjectState_TX;
		this.Place_TX = Place_TX;
		this.Name_TX = Name_TX;
	}

	/** Not-null value. */
	public String getDJ_Plan_ID() {
		return DJ_Plan_ID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setDJ_Plan_ID(String DJ_Plan_ID) {
		this.DJ_Plan_ID = DJ_Plan_ID;
	}

	/** Not-null value. */
	public String getPlanDesc_TX() {
		return PlanDesc_TX;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setPlanDesc_TX(String PlanDesc_TX) {
		this.PlanDesc_TX = PlanDesc_TX;
	}

	public int getSortNo_NR() {
		return SortNo_NR;
	}

	public void setSortNo_NR(int SortNo_NR) {
		this.SortNo_NR = SortNo_NR;
	}

	public String getESTStandard_TX() {
		return ESTStandard_TX;
	}

	public void setESTStandard_TX(String ESTStandard_TX) {
		this.ESTStandard_TX = ESTStandard_TX;
	}

	public String getDataType_CD() {
		return DataType_CD;
	}

	public void setDataType_CD(String DataType_CD) {
		this.DataType_CD = DataType_CD;
	}

	public Integer getCycle_ID() {
		return Cycle_ID;
	}

	public void setCycle_ID(Integer Cycle_ID) {
		this.Cycle_ID = Cycle_ID;
	}

	public String getIDPos_ID() {
		return IDPos_ID;
	}

	public void setIDPos_ID(String IDPos_ID) {
		this.IDPos_ID = IDPos_ID;
	}

	public int getCheckMethod() {
		return CheckMethod;
	}

	public void setCheckMethod(int CheckMethod) {
		this.CheckMethod = CheckMethod;
	}

	/** Not-null value. */
	public String getMustCheck_YN() {
		return MustCheck_YN;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setMustCheck_YN(String MustCheck_YN) {
		this.MustCheck_YN = MustCheck_YN;
	}

	public String getSpecCase_YN() {
		return SpecCase_YN;
	}

	public void setSpecCase_YN(String SpecCase_YN) {
		this.SpecCase_YN = SpecCase_YN;
	}

	public String getSpecCase_TX() {
		return SpecCase_TX;
	}

	public void setSpecCase_TX(String SpecCase_TX) {
		this.SpecCase_TX = SpecCase_TX;
	}	

	public String getSRPoint_ID() {
		return SRPoint_ID;
	}

	public void setSRPoint_ID(String SRPoint_ID) {
		this.SRPoint_ID = SRPoint_ID;
	}

	public String getMObjectState_TX() {
		return MObjectState_TX;
	}

	public void setMObjectState_TX(String MObjectState_TX) {
		this.MObjectState_TX = MObjectState_TX;
	}

	public String getLastComplete_DT() {
		return LastComplete_DT;
	}

	public void setLastComplete_DT(String LastComplete_DT) {
		this.LastComplete_DT = LastComplete_DT;
	}
	
	public String getPlace_TX() {
		return Place_TX;
	}

	public void setPlace_TX(String Place_TX) {
		this.Place_TX = Place_TX;
	}
	
	public String getName_TX() {
		return Name_TX;
	}
	
	public void setName_TX(String Name_TX) {
		this.Name_TX = Name_TX;
	}
}