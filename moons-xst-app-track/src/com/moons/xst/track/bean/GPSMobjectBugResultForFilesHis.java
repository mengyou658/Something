package com.moons.xst.track.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table GPSMOBJECT_BUG_RESULT_FOR_FILES_HIS.
 */
public class GPSMobjectBugResultForFilesHis {

    /** Not-null value. */
    private String Result_ID;
    /** Not-null value. */
    private String GUID;
    /** Not-null value. */
    private String File_DT;
    /** Not-null value. */
    private String File_Type;

    public GPSMobjectBugResultForFilesHis() {
    }

    public GPSMobjectBugResultForFilesHis(String GUID) {
        this.GUID = GUID;
    }

    public GPSMobjectBugResultForFilesHis(String Result_ID, String GUID, String File_DT, String File_Type) {
        this.Result_ID = Result_ID;
        this.GUID = GUID;
        this.File_DT = File_DT;
        this.File_Type = File_Type;
    }

    /** Not-null value. */
    public String getResult_ID() {
        return Result_ID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setResult_ID(String Result_ID) {
        this.Result_ID = Result_ID;
    }

    /** Not-null value. */
    public String getGUID() {
        return GUID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    /** Not-null value. */
    public String getFile_DT() {
        return File_DT;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFile_DT(String File_DT) {
        this.File_DT = File_DT;
    }

    /** Not-null value. */
    public String getFile_Type() {
        return File_Type;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFile_Type(String File_Type) {
        this.File_Type = File_Type;
    }

}
