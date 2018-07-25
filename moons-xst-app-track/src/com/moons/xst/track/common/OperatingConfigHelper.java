package com.moons.xst.track.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.os.Handler;
import android.util.Log;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;

/**
 * 自定义的日志输出
 */
public class OperatingConfigHelper {

	public enum LogType {
		debug, error, info, verbose, warn
	}

	private static final String TAG = "Log_";
	private static OperatingConfigHelper log;
	private static String userName = "";

	/** 是否打印调试日志 */
	private static boolean isPrintDebugLog = false;
	/** 是否写日志到本地 */
	private static boolean isWriteToSdcard = true;
	/** 项目文件夹 */
	private final static String projectDirectory = AppConst.XSTLogFilePath();

	/**
	 * 单例模式
	 * 
	 * @param context
	 *            上下文
	 */
	private OperatingConfigHelper() {
	}

	/**
	 * 初始化单例模式
	 * 
	 * @param context
	 *            上下文
	 * @return 日志的单例模式
	 */
	public static OperatingConfigHelper getInstance() {
		if (log == null) {
			log = new OperatingConfigHelper();
		}
		if (AppContext.isLogin())
			userName = AppContext.getUserName();		
		else
			userName = "Not Logged in";
		return log;
	}

	public void d(String tag, String msg) {
		if (isPrintDebugLog) {
			Log.d(TAG + tag, msg);
		}

		if (isWriteToSdcard) {
			writeLogToSdCard(LogType.debug, tag, "", msg);
		}
	}

	public void e(String tag, String msg) {
		if (isPrintDebugLog)
			Log.e(TAG + tag, msg);

		if (isWriteToSdcard) {
			writeLogToSdCard(LogType.error, tag, "", msg);
		}
	}

	public void i(final String tag, final String state, final String msg) {
		new Thread() {
			public void run() {
				boolean b=false;
				while(!b){
					b=writeLog(tag,state,msg);
				}
			}
		}.start();
	}
	
	private synchronized boolean writeLog(String tag,String state,String msg){
		try{
			if (isPrintDebugLog)
				Log.i(TAG + tag, msg);

			if (isWriteToSdcard) {
				writeLogToSdCard(LogType.info, tag, state, msg);
			}
		}catch(Exception e){
		}
		return true;
	}

	public void v(String tag, String msg) {
		if (isPrintDebugLog)
			Log.v(TAG + tag, msg);

		if (isWriteToSdcard) {
			writeLogToSdCard(LogType.verbose, tag, "", msg);
		}
	}

	public void w(String tag, String msg) {
		if (isPrintDebugLog)
			Log.w(TAG + tag, msg);

		if (isWriteToSdcard) {
			writeLogToSdCard(LogType.warn, tag, "", msg);
		}
	}

	public static OperatingConfigHelper getLog() {
		return log;
	}

	public static void setLog(OperatingConfigHelper log) {
		OperatingConfigHelper.log = log;
	}

