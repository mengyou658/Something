package com.moons.xst.track.widget.calendarview;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import com.moons.xst.track.R;
import com.moons.xst.track.widget.calendarview.listener.CalendarViewAdapter;
import com.moons.xst.track.widget.calendarview.utils.CalendarUtil;
import com.moons.xst.track.widget.calendarview.utils.SolarUtil;

public class CalendarPagerAdapter extends PagerAdapter { // 缓存上一次回收的MonthView
	private LinkedList<MonthView> cache = new LinkedList<MonthView>();
	private SparseArray<MonthView> mViews = new SparseArray<MonthView>();

	private int count;

	private int[] dateStart;
	private int[] dateInit;

	private boolean showLastNext;
	private boolean showLunar;
	private boolean showHoliday;
	private boolean showTerm;
	private boolean disableBefore;
	private int colorSolar;
	private int colorLunar;
	private int colorHoliday;
	private int colorChoose;
	private int sizeSolar;
	private int sizeLunar;
	private int dayBg;
	private boolean dateRestrictions;
	int endDayBg;

	private int item_layout;
	private CalendarViewAdapter calendarViewAdapter;

	public CalendarPagerAdapter(int count) {
		this.count = count;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		MonthView view;
		if (!cache.isEmpty()) {
			view = cache.removeFirst();
		} else {
			view = new MonthView(container.getContext());
		}
		// 根据position计算对应年、月
		int[] date = CalendarUtil.positionToDate(position, dateStart[0],
				dateStart[1]);
		view.setAttrValues(dateInit, showLastNext, showLunar, showHoliday,
				showTerm, disableBefore, colorSolar, colorLunar, colorHoliday,
				colorChoose, sizeSolar, sizeLunar, dayBg, position,
				dateRestrictions, endDayBg);
		view.setOnCalendarViewAdapter(item_layout, calendarViewAdapter);
		// 当月天数
		int maxDay = SolarUtil.getMonthDays(date[0], date[1]);
		// 需要展示的日期数据
		List<DateBean> dates = CalendarUtil.getMonthDate(date[0], date[1]);
		view.setDateList(dates, maxDay);
		// view.setTag(R.id.date_year, date[0]);
		// view.setTag(R.id.date_month, date[1]);
		view.setTag(R.id.date_max_day, maxDay);
		view.setTag(R.id.date_show_data, dates);
		view.setTag(R.id.date_pagination, position);
		mViews.put(position, view);
		container.addView(view);

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((MonthView) object);
		cache.addLast((MonthView) object);
		mViews.remove(position);
	}

	/**
	 * 获得ViewPager缓存的View
	 * 
	 * @return
	 */
	public SparseArray<MonthView> getViews() {
		return mViews;
	}

	public void setAttrValues(int[] dateInit, int[] dateStart,
			boolean showLastNext, boolean showLunar, boolean showHoliday,
			boolean showTerm, boolean disableBefore, int colorSolar,
			int colorLunar, int colorHoliday, int colorChoose, int sizeSolar,
			int sizeLunar, int dayBg, boolean dateRestrictions, int endDayBg) {
		this.dateInit = dateInit;
		this.dateStart = dateStart;
		this.showLastNext = showLastNext;
		this.showLunar = showLunar;
		this.showHoliday = showHoliday;
		this.showTerm = showTerm;
		this.disableBefore = disableBefore;
		this.colorSolar = colorSolar;
		this.colorLunar = colorLunar;
		this.colorHoliday = colorHoliday;
		this.colorChoose = colorChoose;
		this.sizeSolar = sizeSolar;
		this.sizeLunar = sizeLunar;
		this.dayBg = dayBg;
		this.dateRestrictions = dateRestrictions;
		this.endDayBg = endDayBg;
	}

	public void setOnCalendarViewAdapter(int item_layout,
			CalendarViewAdapter calendarViewAdapter) {
		this.item_layout = item_layout;
		this.calendarViewAdapter = calendarViewAdapter;
	}
}
