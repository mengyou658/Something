package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.Z_Shift;
import com.moons.xst.track.bean.Z_ShiftGroup;
import com.moons.xst.track.bean.Z_ShiftRule;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.dao.Z_ShiftDao;
import com.moons.xst.track.dao.Z_ShiftGroupDao;
import com.moons.xst.track.dao.Z_ShiftRuleDao;

public class DJShiftHelper {

	InitDJsqlite init = new InitDJsqlite();
	private DJDAOSession djdaoSession;

	private Z_ShiftDao z_ShiftDao;
	private Z_ShiftRuleDao z_ShiftRuleDao;
	private Z_ShiftGroupDao z_ShiftGroupDao;

	List<Z_Shift> shifts = new ArrayList<Z_Shift>();
	List<Z_ShiftRule> shiftrules = new ArrayList<Z_ShiftRule>();
	List<Z_ShiftGroup> shiftgroups = new ArrayList<Z_ShiftGroup>();

	public String getShiftCDByTime(Context context, Date completeTime) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext())
					.getDJSession(AppContext.GetCurrLineID());
			z_ShiftDao = djdaoSession.getZ_ShiftDao();
			shifts = z_ShiftDao.loadAll();
			if (shifts.size() == 0) {
				return "0";
			}
			String shiftcd = "0";
			Date startTime = DateTimeHelper.GetDateTimeNow();
			Date endTime = DateTimeHelper.GetDateTimeNow();
			for (Z_Shift shift : shifts) {
				String tempStart = DateTimeHelper.getDateNow() + " " + shift.getStartTime_TX() + ":00";
				String tempEnd = DateTimeHelper.getDateNow() + " " + shift.getEndTime_TX() + ":00";
				startTime = DateTimeHelper.StringToDate(
						tempStart, "yyyy-MM-dd HH:mm:ss");
				endTime = DateTimeHelper.StringToDate(
						tempEnd, "yyyy-MM-dd HH:mm:ss");
				if (startTime.compareTo(endTime) > 0) {
					if (completeTime.compareTo(startTime) <= 0) {
						startTime = DateTimeHelper.AddDays(startTime, -1);
					} else {
						endTime = DateTimeHelper.AddDays(startTime, 1);
					}
				}

				if (completeTime.compareTo(startTime) > 0
						&& completeTime.compareTo(endTime) <= 0) {
					shiftcd = shift.getShift_CD();
					break;
				}

			}
			return shiftcd;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "0";
		}

	}

	public String getShiftNameByCD(Context context, String shiftcd) {
		djdaoSession = ((AppContext) context.getApplicationContext())
				.getDJSession(AppContext.GetCurrLineID());
		z_ShiftDao = djdaoSession.getZ_ShiftDao();
		shifts = z_ShiftDao.loadAll();
		String shiftname = "";
		for (Z_Shift shift : shifts) {
			if (shift.getShift_CD().equals(shiftcd)) {
				shiftname = shift.getName_TX();
			}
		}
		return shiftname;
	}

	public String computeNowShiftGroupName(Context context, Date completeTime,
			String shiftcd) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext())
					.getDJSession(AppContext.GetCurrLineID());
			z_ShiftRuleDao = djdaoSession.getZ_ShiftRuleDao();
			shiftrules = z_ShiftRuleDao.loadAll();
			int loopDays = shiftrules.size() / shifts.size();
			if (shiftrules.size() <= 0) {
				return "";
			}
			Date startDate = DateTimeHelper.StringToDate(shiftrules.get(0)
					.getWork_DT(), "yyyy-MM-dd HH:mm:ss");
			long spanDays = (completeTime.getTime() - startDate.getTime())
					/ (1000 * 60 * 60 * 24);
			int modeDay = 0;
			if (completeTime.compareTo(startDate) < 0
					&& Math.abs(spanDays) % loopDays != 0) {
				modeDay = (int) (loopDays - Math.abs(spanDays) % loopDays);
			} else {
				modeDay = (int) (Math.abs(spanDays) % loopDays);
			}
			Date ruleDate = DateTimeHelper.AddDays(startDate, modeDay);
			String shiftGroupCD = "";
			String shiftGroupName = "";
			for (Z_ShiftRule rule : shiftrules) {
				if (DateTimeHelper.StringToDate(rule.getWork_DT(),
						"yyyy-MM-dd HH:mm:ss").compareTo(ruleDate) == 0
						&& rule.getShift_CD().equals(shiftcd)) {
					shiftGroupCD = rule.getShiftGroup_CD();
					break;
				}
			}

			if (!shiftGroupCD.equals("")) {
				shiftGroupName = getShiftGroupNameByCD(context, shiftGroupCD);
			}
			return shiftGroupName;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}

	}

	private String getShiftGroupNameByCD(Context context, String cd) {
		String result = "";
		djdaoSession = ((AppContext) context.getApplicationContext())
				.getDJSession(AppContext.GetCurrLineID());
		z_ShiftGroupDao = djdaoSession.getZ_ShiftGroupDao();
		shiftgroups = z_ShiftGroupDao.loadAll();
		if (shiftgroups.size() == 0) {
			return result;
		}
		for (Z_ShiftGroup group : shiftgroups) {
			if (group.getShiftGroup_CD().equals(cd)) {
				result = group.getShiftGroupName_TX();
				break;
			}
		}
		return result;
	}

	static DJShiftHelper _intance;

	public static DJShiftHelper GetIntance() {
		if (_intance == null) {
			_intance = new DJShiftHelper();
		}
		return _intance;
	}
}