	/***
	 * 设置日志文件的名称，日志文件的名称按照日期来设置，如2015-12-29
	 * 
	 * @return 日志文件的名称
	 */
	private static String setLogFileName() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return TAG + dateFormat.format(new Date()) + ".xml";
	}

	/***
	 * 写日志到
	 * 
	 * @param logType
	 * @param tag
	 * @param msg
	 */
	private static void writeLogToSdCard(LogType logType, String tag, String state, String msg) {

		String dir = projectDirectory + setLogFileName();
		writeLogMsg(dir, tag, state, msg);
	}

	/**
	 * 按照固定格式设置日志内容
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	private static String setLogContent(String tag, String msg) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(new Date());
		return time + " " + tag + " " + userName + " " + msg + "\r\n";
	}

	/***
	 * 写日志信息
	 * 
	 * @param file
	 * @param tag
	 * @param msg
	 */
	private static void writeLogMsg(String filePath, String tag, String state, String msg) {
		File file = new File(filePath);
		if (!file.exists())
			create_xml(filePath, tag, state, msg);
		else 
			add_xml(filePath, tag, state, msg);
	}

	/***
	 * 清理日志文件夹
	 */
	public static void clearLogDir() {
		// TODO:待完成
	}

	private static void create_xml(String filePath, String tag, String s, String msg) {  
        try {  
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();  
            DocumentBuilder db=dbf.newDocumentBuilder();  
            Document document=db.newDocument(); 
            document.setXmlStandalone(true); 
            Element root = document.createElement("Logs");
            Element log=document.createElement("Log");  
            Element name=document.createElement("UserName");  
            name.setTextContent(userName);
            Element operate=document.createElement("Operate");  
            operate.setTextContent(tag);
            Element time=document.createElement("Time");  
            time.setTextContent(DateTimeHelper.getDateTimeNow());
            Element state=document.createElement("State");  
            state.setTextContent(s);
            Element message=document.createElement("Message");  
            message.setTextContent(msg);
            log.appendChild(name);
            log.appendChild(operate);
            log.appendChild(time);
            log.appendChild(state);
            log.appendChild(message);
            root.appendChild(log);
            document.appendChild(root);  
            TransformerFactory tf=TransformerFactory.newInstance();  
            Transformer transformer=tf.newTransformer();  
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
            transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));  
        } catch (ParserConfigurationException  e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (TransformerConfigurationException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (TransformerException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    } 
	
	private static void add_xml(String filePath, String tag, String s, String msg) {
        DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();        
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(filePath));
			
			Element root = document.getDocumentElement();
			//创建需要增加的节点
	        Node log=document.createElement("Log");
	        Element name=document.createElement("UserName");  
            name.setTextContent(userName);
            Element operate=document.createElement("Operate");  
            operate.setTextContent(tag);
            Element time=document.createElement("Time");  
            time.setTextContent(DateTimeHelper.getDateTimeNow());
            Element state=document.createElement("State");  
            state.setTextContent(s);
            Element message=document.createElement("Message");  
            message.setTextContent(msg);
            log.appendChild(name);
            log.appendChild(operate);
            log.appendChild(time);
            log.appendChild(state);
            log.appendChild(message);
	        //得到需要增加节点的父亲
            Node parent=document.getElementsByTagName("Logs").item(0);
	        //把需要增加的节点挂到父节点上
	        parent.appendChild(log);
	        //获取工厂
	        TransformerFactory tf=TransformerFactory.newInstance();
	        //获取转换器
	        Transformer ts=tf.newTransformer();
	        //将document写回book.xml文当
	        ts.transform(new DOMSource(document), new StreamResult(new File(filePath)));
		} catch (ParserConfigurationException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} catch (SAXException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}   
	}
	
	public void deleteOverdueLog(File path, String date) {
		try {
			if(path.isDirectory()){
	            //返回文件夹中有的数据
	            File[] files = path.listFiles();
	            //先判断下有没有权限，如果没有权限的话，就不执行了
	            if(null == files) {
	                return;
	            }
	            for(int i = 0; i < files.length; i++){
	            	if (FileUtils.getFileFormat(files[i].getName()).equalsIgnoreCase("xml"))
	            		deleteOverdueLog(files[i], date);
	            }
	        } else {
	        	//进行文件的处理
	            String filePath = path.getAbsolutePath();	            
	            //文件名
	            String fileName = FileUtils.getFileNameNoFormat(filePath);
	            
	            File file = new File(filePath);
	            try {
	            	String fileDate = fileName.split("_", 2)[1].toString();
	            	if (DateTimeHelper.StringToDate(fileDate, "yyyy-MM-dd")
	            			.compareTo(DateTimeHelper.StringToDate(date, "yyyy-MM-dd")) < 0) {	            		
	            		if (file.exists())
	            			file.delete();
	            	}
	            } catch (Exception ex) {
	            	if (file.exists())
	            		file.delete();
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}