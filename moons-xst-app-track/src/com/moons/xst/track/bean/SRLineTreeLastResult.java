package com.moons.xst.track.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SRLINE_TREE_LAST_RESULT.
 */
public class SRLineTreeLastResult {

    /** Not-null value. */
    private String SRControlPoint_ID;
    private String LastSRResult_TM;
    private String LastSRResult_TX;
    private String LastSRMemo_TX;

    public SRLineTreeLastResult() {
    }

    public SRLineTreeLastResult(String SRControlPoint_ID, String LastSRResult_TM, String LastSRResult_TX, String LastSRMemo_TX) {
        this.SRControlPoint_ID = SRControlPoint_ID;
        this.LastSRResult_TM = LastSRResult_TM;
        this.LastSRResult_TX = LastSRResult_TX;
        this.LastSRMemo_TX = LastSRMemo_TX;
    }

    /** Not-null value. */
    public String getSRControlPoint_ID() {
        return SRControlPoint_ID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSRControlPoint_ID(String SRControlPoint_ID) {
        this.SRControlPoint_ID = SRControlPoint_ID;
    }

    public String getLastSRResult_TM() {
        return LastSRResult_TM;
    }

    public void setLastSRResult_TM(String LastSRResult_TM) {
        this.LastSRResult_TM = LastSRResult_TM;
    }

    public String getLastSRResult_TX() {
        return LastSRResult_TX;
    }

    public void setLastSRResult_TX(String LastSRResult_TX) {
        this.LastSRResult_TX = LastSRResult_TX;
    }

    public String getLastSRMemo_TX() {
        return LastSRMemo_TX;
    }

    public void setLastSRMemo_TX(String LastSRMemo_TX) {
        this.LastSRMemo_TX = LastSRMemo_TX;
    }

}