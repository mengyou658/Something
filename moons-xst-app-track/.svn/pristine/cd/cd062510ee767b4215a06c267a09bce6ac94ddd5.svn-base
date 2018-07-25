package com.moons.xst.buss.cycle;

import java.util.Calendar;
import java.util.Date;

import com.moons.xst.track.bean.Z_DJ_CycleDTL;
import com.moons.xst.track.common.DateTimeHelper;

public class CycleComMethod {

	/**
	 * 当频度为天时跨度开始时间
	 * 
	 * @param cycle
	 * @param cycledtl
	 * @return
	 */
	private static Date CalcStartTimeForDay(Cycle cycle, Z_DJ_CycleDTL cycledtl) {
		Date baseDate = DateTimeHelper.StringToDate(cycle.getDJCycle()
				.getBaseDate_DT());
		int boundValue = cycle.getDJCycle().getBoundValue_NR();
		String cycstart = cycledtl.getCycStart_TX();

		// 离基准时间天数
		int days = Integer.parseInt(cycstart) - 1;
		//第一次开始时间
		Date cBeginDate = DateTimeHelper.AddDays(baseDate, days);

		// 第一次开始时间计算后，再加上频度处理，计算出当前有效的开始时间
		Date cDate = DateTimeHelper.StringToDate(DateTimeHelper.DateToString(cycle.getCurCalDTM(),"yyyy-MM-dd") + " 00:00:00");
		Date cStartDate = cBeginDate;
		while (cBeginDate.compareTo(cDate) < 0) {
			cBeginDate = DateTimeHelper.AddDays(cBeginDate, boundValue);
			if (cBeginDate.compareTo(cDate) <= 0)
				cStartDate = cBeginDate;
		}

		return cStartDate;
	}

	/**
	 * 当频度为周时跨度开始时间
	 * 
	 * @param cycle
	 * @param cycledtl
	 * @return
	 */

	private static Date CalcStartTimeForWeek(Cycle cycle, Z_DJ_CycleDTL cycledtl) {
		Date baseDate = DateTimeHelper.StringToDate(cycle.getDJCycle()
				.getBaseDate_DT());
		int boundValue = cycle.getDJCycle().getBoundValue_NR();
		String cycstart = cycledtl.getCycStart_TX();
		Calendar c = Calendar.getInstance();
		c.setTime(baseDate);

		// 求基准时间为周几,周日为1，周一为2....
		int dayofweek = c.get(Calendar.DAY_OF_WEEK);
		int gap = Integer.parseInt(cycstart.substring(2, 3)) + 1 - dayofweek;
		// 离基准时间天数
		int days = (Integer.parseInt(cycstart.substring(0, 2)) - 1) * 7 + gap;
		//第一次开始时间
		Date cBeginDate = DateTimeHelper.AddDays(baseDate, days);

		// 第一次开始时间计算后，再加上频度处理，计算出当前有效的开始时间
		Date cDate = DateTimeHelper.StringToDate(DateTimeHelper.DateToString(cycle.getCurCalDTM(),"yyyy-MM-dd") + " 00:00:00");
		Date cStartDate = cBeginDate;
		while (cBeginDate.compareTo(cDate) < 0) {
			cBeginDate = DateTimeHelper.AddWeeks(cBeginDate, boundValue);
			if (cBeginDate.compareTo(cDate) <= 0)
				cStartDate = cBeginDate;
		}

		return cStartDate;

	}

