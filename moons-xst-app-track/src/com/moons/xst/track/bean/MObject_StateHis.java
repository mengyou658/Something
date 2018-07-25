package com.moons.xst.track.bean;
// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MOBJECT__STATE_HIS.
 */
public class MObject_StateHis {

    /** Not-null value. */
    private String StartAndEndPoint_ID;
    /** Not-null value. */
    private String CompleteTime_DT;
    private String MObjectState_CD;
    /** Not-null value. */
    private String MObjectStateName_TX;
    private String AppUser_CD;
    private String DataState_YN;
    private String TransInfo_TX;

    public MObject_StateHis() {
    }

    public MObject_StateHis(String StartAndEndPoint_ID, String CompleteTime_DT, String MObjectState_CD, String MObjectStateName_TX, String AppUser_CD, String DataState_YN, String TransInfo_TX) {
        this.StartAndEndPoint_ID = StartAndEndPoint_ID;
        this.CompleteTime_DT = CompleteTime_DT;
        this.MObjectState_CD = MObjectState_CD;
        this.MObjectStateName_TX = MObjectStateName_TX;
        this.AppUser_CD = AppUser_CD;
        this.DataState_YN = DataState_YN;
        this.TransInfo_TX = TransInfo_TX;
    }

    /** Not-null value. */
    public String getStartAndEndPoint_ID() {
        return StartAndEndPoint_ID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStartAndEndPoint_ID(String StartAndEndPoint_ID) {
        this.StartAndEndPoint_ID = StartAndEndPoint_ID;
    }

    /** Not-null value. */
    public String getCompleteTime_DT() {
        return CompleteTime_DT;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCompleteTime_DT(String CompleteTime_DT) {
        this.CompleteTime_DT = CompleteTime_DT;
    }

    public String getMObjectState_CD() {
        return MObjectState_CD;
    }

    public void setMObjectState_CD(String MObjectState_CD) {
        this.MObjectState_CD = MObjectState_CD;
    }

    /** Not-null value. */
    public String getMObjectStateName_TX() {
        return MObjectStateName_TX;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMObjectStateName_TX(String MObjectStateName_TX) {
        this.MObjectStateName_TX = MObjectStateName_TX;
    }

    public String getAppUser_CD() {
        return AppUser_CD;
    }

    public void setAppUser_CD(String AppUser_CD) {
        this.AppUser_CD = AppUser_CD;
    }

    public String getDataState_YN() {
        return DataState_YN;
    }

    public void setDataState_YN(String DataState_YN) {
        this.DataState_YN = DataState_YN;
    }

    public String getTransInfo_TX() {
        return TransInfo_TX;
    }

    public void setTransInfo_TX(String TransInfo_TX) {
        this.TransInfo_TX = TransInfo_TX;
    }

}