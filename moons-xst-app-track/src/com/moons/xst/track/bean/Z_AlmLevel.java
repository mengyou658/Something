package com.moons.xst.track.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table Z__ALM_LEVEL.
 */
public class Z_AlmLevel {

    private int AlmLevel_ID;
    private String Name_TX;

    public Z_AlmLevel() {
    }

    public Z_AlmLevel(int AlmLevel_ID, String Name_TX) {
        this.AlmLevel_ID = AlmLevel_ID;
        this.Name_TX = Name_TX;
    }

    public int getAlmLevel_ID() {
        return AlmLevel_ID;
    }

    public void setAlmLevel_ID(int AlmLevel_ID) {
        this.AlmLevel_ID = AlmLevel_ID;
    }

    public String getName_TX() {
        return Name_TX;
    }

    public void setName_TX(String Name_TX) {
        this.Name_TX = Name_TX;
    }

}