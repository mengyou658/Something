package com.moons.xst.track.bean;

/**
 * 实体类
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public abstract class Entity extends Base {

	protected int id;

	public int getId() {
		return id;
	}

	protected String cacheKey;

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
}
