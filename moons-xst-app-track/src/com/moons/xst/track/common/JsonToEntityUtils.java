package com.moons.xst.track.common;

import java.lang.reflect.Type;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.EventTypeInfo;
import com.moons.xst.track.bean.GPSXXTempTask;
import com.moons.xst.track.bean.OperateBillBaseInfo;
import com.moons.xst.track.bean.ReturnResultInfo;

public class JsonToEntityUtils {

	public static GPSXXTempTask[] jsontoTempTask(String json) {
		Gson gson = new Gson();  
		Type listType = new TypeToken<LinkedList<GPSXXTempTask>>(){}.getType(); 
		LinkedList<GPSXXTempTask> users = gson.fromJson(json, listType); 
		GPSXXTempTask[] gpsxxTempTaskstu = gson.fromJson(json, GPSXXTempTask[].class); 
		if (gpsxxTempTaskstu.length == 0) {
			return null;
		}
		return gpsxxTempTaskstu;
		
	}
	/**
	 * 事件类型json字符串转换为事件类型对象
	 * @param json
	 * @return
	 */
	public static EventTypeInfo[] jsontoEventTypeInfo(String json)
	{
		Gson gson = new Gson();  
		Type listType = new TypeToken<LinkedList<EventTypeInfo>>(){}.getType(); 
		LinkedList<EventTypeInfo> users = gson.fromJson(json, listType); 
		EventTypeInfo[] eventTypeInfoList = gson.fromJson(json, EventTypeInfo[].class); 
		if (eventTypeInfoList.length == 0) {
			return null;
		}
		return eventTypeInfoList;
	}
	
	/**
	 * 操作票json字符串转换为操作票基本信息对象
	 * @param json
	 * @return
	 */
	public static OperateBillBaseInfo[] jsontoOperateBillInfo(String json) {
		Gson gson = new Gson();  
		Type listType = new TypeToken<LinkedList<OperateBillBaseInfo>>(){}.getType(); 
		LinkedList<OperateBillBaseInfo> users = gson.fromJson(json, listType); 
		OperateBillBaseInfo[] operateBillInfoList = gson.fromJson(json, OperateBillBaseInfo[].class); 
		if (operateBillInfoList.length == 0) {
			return null;
		}
		return operateBillInfoList;
	}
	
	/**
	 * 调用WS返回的结果信息JSON转换成对象
	 * @param json
	 * @return
	 */
	public static ReturnResultInfo jsontoReturnResultInfo(String json) {
		ReturnResultInfo returnInfo = new ReturnResultInfo();
		try {
			Gson gson = new Gson();
			returnInfo = gson.fromJson(json, ReturnResultInfo.class);
			return returnInfo;
		} catch (Exception e) {
			returnInfo.setResult_YN(false);
			returnInfo.setError_Message_TX(e.getMessage());
			return returnInfo;
		}
	}
}
