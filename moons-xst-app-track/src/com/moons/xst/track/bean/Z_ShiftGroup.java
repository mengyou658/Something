package com.moons.xst.track.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table Z__SHIFT_GROUP.
 */
public class Z_ShiftGroup {

    /** Not-null value. */
    private String ShiftGroup_CD;
    /** Not-null value. */
    private String ShiftGroupName_TX;

    public Z_ShiftGroup() {
    }

    public Z_ShiftGroup(String ShiftGroup_CD, String ShiftGroupName_TX) {
        this.ShiftGroup_CD = ShiftGroup_CD;
        this.ShiftGroupName_TX = ShiftGroupName_TX;
    }

    /** Not-null value. */
    public String getShiftGroup_CD() {
        return ShiftGroup_CD;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setShiftGroup_CD(String ShiftGroup_CD) {
        this.ShiftGroup_CD = ShiftGroup_CD;
    }

    /** Not-null value. */
    public String getShiftGroupName_TX() {
        return ShiftGroupName_TX;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setShiftGroupName_TX(String ShiftGroupName_TX) {
        this.ShiftGroupName_TX = ShiftGroupName_TX;
    }

}
