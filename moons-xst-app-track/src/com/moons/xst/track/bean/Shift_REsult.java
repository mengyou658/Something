package com.moons.xst.track.bean;

public class Shift_REsult {
	String Shift_Id;
	String ShiftStartTime_DT;
	String ShiftEndTime_DT;
	String JiaoBUser_TX;
	String JieBUser_TX;

	public Shift_REsult() {

	}

	public Shift_REsult(String Shift_Id, String ShiftStartTime_DT,
			String ShiftEndTime_DT, String JiaoBUser_TX, String JieBUser_TX) {
		this.Shift_Id=Shift_Id;
		this.ShiftStartTime_DT=ShiftStartTime_DT;
		this.ShiftEndTime_DT=ShiftEndTime_DT;
		this.JiaoBUser_TX=JiaoBUser_TX;
		this.JieBUser_TX=JieBUser_TX;

	}

	public String getShift_Id() {
		return Shift_Id;
	}

	public void setShift_Id(String shift_Id) {
		Shift_Id = shift_Id;
	}

	public String getShiftStartTime_DT() {
		return ShiftStartTime_DT;
	}

	public void setShiftStartTime_DT(String shiftStartTime_DT) {
		ShiftStartTime_DT = shiftStartTime_DT;
	}

	public String getShiftEndTime_DT() {
		return ShiftEndTime_DT;
	}

	public void setShiftEndTime_DT(String shiftEndTime_DT) {
		ShiftEndTime_DT = shiftEndTime_DT;
	}

	public String getJiaoBUser_TX() {
		return JiaoBUser_TX;
	}

	public void setJiaoBUser_TX(String jiaoBUser_TX) {
		JiaoBUser_TX = jiaoBUser_TX;
	}

	public String getJieBUser_TX() {
		return JieBUser_TX;
	}

	public void setJieBUser_TX(String jieBUser_TX) {
		JieBUser_TX = jieBUser_TX;
	}

}
