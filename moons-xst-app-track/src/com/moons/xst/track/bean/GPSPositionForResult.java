package com.moons.xst.track.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table GPSPOSITION_FOR_RESULT.
 */
public class GPSPositionForResult {

    private int GPSPosition_ID;
    /** Not-null value. */
    private String GPSPosition_CD;
    /** Not-null value. */
    private String XJQMark;
    /** Not-null value. */
    private String Longitude;
    /** Not-null value. */
    private String Latitude;
    /** Not-null value. */
    private String UTCDateTime;
    /** Not-null value. */
    private String GPSDesc;
    /** Not-null value. */
    private String LineID;
    private int AppUser_ID;
    /** Not-null value. */
    private String AppUserName_TX;
    private int Post_ID;
	private String DistanceTime;

    public GPSPositionForResult() {
    }

    public GPSPositionForResult(int GPSPosition_ID, String GPSPosition_CD, String XJQMark, String Longitude, String Latitude, String UTCDateTime, String GPSDesc, String LineID, int AppUser_ID, String AppUserName_TX, int Post_ID,String DistanceTime) {
        this.GPSPosition_ID = GPSPosition_ID;
        this.GPSPosition_CD = GPSPosition_CD;
        this.XJQMark = XJQMark;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.UTCDateTime = UTCDateTime;
        this.GPSDesc = GPSDesc;
        this.LineID = LineID;
        this.AppUser_ID = AppUser_ID;
        this.AppUserName_TX = AppUserName_TX;
        this.Post_ID = Post_ID;
        this.DistanceTime = DistanceTime;
    }

    public int getGPSPosition_ID() {
        return GPSPosition_ID;
    }

    public void setGPSPosition_ID(int GPSPosition_ID) {
        this.GPSPosition_ID = GPSPosition_ID;
    }

    /** Not-null value. */
    public String getGPSPosition_CD() {
        return GPSPosition_CD;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGPSPosition_CD(String GPSPosition_CD) {
        this.GPSPosition_CD = GPSPosition_CD;
    }

    /** Not-null value. */
    public String getXJQMark() {
        return XJQMark;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setXJQMark(String XJQMark) {
        this.XJQMark = XJQMark;
    }

    /** Not-null value. */
    public String getLongitude() {
        return Longitude;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    /** Not-null value. */
    public String getLatitude() {
        return Latitude;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    /** Not-null value. */
    public String getUTCDateTime() {
        return UTCDateTime;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUTCDateTime(String UTCDateTime) {
        this.UTCDateTime = UTCDateTime;
    }

    /** Not-null value. */
    public String getGPSDesc() {
        return GPSDesc;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGPSDesc(String GPSDesc) {
        this.GPSDesc = GPSDesc;
    }

    /** Not-null value. */
    public String getLineID() {
        return LineID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLineID(String LineID) {
        this.LineID = LineID;
    }

    public int getAppUser_ID() {
        return AppUser_ID;
    }

    public void setAppUser_ID(int AppUser_ID) {
        this.AppUser_ID = AppUser_ID;
    }

    /** Not-null value. */
    public String getAppUserName_TX() {
        return AppUserName_TX;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAppUserName_TX(String AppUserName_TX) {
        this.AppUserName_TX = AppUserName_TX;
    }

    public int getPost_ID() {
        return Post_ID;
    }

    public void setPost_ID(int Post_ID) {
        this.Post_ID = Post_ID;
    }

    public String getDistanceTime() {
        return DistanceTime;
    }

    public void setDistanceTime(String DistanceTime) {
        this.DistanceTime = DistanceTime;
    }
}