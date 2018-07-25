package com.moons.xst.track.widget.calendarview.utils;

import android.text.TextUtils;

import java.util.Calendar;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;

public class SolarUtil {
	/**
	 * 计算阳历节日
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getSolarHoliday(int year, int month, int day) {
		int md = (int) (month * Math.pow(10, day >= 10 ? 2 : 1) + day);
		String holiday = "";
		switch (md) {
		case 11:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_yuedan);
			break;
		case 214:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_lover);
			break;
		case 38:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_woman);
			break;
		case 312:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_zhishu);
			break;
		case 41:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_yuren);
			break;
		case 51:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_labour);
			break;
		case 54:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_youth);
			break;
		case 512:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_nurse);
			break;
		case 61:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_child);
			break;
		case 71:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_jiandan);
			break;
		case 81:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_jianjun);
			break;
		case 910:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_teacher);
			break;
		case 101:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_National);
			break;
		case 1111:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_bachelordom);
			break;
		case 1224:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_safeness);
			break;
		case 1225:
			holiday = AppContext.baseContext
					.getString(R.string.date_festival_Christmas);
			break;
		}

		if (!TextUtils.isEmpty(holiday)) {
			return holiday;
		}

		if (month == 4) {
			holiday = chingMingDay(year, day);
		} else if (month == 5) {
			if (day == motherFatherDay(year, month, 1)) {
				holiday = AppContext.baseContext
						.getString(R.string.date_festival_mother);
			}
		} else if (month == 6) {
			if (day == motherFatherDay(year, month, 2)) {
				holiday = AppContext.baseContext
						.getString(R.string.date_festival_father);
			}
		}

		return holiday;
	}

	/**
	 * 计算母亲节、父亲节是几号
	 * 
	 * @param year
	 * @param month
	 * @param delta
	 * @return
	 */
	private static int motherFatherDay(int year, int month, int delta) {
		int f = getFirstWeekOfMonth(year, month - 1);
		f = f == 0 ? 7 : f;
		return 7 - f + 1 + 7 * delta;
	}

	public static String chingMingDay(int year, int day) {
		String holiday = "";
		if (day >= 4 && day <= 6) {
			if (year <= 1999) {
				int compare = (int) (((year - 1900) * 0.2422 + 5.59) - ((year - 1900) / 4));
				if (compare == day) {
					holiday = AppContext.baseContext
							.getString(R.string.date_festival_qingming);
				}
			} else {
				int compare = (int) (((year - 2000) * 0.2422 + 4.81) - ((year - 2000) / 4));
				if (compare == day) {
					holiday = AppContext.baseContext
							.getString(R.string.date_festival_qingming);
				}
			}
		}
		return holiday;
	}

	/**
	 * 计算指定月份的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
				return 29;
			} else {
				return 28;
			}
		default:
			return -1;
		}
	}

	/**
	 * 计算当月1号是周几
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getFirstWeekOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 计算当前日期
	 * 
	 * @return
	 */
	public static int[] getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		return new int[] { calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH) };
	}
}
