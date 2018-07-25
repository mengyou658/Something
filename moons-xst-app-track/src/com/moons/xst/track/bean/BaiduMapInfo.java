package com.moons.xst.track.bean;

import java.io.Serializable;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class BaiduMapInfo implements Serializable{

	public final static String UTF8 = "UTF-8";
	
	private int cityID;
	private String cityName;
	private String version;
	private String downloadurl;
	private String updateDateTime;
	
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityid) {
		this.cityID = cityid;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityname) {
		this.cityName = cityname;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDownloadUrl() {
		return downloadurl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadurl = downloadUrl;
	}
	public String getUpdateDateTime() {
		return updateDateTime;
	}
	public void setUpdateDateTime(String updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	
	public static BaiduMapInfo[] parse(Document doc) {		
	try {
		//DocumentBuilderFactory factory = DocumentBuilderFactory .newInstance();
        //DocumentBuilder builder = factory.newDocumentBuilder(); 
        //Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
		
		NodeList tableList = doc.getElementsByTagName("city");
		int count = tableList.getLength();
		BaiduMapInfo[] mapInfos = new BaiduMapInfo[count];
		for (int i = 0; i < count; i++) {
			Node table = tableList.item(i);
			BaiduMapInfo info = new BaiduMapInfo();
			
			for (Node node = table.getFirstChild(); 
					node != null; node = node.getNextSibling()) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.getNodeName().toUpperCase()
							.equals("CITYID")) {
						info.cityID = Integer.valueOf(node.getTextContent())
								.intValue();
					} else if (node.getNodeName().toUpperCase()
							.equals("CITYNAME")) {
						info.cityName = node.getTextContent();
					} else if (node.getNodeName().toUpperCase()
							.equals("VERSION")) {
						info.version = node.getTextContent();
					} else if (node.getNodeName().toUpperCase()
							.equals("DOWNLOADURL")) {
						info.downloadurl = node.getTextContent();
					} else if (node.getNodeName().toUpperCase()
							.equals("UPDATEDATETIME")) {
						info.updateDateTime = node.getTextContent();
					} 
				}
			}
			mapInfos[i] = info;
		}		
		return mapInfos;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	}

	public static BaiduMapInfo[] parse(String xmlStr){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory .newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder(); 
	        Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
	
	        return parse(doc);	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
