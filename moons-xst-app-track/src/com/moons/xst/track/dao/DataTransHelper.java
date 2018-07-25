/**
 * DataTransHelper.java
 * @author LKZ
 * @created 2014-12-27
 * */
package com.moons.xst.track.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.R.integer;
import android.os.Handler;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.User;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.DownLoad;

/**
 * 此类意在将源数据按需要再次包装
 * 
 */
public class DataTransHelper {

	/**
	 * 将路线的XML转换成List<DJLine>,并只返回lineList数组中的路线信息
	 * 
	 * @author LKZ
	 */
	public static List<DJLine> ConvertLineFromXMLFile(String lineListStr) {
		ArrayList<String> lineList = new ArrayList<String>();
		List<DJLine> djLineList = new ArrayList<DJLine>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			if ((!StringUtils.isEmpty(lineListStr))
					&& lineListStr.contains(";")) {
				String[] _LineList = lineListStr.split(";");
				lineList.clear();
				for (String _line : _LineList) {
					if (!StringUtils.isEmpty(_line)) {
						lineList.add(_line);
					}
				}
			}
			DocumentBuilder db = dbf.newDocumentBuilder();
			File f = new File(AppConst.XSTBasePath()
					+ AppConst.DJLineXmlFile);
			if (!f.exists())
				return null;
			Document doc = db.parse(f);
			doc.normalize();
			NodeList tableList = doc.getElementsByTagName("Table");
			for (int i = 0; i < tableList.getLength(); i++) {
				Node table = tableList.item(i);
				Element elem = (Element) table;
				DJLine _DjLine = new DJLine();
				int LineID = 0;
				int IdCount = 0;
				int PlanCount = 0;
				String LineName = "";
				int LineType = 0;
				Boolean buildYN = false;
				Date BuildTime = StringUtils.toDate("1911-11-11 11:11:11");
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						if (node.getNodeName().toUpperCase().equals("LINE_ID")) {
							LineID = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("NAME_TX")) {
							LineName = node.getTextContent();
						} else if (node.getNodeName().toUpperCase()
								.equals("BUILD_TM")) {
							if (node.getTextContent().length() > 0) {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd'T'HH:mm:ss");
								sdf.setTimeZone(TimeZone
										.getTimeZone("GMT+08:00"));
								BuildTime = sdf.parse(node.getTextContent());
							}
						} else if (node.getNodeName().toUpperCase()
								.equals("IDCOUNT")) {
							IdCount = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("PLANCOUNT")) {
							PlanCount = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("PCTYPE_CD")) {
							LineType = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("BUILD_YN")) {
							buildYN = node.getTextContent().equals("1") ? true
									: false;
						}
					}
				}
				_DjLine.setLineID(LineID);
				_DjLine.setLineName(LineName);
				_DjLine.setBuidYN(buildYN);
				_DjLine.setBuildTime(BuildTime);
				_DjLine.setIdCount(IdCount);
				_DjLine.setPlanCount(PlanCount);
				_DjLine.setlineType(LineType);
				if (lineList.contains(String.valueOf(LineID)))
					djLineList.add(_DjLine);
			}
			return djLineList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将路线的XML转换成List<DJLine>
	 * 
	 * @author LKZ
	 */
	public synchronized static List<DJLine> ConvertLineFromXMLFile() {
		boolean isDJPC = false;
		List<DJLine> djLineList = new ArrayList<DJLine>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			if (!FileUtils.checkFileExists(AppConst.XSTBasePath()
					+ AppConst.DJLineXmlFile))
				return null;
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(AppConst.XSTBasePath()
					+ AppConst.DJLineXmlFile));
			doc.normalize();
			NodeList tableList = doc.getElementsByTagName("Table");
			for (int i = 0; i < tableList.getLength(); i++) {
				Node table = tableList.item(i);
				Element elem = (Element) table;
				DJLine _DjLine = new DJLine();
				int LineID = 0;
				int IdCount = 0;
				int PlanCount = 0;
				String LineName = "";
				int LineType = 0;
				Boolean buildYN = false;
				Date BuildTime = StringUtils.toDate("1911-11-11 11:11:11");
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						if (node.getNodeName().toUpperCase().equals("LINE_ID")) {
							LineID = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("NAME_TX")) {
							LineName = node.getTextContent();
						} else if (node.getNodeName().toUpperCase()
								.equals("BUILD_TM")) {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd'T'HH:mm:ss");
							sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
							if (!node.getTextContent().equals("")) {
								BuildTime = sdf.parse(node.getTextContent());
								String a = node.getTextContent();
							}

						} else if (node.getNodeName().toUpperCase()
								.equals("IDCOUNT")) {
							IdCount = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("PLANCOUNT")) {
							PlanCount = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("PCTYPE_CD")) {
							LineType = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("BUILD_YN")) {
							buildYN = node.getTextContent().equals("1") ? true
									: false;
						}
					}
				}
				_DjLine.setLineID(LineID);
				_DjLine.setLineName(LineName);
				_DjLine.setBuidYN(buildYN);
				_DjLine.setBuildTime(BuildTime);
				_DjLine.setIdCount(IdCount);
				_DjLine.setPlanCount(PlanCount);
				_DjLine.setlineType(LineType);
				djLineList.add(_DjLine);
			}
			return djLineList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将路线的XML字符串转换成List<DJLine>
	 * 
	 * @author LKZ
	 */
	public static List<DJLine> ConvertLineFromXML(String xmlLineStr) {
		List<DJLine> djLineList = new ArrayList<DJLine>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			StringReader stringReader = new StringReader(xmlLineStr);
			InputSource inputSource = new InputSource(stringReader);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputSource);
			doc.normalize();
			NodeList tableList = doc.getElementsByTagName("Table");
			for (int i = 0; i < tableList.getLength(); i++) {
				Node table = tableList.item(i);
				Element elem = (Element) table;
				DJLine _DjLine = new DJLine();
				int LineID = 0;
				int IdCount = 0;
				int PlanCount = 0;
				String LineName = "";
				int LineType = 0;
				Boolean buildYN = false;
				Date BuildTime = StringUtils.toDate("1911-11-11 11:11:11");
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						if (node.getNodeName().toUpperCase().equals("LINE_ID")) {
							LineID = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("NAME_TX")) {
							LineName = node.getTextContent();
						} else if (node.getNodeName().toUpperCase()
								.equals("BUILD_TM")) {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd'T'HH:mm:ss");
							sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
							BuildTime = sdf.parse(node.getTextContent());
						} else if (node.getNodeName().toUpperCase()
								.equals("IDCOUNT")) {
							IdCount = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("PLANCOUNT")) {
							PlanCount = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("PCTYPE_CD")) {
							LineType = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("BUILD_YN")) {
							buildYN = node.getTextContent().equals("1") ? true
									: false;
						}
					}
				}
				_DjLine.setLineID(LineID);
				_DjLine.setLineName(LineName);
				_DjLine.setBuidYN(buildYN);
				_DjLine.setBuildTime(BuildTime);
				_DjLine.setIdCount(IdCount);
				_DjLine.setPlanCount(PlanCount);
				_DjLine.setlineType(LineType);
				djLineList.add(_DjLine);
			}
			return djLineList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 删除xml中的某条路线
	public static boolean DeleteLine(String LineListPath, String LineID) {
		try {
			File f = new File(LineListPath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(f);
			doc.normalize();
			NodeList tableList = doc.getElementsByTagName("Table");
			for (int i = 0; i < tableList.getLength(); i++) {
				Node table = tableList.item(i);
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeName().toUpperCase().equals("LINE_ID")
							&& node.getTextContent().equals(LineID)) {
						doc.getElementsByTagName("NewDataSet").item(0)
								.removeChild(table);
						break;
					}
				}
			}
			DownLoad.write2Xml(doc, f);
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 清理点检排程过期路线
	 */
	public static boolean ClearPastDueLine() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// LineList.xml路径
			File tempFile = new File(AppConst.XSTBasePath()
					+ "LineList.xml");
			DocumentBuilder tempDocDb = dbf.newDocumentBuilder();
			Document tempDoc = tempDocDb.parse(tempFile);
			tempDoc.normalize();
			NodeList tempList = tempDoc.getElementsByTagName("Table");
			for (int i = 0; i < tempList.getLength(); i++) {
				Node tempNode = tempList.item(i);
				String Line_ID = "";
				String PCTYPE_CD = "";
				String Time = "";
				for (Node node = tempNode.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeName().toUpperCase().equals("LINE_ID")) {
						Line_ID = node.getTextContent();
					}
					if (node.getNodeName().toUpperCase().equals("PCTYPE_CD")) {
						PCTYPE_CD = node.getTextContent();
					}
					if (node.getNodeName().toUpperCase().equals("STANDARD_TM")) {
						Time = node.getTextContent();
					}
				}
				// 只清理点检排程路线
				if (PCTYPE_CD.equals("1")) {
					// 如果日期不是今天则视为超时（清理该路线）
					if (!DateTimeHelper.isToday(Time)) {
						tempDoc.getElementsByTagName("NewDataSet").item(0)
								.removeChild(tempNode);
						File Path = new File(AppConst.XSTDBPath() + "424DB_"
								+ Line_ID + ".sdf");
						Path.delete();
					}
				}
			}
			DownLoad.write2Xml(tempDoc, tempFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//list集合转newuserList.xml
	public static boolean ConvertListForXml(List<User> list){
		try{
			File file = new File(AppConst.XSTBasePath(),AppConst.NewUserXmlFile);
			FileOutputStream fos = new FileOutputStream(file);
			StringBuffer sb = new StringBuffer();
			// 添加xml头
			sb.append("<?xml version='1.0'?>");
			// 添加根节点
			sb.append("<NewDataSet>");
			
			for(int i=0;i<list.size();i++){
				sb.append("<Z_AppUser>");
				User user=list.get(i);
				
				sb.append("<AppUser_ID>");
				sb.append(user.getUserID());
				sb.append("</AppUser_ID>");
				
				sb.append("<AppAccount_TX>");
				sb.append(user.getUserAccount());
				sb.append("</AppAccount_TX>");
				
				sb.append("<AppUser_CD>");
				sb.append(user.getUserCD());
				sb.append("</AppUser_CD>");
				
				sb.append("<Name_TX>");
				sb.append(user.getUserName());
				sb.append("</Name_TX>");
				
				sb.append("<XJQPWD_TX>");
				sb.append(user.getUserPwd());
				sb.append("</XJQPWD_TX>");
				
				sb.append("<UserRFID_TX>");
				sb.append(user.getUserRFID());
				sb.append("</UserRFID_TX>");
				
				sb.append("<UserAccess_TX>");
				sb.append(user.getUserAccess());
				sb.append("</UserAccess_TX>");
				
				String line="";
				for(int j=0;j<user.getUserLineList().size();j++){
					line+=user.getUserLineList().get(j)+";";
				}
				sb.append("<LineList>");
				sb.append(line);
				sb.append("</LineList>");
				sb.append("</Z_AppUser>");
			}
			
			sb.append("</NewDataSet>");
			
			fos.write(sb.toString().getBytes());
			fos.close();
			return true;
		}catch(Exception e){
			System.out.println("aaaa[转换]"+e.toString());
			return false;
		}
	}
	//userlist.xml转list集合
	public static List<User> ConvertXMLForList(String path){
		List<User> list=new ArrayList<User>();
		
		try {
			if(!(new File(path)).exists()){
				System.out.println("aaaa[转换不存在"+path+"]");
				return list;
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder DDocDb = dbf.newDocumentBuilder();
			Document DDoc = DDocDb.parse(new File(path));
			DDoc.normalize();
			NodeList DList = DDoc.getElementsByTagName("Z_AppUser");
			
			for(int i=0;i<DList.getLength();i++){
				User user=new User();
				Node DNode = DList.item(i);
				for(Node node = DNode.getFirstChild(); node != null; node = node
						.getNextSibling()){
					if(node.getNodeName().toUpperCase().equals("APPUSER_ID")){
						String text=node.getTextContent();
						user.setUserID(Integer.parseInt(text));
					}
					if(node.getNodeName().toUpperCase().equals("APPACCOUNT_TX")){
						String text=node.getTextContent();
						user.setAccount(text);
					}
					if(node.getNodeName().toUpperCase().equals("APPUSER_CD")){
						String text=node.getTextContent();
						user.setUserCD(text);
					}
					if(node.getNodeName().toUpperCase().equals("NAME_TX")){
						String text=node.getTextContent();
						user.setUserName(text);
					}
					if(node.getNodeName().toUpperCase().equals("XJQPWD_TX")){
						String text=node.getTextContent();
						user.setPwd(text);
					}
					if(node.getNodeName().toUpperCase().equals("USERRFID_TX")){
						String text=node.getTextContent();
						user.setUserRFID(text);
					}
					if(node.getNodeName().toUpperCase().equals("USERACCESS_TX")){
						String text=node.getTextContent();
						user.setUserAccess(text);
					}
					if(node.getNodeName().toUpperCase().equals("LINELIST")){
						//List<String> lineList = java.util.Arrays.asList(text.split(";"));
						String[] text=node.getTextContent().split(";");
						ArrayList<String> lineList=new ArrayList<String>();
						for(int j=0;j<text.length;j++){
							lineList.add(text[j]);
						}
						user.setUserLineList(lineList);
					}
				}
				list.add(user);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("aaaa[转换"+path+"]"+e.toString());
			return list;
		}
		return list;
	}
	
	/**
	 * （新）合并NewUserlist.xml
	 */
	public static boolean RepairUser(String type){
		//拼接userList
		List<User> oldList=ConvertXMLForList(AppConst.XSTBasePath()+AppConst.NewUserXmlFile);
		List<User> newList=ConvertXMLForList(AppConst.XSTBasePath()+AppConst.UserXmlFile);
		try {
			//保存lineList中的所有路线
			Map<String, String> LineList=new HashMap<String, String>();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder LineDocDb = dbf.newDocumentBuilder();
			if((new File(AppConst.XSTBasePath()+ AppConst.DJLineXmlFile)).exists()){
				Document LineDoc = LineDocDb.parse(new File(AppConst.XSTBasePath()+ AppConst.DJLineXmlFile));
				LineDoc.normalize();
				NodeList LineNode = LineDoc.getElementsByTagName("Table");
				//获取LineList.xml中的所有路线
				for(int i=0;i<LineNode.getLength();i++){
					Node LNode = LineNode.item(i);
					String LineId="";
					String PCTYPE="";
					for(Node node = LNode.getFirstChild(); node != null; node = node
							.getNextSibling()){
						if(node.getNodeName().toUpperCase().equals("LINE_ID")){//路线id
							LineId=node.getTextContent();
						}
						if(node.getNodeName().toUpperCase().equals("PCTYPE_CD")){//路线类型
							PCTYPE=node.getTextContent();
						}
					}
					LineList.put(LineId, PCTYPE);
				}
			}
			
			//合并userList
			for(int i=0;i<newList.size();i++){
				String userCD=newList.get(i).getUserCD();
				List<String> line=new ArrayList<String>();
				for(int j=0;j<oldList.size();j++){
					if(userCD.equals(oldList.get(j).getUserCD())){
						line=oldList.get(j).getUserLineList();
						oldList.remove(j);
						break;
					}
				}
				for(int j=0;j<line.size();j++){
					String lineType=LineList.get(line.get(j));
					if(StringUtils.isEmpty(lineType)){
						System.out.println("aaaa[userList]没找到"+line.get(j)+"路线");
						continue;
					}
					
					if(type.equals(AppConst.PlanType_DJPC)//如果当前是点检排程通信并且该路线不是点检排程则添加该路线
							&&!lineType.equals(AppConst.LineType.DJPC.getLineTypeString())
							&&!newList.get(i).getUserLineList().contains(line.get(j))){
						newList.get(i).getUserLineList().add(line.get(j));
					}else if(!type.equals(AppConst.PlanType_DJPC)//如果当前不是点检排程路线且该路线是点检排程则添加该路线
							&&lineType.equals(AppConst.LineType.DJPC.getLineTypeString())
							&&!newList.get(i).getUserLineList().contains(line.get(j))){
						newList.get(i).getUserLineList().add(line.get(j));
					}
				}	
				oldList.add(newList.get(i));
			}
			return ConvertListForXml(oldList);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("aaaa[userlist]"+e.toString());
			return false;
		}
	}
	
	/**
	 * 下载结束后清理userList和数据库
	 */
	public static boolean cleanUserListAndDB(String type){
		try{
			//保存tempLineList中所有路线
			List<String> tempLineList=new ArrayList<String>();
			//保存LineList中所有路线
			List<String> LineList=new ArrayList<String>();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder LineDocDb = dbf.newDocumentBuilder();
			
			//获取tempLineList中所有路线
			Document tempLineDoc=LineDocDb.parse(new File(AppConst.XSTBasePath()+ AppConst.DJLineTempXmlFile));
			tempLineDoc.normalize();
			NodeList tempLineNode = tempLineDoc.getElementsByTagName("Table");
			for(int i=0;i<tempLineNode.getLength();i++){
				Node LNode = tempLineNode.item(i);
				for(Node node = LNode.getFirstChild(); node != null; node = node
						.getNextSibling()){
					if(node.getNodeName().toUpperCase().equals("LINE_ID")){//路线id
						tempLineList.add(node.getTextContent());
					}
				}
			}
			
			//清理在tempLineList中不存在的LineList路线，并获取清理后LineList.xml中的所有路线
			Document LineDoc = LineDocDb.parse(new File(AppConst.XSTBasePath()+ AppConst.DJLineXmlFile));
			LineDoc.normalize();
			NodeList LineNode = LineDoc.getElementsByTagName("Table");
			for(int i=LineNode.getLength()-1;i>=0;i--){
				Node LNode = LineNode.item(i);
				String lineID="";
				String lineType="";
				for(Node node = LNode.getFirstChild(); node != null; node = node
						.getNextSibling()){
					if(node.getNodeName().toUpperCase().equals("LINE_ID")){//路线id
						lineID=node.getTextContent();
					}
					if(node.getNodeName().toUpperCase().equals("PCTYPE_CD")){//路线类型
						lineType=node.getTextContent();
					}
				}
				//如果是点检排程则清理之前点检排程路线
				if(type.equals(AppConst.PlanType_DJPC)&&lineType.equals(AppConst.PlanType_DJPC)&&!tempLineList.contains(lineID)){
					LineDoc.getElementsByTagName("NewDataSet").item(0).removeChild(LNode);
				//如果是巡检则清理之前巡检路线
				}else if(!type.equals(AppConst.PlanType_DJPC)&&!lineType.equals(AppConst.PlanType_DJPC)&&!tempLineList.contains(lineID)){
					LineDoc.getElementsByTagName("NewDataSet").item(0).removeChild(LNode);
				}else{
					LineList.add(lineID);
				}
			}
			DownLoad.write2Xml(LineDoc, new File(AppConst.XSTBasePath()+ AppConst.DJLineXmlFile));
			
			
			//清理LineList中不存在的下载库
			File file=new File(AppConst.XSTDBPath());
			File[] files=file.listFiles();
			for (File f : files) {
				if(!f.toString().contains("424DB")){
					continue;
				}
				//根据路径截取路线id;
				String LineFile=f.toString();
				String LineID=LineFile.substring(LineFile.indexOf("_")+1,LineFile.indexOf("."));
				if(!LineList.contains(LineID)){
					f.delete();
				}
			}
			
			//清理人员（清理人员下的所有路线在linelist中都不存在的人）
			List<User> userList=ConvertXMLForList(AppConst.XSTBasePath()+AppConst.NewUserXmlFile);
			for(int i=0;i<userList.size();i++){
				boolean isDelete=true;
				for(int j=0;j<userList.get(i).getUserLineList().size();j++){
					if(LineList.contains(userList.get(i).getUserLineList().get(j))){
						isDelete=false;
						break;
					}
				}
				if(isDelete){
					userList.remove(i);
					i--;
				}
			}
			return ConvertListForXml(userList);
		}catch(Exception e){
			System.out.println("aaaa[清理]"+e.toString());
			return false;
		}
	}
	
	/**
	 * 修复NewUserlist.xml
	 */
	public static boolean RepairUserListXML(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//临时UserList.xml(保存解密后的userlist)
		String tempUserList=AppConst.XSTBasePath() + AppConst.tempUserXmlFile;
		// 之前的userList路径
		String UserFile = AppConst.XSTBasePath() + AppConst.NewUserXmlFile;
		File UserPath=new File(UserFile);
		//新下的UserList路径
		String downloadUser=AppConst.XSTBasePath()+ AppConst.UserXmlFile;
		boolean newXMlIsDamage=true;
		try {
			
			//保存所有路线
			List<String> LineList=new ArrayList<String>();
			// LineList.xml路径
			File tempFile = new File(AppConst.XSTBasePath()
					+ AppConst.DJLineXmlFile);
			DocumentBuilder LineDocDb = dbf.newDocumentBuilder();
			Document LineDoc = LineDocDb.parse(tempFile);
			LineDoc.normalize();
			NodeList LineNode = LineDoc.getElementsByTagName("Table");
			//获取LineList.xml中的所有路线
			for(int i=0;i<LineNode.getLength();i++){
				Node LNode = LineNode.item(i);
				for(Node node = LNode.getFirstChild(); node != null; node = node
						.getNextSibling()){
					if(node.getNodeName().toUpperCase().equals("LINE_ID")){
						LineList.add(node.getTextContent());
					}
				}
			}
			
			//新下的userList
			//FileEncryptAndDecrypt.decrypt(downloadUser, tempUserList);
			DocumentBuilder DDocDb = dbf.newDocumentBuilder();
			Document DDoc = DDocDb.parse(new File(downloadUser));
			DDoc.normalize();
			NodeList DList = DDoc.getElementsByTagName("Z_AppUser");
			newXMlIsDamage=false;
			if(!UserPath.exists()){
				DownLoad.write2Xml(DDoc, UserPath);
				//FileEncryptAndDecrypt.encrypt(UserFile);
				return true;
			}
			//之前的UserList.xml
			//FileEncryptAndDecrypt.decrypt(UserFile, tempUserList);
			DocumentBuilder tempDocDb = dbf.newDocumentBuilder();
			Document tempDoc = tempDocDb.parse(new File(UserFile));
			tempDoc.normalize();
			NodeList UserList = tempDoc.getElementsByTagName("Z_AppUser");
			
			//循环新下的节点和之前的节点，如果二个xml中存在同一个人则合并人员下路线否则就添加节点
			for(int i=0;i<DList.getLength();i++){
				Node DNode = DList.item(i);
				for(Node node = DNode.getFirstChild(); node != null; node = node
						.getNextSibling()){
					if(node.getNodeName().toUpperCase().equals("APPUSER_CD")){
						String cd=node.getTextContent();
						
						for(int j=0;j<UserList.getLength();j++){
							Node UserNode = UserList.item(j);
							for(Node node2 = UserNode.getFirstChild(); node2 != null; node2 = node2
									.getNextSibling()){
								if(node2.getNodeName().toUpperCase().equals("APPUSER_CD")
										&&cd.equals(node2.getTextContent())){
									tempDoc.getElementsByTagName("NewDataSet").item(0).removeChild(UserNode);
								}
							}
						}
					}
				}
					//添加节点
					tempDoc.getElementsByTagName("NewDataSet").item(0).appendChild(tempDoc.importNode(DNode, true));
			}
			
			
			//清理LineList中不存在的下载库
			File file=new File(AppConst.XSTDBPath());
			File[] files=file.listFiles();
			for (File f : files) {
				if(!f.toString().contains("424DB")){
					continue;
				}
				//根据路径截取路线id;
				String LineFile=f.toString();
				String LineID=LineFile.substring(LineFile.indexOf("_")+1,LineFile.indexOf("."));
				if(!LineList.contains(LineID)){
					f.delete();
				}
			}
			
			DownLoad.write2Xml(tempDoc, new File(tempUserList));
			tempDoc = tempDocDb.parse(new File(tempUserList));
			tempDoc.normalize();
			UserList = tempDoc.getElementsByTagName("Z_AppUser");
			
			//循环所有人员，如果该人员能做的路线在tempLineList中不存在则删除该人员
			for(int i=0;i<UserList.getLength();i++){
				Node UserNode = UserList.item(i);
				for(Node node = UserNode.getFirstChild(); node != null; node = node
						.getNextSibling()){
					if(node.getNodeName().toUpperCase().equals("LINELIST")){
						boolean isDelete=true;
						String[] Line=node.getTextContent().split(";");
						for(int j=0;j<Line.length;j++){
							if(LineList.contains(Line[j])){
								isDelete=false;
								break;
							}
						}
						if(isDelete){
							tempDoc.getElementsByTagName("NewDataSet").item(0).removeChild(UserNode);
							//tempDoc.getElementsByTagName("NewDataSet").item(0).removeChild(tempDoc.importNode(UserNode, true));
						}
					}
				}
			}
			DownLoad.write2Xml(tempDoc, UserPath);
			
			//删除userList
			File downloaFile=new File(downloadUser);
			downloaFile.delete();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//如果是之前的文件损坏
			if(e.getMessage().contains("Unexpected")&&!newXMlIsDamage){
				FileUtils.copyFile(downloadUser, UserFile);
			}
			return false;
		}finally{
			//删除临时UserList
			File tempUserFile=new File(tempUserList);
			tempUserFile.delete();
		}
	}

}
