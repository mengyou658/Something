package com.moons.xst.track.bean;

import java.io.Serializable;

public class CommUser implements BaseSearch,Serializable{
	private String APPUSER_CD;
	private String NAME_TX;
	private String APPUSER_ID;

	public CommUser() {
		super();
	}

	public CommUser(String aPPUSER_CD, String nAME_TX, String aPPUSER_ID) {
		super();
		APPUSER_CD = aPPUSER_CD;
		NAME_TX = nAME_TX;
		APPUSER_ID = aPPUSER_ID;
	}

	public String getAPPUSER_CD() {
		return APPUSER_CD;
	}

	public void setAPPUSER_CD(String aPPUSER_CD) {
		APPUSER_CD = aPPUSER_CD;
	}

	public String getNAME_TX() {
		return NAME_TX;
	}

	public void setNAME_TX(String nAME_TX) {
		NAME_TX = nAME_TX;
	}

	public String getAPPUSER_ID() {
		return APPUSER_ID;
	}

	public void setAPPUSER_ID(String aPPUSER_ID) {
		APPUSER_ID = aPPUSER_ID;
	}

	@Override
	public String getSearchCondition() {
		// TODO 自动生成的方法存根
		StringBuilder searchStr = new StringBuilder();
        searchStr.append(NAME_TX);
        searchStr.append(APPUSER_CD);
        return searchStr.toString();
	}

}
