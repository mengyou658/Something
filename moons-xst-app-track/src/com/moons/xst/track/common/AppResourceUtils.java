package com.moons.xst.track.common;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;

public class AppResourceUtils { 
	 
    public static int getLayoutId(Context paramContext, String paramString) { 
        return paramContext.getResources().getIdentifier(paramString, "layout", 
                paramContext.getPackageName()); 
    } 
 
    public static int getStringId(Context paramContext, String paramString) { 
        return paramContext.getResources().getIdentifier(paramString, "string", 
                paramContext.getPackageName()); 
    } 
 
    public static int getDrawableId(Context paramContext, String paramString) { 
        return paramContext.getResources().getIdentifier(paramString, 
                "drawable", paramContext.getPackageName()); 
    } 
     
    public static int getStyleId(Context paramContext, String paramString) { 
        return paramContext.getResources().getIdentifier(paramString, 
                "style", paramContext.getPackageName()); 
    } 
     
    public static int getId(Context paramContext, String paramString) { 
        return paramContext.getResources().getIdentifier(paramString,"id", paramContext.getPackageName()); 
    } 
     
    public static int getColorId(Context paramContext, String paramString) { 
        return paramContext.getResources().getIdentifier(paramString, 
                "color", paramContext.getPackageName()); 
    } 
    public static int getArrayId(Context paramContext, String paramString) { 
        return paramContext.getResources().getIdentifier(paramString, 
                "array", paramContext.getPackageName()); 
    } 
    
    public static String getValue(Context context, String strText, String attName) {
        String strData = "";
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document document = null;
        InputStream inputStream = null;
        // 首先找到xml文件
        factory = DocumentBuilderFactory.newInstance();
        try {
                // 找到xml，并加载文档
                builder = factory.newDocumentBuilder();
                inputStream = context.getResources().getAssets()
                                .open("custom.xml");
                document = builder.parse(inputStream);
                // 找到根Element
                Element root = document.getDocumentElement();
                NodeList nodes = root.getElementsByTagName("item");
                // 遍历根节点所有子节点
                Element cardElement;
                String strName;
                String strValues;
                for (int i = 0; i < nodes.getLength(); i++) {
                        cardElement = (Element) (nodes.item(i));
                        strName = cardElement.getAttribute(attName);
                        strValues = cardElement.getFirstChild().getNodeValue();
                        if (strValues.equals(strText)) {
                                strData = strName;
                                break;
                        }
                        if (i == nodes.getLength() - 1) {
                                strData = "";
                        }
                }
        } catch (IOException e) {
                e.printStackTrace();
        } catch (SAXException e) {
                e.printStackTrace();
        } catch (ParserConfigurationException e) {
                e.printStackTrace();
        } finally {
                try {
                        inputStream.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        return strData;
    }
}