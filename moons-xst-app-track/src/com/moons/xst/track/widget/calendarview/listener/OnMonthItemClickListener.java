package com.moons.xst.track.widget.calendarview.listener;

import com.moons.xst.track.widget.calendarview.DateBean;

import android.view.View;

/**
 * 日期点击接口
 */
public interface OnMonthItemClickListener {
	/**
	 * @param view
	 * @param date
	 */
	void onMonthItemClick(View view, DateBean date);
}
