package com.moons.xst.track.bean;

import java.util.ArrayList;

/**
 * 登录用户实体类
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class User extends Base {
	
	
	private int userID;
	private String userName;
	private String userAccount;
	private String userPwd;
	private boolean isRememberMe;
	private String userRFID;
	private String userAccess;
	private Result validate;
	private ArrayList<String> lineList;
	private String userlineListStr;
	private String userCD;	
	
	public boolean isRememberMe() {
		return isRememberMe;
	}
	public void setRememberMe(boolean isRememberMe) {
		this.isRememberMe = isRememberMe;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userid) {
		this.userID = userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String username) {
		this.userName = username;
	}

	public String getUserAccess() {
		return userAccess;
	}
	public void setUserAccess(String userAccess) {
		this.userAccess = userAccess;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setPwd(String pwd) {
		this.userPwd = pwd;
	}
	public String getUserRFID(){
		return userRFID;
	}
	public void setUserRFID(String userRFID){
		this.userRFID = userRFID;
	}
	public Result getValidate() {
		return validate;
	}
	public void setValidate(Result validate) {
		this.validate = validate;
	}
	
	public String getUserAccount() {
		return userAccount;
	}
	public void setAccount(String account) {
		this.userAccount = account;
	}
	
	public String getUserCD() {
		return userCD;
	}
	public void setUserCD(String usercd) {
		this.userCD = usercd;
	}
	
	public ArrayList<String> getUserLineList() {
		return lineList;
	}
	public void setUserLineList(ArrayList<String> userLineList) {
		this.lineList = userLineList;
	}
	
	public String getUserLineListStr() {
		return userlineListStr;
	}
	public void setUserLineListStr(String userLineList) {
		this.userlineListStr = userLineList;
	}
	
}
