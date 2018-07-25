package com.moons.xst.track.communication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.EventTypeInfo;
import com.moons.xst.track.bean.OperateBillBaseInfo;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.JsonToEntityUtils;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.StringUtils;

public class DownLoad {

	Context context;

	public DownLoad(Context context) {
		this.context = context;
	}

	public ReturnResultInfo APClientInfo(String appVer, Context context, String xjqCD) {
		return WebserviceFactory.APClientInfo(appVer, context, xjqCD);
	}

	/**
	 * @下载人员信息，并保存到xml文件
	 * @param xjqCD
	 */
	public ReturnResultInfo DownLoadUserListFromWS(Context context, String xjqCD) {
		ReturnResultInfo userString = new ReturnResultInfo();
		try {
			userString = WebserviceFactory.getuserInfo(context, xjqCD);
			if (userString.getResult_YN()) {
				SaveToFile(AppConst.XSTBasePath(), AppConst.UserXmlFile, userString.getResult_Content_TX());
			}
			// FileEncryptAndDecrypt.encrypt(AppConst.XSTBasePath()
			// + AppConst.UserXmlFile);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			userString.setResult_YN(false);
			userString.setError_Message_TX(e.getMessage());
		}
		return userString;
	}

	/**
	 * @下载GPS事件类型信息，保存到xml文件
	 * @param context
	 */
	public void DownLoadEventTypeListFromWS(Context context) {
		try {
			String resultStr = WebserviceFactory.getGPSEventType(context);
			if (resultStr.equals(""))
				return;
			String filePathString = AppConst.XSTBasePath();
			SaveToFile(filePathString, AppConst.EventTypeXmlFile, resultStr);
			loadEventTypeList(resultStr);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 加载GPS事件信息
	 */
	public static void loadEventTypeList(String json) {
		try {
			EventTypeInfo[] eventTypeList = JsonToEntityUtils
					.jsontoEventTypeInfo(json);

			if (!eventTypeList.equals(null)) {
				AppContext.eventTypeBuffer.clear();
				for (int j = 0; j < eventTypeList.length; j++) {
					AppContext.eventTypeBuffer.put(eventTypeList[j].getDD_CD(),
							eventTypeList[j].getNAME_TX());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 可下载的操作票信息
	 * 
	 * @param context
	 */
	public void DownLoadOperateBillInfoFromWS(Context context) {
		try {
			String resultStr = WebserviceFactory.getOperateBillInfo(context);
			if (resultStr.equals(""))
				return;
			loadOperateBillList(resultStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void loadOperateBillList(String json) {
		try {
			OperateBillBaseInfo[] operateBillList = JsonToEntityUtils
					.jsontoOperateBillInfo(json);

			if (!operateBillList.equals(null)) {
				AppContext.operateBillBaseInfoBuffer.clear();
				for (int j = 0; j < operateBillList.length; j++) {
					AppContext.operateBillBaseInfoBuffer
							.add(operateBillList[j]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @节假日信息，并保存到xml文件
	 * @param xjqCD
	 */
	public void DownLoadWorkDateFromWS(Context context, String xjqCD) {
		try {
			String workdateString = WebserviceFactory.getWorkDate(context,
					xjqCD);
			SaveToFile(AppConst.XSTBasePath(), AppConst.WorkDateXmlFile,
					workdateString);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * @下载路线信息
	 */
	public ReturnResultInfo DownLoadLineListFromWS(Context context, String xjqCD) {
		ReturnResultInfo LineString = new ReturnResultInfo();
		try {
			LineString = WebserviceFactory.getlineInfo(context, xjqCD);
			// 保存到临时文件中
			if (LineString.getResult_YN()) {
				SaveToFile(AppConst.XSTBasePath(), AppConst.DJLineTempXmlFile,
						LineString.getResult_Content_TX());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LineString.setResult_YN(false);
			LineString.setError_Message_TX(e.getMessage());
		}
		return LineString;
	}

	/**
	 * @请求下载路线内容
	 * @param pdaGUID
	 * @param xjqCD
	 * @param lineID
	 * @return
	 */
	public ReturnResultInfo RequestPlanDownLoad(String xjqCD, DJLine djline) {
		return WebserviceFactory.RequestPlanDownLoadNew(context, xjqCD,
				djline.getLineID());
	}

	/**
	 * @下载计划库
	 * @param context
	 * @param view
	 * @param posID
	 * @param djLine
	 * @param mProgBarHandler
	 */
	public boolean DownLoadPlanDB(Context context, View view, int posID,
			DJLine djLine, Handler mProgBarHandler, String planDBName) {
		try {
			return WebserviceFactory.downLoadPlanDB(context, planDBName,
					mProgBarHandler, view, posID, djLine);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	private DJLine getLastDJLine(String lineID, List<DJLine> lastDJLineList) {
		DJLine resultDjLine = null;
		if (lastDJLineList == null)
			return resultDjLine;

		for (int i = 0; i < lastDJLineList.size(); i++) {
			if (lineID
					.equals(String.valueOf(lastDJLineList.get(i).getLineID()))) {
				resultDjLine = lastDJLineList.get(i);
				break;
			}
		}
		return resultDjLine;
	}

	public static boolean write2Xml(Document document, File file) {
		// 创建转化工厂
		TransformerFactory factory = TransformerFactory.newInstance();
		// 创建转换实例
		try {
			javax.xml.transform.Transformer transformer = factory
					.newTransformer();

			// 将建立好的DOM放入DOM源中
			DOMSource domSource = new DOMSource(document);

			// 创建输出流
			StreamResult result = new StreamResult(file);

			// 开始转换
			transformer.transform(domSource, result);

			if (result.getOutputStream() != null)
				result.getOutputStream().close();
			return true;
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean UpdateLineListFile(boolean isDJPC) {
		try {
			// 保存tempLineList.xml中的所有路线id
			List<String> tempLineIDList = new ArrayList<String>();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// tempLineList.xml
			Document tempdoc = db.parse(new File(AppConst.XSTBasePath()
					+ AppConst.DJLineTempXmlFile));
			tempdoc.normalize();
			NodeList tempList = tempdoc.getElementsByTagName("Table");
			// LineList.xml
			File f = new File(AppConst.XSTBasePath() + AppConst.DJLineXmlFile);
			if (!f.exists()) {
				FileOutputStream fos = new FileOutputStream(f);
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version='1.0'?>");
				sb.append("<NewDataSet>");
				sb.append("</NewDataSet>");
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			Document doc = db.parse(f);
			doc.normalize();
			NodeList LineList = doc.getElementsByTagName("Table");

			for (int i = 0; i < tempList.getLength(); i++) {
				Node table = tempList.item(i);
				String LineID = "0";
				Date BuildTime = StringUtils.toDate("1911-11-11 11:11:11");
				Date StandardTime = StringUtils.toDate("1911-11-11 11:11:11");
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
					if (node.getNodeName().toUpperCase().equals("LINE_ID")) {
						LineID = node.getTextContent();
						tempLineIDList.add(LineID);
					}
					if (node.getNodeName().toUpperCase().equals("BUILD_TM")) {
						BuildTime = sdf.parse(node.getTextContent());
						node.setTextContent(sdf.format(BuildTime));
					}
					if (node.getNodeName().toUpperCase().equals("STANDARD_TM")) {
						StandardTime = sdf.parse(node.getTextContent());
						node.setTextContent(sdf.format(StandardTime));
					}
				}

				for (int j = 0; j < LineList.getLength(); j++) {
					Node table2 = LineList.item(j);
					for (Node node2 = table2.getFirstChild(); node2 != null; node2 = node2
							.getNextSibling()) {
						if (node2.getNodeName().toUpperCase().equals("LINE_ID")
								&& LineID.equals(node2.getTextContent())) {
							// 重复就删除之前的
							doc.getElementsByTagName("NewDataSet").item(0)
									.removeChild(table2);
						}
					}
				}
				// 循环添加路线
				doc.getElementsByTagName("NewDataSet").item(0)
						.appendChild(doc.importNode(table, true));
			}

			for (int i = 0; i < LineList.getLength(); i++) {
				Node table = LineList.item(i);
				String LineID = "";
				String type = "";
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeName().toUpperCase().equals("LINE_ID")) {
						LineID = node.getTextContent();
					}
					if (node.getNodeName().toUpperCase().equals("PCTYPE_CD")) {
						type = node.getTextContent();
					}
				}
				if (!tempLineIDList.contains(LineID)) {
					// 如果是点检排程且配置为下载后清理则清理所有点检路线，如果是巡检则清理所有类型为
					// 巡检,GPS巡线,新GPS巡线
					// 的路线
					// 如果是巡检且类型为条件巡检配置为下载后清理则清理该路线
					if (isDJPC) {
						// 下载后清理
						if (AppContext.getClearDJPCLineMode().equalsIgnoreCase(
								AppConst.ClearDJPCLineMode.Other.toString())) {
							if (type.equals(AppConst.LineType.DJPC
									.getLineTypeString())) {
								doc.getElementsByTagName("NewDataSet").item(0)
										.removeChild(table);
							}
						}
					} else {
						if (type.equals(AppConst.LineType.XDJ
								.getLineTypeString())
								|| type.equals(AppConst.LineType.GPSXX
										.getLineTypeString())
								|| type.equals(AppConst.LineType.GPSXXNew
										.getLineTypeString())) {
							doc.getElementsByTagName("NewDataSet").item(0)
									.removeChild(table);
						} else if (type.equals(AppConst.LineType.CaseXJ
								.getLineTypeString())) {
							// 下载后清理
							if (AppContext.getClearDJPCLineMode()
									.equalsIgnoreCase(
											AppConst.ClearDJPCLineMode.Other
													.toString())) {
								doc.getElementsByTagName("NewDataSet").item(0)
										.removeChild(table);
							}
						}
					}
				}

			}
			write2Xml(doc, f);
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 拼接LineList（下载一条拼接一条）
	 */
	public static String UpdateLineListFile(String lineID) {
		String result = "true";
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// tempLineList.xml
			Document tempdoc = db.parse(new File(AppConst.XSTBasePath()
					+ AppConst.DJLineTempXmlFile));
			tempdoc.normalize();
			NodeList tempList = tempdoc.getElementsByTagName("Table");
			// LineList.xml
			File f = new File(AppConst.XSTBasePath() + AppConst.DJLineXmlFile);
			if (!f.exists()) {
				System.out.println("aaa[LineList不存在]");
				FileOutputStream fos = new FileOutputStream(f);
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version='1.0'?>");
				sb.append("<NewDataSet>");
				sb.append("</NewDataSet>");
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			Document doc = db.parse(f);
			doc.normalize();
			NodeList LineList = doc.getElementsByTagName("Table");

			// 循环tempLineList,添加当前正在下载的路线到lineList
			for (int i = 0; i < tempList.getLength(); i++) {
				Node table = tempList.item(i);
				String LineID = "0";
				Date BuildTime = StringUtils.toDate("1911-11-11 11:11:11");
				Date StandardTime = StringUtils.toDate("1911-11-11 11:11:11");
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
					if (node.getNodeName().toUpperCase().equals("LINE_ID")) {
						LineID = node.getTextContent();
					}
					if (node.getNodeName().toUpperCase().equals("BUILD_TM")) {
						BuildTime = sdf.parse(node.getTextContent());
						node.setTextContent(sdf.format(BuildTime));
					}
					if (node.getNodeName().toUpperCase().equals("STANDARD_TM")) {
						StandardTime = sdf.parse(node.getTextContent());
						node.setTextContent(sdf.format(StandardTime));
					}
				}
				// 如果当前LineID不是正在下载的LineID则跳过当次循环
				if (!LineID.equals(lineID)) {
					continue;
				}
				// 循环LineList,如果之前存在该路线则先删除该路线
				for (int j = 0; j < LineList.getLength(); j++) {
					Node table2 = LineList.item(j);
					for (Node node2 = table2.getFirstChild(); node2 != null; node2 = node2
							.getNextSibling()) {
						if (node2.getNodeName().toUpperCase().equals("LINE_ID")
								&& LineID.equals(node2.getTextContent())) {
							// 重复就删除之前的
							doc.getElementsByTagName("NewDataSet").item(0)
									.removeChild(table2);
						}
					}
				}
				// 循环添加路线
				doc.getElementsByTagName("NewDataSet").item(0)
						.appendChild(doc.importNode(table, true));
			}

			write2Xml(doc, f);

		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("aaaa[LineList]" + e.toString());
			result = e.getMessage();
		}
		return result;
	}

	private void SaveToFile(String FilePath, String FileName, String ConentStr)
			throws IOException {
		File file = new File(FilePath);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
			}
		}
		File dir = new File(FilePath + FileName);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}
		}
		WriteInFile(FilePath + FileName, ConentStr);
	}

	// 向已创建的文件中写入数据
	private void WriteInFile(String filePath, String str) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			if (StringUtils.isEmpty(str))
				return;
			fw = new FileWriter(filePath, false);
			bw = new BufferedWriter(fw);
			bw.write(str);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				bw.close();
				fw.close();
			} catch (IOException e1) {
			}
		}
	}

	public ReturnResultInfo DownLoadSettingXMLFromWS(Context context, String xjqCD) {
		ReturnResultInfo settingString = new ReturnResultInfo();
		try {
			settingString = WebserviceFactory.getSettingXML(context, xjqCD);
			if (settingString.getResult_YN()) {
				SaveToFile(AppConst.XSTBasePath(), AppConst.SettingxmlFile,
						settingString.getResult_Content_TX());
				FileEncryptAndDecrypt.encrypt(AppConst.XSTBasePath()
						+ AppConst.SettingxmlFile);
	
				// 为上位机配置，APP中不可见的配置项赋值，下载成功后就赋值
				LoadAppConfigHelper.getAppConfigHelper(
						AppConst.AppConfigType.Invisible.toString())
						.LoadConfigByType();
	
				// 读取两票模块是否可见
				LoadAppConfigHelper.getAppConfigHelper(
						AppConst.AppConfigType.TwoBill.toString())
						.LoadConfigByType();
				// 读取检修模块是否可见
				LoadAppConfigHelper.getAppConfigHelper(
						AppConst.AppConfigType.Overhaul.toString())
						.LoadConfigByType();
			}
		} catch (Exception e) {
			e.printStackTrace();
			settingString.setResult_YN(false);
			settingString.setError_Message_TX(e.getMessage());
			// TODO: handle exception
		}
		return settingString;
	}

	// 下载模块授权文件
	public ReturnResultInfo DownLoadModuleXMLFromWS(Context context, String xjqCD) {
		ReturnResultInfo moduleString = new ReturnResultInfo();
		try {
			moduleString = WebserviceFactory
					.getModuleXML(context, xjqCD);
			if (moduleString.getResult_YN()) {
				SaveToFile(AppConst.XSTBasePath(), AppConst.ModuleSettingxmlFile,
						moduleString.getResult_Content_TX());
				FileEncryptAndDecrypt.encrypt(AppConst.XSTBasePath()
						+ AppConst.ModuleSettingxmlFile);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			moduleString.setResult_YN(false);
			moduleString.setError_Message_TX(e.getMessage());
		}
		return moduleString;
	}

	public String DownLoadSystemDateTimeFromWS() {
		String sysdatefromws = "";
		try {
			sysdatefromws = WebserviceFactory.getServiceDateTimeOfStr();
			return sysdatefromws;
		} catch (Exception e) {
			return sysdatefromws;
			// TODO: handle exception
		}
	}

	public static boolean deleteLineListNode() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			// LineList.xml
			File f = new File(AppConst.XSTBasePath() + AppConst.DJLineXmlFile);
			if (!f.exists()) {
				FileOutputStream fos = new FileOutputStream(f);
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version='1.0'?>");
				sb.append("<NewDataSet>");
				sb.append("</NewDataSet>");
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			Document doc = db.parse(f);
			doc.normalize();
			NodeList LineList = doc.getElementsByTagName("Table");

			if (AppContext.getPlanType().equals(AppConst.PlanType_XDJ)) {
				for (int i = 0; i < LineList.getLength(); i++) {
					Node table = LineList.item(i);
					String type = "";
					for (Node node = table.getFirstChild(); node != null; node = node
							.getNextSibling()) {
						if (node.getNodeName().toUpperCase()
								.equals("PCTYPE_CD")) {
							type = node.getTextContent();
							break;
						}
					}

					// 删除除点检排程以外的所有节点
					if (!type
							.equals(AppConst.LineType.DJPC.getLineTypeString())) {
						doc.getElementsByTagName("NewDataSet").item(0)
								.removeChild(table);
					}
				}
			} else if (AppContext.getPlanType().equals(AppConst.PlanType_DJPC)) {
				for (int i = 0; i < LineList.getLength(); i++) {
					Node table = LineList.item(i);
					String type = "";
					for (Node node = table.getFirstChild(); node != null; node = node
							.getNextSibling()) {
						if (node.getNodeName().toUpperCase()
								.equals("PCTYPE_CD")) {
							type = node.getTextContent();
							break;
						}
					}

					// 如果设置为下载后清理，删除点检排程的所有节点
					if (type.equals(AppConst.LineType.DJPC.getLineTypeString())
							&& AppContext.getClearDJPCLineMode()
									.equalsIgnoreCase(
											AppConst.ClearDJPCLineMode.Other
													.toString())) {
						doc.getElementsByTagName("NewDataSet").item(0)
								.removeChild(table);
					}
				}
			}

			write2Xml(doc, f);
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
	}

	public static boolean repairUserListByLine(DJLine djLine) {
		try {
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
