package com.moons.xst.track.widget.PieChart.entry;

import java.io.Serializable;

/**
 *
 * 类名称：PieChartDetail
 * 类描述：饼形图基本项
 * 创建人：yanruyi
 * 修改人：yanruyi
 * 修改时间：2016年3月8日 上午10:00:01
 * 修改备注：
 * @version 1.0.0
 *
 */
public class PieChartDetail implements Serializable{

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String perAmount;
	private String perBase;
	
	public String getPerAmount() {
		return perAmount;
	}
	public void setPerAmount(String perAmount) {
		this.perAmount = perAmount;
	}
	public String getPerBase() {
		return perBase;
	}
	public void setPerBase(String perBase) {
		this.perBase = perBase;
	}
	
	

}
