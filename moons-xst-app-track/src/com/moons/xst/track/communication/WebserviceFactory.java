package com.moons.xst.track.communication;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.CommUser;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.HtmlRegexpUtils;
import com.moons.xst.track.common.JsonToEntityUtils;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.ResponseLogInfoHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;

/**
 * Webservice调用入口
 * 
 * @author LKZ
 * @version 1.0
 * @created 2014-11-24
 */
public class WebserviceFactory {

	public static String pdaGUID;
	public static final String SERVICE_NAMESPACE = "http://tempuri.org/";

	private static void showMessage(String mesString) {
		Message _msg = Message.obtain();
		_msg.what = 1;
		_msg.obj = mesString;
		if (AppContext.sysLevelMessageHandler != null)
			AppContext.sysLevelMessageHandler.sendMessage(_msg);
	}

	private static void showIcon(String msg) {
		Message _msg = Message.obtain();
		_msg.what = 1;
		_msg.obj = msg;
		if (AppContext.sysLevelIconHandler != null)
			AppContext.sysLevelIconHandler.sendMessage(_msg);
	}

	/**
	 * 检查web服务是否可用
	 * 
	 * @说明：此方法不要高频度循环调用，尤其是后台timer线程中，因为调用此方法会耗费网络流量
	 * 
	 * @author lkz
	 * @return
	 */
	public static Boolean checkWS() {
		try {
			URL url = new URL(AppContext.GetWSAddress());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(10 * 1000);
			con.setReadTimeout(8 * 1000);
			/* GetResponse，若返回OK说明服务地址是好的 */
			if ((con.getResponseMessage() != null)
					&& con.getResponseMessage().toUpperCase().contains("OK"))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 检查外网管线web服务是否可用
	 * 
	 * @author sx
	 * @return
	 */
	public static Boolean checkWSOther() {
		try {
			URL url = new URL(AppContext.getWebServiceAddressForOther());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(8 * 1000);
			con.setReadTimeout(8 * 1000);
			/* GetResponse，若返回OK说明服务地址是好的 */
			if ((con.getResponseMessage() != null)
					&& con.getResponseMessage().toUpperCase().contains("OK"))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取服务端系统时间(格式：yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return
	 */
	public static String getServiceDateTimeOfStr() {
		String methodName = "GetSystemDateTimeForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			Log.i("", e + "");
		}
		return null;
	}
	/**
	 * 获取服务端系统时间(格式：yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return
	 */
	public static String getXJQInfo(String GUID) {
		String methodName = "GetXJQInfoForAndroid";
		try {
			if (StringUtils.isEmpty(GUID))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("GUID", GUID);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			Log.i("", e + "");
		}
		return null;
	}
	/**
	 * 获取标示符下路线列表信息
	 * 
	 * @param context
	 * @param xjqCD
	 * @return
	 */
	public static ReturnResultInfo getlineInfo(Context context, String xjqCD) {
		String methodName = "GetLineInfoByxjqCDForAndriod";
		ReturnResultInfo returnInfo = new ReturnResultInfo();
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				returnInfo.setResult_YN(true);
				returnInfo.setResult_Content_TX(detail);
			} else {
				returnInfo.setResult_YN(false);
				returnInfo.setError_Message_TX("[" + methodName + "]" + emsgString);
			}
		} catch (Exception e) {
			returnInfo.setResult_YN(false);
			returnInfo.setError_Message_TX("[" + methodName + "]" + e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 获取服务端路线变动信息
	 * 
	 * @param context
	 * @param xjqCD
	 * @return （NO-无变动，YES-有变动 ，其他信息-为报错）
	 */
	public static String getlineChangeInfo(Context context) {
		String methodName = "GetLineChangeInfoForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return null;
	}

	public static String GetNeedUploadFileInfo() {
		String methodName = "GetNeedUploadFileInfoForAndriod";
		try {

			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);

			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return "";
	}

	/*public static void APClientInfo(String appVer, Context context, String xjqCD) {
		String methodName = "APClientInfoForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(xjqCD))
				return;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			soapObject.addProperty("infoStrList", "Android_" + appVer);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);

		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
	}*/
	
	public static ReturnResultInfo APClientInfo(String appVer, Context context, String xjqCD) {
		String methodName = "APClientInfoForAndroid";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(xjqCD)) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return resultInfo;
			}
				
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			soapObject.addProperty("infoStrList", appVer);
			soapObject.addProperty("hardwareInfo", AppContext.getHardwareInfo());
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			//SoapObject result = (SoapObject) envelope.getResponse();
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			
			return resultInfo;
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setResult_YN(false);
			resultInfo.setResult_Content_TX("");
			resultInfo.setError_Message_TX(e.getMessage());
			return resultInfo;
		}
	}

	/**
	 * 获取标识符下的用户信息
	 * 
	 * @param context 
	 * @param xjqCD
	 * @return
	 */
	public static ReturnResultInfo getuserInfo(Context context, String xjqCD) {
		String methodName = "GetUserInfoByxjqCDForAndriod";
		ReturnResultInfo returnInfo = new ReturnResultInfo();
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				returnInfo.setResult_YN(true);
				returnInfo.setResult_Content_TX(detail);
				
			} else {
				returnInfo.setResult_YN(false);
				returnInfo.setError_Message_TX("[" + methodName + "]" + emsgString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnInfo.setResult_YN(false);
			returnInfo.setError_Message_TX("[" + methodName + "]" + e.getMessage());
			
		}
		return returnInfo;
	}

	/**
	 * 获取标识符下的节假日信息
	 * 
	 * @param context
	 * @param xjqCD
	 * @return
	 */
	public static String getWorkDate(Context context, String xjqCD) {
		String methodName = "GetWorkDateByxjqCDForAndriod";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(xjqCD))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("xjqCD", xjqCD);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取服务端APP发布的版本信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionInfo(Context context, String model) {
		String methodName = "GetAPVersionByDeviceForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("appType", "moons-xst-app-track");
			soapObject.addProperty("deviceType", model);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			envelope.encodingStyle = "utf-8";
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			String message = e.getMessage();
		}
		return null;
	}

	public static String getDJPCItemList(String json) throws Exception {
		String methodName = "GetDJPCItemList";
		HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
				60000);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE, methodName);
		soapObject.addProperty("Casejson", json);
		soapObject.addProperty("emsg", "");

		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		ht.call(SERVICE_NAMESPACE + methodName, envelope);
		SoapObject result = (SoapObject) envelope.bodyIn;
		String detail = result.getPropertyAsString(methodName + "Result");
		return detail;
	}

	// 获取点检排程查询条件
	public static String getDJPCCaseItemList(Context context) {
		String methodName = "GetDJPCCaseItemList";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);

			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			String message = e.getMessage();
		}
		return null;
	}
	
	//接口获取点检排程userList和LineList
	public static ReturnResultInfo getDJPCLineAndUserXML(String line){
		String methodName = "getDJPCLineAndUserXML";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		resultInfo.setResult_YN(false);
		resultInfo.setResult_Content_TX("");
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("line", line);
			soapObject.addProperty("GUID", AppContext.GetUniqueID());

			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
		} catch (Exception e) {
			resultInfo.setError_Message_TX(e.getMessage());
		}
		return resultInfo;
	}

	// 接口标准化点检排程路线
	public static ReturnResultInfo StartDownDJPCLineForInterface(String json) {
		String methodName = "StartDownDJPCLineForInterface";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		resultInfo.setResult_YN(false);
		resultInfo.setResult_Content_TX("");
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("json", json);
			soapObject.addProperty("GUID", AppContext.GetUniqueID());

			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
		} catch (Exception e) {
			resultInfo.setError_Message_TX(e.getMessage());
		}
		return resultInfo;
	}

	// 判断是2641还是第三方系统
	public static String GetCustomType() {
		String methodName = "GetCustomType";
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("emsg", "");
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			// SoapObject result = (SoapObject) envelope.getResponse();

			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;
		} catch (Exception e) {
			String message = e.getMessage();
			return message;
		}
	}

	// 点检排程下载路线xml以及标准化路线
	public static ReturnResultInfo AndroidInitDJPCLine(String json) {
		String methodName = "StartDownLoadDJPCNew";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		resultInfo.setResult_YN(false);
		resultInfo.setResult_Content_TX("");
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("json", json);
			soapObject.addProperty("xjqCD",AppContext.GetUniqueID()+"|"+AppContext.GetxjqCD());
			soapObject.addProperty("emsg", "");
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);

			SoapObject result = (SoapObject) envelope.bodyIn;
			// SoapObject result = (SoapObject) envelope.getResponse();

			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			return resultInfo;
		} catch (Exception e) {
			String message = e.getMessage();
			resultInfo.setError_Message_TX(message);
		}
		return resultInfo;
	}

	// 根据用户获取路线
	/*
	 * public static Object getLineListByUserListWithXJQGUID(Context
	 * context,String json) { String methodName =
	 * "AndroidGetLineListByUserListWithXJQGUID"; try { if
	 * (StringUtils.isEmpty(AppContext.getMac())) return null; HttpTransportSE
	 * ht = new HttpTransportSE(AppContext.GetWSAddress(), 60000); ht.debug =
	 * true; SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
	 * SoapEnvelope.VER11); SoapObject soapObject = new
	 * SoapObject(SERVICE_NAMESPACE, methodName); soapObject.addProperty("json",
	 * json); soapObject.addProperty("XJQGUID", AppContext.getMac());
	 * 
	 * envelope.bodyOut = soapObject; envelope.dotNet = true;
	 * ht.call(SERVICE_NAMESPACE + methodName, envelope); SoapObject result =
	 * (SoapObject) envelope.bodyIn;
	 * 
	 * SoapObject detail = (SoapObject) result.getProperty(methodName +
	 * "Result");
	 * 
	 * return detail; } catch (Exception e) { String message = e.getMessage(); }
	 * return null; }
	 */

	/**
	 * 获取需要标准化的人员
	 * 
	 * @param context
	 * @return
	 */
	public static List<CommUser> getQueryUser(Context context, String userNameTX) {
		String methodName = "QueryUser";
		List<CommUser> list = new ArrayList<CommUser>();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("userNameTX", userNameTX);
			soapObject.addProperty("userCD", "");
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			// String detail = result.getPropertyAsString(methodName +
			// "Result");

			SoapObject detail = (SoapObject) result.getProperty(methodName
					+ "Result");
			String detail2 = result.getPropertyAsString(methodName +
						 "Result");
			if (!StringUtils.isEmpty(detail2)
					&& !detail2.substring(detail2.indexOf("diffgram")).toUpperCase().contains("ANYTYPE{}")) {
				SoapObject diffgram = (SoapObject) detail.getProperty("diffgram");
				SoapObject NewDataSet = (SoapObject) diffgram
						.getProperty("NewDataSet");
				for (int i = 0; i < NewDataSet.getPropertyCount(); i++) {
					CommUser entity = new CommUser();
					SoapObject Table = (SoapObject) NewDataSet.getProperty(i);
					entity.setAPPUSER_CD(Table.getProperty(0).toString());
					entity.setNAME_TX(Table.getProperty(1).toString());
					entity.setAPPUSER_ID(Table.getProperty(2).toString());
					list.add(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			showMessage(message);
		}
		return list;
	}

	/**
	 * 请求下载计划
	 * 
	 * @param context
	 * @param xjqCD
	 * @param lineID
	 * @return
	 */
	public static String RequestPlanDownLoad(Context context, String xjqCD,
			int lineID, String lineName) {
		String methodName = "StartDownLoadForAndriod";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(xjqCD) || lineID <= 0)
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			soapObject.addProperty("lineid", lineID);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			
			if (resultInfo != null) {
				if (resultInfo.getResult_YN()) {
					return resultInfo.getResult_Content_TX();
				} else {
					OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI + 
							AppConst.ANDROID_DATA_STANDARD_ERROR,
							AppConst.LOGSTATUS_ERROR,
							lineName + "\n[" + resultInfo.getError_Message_TX() + "]");
					return null;
				}
			} else {
				OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI + 
						AppConst.ANDROID_DATA_STANDARD_ERROR,
						AppConst.LOGSTATUS_ERROR,
						context.getString(R.string.cumm_wifi_checkwsversion));
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI + 
					AppConst.ANDROID_DATA_STANDARD_ERROR,
					AppConst.LOGSTATUS_ERROR,
					lineName + "\n[" + e.getMessage() + "]");
			return null;
		}		
	}
	
	/**
	 * 请求下载计划
	 * 返回对象
	 * @param context
	 * @param xjqCD
	 * @param lineID
	 * @return
	 */
	public static ReturnResultInfo RequestPlanDownLoadNew(Context context, String xjqCD,
			int lineID) {
		String methodName = "StartDownLoadForAndriod";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(xjqCD)) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return resultInfo;
			}
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			soapObject.addProperty("lineid", lineID);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			
			return resultInfo;
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setResult_YN(false);
			resultInfo.setResult_Content_TX("");
			resultInfo.setError_Message_TX(e.getMessage());
			return resultInfo;
		}		
	}

	/**
	 * 下载计划库
	 * 
	 * @param context
	 * @param planDBName
	 * @param mHandler
	 * @param view
	 * @param posID
	 * @return
	 */
	public static boolean downLoadPlanDB(Context context, String planDBName,
			Handler mHandler, View view, int posID, DJLine djLine) {
		Message _msg;
		try {
			String urlString = AppContext.GetWSAddressBasePath() + planDBName;
			String saveFileNameString = HtmlRegexpUtils.getFileName(urlString);
			String savePathString = AppConst.XSTDBPath();
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(1000 * 60);
			con.setRequestMethod("GET");
			int cutsize = 256 * 256;
			InputStream is = con.getInputStream();
			int bytelength = con.getContentLength();
			if (bytelength > 0) {
				if (bytelength < 1024) {
					cutsize = 128;
				} else if (bytelength < 1024 * 1024)
					cutsize = 1024 * 16;
				else if (bytelength < 1024 * 1024 * 10) {
					cutsize = 1024 * 16 * 8;
				} else if (bytelength < 1024 * 1024 * 50) {
					cutsize = 1024 * 16 * 16;
				} else {
					cutsize = 1024 * 512;
				}
			}
			int precent = 0, x = 0;
			byte[] bs = new byte[cutsize];
			int len;
			OutputStream os = new FileOutputStream(savePathString + "/"
					+ saveFileNameString.replaceAll(".jpg", ".sdf"));
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				try {
					x += len;
					precent = ((x * 100) / bytelength);
					if (precent > 100)
						precent = 100;
					_msg = Message.obtain();
					_msg.what = 1;
					_msg.obj = view;
					_msg.arg1 = precent;
					_msg.arg2 = posID;
					if (mHandler != null)
						mHandler.sendMessage(_msg);
					Thread.sleep(10);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_msg = Message.obtain();
					_msg.what = 1;
					_msg.obj = view;
					_msg.arg1 = -3;
					_msg.arg2 = posID;
					if (mHandler != null)
						mHandler.sendMessage(_msg);
					// 插入操作日志
					OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI + 
							AppConst.ANDROID_DATA_DOWNLOAD,
							AppConst.LOGSTATUS_ERROR,
							djLine.getLineName() + "\n[" + e.getMessage() + "]");
					
					return false;
				}
			}
			_msg = Message.obtain();
			_msg.what = 1;
			_msg.obj = view;
			_msg.arg1 = 100;
			_msg.arg2 = posID;
			if (mHandler != null)
				mHandler.sendMessage(_msg);
			os.close();
			is.close();
			
			return true;
		} catch (Exception e) {
			_msg = Message.obtain();
			_msg.what = 1;
			_msg.obj = view;
			_msg.arg1 = -3;
			_msg.arg2 = posID;
			if (mHandler != null)
				mHandler.sendMessage(_msg);
			// 插入操作日志
			OperatingConfigHelper.getInstance().i(AppConst.CONNECTIONTYPE_WIFI + 
					AppConst.ANDROID_DATA_DOWNLOAD,
					AppConst.LOGSTATUS_ERROR,
					djLine.getLineName() + "\n[" + e.getMessage() + "]");
			return false;
		}
	}

	/**
	 * 下载权限配置文件
	 * 
	 * @param context
	 * @param xjqCD
	 * @return
	 */
	/*public static String getSettingXML(Context context, String xjqCD) {
		String methodName = "GetSettingXmlForAndroid";
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				return detail;
			} else {
				showMessage(emsgString);
				return null;
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return null;
	}*/
	
	/**
	 * 下载权限配置文件
	 * 
	 * @param context
	 * @param xjqCD
	 * @return
	 */
	public static ReturnResultInfo getSettingXML(Context context, String xjqCD) {
		String methodName = "GetSettingXmlForAndroid";
		ReturnResultInfo returnInfo = new ReturnResultInfo();	
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				returnInfo.setResult_YN(true);
				returnInfo.setResult_Content_TX(detail);
				
			} else {
				returnInfo.setResult_YN(false);
				returnInfo.setError_Message_TX("[" + methodName + "]" + emsgString );
			}
			
		}  catch (Exception e) {
			returnInfo.setResult_YN(false);
			returnInfo.setError_Message_TX("[" + methodName + "]" + e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 下载模块授权文件
	 * 
	 * @param context
	 * @param xjqCD
	 * @return
	 */
	public static ReturnResultInfo getModuleXML(Context context, String xjqCD) {
		String methodName = "GetModuleXmlForAndroid";
		ReturnResultInfo returnInfo = new ReturnResultInfo();
		try {
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				returnInfo.setResult_YN(true);
				returnInfo.setResult_Content_TX(detail);
			} else {
				returnInfo.setResult_YN(false);
				returnInfo.setError_Message_TX("[" + methodName + "]" + emsgString );
			}
		} catch (Exception e) {
			returnInfo.setResult_YN(false);
			returnInfo.setError_Message_TX("[" + methodName + "]" + e.getMessage());
		}
		return returnInfo;
	}

	/**
	 * 请求上传文件
	 * 
	 * @param context
	 * @param xjqCD
	 * @return
	 */
	public static String RequestResultUpload(Context context, String xjqCD) {
		String methodName = "StartUpLoadForAndriod";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(xjqCD))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", xjqCD);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				return detail;
			} else {
				showMessage(emsgString);
				return null;
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return null;

	}

	/**
	 * 上位结果文件WebService断点传
	 * 
	 * @param urlPath
	 * @param filePath
	 * @param newName
	 * @return
	 * @throws Exception
	 */
	public static boolean UploadResultDBForWeb(String appVer, Context context,
			Handler proBarHandler, String xjqCD, String filePath) {
		try {

			String lineid = new File(filePath).getName();
			String newName = lineid + "_" + xjqCD + "_0_" + AppContext.GetUniqueID();
			if (!appVer.equals(""))
				newName = newName + "_" + appVer;

			boolean result = UploadFile.getIntance().UploadFileByWeb(context,
					proBarHandler, filePath, newName, "DJRESULT");

			if (result) {
				String path = AppConst.XSTResultPath() + lineid;
				FileUtils.clearFileWithPath(path);
			}
			return result;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 上传结果文件WebService断点续传
	 * 返回结果信息对象
	 * @param urlPath
	 * @param filePath
	 * @param newName
	 * @return
	 * @throws Exception
	 */
	public static ReturnResultInfo UploadResultDBForWebNew(String appVer, Context context,
			Handler proBarHandler, String xjqCD, String filePath) {
		ReturnResultInfo returnInfo = new ReturnResultInfo();
		try {
			String lineid = new File(filePath).getName();
			String newName = lineid + "_" + xjqCD + "_0_" + AppContext.GetUniqueID();
			if (!appVer.equals(""))
				newName = newName + "_" + appVer;

			returnInfo = UploadFile.getIntance().UploadFileByWebNew(context,
					proBarHandler, filePath, newName, "DJRESULT");
			return returnInfo;
		} catch (Exception e) {
			AppContext.SetIsUpload(false);
			
			returnInfo.setResult_YN(false);
			returnInfo.setResult_Content_TX("");
			returnInfo.setError_Message_TX(e.getMessage());
			return returnInfo;
		}
	}

	/**
	 * 上位结果文件
	 * 
	 * @param urlPath
	 * @param filePath
	 * @param newName
	 * @return
	 * @throws Exception
	 */
	public static String UploadResultDB(String appVer, Context context,
			Handler proBarHandler, String xjqCD, String filePath) {
		try {

			String lineid = new File(filePath).getName();
			String newName = lineid + "_" + xjqCD + "_0_" + AppContext.GetUniqueID();
			if (!appVer.equals(""))
				newName = newName + "_" + appVer;
			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			String urlPath = AppContext.GetWSAddressBasePath()
					+ "/UploadResultToServer.ashx";
			URL url = new URL(urlPath);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			// 不使用HttpURLConnection的缓存机制，直接将流提交到服务器上
			con.setChunkedStreamingMode(0);

			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);

			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(filePath);
			int bytelength = (int) new File(filePath).length();
			int bufferSize = 256 * 256;
			if (bytelength > 0) {
				if (bytelength < 1024) {
					bufferSize = 128;
				} else if (bytelength < 1024 * 1024)
					bufferSize = 1024 * 8;
				else if (bytelength < 1024 * 1024 * 10) {
					bufferSize = 1024 * 16;
				} else if (bytelength < 1024 * 1024 * 50) {
					bufferSize = 1024 * 16 * 8;
				} else {
					bufferSize = 1024 * 256;
				}
			}
			byte[] buffer = new byte[bufferSize];
			int precent = 0, x = 0;
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
				try {
					x += length;
					precent = ((x * 100) / bytelength);
					if (precent > 100)
						precent = 100;
					Message _msg = Message.obtain();
					_msg.what = 1;

					_msg.obj = context.getString(R.string.uploading_file_hint,
							lineid) + String.valueOf((bytelength / 1024)) + "K";
					_msg.arg1 = precent;
					if (proBarHandler != null)
						proBarHandler.sendMessage(_msg);
					Thread.sleep(10);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					UIHelper.ToastMessage(context, e.getMessage());
				}
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

			/* close streams */
			fStream.close();
			ds.flush();

			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			if (String.valueOf(b).toUpperCase().equals("SUCCESS")) {
				String path = AppConst.XSTResultPath() + lineid;
				FileUtils.clearFileWithPath(path);
			}
			/* 关闭DataOutputStream */
			ds.close();
			return b.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 插入GPS初始化坐标信息
	 * 
	 * @param xjqCD
	 * @param jsonString
	 * @return
	 */
	public static boolean insertGPSInitInfoJIT(String jsonString) {
		boolean okYn = false;
		String detail = "";
		String methodName = "InsertXXGPSPositionInitJITForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("gpspositionInitsJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return okYn;
	}

	/**
	 * 插入GPS轨迹
	 * 
	 * @param xjqCD
	 * @param jsonString
	 * @return
	 */
	public static boolean insertGPSInfoJIT(String jsonString) {
		boolean okYn = false;
		String detail = "";
		String methodName = "InsertGPSInfoJITForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("gpspositionsJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okYn;
	}

	/**
	 * 插入GPS巡线到位考核记录
	 * 
	 * @param xjqCD
	 * @param jsonString
	 * @return
	 */
	public static boolean insertCPArriveResJIT(String jsonString) {
		String detail = "";
		boolean okYn = false;
		String methodName = "InsertXXDWResultJITForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("xxdwresultsJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return okYn;
	}

	/**
	 * 插入GPS巡线报缺单记录
	 * 
	 * @param xjqCD
	 * @param jsonString
	 * @return
	 */
	public static boolean insertMobjectBugResJIT(String jsonString) {
		String detail = "";
		boolean okYn = false;
		String methodName = "InsertXXMobjectBugCodeJITForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("xxmobjectbugcoderesultsJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return okYn;
	}

	/**
	 * 插入巡点检到位记录
	 */
	public static boolean insertTaskIDPosActiveJIT(String lineID,
			String jsonString) {
		String detail = "";
		boolean okYn = false;
		String methodName = "InsertDJTaskIDPosJITForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("lineID", lineID);
			soapObject.addProperty("djtaskidposJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return okYn;
	}

	/**
	 * 插入启停信息记录
	 */
	public static boolean insertSRInfoJIT(String jsonString) {
		String detail = "";
		boolean okYn = false;
		String methodName = "InsertSRResultJITForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("srResultJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return okYn;
	}

	/**
	 * 获取地图版本信息
	 * 
	 * @param context
	 * @param xjqCD
	 * @return
	 */
	public static String getCityMapVersion(Context context, int cityID) {
		String methodName = "GetCityMapVersionForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID()) || cityID <= 0)
				return "";
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("cityID", cityID);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				return detail;
			} else {
				showMessage(emsgString);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回所在位置地址信息 Json
	 * 
	 * @return
	 */
	public static String getLocationInfo() {
		try {
			String _urlString = "";
			if (AppContext.getCurrLocation() == null)
				return null;
			else {
				_urlString = AppConst.getLocationUrl()
						+ "&location="
						+ String.valueOf(AppContext.getCurrLocation()
								.getLatitude())
						+ ","
						+ String.valueOf(AppContext.getCurrLocation()
								.getLongitude());
			}
			URL url = new URL(_urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			InputStream is = con.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			String myString = EncodingUtils.getString(baf.toByteArray(),
					"UTF-8");
			return myString;
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	/**
	 * 操作记录回传至服务端集中保存
	 * 
	 * @param MsgType
	 * @param jsonString
	 * @return
	 */
	public static boolean ResponseJIT(String jsonString) {
		String detail = "";
		boolean okYn = false;
		
		String methodName = "ResponseMsgForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); 
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("MsgJsonStr", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
		}
		return okYn;
	}

	/**
	 * 获取上位机下发的临时任务信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getTempTaskInfo(Context context) {
		String methodName = "GetTempTaskInfoForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				showIcon(emsgString);
				return detail;
			} else {
				showIcon(emsgString);
				return null;
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新临时任务状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean updateTempTaskInfo(Context context,
			String gpsTempTaskID, String comtime) {
		float aa = Float.parseFloat(gpsTempTaskID);
		int ID = (int) aa;

		String methodName = "UpdateTempTaskInfoForAndroid";
		boolean okYn = false;
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD()))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("gpsTempTaskID", String.valueOf(ID));
			soapObject.addProperty("comptime", comtime);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString(methodName
					+ "Result");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				return true;
			} else {
				showMessage(emsgString);
				return false;
			}

			// String detail = result.getPropertyAsString(methodName+ "Result");
			// okYn = detail.equals("SUCCES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okYn;
	}

	/**
	 * 实时上传巡线任务结果数据
	 * 
	 * @param jsonString
	 * @return
	 */
	public static boolean insertGPSXXPlanResJIT(String jsonString) {
		String methodName = "InsertGPSXXPlanResultJITForAndroid";
		boolean okYn = false;
		String detail = "";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD()))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("xxPlanResultsJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}
			return okYn;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			String ss = e.getMessage();
		}
		return okYn;
	}

	/**
	 * 插入GPS巡线超速记录
	 * 
	 * @param xjqCD
	 * @param jsonString
	 * @return
	 */
	public static boolean insertOverSpeedInfoJIT(String jsonString) {
		String detail = "";
		boolean okYn = false;
		String methodName = "InsertOverSpeedInfoForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("xxEventresultsJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");
			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okYn;
	}

	/**
	 * 插入点检实时上传多条结果
	 * 
	 * @param pdaGUID
	 * @param xjqCD
	 * @param djResultJson
	 * @return
	 */
	public static boolean InsertMultiDJResultJITForAndroid(String djResultJson) {
		String methodName = "InsertMultiDJResultJITForAndroid";
		boolean okYn = false;
		String detail = "";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(djResultJson))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("djResultJson", djResultJson);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");

			
			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			 }
			return okYn;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okYn;
	}

	/**
	 * 插入点检实时上传多条结果(条件巡检)
	 * 
	 * @param pdaGUID
	 * @param xjqCD
	 * @param djResultJson
	 * @return
	 */
	public static boolean InsertMultiDJResultJITForAndroidByCaseXJ(
			String djResultJson) {
		String methodName = "InsertMultiDJResultJITForAndroidByCaseXJ";
		boolean okYn = false;
		String detail = "";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(djResultJson))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("djResultJson", djResultJson);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");

			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}

			return okYn;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okYn;
	}

	/**
	 * 插入启停结果实时上传多条结果(条件巡检)
	 * 
	 * @param pdaGUID
	 * @param xjqCD
	 * @param djResultJson
	 * @return
	 */
	public static boolean InsertSRResultJITForAndroidByCaseXJ(
			String srResultJson) {
		String methodName = "InsertSRResultJITForAndroidByCaseXJ";
		boolean okYn = false;
		String detail = "";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(srResultJson))
				return false;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("srResultJson", srResultJson);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = detail.equals("SUCCES");

			if (!okYn) {
				// 记录日志
				OperatingConfigHelper.getInstance().i(AppConst.ANDROID_DATA_JITUPLOAD,
						AppConst.LOGSTATUS_ERROR,
						detail);
			}

			return okYn;
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okYn;
	}

	/**
	 * 获取GPS事件类型信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getGPSEventType(Context context) {
		String methodName = "GetGPSEventTypeForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				return detail;
			} else {
				showMessage(emsgString);
				return null;
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取GPS初始化密码
	 * 
	 * @param context
	 * @return
	 */
	public static String getGPSInitPsw(Context context) {
		String methodName = "GetInitPasswordAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String emsgString = result.getPropertyAsString("emsg");
			if (StringUtils.isEmpty(emsgString)
					|| emsgString.toUpperCase().contains("ANYTYPE{}")) {
				String detail = result.getPropertyAsString(methodName
						+ "Result");
				String filePathString = AppConst.XSTBasePath();
				FileUtils.SaveToFile(filePathString, AppConst.InitPSWXmlFile,
						detail, false);
				FileEncryptAndDecrypt.encrypt(AppConst.XSTBasePath()
						+ AppConst.InitPSWXmlFile);
				return detail;
			} else {
				showMessage(emsgString);
				return null;
			}
		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取外网管线信息 http方式
	 * 
	 * @param context
	 * @return
	 */
	public static String getGXInfo(Context context, String lot, String lat,
			String rad) {
		String url = AppContext.getWebServiceAddressForOther()
				+ "spatital.php?q=" + lot + "-" + lat + "-" + rad;
		// String url = AppContext.webgetLamp + "userName="+"xwj@moons.com.cn";
		String result = null;
		// 创建HttpGet对象
		HttpGet request = new HttpGet(url);
		// 创建HttpClient对象
		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		// Represents a collection of HTTP protocol and framework parameters
		org.apache.http.params.HttpParams params = null;
		params = client.getParams();
		// set timeout
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 25000);
		try {
			httpResponse = client.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils
						.toString(httpResponse.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String string = result.replaceAll("\\s*", "");
		return string;

	}

	/**
	 * 获取可下载的操作票信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getOperateBillInfo(Context context) {
		String methodName = "GetOperateBillInfoForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD()))
				return null;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			// String emsgString = result.getPropertyAsString("emsg");

			String detail = result.getPropertyAsString(methodName + "Result");
			return detail;

		} catch (IOException e) {
		} catch (XmlPullParserException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 标准化操作票信息
	 * 
	 * @param context
	 * @return
	 */
	public static ReturnResultInfo encodeOperateBillInfo(Context context,
			String billCode) {
		String methodName = "EncodeOperateBillInfoForAndroid";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return resultInfo;
			}
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("billCode", billCode);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			// String emsgString = result.getPropertyAsString("emsg");

			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			return resultInfo;
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setResult_YN(false);
			resultInfo.setResult_Content_TX("");
			resultInfo.setError_Message_TX(e.getMessage());
			return resultInfo;
		}
	}

	/**
	 * 标准化工作票信息
	 * 
	 * @param context
	 * @return
	 */
	public static ReturnResultInfo encodeWorkBillInfo(Context context,
			String billCode, String workBillType) {
		String methodName = "EncodeWorkBillInfoForAndroid";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return resultInfo;
			}
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("billCode", billCode);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("workBillType", workBillType);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;

			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			return resultInfo;
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setResult_YN(false);
			resultInfo.setResult_Content_TX("");
			resultInfo.setError_Message_TX(e.getMessage());
			return resultInfo;
		}
	}

	/**
	 * 上传检修计划
	 * 
	 * @param json
	 * @return
	 */
	public static ReturnResultInfo OverhaulUploading(Context context,
			String json) {
		String methodName = "InsertOverhaulWorkOrderResultForAndroid";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return resultInfo;
			}
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("resultJson", json);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			return resultInfo;
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setResult_YN(false);
			resultInfo.setResult_Content_TX("");
			resultInfo.setError_Message_TX(e.getMessage());
			return resultInfo;
		}
	}

	/**
	 * 上传操作票
	 * 
	 * @param json
	 * @return
	 */
	public static ReturnResultInfo OperateUploading(Context context, String json) {
		String methodName = "InsertOperateBillResultForAndroid";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return resultInfo;
			}
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("resultJson", json);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			return resultInfo;
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setResult_YN(false);
			resultInfo.setResult_Content_TX("");
			resultInfo.setError_Message_TX(e.getMessage());
			return resultInfo;
		}
	}

	/**
	 * 上传工作票
	 * 
	 * @param json
	 * @return
	 */
	public static ReturnResultInfo WorkUploading(Context context, String json) {
		String methodName = "InsertWorkBillResultForAndroid";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return resultInfo;
			}

			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			soapObject.addProperty("resultJson", json);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			return resultInfo;
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setResult_YN(false);
			resultInfo.setResult_Content_TX("");
			resultInfo.setError_Message_TX(e.getMessage());
			return resultInfo;
		}
	}

	/**
	 * 标准化检修计划
	 * 
	 * @param context
	 * @return
	 */
	public static ReturnResultInfo encodeOverhaulInfo(Context context) {
		String methodName = "EncodeOverhaulInfoForAndroid";
		ReturnResultInfo resultInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return resultInfo;
			}
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					60000);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("jxPlanID", "");
			soapObject.addProperty("pdaGUID", AppContext.GetUniqueID());
			soapObject.addProperty("xjqCD", AppContext.GetxjqCD());
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;

			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				resultInfo.setResult_YN(false);
				resultInfo.setResult_Content_TX("");
				resultInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				resultInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			return resultInfo;
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setResult_YN(false);
			resultInfo.setResult_Content_TX("");
			resultInfo.setError_Message_TX(e.getMessage());
			return resultInfo;
		}
	}

	/**
	 * 事件上传准备
	 * 
	 * @param jsonString
	 * @return
	 */
	public static long uploadFilePrepare(String jsonString) {
		String detail = "";
		long okYn = -1;
		String methodName = "PrepareUploadFileForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return okYn;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("paraJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = Long.parseLong(detail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okYn;
	}
	
	/**
	 * 文件上传开始进行
	 * 返回结果对象
	 * @param jsonString
	 * @return
	 */
	public static ReturnResultInfo uploadFile(Context context, String jsonString) {
		String methodName = "UploadFileForAndroid";
		ReturnResultInfo returnInfo = new ReturnResultInfo();
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString)) {
				returnInfo.setResult_YN(false);
				returnInfo.setResult_Content_TX("");
				returnInfo.setError_Message_TX(context
						.getString(R.string.download_message_errormacaddress));
				return returnInfo;
			}
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("paraJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			String detail = result.getPropertyAsString(methodName + "Result");
			if (StringUtils.isEmpty(detail)
					|| detail.toUpperCase().contains("ANYTYPE{}")) {
				returnInfo.setResult_YN(false);
				returnInfo.setResult_Content_TX("");
				returnInfo.setError_Message_TX("ANYTYPE{}");
			} else {
				returnInfo = JsonToEntityUtils.jsontoReturnResultInfo(detail);
			}
			return returnInfo;
		} catch (Exception e) {
			e.printStackTrace();
			returnInfo.setResult_YN(false);
			returnInfo.setResult_Content_TX("");
			returnInfo.setError_Message_TX(e.getMessage());
			return returnInfo;
		}
	}

	/**
	 * 事件上传开始进行
	 * 
	 * @param jsonString
	 * @return
	 */
	public static long uploadFile(String jsonString) {
		// FileUtils.WriteInFile(AppConst.XSTBasePath()+"Json.txt",jsonString,false);
		String detail = "";
		long okYn = -1;
		String methodName = "UploadFileForAndroid";
		try {
			if (StringUtils.isEmpty(AppContext.GetUniqueID())
					|| StringUtils.isEmpty(AppContext.GetxjqCD())
					|| StringUtils.isEmpty(jsonString))
				return okYn;
			HttpTransportSE ht = new HttpTransportSE(AppContext.GetWSAddress(),
					1000 * 60);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE,
					methodName);
			soapObject.addProperty("paraJson", jsonString);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			ht.call(SERVICE_NAMESPACE + methodName, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = result.getPropertyAsString(methodName + "Result");
			okYn = Long.parseLong(detail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okYn;
	}
}
