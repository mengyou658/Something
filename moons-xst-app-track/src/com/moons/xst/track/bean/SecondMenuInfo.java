/**
 * 
 */
package com.moons.xst.track.bean;


/**
 * @author lkz
 * 
 */
public class SecondMenuInfo extends Entity {
	private int MenuItemID;// ID

	public int getMenuItemID() {
		return MenuItemID;
	}

	public void setMenuItemID(int MenuItemID) {
		this.MenuItemID = MenuItemID;
	}

	private String MenuItemDesc;// 描述

	public String getMenuItemDesc() {
		return MenuItemDesc;
	}

	public void setMenuItemDesc(String desc) {
		this.MenuItemDesc = desc;
	}

	private String MenuItemTime;// 时间

	public String getMenuItemTime() {
		return MenuItemTime;
	}

	public void setMenuItemTime(String todotime) {
		this.MenuItemTime = todotime;
	}

	private int childrenCount;// 子目录数量

	public int getChildrenCount() {
		return this.childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}

	private Integer menuItemLevel;

	/**
	 * 目录级别
	 * 
	 */
	public Integer getMenuItemLevel() {
		return this.menuItemLevel;
	}

	/**
	 * 目录级别
	 */
	public void setMenuItemLevel(Integer menuItemLevel) {
		this.menuItemLevel = menuItemLevel;
	}

	private Object menuItemEntity;

	/**
	 * 目录对象
	 * 
	 */
	public Object getMenuItemEntity() {
		return this.menuItemEntity;
	}

	/**
	 * 目录对象
	 */
	public void setMenuItemEntity(Object menuItemEntity) {
		this.menuItemEntity = menuItemEntity;
	}
}
