package com.moons.xst.track.dao;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.StringUtils;

public class DJLineDAO {

	/**
	 * 初始化路线列表
	 */
	public static List<DJLine> loadDJLineByUser(int appuserID) {

		List<DJLine> lvDJLineData = new ArrayList<DJLine>();
		String xjqCD = AppContext.GetxjqCD();
		
		lvDJLineData = DataTransHelper.ConvertLineFromXMLFile(AppContext
				.GetlineList());
		
		/*if (!AppContext.getxjqCDIsAlter()) {
			lvDJLineData = DataTransHelper.ConvertLineFromXMLFile(AppContext
					.GetlineList());
		} else {
			// try{
			// 加载数据
			lvDJLineData.clear();
			AppContext.setxjqCDIsAlter(false);
		}*/
		// }catch(AppException e){
		// e.printStackTrace();
		// throw e;
		// }
		List<DJLine> DJLineData=new ArrayList<DJLine>();
		//没下载的路线则不加载
		if (lvDJLineData != null && lvDJLineData.size() > 0) {
			for(int i=0;i<lvDJLineData.size();i++){
				File file=new File(AppConst.XSTDBPath()+"424DB_"+lvDJLineData.get(i).getLineID()+".sdf");
				if(file.exists()){
					DJLineData.add(lvDJLineData.get(i));
				}
			}
		}
		return DJLineData;
	}
	
	/**
	 * 初始化所有路线列表
	 */
	public static List<DJLine> loadAllDJLine() {

		List<DJLine> lvDJLineData = new ArrayList<DJLine>();
		String xjqCD = AppContext.GetxjqCD();
		if (!StringUtils.isEmpty(xjqCD)) {
			lvDJLineData = DataTransHelper.ConvertLineFromXMLFile();
		} else {
			// try{
			// 加载数据
			lvDJLineData.clear();
		}
		// }catch(AppException e){
		// e.printStackTrace();
		// throw e;
		// }
		List<DJLine> DJLineData=new ArrayList<DJLine>();
		//没下载的路线则不加载
		if (lvDJLineData != null && lvDJLineData.size() > 0) {
			for(int i=0;i<lvDJLineData.size();i++){
				File file=new File(AppConst.XSTDBPath()+"424DB_"+lvDJLineData.get(i).getLineID()+".sdf");
				if(file.exists()){
					DJLineData.add(lvDJLineData.get(i));
				}
			}
		}
		return DJLineData;
	}

	/**
	 * 加载结果库列表 （系统启动时调用）
	 * 
	 * @return
	 */
	public synchronized static boolean loadResultDataFileList() {
		try {
			AppContext.resultDataFileListBuffer.clear();
			AppContext.resultDataFileListBuffer = new CopyOnWriteArrayList<DJLine>();
			File resultPath = new File(AppConst.XSTResultPath());
			if (resultPath.exists()) {
				File[] linelist = resultPath.listFiles();
				for (File linepath : linelist) {
					if (linepath.listFiles() != null
							&& linepath.listFiles().length > 0
							&& AppConst.isLineDBYn(linepath.getName())) {
						int lineID = Integer.parseInt(linepath.getName());
						DJLine djLine = new DJLine();
						djLine.setLineID(lineID);
						djLine.setResultDBPath(linepath.getPath()
								+ File.separator
								+ AppConst.ResultDBName(lineID));
						djLine.setPlanDBPath(AppConst.XSTDBPath()
								+ File.separator + AppConst.PlanDBName(lineID));
						AppContext.resultDataFileListBuffer.add(djLine);
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
			// TODO: handle exception
		}
	}

	/**
	 * 将路线的XML转换成List<DJLine>,并只返回lineList数组中的路线信息
	 * 
	 * @author LKZ
	 */
	private static List<DJLine> ConvertLineFromXML(String lineListStr) {
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
			Document doc = db.parse(new File(AppConst.XSTBasePath()
					+ "LineList.xml"));
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
				Date BuildTime = StringUtils.toDate("2014-09-03 10:14:12");
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
						}
					}
				}
				_DjLine.setLineID(LineID);
				_DjLine.setLineName(LineName);
				_DjLine.setBuildTime(BuildTime);
				_DjLine.setIdCount(IdCount);
				_DjLine.setPlanCount(PlanCount);
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
	 * 从服务端获取路线列表
	 * 
	 * @return
	 */
	public static List<DJLine> getDJLineInfoFromWS() {
		List<DJLine> lvDJLineData = new ArrayList<DJLine>();
		lvDJLineData = DataTransHelper.ConvertLineFromXMLFile();
		return lvDJLineData;
	}
}
