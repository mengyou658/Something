package com.moons.xst.track.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table Z__DATA_CODE_GROUP_ITEM.
 */
public class Z_DataCodeGroupItem {

    /** Not-null value. */
    private String Item_ID;
    private int DataCodeGroup_ID;
    private String Item_CD;
    private int SortNo_NR;
    /** Not-null value. */
    private String Name_TX;
    private Integer AlarmLevel_ID;
    /** Not-null value. */
    private String Status_CD;

    public Z_DataCodeGroupItem() {
    }

    public Z_DataCodeGroupItem(String Item_ID, int DataCodeGroup_ID, String Item_CD, int SortNo_NR, String Name_TX, Integer AlarmLevel_ID, String Status_CD) {
        this.Item_ID = Item_ID;
        this.DataCodeGroup_ID = DataCodeGroup_ID;
        this.Item_CD = Item_CD;
        this.SortNo_NR = SortNo_NR;
        this.Name_TX = Name_TX;
        this.AlarmLevel_ID = AlarmLevel_ID;
        this.Status_CD = Status_CD;
    }

    /** Not-null value. */
    public String getItem_ID() {
        return Item_ID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setItem_ID(String Item_ID) {
        this.Item_ID = Item_ID;
    }

    public int getDataCodeGroup_ID() {
        return DataCodeGroup_ID;
    }

    public void setDataCodeGroup_ID(int DataCodeGroup_ID) {
        this.DataCodeGroup_ID = DataCodeGroup_ID;
    }

    public String getItem_CD() {
        return Item_CD;
    }

    public void setItem_CD(String Item_CD) {
        this.Item_CD = Item_CD;
    }

    public int getSortNo_NR() {
        return SortNo_NR;
    }

    public void setSortNo_NR(int SortNo_NR) {
        this.SortNo_NR = SortNo_NR;
    }

    /** Not-null value. */
    public String getName_TX() {
        return Name_TX;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName_TX(String Name_TX) {
        this.Name_TX = Name_TX;
    }

    public Integer getAlarmLevel_ID() {
        return AlarmLevel_ID;
    }

    public void setAlarmLevel_ID(Integer AlarmLevel_ID) {
        this.AlarmLevel_ID = AlarmLevel_ID;
    }

    /** Not-null value. */
    public String getStatus_CD() {
        return Status_CD;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStatus_CD(String Status_CD) {
        this.Status_CD = Status_CD;
    }

}