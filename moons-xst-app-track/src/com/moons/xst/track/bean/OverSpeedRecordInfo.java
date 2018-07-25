package com.moons.xst.track.bean;

public class OverSpeedRecordInfo {

	/** Not-null value. */
	private String OverSpeedRecord_ID;
	/** Not-null value. */
	private String CheckPoint_ID;
	/** Not-null value. */
	private String BeginDT;
	/** Not-null value. */
	private String EndDT;
	private String Line_ID;
	private String LimitSpeed;
	private String TopSpeed;
	private String OverTimeLong;

	public OverSpeedRecordInfo() {
	}

	public OverSpeedRecordInfo(String CheckPoint_ID) {
		this.CheckPoint_ID = CheckPoint_ID;
	}

	public OverSpeedRecordInfo(String OverSpeedRecord_ID, String CheckPoint_ID,
			String BeginDT, String EndDT,String Line_ID, String LimitSpeed, 
			String TopSpeed, String OverTimeLong) {
		this.OverSpeedRecord_ID = OverSpeedRecord_ID;
		this.CheckPoint_ID = CheckPoint_ID;
		this.BeginDT = BeginDT;
		this.EndDT = EndDT;
		this.Line_ID = Line_ID;
		this.LimitSpeed = LimitSpeed;
		this.TopSpeed = TopSpeed;
		this.OverTimeLong = OverTimeLong;
	}

	/** Not-null value. */
	public String getOverSpeedRecord_ID() {
		return OverSpeedRecord_ID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setOverSpeedRecord_ID(String OverSpeedRecord_ID) {
		this.OverSpeedRecord_ID = OverSpeedRecord_ID;
	}

	/** Not-null value. */
	public String getCheckPoint_ID() {
		return CheckPoint_ID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setCheckPoint_ID(String CheckPoint_ID) {
		this.CheckPoint_ID = CheckPoint_ID;
	}

	/** Not-null value. */
	public String getBeginDT() {
		return BeginDT;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setBeginDT(String BeginDT) {
		this.BeginDT = BeginDT;
	}

	/** Not-null value. */
	public String getEndDT() {
		return EndDT;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setEndDT(String EndDT) {
		this.EndDT = EndDT;
	}

	public void setLimitSpeed(String LimitSpeed) {
		this.LimitSpeed = LimitSpeed;
	}

	public String getLimitSpeed() {
		return LimitSpeed;
	}

	public void setTopSpeed(String TopSpeed) {
		this.TopSpeed = TopSpeed;
	}

	public String getTopSpeed() {
		return TopSpeed;
	}

	public void setOverTimeLong(String OverTimeLong) {
		this.OverTimeLong = OverTimeLong;
	}

	public String getOverTimeLong() {
		return OverTimeLong;
	}

	public void setLine_ID(String line_ID) {
		Line_ID = line_ID;
	}

	public String getLine_ID() {
		return Line_ID;
	}
}
