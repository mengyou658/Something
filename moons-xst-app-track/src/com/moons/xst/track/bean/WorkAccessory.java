package com.moons.xst.track.bean;

public class WorkAccessory {
	String AccessoryName;// 附件名
	String Accessory;// 附件

	public WorkAccessory() {
	}

	public WorkAccessory(String accessoryName, String accessory) {
		AccessoryName = accessoryName;
		Accessory = accessory;
	}

	public String getAccessoryName() {
		return AccessoryName;
	}

	public void setAccessoryName(String accessoryName) {
		AccessoryName = accessoryName;
	}

	public String getAccessory() {
		return Accessory;
	}

	public void setAccessory(String accessory) {
		Accessory = accessory;
	}

}
