package com.moons.xst.track.bean;

public class OperDetailResult {
	String Operate_Bill_Code;
	int Operate_Detail_Item_ID;
	int Operate_Detail_Item_State;
	String Operate_Detail_Item_Exe_Time;

	public OperDetailResult() {

	}

	public OperDetailResult(String Operate_Bill_Code,
			int Operate_Detail_Item_ID, int Operate_Detail_Item_State,
			String Operate_Detail_Item_Exe_Time) {
		this.Operate_Bill_Code=Operate_Bill_Code;
		this.Operate_Detail_Item_ID=Operate_Detail_Item_ID;
		this.Operate_Detail_Item_State=Operate_Detail_Item_State;
		this.Operate_Detail_Item_Exe_Time=Operate_Detail_Item_Exe_Time;
	}

	public String getOperate_Bill_Code() {
		return Operate_Bill_Code;
	}

	public void setOperate_Bill_Code(String operate_Bill_Code) {
		Operate_Bill_Code = operate_Bill_Code;
	}

	public int getOperate_Detail_Item_ID() {
		return Operate_Detail_Item_ID;
	}

	public void setOperate_Detail_Item_ID(int operate_Detail_Item_ID) {
		Operate_Detail_Item_ID = operate_Detail_Item_ID;
	}

	public int getOperate_Detail_Item_State() {
		return Operate_Detail_Item_State;
	}

	public void setOperate_Detail_Item_State(int operate_Detail_Item_State) {
		Operate_Detail_Item_State = operate_Detail_Item_State;
	}

	public String getOperate_Detail_Item_Exe_Time() {
		return Operate_Detail_Item_Exe_Time;
	}

	public void setOperate_Detail_Item_Exe_Time(
			String operate_Detail_Item_Exe_Time) {
		Operate_Detail_Item_Exe_Time = operate_Detail_Item_Exe_Time;
	}

}
