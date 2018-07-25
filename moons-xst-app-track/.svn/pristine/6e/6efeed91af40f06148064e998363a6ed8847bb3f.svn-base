package com.moons.xst.track.bean;


/**
 * 数据操作结果实体类
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class Result extends Base {

	private int errorCode;
	private String errorMessage;
	
	public boolean OK() {
		return errorCode == 1;
	}
    public void setErrorCode(int errCode) {
    	errorCode =errCode;
	}
	public String getErrorMessage(){
		return errorMessage;
	}
	public void setErrorMessage(String errmes) {
		errorMessage =errmes;
	}
	
	@Override
	public String toString(){
		return String.format("RESULT: CODE:%d,MSG:%s", errorCode, errorMessage);
	}
}
