package com.moons.xst.track.bean;

import java.io.Serializable;

public class OperateLogBean implements Serializable {
	
	private String UserName_TX = "";
	private String Operate_TX = "";
	private String Time_TX = "";
	private String State_TX = "";
	private String Message_TX = "";
	
	public OperateLogBean() {}
	
	public String getUserName_TX() {
		return UserName_TX;
	}
	public void setUserName_TX(String username_tx) {
		UserName_TX = username_tx;
	}
	
	public String getOperate_TX() {
		return Operate_TX;
	}
	public void setOperate_TX(String operate_tx) {
		Operate_TX = operate_tx;
	}
	
	public String getTime_TX() {
		return Time_TX;
	}
	public void setTime_TX(String time_tx) {
		Time_TX = time_tx;
	}
	
	public String getState_TX() {
		return State_TX;
	}
	public void setState_TX(String state_tx) {
		State_TX = state_tx;
	}
	
	public String getMessage_TX() {
		return Message_TX;
	}
	public void setMessage_TX(String message_tx) {
		Message_TX = message_tx;
	}
}