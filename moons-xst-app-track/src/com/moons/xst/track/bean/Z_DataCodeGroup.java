package com.moons.xst.track.bean;

import java.util.ArrayList;
import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table Z__DATA_CODE_GROUP.
 */
public class Z_DataCodeGroup {

	/** Not-null value. */
	private String DataCodeGroup_ID;
	/** Not-null value. */
	private String Name_TX;
	/** Not-null value. */
	private String MultiValue_YN;

	public Z_DataCodeGroup() {
	}

	public Z_DataCodeGroup(String DataCodeGroup_ID, String Name_TX,
			String MultiValue_YN) {
		this.DataCodeGroup_ID = DataCodeGroup_ID;
		this.Name_TX = Name_TX;
		this.MultiValue_YN = MultiValue_YN;
	}

	/** Not-null value. */
	public String getDataCodeGroup_ID() {
		return DataCodeGroup_ID;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setDataCodeGroup_ID(String DataCodeGroup_ID) {
		this.DataCodeGroup_ID = DataCodeGroup_ID;
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

	/** Not-null value. */
	public String getMultiValue_YN() {
		return MultiValue_YN;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setMultiValue_YN(String MultiValue_YN) {
		this.MultiValue_YN = MultiValue_YN;
	}

	List<Z_DataCodeGroupItem> _dataCodeGroupItems = new ArrayList<Z_DataCodeGroupItem>();

	public List<Z_DataCodeGroupItem> getDataCodeGroupItems() {
		return _dataCodeGroupItems;
	}

	public void setDataCodeGroupItems(
			ArrayList<Z_DataCodeGroupItem> dataCodeGroupItems) {
		_dataCodeGroupItems = dataCodeGroupItems;
	}
}
