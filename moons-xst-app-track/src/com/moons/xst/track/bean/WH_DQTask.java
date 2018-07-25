package com.moons.xst.track.bean;

public class WH_DQTask {
	int DQSpec_ID;
	String ZXRName_TX;
	String JHRName_TX;
	String TaskStatus_CD;
	String Finish_DT;
	String Content_TX;
	String IDPosTime_DT;

	public WH_DQTask() {

	}

	public WH_DQTask(int DQSpec_ID, String ZXRName_TX, String JHRName_TX,
			String TaskStatus_CD, String Finish_DT, String Content_TX,
			String IDPosTime_DT) {
		this.DQSpec_ID=DQSpec_ID;
		this.ZXRName_TX=ZXRName_TX;
		this.JHRName_TX=JHRName_TX;
		this.TaskStatus_CD=TaskStatus_CD;
		this.Finish_DT=Finish_DT;
		this.Content_TX=Content_TX;
		this.IDPosTime_DT=IDPosTime_DT;
	}

	public int getDQSpec_ID() {
		return DQSpec_ID;
	}

	public void setDQSpec_ID(int dQSpec_ID) {
		DQSpec_ID = dQSpec_ID;
	}

	public String getZXRName_TX() {
		return ZXRName_TX;
	}

	public void setZXRName_TX(String zXRName_TX) {
		ZXRName_TX = zXRName_TX;
	}

	public String getJHRName_TX() {
		return JHRName_TX;
	}

	public void setJHRName_TX(String jHRName_TX) {
		JHRName_TX = jHRName_TX;
	}

	public String getTaskStatus_CD() {
		return TaskStatus_CD;
	}

	public void setTaskStatus_CD(String taskStatus_CD) {
		TaskStatus_CD = taskStatus_CD;
	}

	public String getFinish_DT() {
		return Finish_DT;
	}

	public void setFinish_DT(String finish_DT) {
		Finish_DT = finish_DT;
	}

	public String getContent_TX() {
		return Content_TX;
	}

	public void setContent_TX(String content_TX) {
		Content_TX = content_TX;
	}

	public String getIDPosTime_DT() {
		return IDPosTime_DT;
	}

	public void setIDPosTime_DT(String iDPosTime_DT) {
		IDPosTime_DT = iDPosTime_DT;
	}

}
