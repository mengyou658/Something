package com.moons.xst.track.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SRIN_LKCONTROL_POINT_REL.
 */
public class SRinLKControlPointRel {

    /** Not-null value. */
    private String LKControlPoint_ID;
    /** Not-null value. */
    private String SRControlPoint_ID;

    public SRinLKControlPointRel() {
    }

    public SRinLKControlPointRel(String LKControlPoint_ID, String SRControlPoint_ID) {
        this.LKControlPoint_ID = LKControlPoint_ID;
        this.SRControlPoint_ID = SRControlPoint_ID;
    }

    /** Not-null value. */
    public String getLKControlPoint_ID() {
        return LKControlPoint_ID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLKControlPoint_ID(String LKControlPoint_ID) {
        this.LKControlPoint_ID = LKControlPoint_ID;
    }

    /** Not-null value. */
    public String getSRControlPoint_ID() {
        return SRControlPoint_ID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSRControlPoint_ID(String SRControlPoint_ID) {
        this.SRControlPoint_ID = SRControlPoint_ID;
    }

}