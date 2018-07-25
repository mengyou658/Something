package com.moons.xst.track.bean;

public class BillUsers extends Base {

	/** Not-null value. */
	private String UserID;
	private String UserCD;
	/** Not-null value. */
	private String Account_TX;
	private String UserType;
	/** Not-null value. */
	private String UserName_TX;
	private String PassWord_TX;
	private String Tache_ID;

	public BillUsers() {
	}

	public BillUsers(String userID, String userCD, String account_TX,
			String userType, String userName_TX, String passWord_TX,
			String tache_ID) {
		super();
		UserID = userID;
		UserCD = userCD;
		Account_TX = account_TX;
		UserType = userType;
		UserName_TX = userName_TX;
		PassWord_TX = passWord_TX;
		Tache_ID = tache_ID;
	}

	/** Not-null value. */
	public String getUser_ID() {
		return UserID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setUser_ID(String AppUser_ID) {
		this.UserID = AppUser_ID;
	}

	public String getUser_CD() {
		return UserCD;
	}

	public void setUser_CD(String AppUser_CD) {
		this.UserCD = AppUser_CD;
	}

	/** Not-null value. */
	public String getAccount_TX() {
		return Account_TX;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setAccount_TX(String AppAccount_TX) {
		this.Account_TX = AppAccount_TX;
	}

	/** Not-null value. */
	public String getUserName_TX() {
		return UserName_TX;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setUserName_TX(String Name_TX) {
		this.UserName_TX = Name_TX;
	}

	public String getPassWord_TX() {
		return PassWord_TX;
	}

	public void setPassWord_TX(String XJQPWD_TX) {
		this.PassWord_TX = XJQPWD_TX;
	}

	public String getUserType() {
		return UserType;
	}

	public void setUserType(String mUserType) {
		this.UserType = mUserType;
	}

	public String getTache_ID() {
		return Tache_ID;
	}

	public void setTache_ID(String tache_ID) {
		this.Tache_ID = tache_ID;
	}

}