	/**
	 * 当频度为旬时跨度开始时间
	 * 
	 * @param cycle
	 * @param cycledtl
	 * @return
	 */
	private static Date CalcStartTimeForXun(Cycle cycle, Z_DJ_CycleDTL cycledtl) {
		Date baseDate = DateTimeHelper.StringToDate(cycle.getDJCycle()
				.getBaseDate_DT());
		int boundValue = cycle.getDJCycle().getBoundValue_NR();
		String cycstart = cycledtl.getCycStart_TX();

		int xunCount = Integer.parseInt(cycstart.substring(0, 2)) - 1;
		int dayCount = Integer.parseInt(cycstart.substring(2, 4));
		// 处理旬的最后一天
		if (dayCount >= 11)
			dayCount = 11;

		Date tempDate = DateTimeHelper.AddXuns(baseDate, xunCount);
        
		int tempDayofXun = DateTimeHelper.GetDayOfXun(tempDate);
		//离基准最近起点旬第一天对应日期
		tempDate = DateTimeHelper.AddDays(tempDate, 0 - tempDayofXun + 1);
		//第一次开始时间
		Date cBeginDate = tempDate;

		// 第一次开始时间计算后，再加上频度处理，计算出当前有效的开始时间
		Date cDate = DateTimeHelper.StringToDate(DateTimeHelper.DateToString(cycle.getCurCalDTM(),"yyyy-MM-dd") + " 00:00:00");
		Date cStartDate = cBeginDate;
		while (cBeginDate.compareTo(cDate) < 0) {
			cBeginDate = DateTimeHelper.AddXuns(cBeginDate, boundValue);
			if (cBeginDate.compareTo(cDate) <= 0)
				cStartDate = cBeginDate;
		}
		
		int maxDayofXun = DateTimeHelper.GetMaxDayofXun(cStartDate);

		if (dayCount > maxDayofXun)
			dayCount = maxDayofXun;

		cBeginDate = DateTimeHelper.AddDays(cStartDate, dayCount - 1);


		return cBeginDate;
	}

	/**
	 * 当频度为月，起点类型为天时计算跨度开始时间
	 * 
	 * @param cBegin
	 *            起点月的第一天对应日期
	 * @param cycstart
	 *            起点方式
	 * @return
	 */
	private static Date CalStartTimeByDayForMonth(Date cFirstDayOfMonth, String cycstart) {
		int days = Integer.parseInt(cycstart.substring(2, 4));

		int maxDayOfMonth = DateTimeHelper.GetMaxDayofMonth(cFirstDayOfMonth);
		if (days > maxDayOfMonth)
			days = maxDayOfMonth;
		Date cBegin = DateTimeHelper.AddDays(cFirstDayOfMonth, days - 1);

		return cBegin;
	}

	/**
	 * 当频度为月，起点类型为周时计算跨度开始时间
	 * 
	 * @param cBegin
	 *            起点月的第一天对应日期
	 * @param cycstart
	 *            起点方式
	 * @return
	 */
	private static Date CalStartTimeByWeekForMonth(Date cFirstDayOfMonth,
			String cycstart) {
		
		// 当月最大天数
		int maxDayOfMonth = DateTimeHelper.GetMaxDayofMonth(cFirstDayOfMonth);
		Calendar c = Calendar.getInstance();
		c.setTime(cFirstDayOfMonth);

		// 求cFirstDayOfMonth为周几
		int dayofweek = c.get(Calendar.DAY_OF_WEEK);
		// 第一周离cFirstDayOfMonth天数，必须 > 0
		int gap = Integer.parseInt(cycstart.substring(3, 4)) + 1 - dayofweek;
		if (gap < 0)
			gap = gap + 7;

		// 起点离cFirstDayOfMonth天数
		int days = (Integer.parseInt(cycstart.substring(2, 3)) - 1) * 7 + gap;
		// 修复增加天数后跨月情况
		if (days > maxDayOfMonth - 1) {
			while (days > maxDayOfMonth - 1) {
				days = days - 7;
			}
		}
		Date cBegin = DateTimeHelper.AddDays(cFirstDayOfMonth, days);

		return cBegin;
	}

	/**
	 * 当频度为月，起点类型为旬时计算跨度开始时间
	 * 
	 * @param cBegin
	 *            起点月的第一天对应日期
	 * @param cycstart
	 *            起点方式
	 * @return
	 */
	private static Date CalStartTimeByXunForMonth(Date cFirstDayOfMonth, String cycstart) {

		int xunCount = Integer.parseInt(cycstart.substring(2, 3)) - 1;
		int dayCount = Integer.parseInt(cycstart.substring(3, 5));
		// 处理旬的最后一天
		if (dayCount >= 11)
			dayCount = 11;

		Date tempDate = DateTimeHelper.AddXuns(cFirstDayOfMonth, xunCount);

		int maxDayofXun = DateTimeHelper.GetMaxDayofXun(tempDate);

		if (dayCount > maxDayofXun)
			dayCount = maxDayofXun;

		tempDate = DateTimeHelper.AddDays(tempDate, dayCount - 1);

		Date cBegin = tempDate;

		return cBegin;

	}

