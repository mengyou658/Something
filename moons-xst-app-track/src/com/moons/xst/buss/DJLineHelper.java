/**
 * DJLineHelper.java
 * @author LKZ
 * @created 2014-12-27
 * */
package com.moons.xst.buss;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.SystemControlHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.DownLoad;
import com.moons.xst.track.dao.DataTransHelper;

public class DJLineHelper {
	private static final String TAG = "DJlineHelper";

	/**
	 * 加载路线列表
	 */
	public void loadCommLineData(String appVer,Context context) {
		try {
			DownLoad downLoad = new DownLoad(context);
			AppContext.CommDJLineBuffer.clear();
			AppContext.SetDownLoadYN(true);
			
			/*// 进行授权个数的判断
			ReturnResultInfo returnInfo = 
					downLoad.APClientInfo(appVer,context, AppContext.GetxjqCD());
			if (!returnInfo.getResult_YN()) {
				UIHelper.ToastMessage(context, returnInfo.getError_Message_TX());
				return;
			}*/
			downLoad.DownLoadSettingXMLFromWS(context, AppContext.GetxjqCD());		
			downLoad.DownLoadModuleXMLFromWS(context, AppContext.GetxjqCD());
			downLoad.DownLoadEventTypeListFromWS(context);
			String djlineXmlString="";
			if (AppContext.getPlanType().equals(AppConst.PlanType_XDJ)) {				
//				djlineXmlString = downLoad.DownLoadLineListFromWS(context,
//						AppContext.GetxjqCD());
				downLoad.DownLoadUserListFromWS(context, AppContext.GetxjqCD());
			} else if (AppContext.getPlanType().equals(AppConst.PlanType_DJPC)) {
				djlineXmlString=FileUtils.read(AppConst.XSTBasePath()
						+ AppConst.DJLineTempXmlFile);
			}
			
			if (!StringUtils.isEmpty(djlineXmlString)) {
				AppContext.CommDJLineBuffer = DataTransHelper
						.ConvertLineFromXML(djlineXmlString);
				List<DJLine> lastDJLineList = DataTransHelper
						.ConvertLineFromXMLFile();
				for (DJLine djLine : AppContext.CommDJLineBuffer) {
					if (lastDJLineList != null) {
						if (lastDJLineList.contains(djLine)) {
							File file=new File(AppConst.XSTDBPath()+"424DB_"+djLine.getLineID()+".sdf");
							if(!file.exists()) {
								djLine.setneedUpdate(true);
							} else {
								djLine.setneedUpdate(false);
							}
						} else {
							djLine.setneedUpdate(djLine.getBuidYN() ? true : false);
						}
					}
					else {
						djLine.setneedUpdate(djLine.getBuidYN() ? true : false);
					}
				}
			}
			
			if (AppContext.getPlanType().equals(AppConst.PlanType_XDJ)
					&& AppContext.CommDJLineBuffer.size() > 0) {
				// 巡点检才下载节假日
				downLoad.DownLoadWorkDateFromWS(context,  AppContext.GetxjqCD());
			}
			
			// 通信校时
			if (AppContext.getUpdateSysDateType()
						.equals(AppConst.UpdateSysDate_Commu)) {
				String timeString = downLoad.DownLoadSystemDateTimeFromWS();
				if (!StringUtils.isEmpty(timeString)) {
					DateTimeHelper.setSystemDatatime(DateTimeHelper
							.StringToDate(timeString).getTime());
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}
	
	public ReturnResultInfo loadCommLineDataNew(Context context, String appVer) {
		ReturnResultInfo returnInfo = new ReturnResultInfo();
		returnInfo.setResult_YN(true);
		try {
			DownLoad downLoad = new DownLoad(context);
			AppContext.CommDJLineBuffer.clear();
			AppContext.SetDownLoadYN(true);
			
			// 配置文件
			returnInfo = downLoad.DownLoadSettingXMLFromWS(context, AppContext.GetxjqCD());
			if (!returnInfo.getResult_YN()) {
				return returnInfo;
			}
			// 权限文件(暂不使用)
			returnInfo = downLoad.DownLoadModuleXMLFromWS(context, AppContext.GetxjqCD());
			if (!returnInfo.getResult_YN()) {
				return returnInfo;
			}
			// LineList
			String djlineXmlString="";
			if (AppContext.getPlanType().equals(AppConst.PlanType_XDJ)) {
				returnInfo = downLoad.DownLoadLineListFromWS(context,
						AppContext.GetxjqCD());
				if (!returnInfo.getResult_YN()) {
					return returnInfo;
				}
				djlineXmlString = returnInfo.getResult_Content_TX();
				
				// UserList
				returnInfo = downLoad.DownLoadUserListFromWS(context, AppContext.GetxjqCD());
				if (!returnInfo.getResult_YN()) {
					return returnInfo;
				}
			} else if (AppContext.getPlanType().equals(AppConst.PlanType_DJPC)) {
				djlineXmlString=FileUtils.read(AppConst.XSTBasePath()
						+ AppConst.DJLineTempXmlFile);
			}
			
			if (!StringUtils.isEmpty(djlineXmlString)) {
				AppContext.CommDJLineBuffer = DataTransHelper
						.ConvertLineFromXML(djlineXmlString);
				List<DJLine> lastDJLineList = DataTransHelper
						.ConvertLineFromXMLFile();
				for (DJLine djLine : AppContext.CommDJLineBuffer) {
					if (lastDJLineList != null) {
						if (lastDJLineList.contains(djLine)) {
							File file=new File(AppConst.XSTDBPath()+"424DB_"+djLine.getLineID()+".sdf");
							if(!file.exists()) {
								djLine.setneedUpdate(true);
							} else {
								djLine.setneedUpdate(false);
							}
						} else {
							djLine.setneedUpdate(djLine.getBuidYN() ? true : false);
						}
					}
					else {
						djLine.setneedUpdate(djLine.getBuidYN() ? true : false);
					}
				}
			}
			
			if (AppContext.getPlanType().equals(AppConst.PlanType_XDJ)
					&& AppContext.CommDJLineBuffer.size() > 0) {
				// 巡点检才下载节假日
				downLoad.DownLoadWorkDateFromWS(context, AppContext.GetxjqCD());
			}
			
			// 通信校时
			if (AppContext.getUpdateSysDateType()
						.equals(AppConst.UpdateSysDate_Commu)) {
				if (!DateTimeHelper.getCurrentTimeZone().equalsIgnoreCase(AppContext.getTimeZone())) {
					SystemControlHelper.setSystemTimeZone(AppContext.getTimeZone());
				}
				
				String timeString = downLoad.DownLoadSystemDateTimeFromWS();
				if (!StringUtils.isEmpty(timeString)) {
					DateTimeHelper.setSystemDatatime(DateTimeHelper
							.StringToDate(timeString).getTime());
				}
			}
			// GPS巡线事件类型
			downLoad.DownLoadEventTypeListFromWS(context);
		} catch (Exception e) {
			e.printStackTrace();
			returnInfo.setResult_YN(false);
			returnInfo.setError_Message_TX(e.getMessage());
		}
		
		return returnInfo;
	}
}
