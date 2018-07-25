package com.moons.xst.track.bean;

public class XJ_MobjectStateHis extends MObject_State {
	private String Line_ID;
	private String ControlPointDesc_TX;
	
	public String getLine_ID() {
		return Line_ID;
	}

	public void setLine_ID(String line_ID) {
		Line_ID = line_ID;
	}
	
	public String getControlPointDesc_TX() {
		return ControlPointDesc_TX;
	}

	public void setControlPointDesc_TX(String controlPointDesc_TX) {
		ControlPointDesc_TX = controlPointDesc_TX;
	}
	
	public XJ_MobjectStateHis() {}
	
	public XJ_MobjectStateHis(String Line_ID,String StartAndEndPoint_ID, String CompleteTime_DT,
			String ControlPointDesc_TX, String MObjectState_CD, String MObjectStateName_TX,
			String AppUser_CD, String DataState_YN, String TransInfo_TX) {
		this.Line_ID = Line_ID;
		this.StartAndEndPoint_ID = StartAndEndPoint_ID;
		this.CompleteTime_DT = CompleteTime_DT;
		this.ControlPointDesc_TX = ControlPointDesc_TX;
		this.MObjectState_CD = MObjectState_CD;
		this.MObjectStateName_TX = MObjectStateName_TX;
		this.AppUser_CD = AppUser_CD;
		this.DataState_YN = DataState_YN;
		this.TransInfo_TX = TransInfo_TX;
	}
}