	/**
	 * 当频度为季度，起点类型为天时计算跨度开始时间
	 * 
	 * @param cBegin
	 *            起点月的第一天对应日期
	 * @param cycstart
	 *            起点方式
	 * @return
	 */
	private static Date CalStartTimeByDayForQuarter(Date cFirstDayOfMonth, String cycstart) {
		int months = Integer.parseInt(cycstart.substring(2, 4));
		cFirstDayOfMonth = DateTimeHelper.AddMonths(cFirstDayOfMonth,months - 1);
		
		int days = Integer.parseInt(cycstart.substring(4, 6));
		int maxDayOfMonth = DateTimeHelper.GetMaxDayofMonth(cFirstDayOfMonth);
		if (days > maxDayOfMonth)
			days = maxDayOfMonth;
		Date cBegin = DateTimeHelper.AddDays(cFirstDayOfMonth, days - 1);

		return cBegin;
	}

	/**
	 * 当频度为季度，起点类型为周时计算跨度开始时间
	 * 
	 * @param cBegin
	 *            起点月的第一天对应日期
	 * @param cycstart
	 *            起点方式
	 * @return
	 */
	private static Date CalStartTimeByWeekForQuarter(Date cFirstDayOfMonth,
			String cycstart) {
		int months = Integer.parseInt(cycstart.substring(2, 4));
		cFirstDayOfMonth = DateTimeHelper.AddMonths(cFirstDayOfMonth,months - 1);
		
		// 当月最大天数
		int maxDayOfMonth = DateTimeHelper.GetMaxDayofMonth(cFirstDayOfMonth);
		Calendar c = Calendar.getInstance();
		c.setTime(cFirstDayOfMonth);

		// 求cFirstDayOfMonth为周几
		int dayofweek = c.get(Calendar.DAY_OF_WEEK);
		// 第一周离cFirstDayOfMonth天数，必须 > 0
		int gap = Integer.parseInt(cycstart.substring(5, 6)) + 1 - dayofweek;
		if (gap < 0)
			gap = gap + 7;

		// 起点离cFirstDayOfMonth天数
		int days = (Integer.parseInt(cycstart.substring(4, 5)) - 1) * 7 + gap;
		// 修复增加天数后跨月情况
		if (days > maxDayOfMonth - 1) {
			while (days > maxDayOfMonth - 1) {
				days = days - 7;
			}
		}
		Date cBegin = DateTimeHelper.AddDays(cFirstDayOfMonth, days);

		return cBegin;
	}

	/**
	 * 当频度为季度，起点类型为旬时计算跨度开始时间
	 * 
	 * @param cBegin
	 *            起点月的第一天对应日期
	 * @param cycstart
	 *            起点方式
	 * @return
	 */
	private static Date CalStartTimeByXunForQuarter(Date cFirstDayOfMonth, String cycstart) {

		int months = Integer.parseInt(cycstart.substring(2, 4));
		cFirstDayOfMonth = DateTimeHelper.AddMonths(cFirstDayOfMonth,months - 1);
		
		int xunCount = Integer.parseInt(cycstart.substring(4, 5)) - 1;
		int dayCount = Integer.parseInt(cycstart.substring(5, 7));
		// 处理旬的最后一天
		if (dayCount >= 11)
			dayCount = 11;

		Date tempDate = DateTimeHelper.AddXuns(cFirstDayOfMonth, xunCount);

		int maxDayofXun = DateTimeHelper.GetMaxDayofXun(tempDate);

		if (dayCount > maxDayofXun)
			dayCount = maxDayofXun;

		tempDate = DateTimeHelper.AddDays(tempDate, dayCount - 1);

		Date cBegin = tempDate;

		return cBegin;

	}
	
