package com.moons.xst.track.common;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import android.content.Context;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;

public class LoadAppConfigHelper {
	
	private static LoadAppConfigHelper instance = null;
	private static String mConfigType = "";
	
	public static LoadAppConfigHelper getAppConfigHelper(String configType) {  
		   
	     synchronized (LoadAppConfigHelper.class) {  
	         if (instance == null) {  
	             instance = new LoadAppConfigHelper();  
	         }  
	         mConfigType = configType;
	     }  
	     return instance;  
	}
	
	public void LoadConfigByType() {
		if (mConfigType.equals(AppConst.AppConfigType.Gesture.toString())) {
			LoadGestureConfig();
		} else if (mConfigType.equals(AppConst.AppConfigType.MeasureTypeForOuter.toString())) {
			LoadMeasureTypeForOuterConfig();
		} else if (mConfigType.equals(AppConst.AppConfigType.TwoBill.toString())) {
			LoadTwoBillSettingConfig();
		} else if (mConfigType.equals(AppConst.AppConfigType.Invisible.toString())) {
			LoadConfigForInvisible();
		} else if (mConfigType.equals(AppConst.AppConfigType.Overhaul.toString())) {
			LoadOverhaulSettingConfig();
		}
	}

	private static void LoadConfigForInvisible(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.XSTBasePath() + AppConst.SettingxmlFile;
			String temppath = AppConst.XSTBasePath()
					+ AppConst.tempSettingxmlFile;
			FileEncryptAndDecrypt.decrypt(path, temppath);
			File f = new File(temppath);
			if (!f.exists()) {
				//AppContext.setConfigDoPlanByIDPos(true);
				return;
			}
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("DoPlanByIDPos")) {
					if (element.getAttribute("Using").equals("Yes"))
						AppContext.setConfigDoPlanByIDPos(true);
					else
						AppContext.setConfigDoPlanByIDPos(false);
				} else if (element.getAttribute("Name").equals("CheckInAfterEnterLine")) {
					if (element.getAttribute("Using").equals("Yes"))
						AppContext.setCheckInAfterEnterLine(true);
					else
						AppContext.setCheckInAfterEnterLine(false);
				} else if (element.getAttribute("Name").equals("UserLoginType")) {
						AppContext.setUserLoginType(element.getAttribute("Using").toString());
				} else if (element.getAttribute("Name").equals("ParameterSettingPwd")) {
					if (element.getAttribute("Using").equals("Yes")) {
						AppContext.setPwdYN(true);
						AppContext.setPwd(element.getAttribute("Pwd"));
					}else{
						AppContext.setPwdYN(false);
						AppContext.setPwd("");
					}
				} else if (element.getAttribute("Name").equals("DoPlanByScan")) {
					if (element.getAttribute("Using").equals("Yes"))
						AppContext.setConfigDoPlanByScan(true);
					else
						AppContext.setConfigDoPlanByScan(false);
				} else if (element.getAttribute("Name").equals("DownloadLineByLongClick")) {
					if (element.getAttribute("Using").equals("Yes"))
						AppContext.setConfigLongClickDownloadLine(true);
					else
						AppContext.setConfigLongClickDownloadLine(false);
				} else if (element.getAttribute("Name").equals("DrawleftLayoutYN")) {
					if (element.getAttribute("Using").equals("Yes"))
						AppContext.setDrawleftLayoutYN(true);
					else
						AppContext.setDrawleftLayoutYN(false);
				} else if (element.getAttribute("Name").equals("CheckNetworkYN")) {
					if (element.getAttribute("Using").equals("Yes"))
						AppContext.setCheckNetworkYN(true);
					else
						AppContext.setCheckNetworkYN(false);
				} else if (element.getAttribute("Name").equals("ShieldOSSetTimeYN")) {
					if (element.getAttribute("Using").equals("Yes"))
						AppContext.setShieldOSSetTimeYN(true);
					else
						AppContext.setShieldOSSetTimeYN(false);
				} else if (element.getAttribute("Name").equals("ClearDJPCLineMode")) {
						AppContext.setClearDJPCLineMode(element.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("TransExceptionYN")) {
					if (element.getAttribute("Using").equals("Yes"))
						AppContext.setTransExceptionYN(true);
					else
						AppContext.setTransExceptionYN(false);
				} else if (element.getAttribute("Name").equals("TimeZoneSetting")) {
					AppContext.setTimeZone(element.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("NoticeType")) {
					AppContext.setNoticeType(element.getAttribute("Using"));
				}
			}
			f.delete();
		} catch (Exception ex) {
			AppContext.setConfigDoPlanByIDPos(true);
		}
	}
	private static void LoadGestureConfig(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.GestureConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			AppContext.gestureUserInfo.clear();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				String key = element.getAttribute("Account");
				String[] values = new String[2];
				// 用户密码
				values[0] = element.getAttribute("Password");
				// 手势密码
				values[1] = element.getAttribute("Gesture");
				AppContext.gestureUserInfo.put(key, values);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private static void LoadMeasureTypeForOuterConfig() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.MeasureTypeForOuterConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("Temperature")) {
					AppContext.setBlueToothAddressforTemperature(element
							.getAttribute("BTAddress"));
					AppContext.setBTConnectPwdforTemperature(element
							.getAttribute("Password"));
				} else if (element.getAttribute("Name").equals("Vibration")) {
					AppContext.setBlueToothAddressforVibration(element
							.getAttribute("BTAddress"));
					AppContext.setBTConnectPwdforVibration(element
							.getAttribute("Password"));
				}
			}
		} catch (SAXParseException e) {
			AppConst.MeasureTypeForOuterConfigPathOnError();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void LoadTwoBillSettingConfig() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.XSTBasePath() + AppConst.SettingxmlFile;
			String temppath = AppConst.XSTBasePath()
					+ AppConst.tempSettingxmlFile;
			FileEncryptAndDecrypt.decrypt(path, temppath);
			File f = new File(temppath);
			if (!f.exists()) {
				return;
			}
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("TwoBill")) {
					if (element.getAttribute("Using").equals("Yes")) {
						AppContext.setTwoBillYN(true);
					}else{
						AppContext.setTwoBillYN(false);
					}
				}
			}
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void LoadOverhaulSettingConfig() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.XSTBasePath() + AppConst.SettingxmlFile;
			String temppath = AppConst.XSTBasePath()
					+ AppConst.tempSettingxmlFile;
			FileEncryptAndDecrypt.decrypt(path, temppath);
			File f = new File(temppath);
			if (!f.exists()) {
				return;
			}
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("Overhaul")) {
					if (element.getAttribute("Using").equals("Yes")) {
						AppContext.setOverhaulYN(true);
					}else{
						AppContext.setOverhaulYN(false);
					}
				}
			}
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void LoadPWDSettingConfig(){
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		try {
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			String path = AppConst.XSTBasePath() + AppConst.SettingxmlFile;
//			String temppath = AppConst.XSTBasePath()
//					+ AppConst.tempSettingxmlFile;
//			FileEncryptAndDecrypt.decrypt(path, temppath);
//			File f = new File(temppath);
//			if (!f.exists()) {
//				return;
//			}
//			Document doc = db.parse(f);
//			doc.normalize();
//			// 找到根Element
//			Element root = doc.getDocumentElement();
//			NodeList nodes = root.getElementsByTagName("Setting");
//			for (int i = 0; i < nodes.getLength(); i++) {
//				Element element = (Element) (nodes.item(i));
//				if (element.getAttribute("Name").equals("ParameterSettingPwd")) {
//					if (element.getAttribute("Using").equals("Yes")) {
//						AppContext.setPwdYN(true);
//						AppContext.setPwd(element.getAttribute("Pwd"));
//					}else{
//						AppContext.setPwdYN(false);
//						AppContext.setPwd("");
//					}
//				}
//			}
//			f.delete();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}