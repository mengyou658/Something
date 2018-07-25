package com.moons.xst.track.bean;

public class KeyValueInfo {
	public String Key;
	public String Value;
	public String ValueEmptyYN;// 值是否为空

	public KeyValueInfo() {
		super();
	}

	public KeyValueInfo(String key, String value, String valueEmptyYN) {
		super();
		Key = key;
		Value = value;
		ValueEmptyYN = valueEmptyYN;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public String getValueEmptyYN() {
		return ValueEmptyYN;
	}

	public void setValueEmptyYN(String valueEmptyYN) {
		ValueEmptyYN = valueEmptyYN;
	}

}