	/**
	 * 当频度为月时跨度开始时间
	 * 
	 * @param cycle
	 * @param cycledtl
	 * @return
	 */
	private static Date CalcStartTimeForMonth(Cycle cycle,
			Z_DJ_CycleDTL cycledtl) {
		Date baseDate = DateTimeHelper.StringToDate(cycle.getDJCycle()
				.getBaseDate_DT());
		int boundValue = cycle.getDJCycle().getBoundValue_NR();
		String cycstart = cycledtl.getCycStart_TX();
		String startWay = cycle.getDJCycle().getStartWay_CD();

		int monthCount = Integer.parseInt(cycstart.substring(0, 2)) - 1;
		Date tempDate = DateTimeHelper.AddMonths(baseDate, monthCount);
		int dayOfMonth = DateTimeHelper.GetDayOfMonth(tempDate);
		// 起点当月第1天对应日期
		Date firstDayStartDate = DateTimeHelper.AddDays(tempDate,
				0 - dayOfMonth + 1);
		// 起点当月第1天对应日期
		Date cBeginDate = firstDayStartDate;
		// 起点当月第1天对应日期计算后，先加上频度处理，计算出当前月第1天对应的日期
		Date cDate = DateTimeHelper.StringToDate(DateTimeHelper.DateToString(cycle.getCurCalDTM(),"yyyy-MM-dd") + " 00:00:00");
		Date cStartDate = firstDayStartDate;
		while (cBeginDate.compareTo(cDate) < 0) {
					cBeginDate = DateTimeHelper.AddMonths(cBeginDate, boundValue);
					if (cBeginDate.compareTo(cDate) <= 0)
						cStartDate = cBeginDate;
				
		}
				
        //根据起点计算出开始日期
		switch (Integer.parseInt(startWay)) {
		// 按天
		case 0:
			cBeginDate = CalStartTimeByDayForMonth(cStartDate, cycstart);
			break;
		// 按周
		case 1:
			cBeginDate = CalStartTimeByWeekForMonth(cStartDate, cycstart);
			break;
		// 按旬
		case 2:
			cBeginDate = CalStartTimeByXunForMonth(cStartDate, cycstart);
			break;
		}

		return cBeginDate;
	}

	/**
	 * 获取指定日期所在季度第一天对应的日期
	 * @param dt
	 * @return
	 */
	private static Date GetFirstDayOfQuarter(Date dt)
	{
		Date firstDayOfQuarterDate = null;
		String yyyyStr = DateTimeHelper.DateToString(dt,"yyyy");
		String mmStr = DateTimeHelper.DateToString(dt,"MM");
		int mm = Integer.parseInt(mmStr);
		if (mm <= 3)
			firstDayOfQuarterDate = DateTimeHelper.StringToDate(yyyyStr + "-" + "01-01 00:00:00");
		else if (mm <= 6)
			firstDayOfQuarterDate = DateTimeHelper.StringToDate(yyyyStr + "-" + "04-01 00:00:00");
		else if (mm <= 9)
			firstDayOfQuarterDate = DateTimeHelper.StringToDate(yyyyStr + "-" + "07-01 00:00:00");
		else 
			firstDayOfQuarterDate = DateTimeHelper.StringToDate(yyyyStr + "-" + "10-01 00:00:00");
		
		return firstDayOfQuarterDate;
	}
	/**
	 * 频度为季度时跨度开始时间
	 * 
	 * @param cycle
	 * @param cycledtl
	 * @return
	 */
	private static Date CalcStartTimeForQuarter(Cycle cycle,
			Z_DJ_CycleDTL cycledtl) {
		Date baseDate = DateTimeHelper.StringToDate(cycle.getDJCycle()
				.getBaseDate_DT());
		
		
		int boundValue = cycle.getDJCycle().getBoundValue_NR();
		String cycstart = cycledtl.getCycStart_TX();
		String startWay = cycle.getDJCycle().getStartWay_CD();
		
		// 起点当月第1天对应日期
		Date tempDate = GetFirstDayOfQuarter(baseDate);
		int quarterCount = Integer.parseInt(cycstart.substring(0, 2)) - 1;
		Date firstDayStartDate = DateTimeHelper.AddQuarter(tempDate, quarterCount);
		// 起点当月第1天对应日期
		Date cBeginDate = firstDayStartDate;
		// 起点当月第1天对应日期计算后，先加上频度处理，计算出当前月第1天对应的日期
		Date cDate = DateTimeHelper.StringToDate(DateTimeHelper.DateToString(cycle.getCurCalDTM(),"yyyy-MM-dd") + " 00:00:00");
		Date cStartDate = firstDayStartDate;
		while (cBeginDate.compareTo(cDate) < 0) {
					cBeginDate = DateTimeHelper.AddQuarter(cBeginDate, boundValue);
					if (cBeginDate.compareTo(cDate) <= 0)
						cStartDate = cBeginDate;
				
		}

		switch (Integer.parseInt(startWay)) {
		// 按天
		case 0:
			cBeginDate = CalStartTimeByDayForQuarter(cStartDate, cycstart);
			break;
		// 按周
		case 1:
			cBeginDate = CalStartTimeByWeekForQuarter(cStartDate, cycstart);
			break;
		// 按旬
		case 2:
			cBeginDate = CalStartTimeByXunForQuarter(cStartDate, cycstart);
			break;
		}

		

		return cBeginDate;

	}

