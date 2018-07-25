package com.moons.xst.track.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 封装的搜索bean用于传递和使用不同类型的数据源
 */
public class CommonSearchBean<T> implements Serializable {
	/**
	 * 数据源
	 */
	private List<T> list;
	/**
	 * 搜索类型
	 */
	private String searchType;
	/**
	 * 备用的list,兼容部分adapter要传二个list
	 */
	private Object standbyList;
	/**
	 * 搜索提示
	 */
	private String hint;

	public Object getStandbyList() {
		return standbyList;
	}

	public void setStandbyList(Object standbyList) {
		this.standbyList = standbyList;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

}
