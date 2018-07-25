package com.moons.xst.track.bean;

public class EventTypeInfo {
	 /** Not-null value. */
    private String DDTYPE_CD;
    /** Not-null value. */
    private String DD_CD;
    /** Not-null value. */
    private String NAME_TX;
    

    public EventTypeInfo() {
    }

   

    public EventTypeInfo(String typeCD, String ddCD, String nameTX) {
        this.DDTYPE_CD = typeCD;
        this.DD_CD = ddCD;
        this.NAME_TX = nameTX;
        
    }

    /** Not-null value. */
    public String getDDTYPE_CD() {
        return DDTYPE_CD;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDDTYPE_CD(String ddtyp) {
        this.DDTYPE_CD = ddtyp;
    }

    /** Not-null value. */
    public String getDD_CD() {
        return DD_CD;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDD_CD(String ddCD) {
        this.DD_CD = ddCD;
    }

    /** Not-null value. */
    public String getNAME_TX() {
        return NAME_TX;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNAME_TX(String nameTX) {
        this.NAME_TX = nameTX;
    }

    
}
