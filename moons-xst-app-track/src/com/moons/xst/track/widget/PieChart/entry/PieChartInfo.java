package com.moons.xst.track.widget.PieChart.entry;
/**
 *
 * 类名称：PieChartInfo
 * 类描述：构建饼形图实体类
 * 创建人：yanruyi
 * 修改人：yanruyi
 * 修改时间：2016年2月23日 下午4:33:47
 * 修改备注：
 * @version 1.0.0
 *
 */
public class PieChartInfo {
	
	//项名称
	private String itemName;
	//项值
	private float itemValue;
	//项颜色
	private String itemColor;
	//项所在角度
	private float itemAngle;
	//项开始角度
	private float itemBeginAngle;
	//项的比例
	private float itemScale;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public float getItemValue() {
		return itemValue;
	}
	public void setItemValue(float itemValue) {
		this.itemValue = itemValue;
	}
	public String getItemColor() {
		return itemColor;
	}
	public void setItemColor(String itemColor) {
		this.itemColor = itemColor;
	}
	public float getItemAngle() {
		return itemAngle;
	}
	public void setItemAngle(float itemAngle) {
		this.itemAngle = itemAngle;
	}
	public float getItemBeginAngle() {
		return itemBeginAngle;
	}
	public void setItemBeginAngle(float itemBeginAngle) {
		this.itemBeginAngle = itemBeginAngle;
	}
	public float getItemScale() {
		return itemScale;
	}
	public void setItemScale(float itemScale) {
		this.itemScale = itemScale;
	}
	
}
