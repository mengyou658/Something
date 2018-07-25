package com.moons.xst.track.bean;

public class GPSXXPlan {

	/** Not-null value. */
	private String GpsPointXXItem_ID;
	/** Not-null value. */
	private String GPSPoint_ID;
	/** Not-null value. */
	private String GpsXXItem_ID;
	/** Not-null value. */
	private String Line_ID;
	
	/** Not-null value. */
	private String Name_TX;
	private String XXContent_TX;
	/** Not-null value. */
	private String PipeLine_ID;
	/** Not-null value. */
	private String PipeLine_TX;
	private String Ext1_TX;
	private String Ext2_TX;
	private String Ext3_TX;
	private String Ext4_TX;

	public GPSXXPlan() {
	}

	public GPSXXPlan(String GpsPointXXItem_ID) {
		this.GpsPointXXItem_ID = GpsPointXXItem_ID;
	}

	public GPSXXPlan(String GpsPointXXItem_ID, String GPSPoint_ID,
			String GpsXXItem_ID, String Line_id,String Name_TX, String XXContent_TX,
			String PipeLine_ID, String PipeLine_TX, String Ext1_TX,
			String Ext2_TX, String Ext3_TX, String Ext4_TX) {
		this.GpsPointXXItem_ID = GpsPointXXItem_ID;
		this.GPSPoint_ID = GPSPoint_ID;
		this.GpsXXItem_ID = GpsXXItem_ID;
		this.Name_TX = Name_TX;
		this.XXContent_TX = XXContent_TX;
		this.PipeLine_ID = PipeLine_ID;
		this.PipeLine_TX = PipeLine_TX;
		this.Ext1_TX = Ext1_TX;
		this.Ext2_TX = Ext2_TX;
		this.Ext3_TX = Ext3_TX;
		this.Ext4_TX = Ext4_TX;
		this.Line_ID = Line_id;
	}

	/** Not-null value. */
	public String getGpsPointXXItem_ID() {
		return GpsPointXXItem_ID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setGpsPointXXItem_ID(String GpsPointXXItem_ID) {
		this.GpsPointXXItem_ID = GpsPointXXItem_ID;
	}

	/** Not-null value. */
	public String getGPSPoint_ID() {
		return GPSPoint_ID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setGPSPoint_ID(String GPSPoint_ID) {
		this.GPSPoint_ID = GPSPoint_ID;
	}

	/** Not-null value. */
	public String getGpsXXItem_ID() {
		return GpsXXItem_ID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setGpsXXItem_ID(String GpsXXItem_ID) {
		this.GpsXXItem_ID = GpsXXItem_ID;
	}

	/** Not-null value. */
	public String getName_TX() {
		return Name_TX;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setName_TX(String Name_TX) {
		this.Name_TX = Name_TX;
	}

	public String getXXContent_TX() {
		return XXContent_TX;
	}

	public void setXXContent_TX(String XXContent_TX) {
		this.XXContent_TX = XXContent_TX;
	}

	/** Not-null value. */
	public String getPipeLine_ID() {
		return PipeLine_ID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setPipeLine_ID(String PipeLine_ID) {
		this.PipeLine_ID = PipeLine_ID;
	}

	/** Not-null value. */
	public String getPipeLine_TX() {
		return PipeLine_TX;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setPipeLine_TX(String PipeLine_TX) {
		this.PipeLine_TX = PipeLine_TX;
	}

	public String getExt1_TX() {
		return Ext1_TX;
	}

	public void setExt1_TX(String Ext1_TX) {
		this.Ext1_TX = Ext1_TX;
	}

	public String getExt2_TX() {
		return Ext2_TX;
	}

	public void setExt2_TX(String Ext2_TX) {
		this.Ext2_TX = Ext2_TX;
	}

	public String getExt3_TX() {
		return Ext3_TX;
	}

	public void setExt3_TX(String Ext3_TX) {
		this.Ext3_TX = Ext3_TX;
	}

	public String getExt4_TX() {
		return Ext4_TX;
	}

	public void setExt4_TX(String Ext4_TX) {
		this.Ext4_TX = Ext4_TX;
	}
	public String getLine_ID() {
		return Line_ID;
	}

	public void setLine_ID(String Line_id) {
		this.Line_ID = Line_ID;
	}
}