	/**
	 * 根据基准日期和起点设置计算跨度开始时间
	 * 
	 * @param cycle
	 * @param cycledtl
	 * @return
	 */
	private static Date GetStartTime(Cycle cycle, Z_DJ_CycleDTL cycledtl) {
		String boundType = cycle.getDJCycle().getBoundType_CD();
		Date beginDate = null;
		switch (Integer.parseInt(boundType)) {
		// 频度为"天"
		case 0:
			beginDate = CalcStartTimeForDay(cycle, cycledtl);
			break;
		// 频度为"周"
		case 1:
			beginDate = CalcStartTimeForWeek(cycle, cycledtl);
			break;
		// 频度为"旬"
		case 2:
			beginDate = CalcStartTimeForXun(cycle, cycledtl);
			break;
		// 频度为"月"
		case 3:
			beginDate = CalcStartTimeForMonth(cycle, cycledtl);
			break;
		// 频度为"季度"
		case 4:
			beginDate = CalcStartTimeForQuarter(cycle, cycledtl);
			break;
		}
		return beginDate;
	}

	/**
	 * 根据跨度开始时间和结束时间计算有效的时段
	 * @param taskBegin
	 * @param taskEnd
	 * @param cycle
	 * @param cycledtl
	 * @param IsTempCal
	 * @return
	 */
	private static Boolean CalCuteBeginAndcEnd(Date taskBegin, Date taskEnd,
			Cycle cycle, Z_DJ_CycleDTL cycledtl,Boolean IsTempCal) {
		Boolean result = false;
		
		if (IsTempCal)
		{
			cycle.setTempTaskBegin(null);
			cycle.setTempTaskEnd(null);
		}
		/*else
		{
			cycle.setTempTaskBegin(cycle.getTaskBegin());
			cycle.setTempTaskEnd(cycle.getTaskEnd());
		}*/
		// 长周期只做一次，长周期支持时段限制
		if (cycledtl.getTimeMode_CD().equals("0")) {
			Date start = DateTimeHelper.StringToDate(DateTimeHelper
					.DateToString(taskBegin).substring(0, 10)
					+ " 00:00:00");
			Date end = DateTimeHelper.StringToDate(DateTimeHelper
					.DateToString(taskEnd).substring(0, 10)
					+ " 23:59:59");
			String startTime = cycledtl.getStartTimeList_TX().substring(0, 4);
			
			String endTime = cycledtl.getEndTimeList_TX().substring(0, 4);
			
			
			startTime = startTime.substring(0, 2) + ":"
					+ startTime.substring(2, 4) + ":00";
			endTime = endTime.substring(0, 2) + ":" + endTime.substring(2, 4)
					+ ":00";

			int i = 0;
			String shiftNo = cycledtl.getShiftList_TX().substring(i * 1,
					(i + 1));
			String overDay = cycledtl.getOverDayList_TX().substring(i * 1,
					(i + 1));
			String other = cycledtl.getOtherList_TX().substring(i * 1, (i + 1));
			
			Date cDate = DateTimeHelper.StringToDate(DateTimeHelper.DateToString(cycle.getCurCalDTM(),"yyyy-MM-dd") + " 00:00:00");
			
			if (cDate.compareTo(taskBegin) >= 0
					&& cDate.compareTo(taskEnd) <= 0) {
				if (IsTempCal) //临时计算周期，只缓存临时开始时间和结束时间
				{
					cycle.setTempTaskBegin(start);
					cycle.setTempTaskEnd(end);
				}
				else           //碰钮扣计算任务周期
				{
					cycle.setTaskBegin(start);
					cycle.setTaskEnd(end);
					cycle.setStartTime(startTime);
					cycle.setEndTime(endTime);
					cycle.setCurShiftNo(shiftNo);
					cycle.setCurSpanFlag(overDay);
					cycle.setCurHolidayTX(other);
					cycle.SetCurCycleDTL(cycledtl);
					
					cycle.setTempTaskBegin(cycle.getTaskBegin());
					cycle.setTempTaskEnd(cycle.getTaskEnd());
				}
				result = true;
			}
			
		} else // 短周期
		{
			while (taskBegin.compareTo(taskEnd) <= 0) {
				for (int i = 0; i < cycledtl.getCycTime_NR(); i++) {
					String startTime = cycledtl.getStartTimeList_TX()
							.substring(i * 4, (i + 1) * 4);
					String endTime = cycledtl.getEndTimeList_TX().substring(
							i * 4, (i + 1) * 4);
					Date start = DateTimeHelper.StringToDate(DateTimeHelper
							.DateToString(taskBegin).substring(0, 10)
							+ " "
							+ startTime.substring(0, 2)
							+ ":"
							+ startTime.substring(2, 4) + ":00");
					Date end = DateTimeHelper.StringToDate(DateTimeHelper
							.DateToString(taskBegin).substring(0, 10)
							+ " "
							+ endTime.substring(0, 2)
							+ ":"
							+ endTime.substring(2, 4) + ":00");
					String shiftNo = cycledtl.getShiftList_TX().substring(
							i * 1, (i + 1));
					String overDay = cycledtl.getOverDayList_TX().substring(
							i * 1, (i + 1));
					String other = cycledtl.getOtherList_TX().substring(i * 1,
							(i + 1));
					// 处理跨天,对于跨天情况，只要计算出来的开始时间和结束日期段与taskBegin和taskEnd的日期段有交叉都算有效，防止客户做不了造成漏检
					//比方说taskBegin和taskEnd均为2011-01-03，时段定义22:000-8:00做，则2011-01-02 23:00:00可做，2011-01-04 05:00:0也可做
					//这里不要根据周期里定义的跨天属性进行修正，周期里定义的跨天属性仅仅用于计算查询日期，不参与周期逻辑计算
					if (start.compareTo(end) > 0) {
						
					    //当前时间为跨天的前一段
						if (cycle.getCurCalDTM().compareTo(start) > 0)
							end = DateTimeHelper.AddDays(end, 1);
						else //当前时间为跨天的后一段
							start = DateTimeHelper.AddDays(start, -1);
					}
					if (cycle.getCurCalDTM().compareTo(start) >= 0
							&& cycle.getCurCalDTM().compareTo(end) <= 0) {
						if (IsTempCal) //临时计算周期，只缓存临时开始时间和结束时间
						{
							cycle.setTempTaskBegin(start);
							cycle.setTempTaskEnd(end);
						}
						else {
							cycle.setTaskBegin(start);
							cycle.setTaskEnd(end);
							cycle.setStartTime(startTime);
							cycle.setEndTime(endTime);
							cycle.setCurShiftNo(shiftNo);
							cycle.setCurSpanFlag(overDay);
							cycle.setCurHolidayTX(other);
							cycle.SetCurCycleDTL(cycledtl);
							
							cycle.setTempTaskBegin(cycle.getTaskBegin());
							cycle.setTempTaskEnd(cycle.getTaskEnd());
						}
						
						result = true;
						break;
					}
					
				}
				taskBegin = DateTimeHelper.AddDays(taskBegin, 1);
			}
		}
		return result;
	}

