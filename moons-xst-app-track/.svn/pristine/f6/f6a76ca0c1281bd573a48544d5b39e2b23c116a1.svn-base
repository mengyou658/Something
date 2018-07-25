/**
 * 
 */
package com.moons.xst.track.common;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.moons.xst.track.AppConst;

/**
 * 模块授权类
 * 
 * @author LKZ
 * 
 */
public class GrantHelper {

	public static final int Commu_Commu = 0;
	public static final int Commu_AppUpdate = 1;
	public static final int Commu_MapUpdate = 2;
	public static final int Tools_NFC = 3;
	public static final int Tools_Scan = 4;
	public static final int Tools_Carema = 5;
	public static final int Tools_Recorder = 6;
	public static final int Tools_GPS = 7;
	public static final int Tools_Voice = 8;
	public static final int Sys_Setting = 9;
	public static final int Sys_About = 10;
	public static final int XDJ = 11;
	public static final int Tools_Temperature = 12;
	public static final int Tools_Vibration = 13;
	public static final int Tools_Speed = 14;

	/**
	 * 返回模块权限列表
	 * 
	 * @return
	 */
	@SuppressWarnings("finally")
	public static HashMap<Integer, Boolean> loadSettingXml() {
		HashMap<Integer, Boolean> _hashMap = new HashMap<Integer, Boolean>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String path = AppConst.XSTBasePath() + AppConst.ModuleSettingxmlFile;
		String temppath = AppConst.XSTBasePath()
				+ AppConst.tempModuleSettingxmlFile;
		try {
			FileEncryptAndDecrypt.decrypt(path, temppath);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File f = new File(temppath);
		try {
			Boolean debug = true;
			DocumentBuilder db = dbf.newDocumentBuilder();
			// 这里暂时静态的控制各个模块权限，等授权方案确定后更换
			if (debug) {
				_hashMap.clear();
				_hashMap.put(Commu_Commu, true);
				_hashMap.put(Commu_AppUpdate, true);
				_hashMap.put(Commu_MapUpdate, true);
				_hashMap.put(Tools_NFC, true);
				_hashMap.put(Tools_Scan, true);
				_hashMap.put(Tools_Carema, true);
				_hashMap.put(Tools_Recorder, true);
				_hashMap.put(Tools_GPS, true);
				_hashMap.put(Tools_Voice, true);
				_hashMap.put(Sys_Setting, true);
				_hashMap.put(Sys_About, true);
				_hashMap.put(XDJ, true);
				_hashMap.put(Tools_Temperature, true);
				_hashMap.put(Tools_Vibration, true);
				_hashMap.put(Tools_Speed, true);
				return _hashMap;
			}
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				// 获取river中name属性值
				if (element.getAttribute("Name").equals("Commu_Commu")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Commu_Commu, true);
					} else {
						_hashMap.put(Commu_Commu, false);
					}
				} else if (element.getAttribute("Name").equals(
						"Commu_AppUpdate")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Commu_AppUpdate, true);
					} else {
						_hashMap.put(Commu_AppUpdate, false);
					}
				} else if (element.getAttribute("Name").equals(
						"Commu_MapUpdate")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Commu_MapUpdate, true);
					} else {
						_hashMap.put(Commu_MapUpdate, false);
					}
				} else if (element.getAttribute("Name").equals("Tools_NFC")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_NFC, true);
					} else {
						_hashMap.put(Tools_NFC, false);
					}
				} else if (element.getAttribute("Name").equals("Tools_Scan")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_Scan, true);
					} else {
						_hashMap.put(Tools_Scan, false);
					}
				} else if (element.getAttribute("Name").equals("Tools_Carema")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_Carema, true);
					} else {
						_hashMap.put(Tools_Carema, false);
					}
				} else if (element.getAttribute("Name")
						.equals("Tools_Recorder")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_Recorder, true);
					} else {
						_hashMap.put(Tools_Recorder, false);
					}
				} else if (element.getAttribute("Name").equals("Tools_GPS")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_GPS, true);
					} else {
						_hashMap.put(Tools_GPS, false);
					}
				} else if (element.getAttribute("Name").equals("Tools_Voice")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_Voice, true);
					} else {
						_hashMap.put(Tools_Voice, false);
					}
				} else if (element.getAttribute("Name").equals("Sys_Setting")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Sys_Setting, true);
					} else {
						_hashMap.put(Sys_Setting, false);
					}
				} else if (element.getAttribute("Name").equals("Sys_About")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Sys_About, true);
					} else {
						_hashMap.put(Sys_About, false);
					}
				} else if (element.getAttribute("Name").equals("XDJ")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(XDJ, true);
					} else {
						_hashMap.put(XDJ, false);
					}				
				} else if (element.getAttribute("Name").equals("Tools_Temperature")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_Temperature, true);
					} else {
						_hashMap.put(Tools_Temperature, false);
					}					
				} else if (element.getAttribute("Name").equals("Tools_Vibration")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_Vibration, true);
					} else {
						_hashMap.put(Tools_Vibration, false);
					}					
				} else if (element.getAttribute("Name").equals("Tools_Speed")) {
					if (element.getAttribute("Using").toUpperCase()
							.equals("YES")) {
						_hashMap.put(Tools_Speed, true);
					} else {
						_hashMap.put(Tools_Speed, false);
					}					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			f.delete();
			return _hashMap;
		}
	}
}
