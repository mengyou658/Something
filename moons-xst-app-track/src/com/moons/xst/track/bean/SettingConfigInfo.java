package com.moons.xst.track.bean;

public class SettingConfigInfo {
	private String letter;
	private String[] infos;
	public SettingConfigInfo() {
		super();
	}
		
	
	public SettingConfigInfo(String letter, String[] infos) {
		super();
		this.letter = letter;
		this.infos = infos;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public String[] getInfo() {
		return infos;
	}
	public void setInfo(String[] infos) {
		this.infos = infos;
	}
}