	/**
	 * 计算单个有效期段内有效时段
	 * @param cycle
	 * @param cycledtl
	 * @param IsTempCal
	 * @return
	 */
	private static Boolean CalDJCycleDTLTaskTime(Cycle cycle,
			Z_DJ_CycleDTL cycledtl,Boolean IsTempCal) {

		int spanType = Integer.parseInt(cycledtl.getSpanType_CD());
		int spanValue = cycledtl.getSpanValue_NR();

		Date startDate = GetStartTime(cycle, cycledtl);

		Date endDate = null;
        
		switch (spanType) {
		// 跨度为"天"
		case 0:
			endDate = DateTimeHelper.AddDays(startDate, spanValue);
			break;
		// 跨度为"周"
		case 1:
			endDate = DateTimeHelper.AddWeeks(startDate, spanValue);
			break;
		// 跨度为"旬"
		case 2:
			endDate = DateTimeHelper.AddXuns(startDate, spanValue);
			break;
		// 跨度为"月"
		case 3:
			endDate = DateTimeHelper.AddMonths(startDate, spanValue);
			break;
		// 跨度为"季度"
		case 4:
			endDate = DateTimeHelper.AddQuarter(startDate, spanValue);
			break;
		}
		//算出的结束日期要减1天，如1号跨天2天算出来结束日期为3号，但实际结束应该为2号，所以要减1天
        endDate = DateTimeHelper.AddDays(endDate, -1);
		return CalCuteBeginAndcEnd(startDate, endDate, cycle, cycledtl,IsTempCal);

	}
	/**
	 * 计算周期有效时段,默认为碰钮扣计算
	 * 
	 * @param cycle
	 * @param curDate
	 */
	public static void CalCycleTaskTime(Cycle cycle, Date curDate)
	{
		CalCycleTaskTime( cycle,  curDate,false);
	}
	/**
	 * 计算周期有效时段
	 * @param cycle
	 * @param curDate
	 * @param IsTempCal
	 */
	public static void CalCycleTaskTime(Cycle cycle, Date curDate,Boolean IsTempCal) {
		cycle.setCurCalTM(curDate);

		// 如果碰钮扣时间在上次计算的有效时段范围内，直接返回true
		if (cycle.getTaskBegin() != null && cycle.getTaskEnd() != null
				&& curDate.compareTo(cycle.getTaskBegin()) >= 0
				&& curDate.compareTo(cycle.getTaskEnd()) <= 0)
			return;

		for (int i = 0; i < cycle.GetCycleDTL().size(); i++) {

			Z_DJ_CycleDTL cycledtl = cycle.GetCycleDTL().get(i);

			if (CalDJCycleDTLTaskTime(cycle, cycledtl,IsTempCal))
				break;
		}
	}
	/**
	 *计算启停点设置信息对应的删漏检查询条件串，保存时提供给上位机使用
	 * @param index
	 * @return
	 */
    public static String TransSrDelWJInfo(Integer index)
    {
    	String tempStr = "";
    	for(int i = 0; i < 8; i ++)
    	{
    		if (i == index)
    		{
    			tempStr = "0" + tempStr; 
    		}
    		else
    		{
    			tempStr = "_" + tempStr; 
    		}
    	}
    	
        return tempStr;
    }
    /**
     *  根据设置的启停点序号返回设置二进制串
     * @param srRst
     * @return
     */
    public static String TransSrRst(String srDelWjInfo)
    {
    	String tempStr = srDelWjInfo;
    	tempStr = tempStr.replace("0", "1");
    	tempStr = tempStr.replace("_", "0");
    	return tempStr;
    }
}
